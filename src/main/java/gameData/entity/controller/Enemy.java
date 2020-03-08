package gameData.entity.controller;

import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import engine.render.Camera;
import engine.render.Model;
import engine.render.Shader;
import engine.render.Texture;

public class Enemy implements IMove, IRender, IAlive {

    private Vector3f targetCord;
    private Texture texture = new Texture("src/main/resources/enemy.png");
    private Transform transform;
    private float delta;
    private static final float force = 400;
    private float speedX;
    private float speedY;
    private float angle;
    private final float[] vertices = new float[]{
            // верхний правый треугольник
            -0.5f, 0.5f, 0, //TOP LEFT      0
            0.5f, 0.5f, 0,  //TOP RIGHT     1
            0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
            -0.5f, -0.5f, 0, //BOTTOM LEFT  3
    };
    private static final float[] texturef = new float[]{
            0, 0, // 0
            1, 0, // 1
            1, 1, // 2
            0, 1, // 3
    };
    private static final int[] indices = new int[]{
            0, 1, 2,
            2, 3, 0
    };
    private final Model model = new Model(vertices, texturef, indices);
    private boolean alive;

    public Enemy(Vector3f pos) {
        speedX = 0;
        speedY = 0;
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Matrix4f().scale(16);
        targetCord = new Vector3f(0, 0, 0);
        angle = 0;
        alive = true;
    }

    public void update(float delta, Vector3f targetCord) {
        if(alive) {
            this.delta = delta;
            setTarget(targetCord);
            setAngle();
            move();
        }
    }

    public void update(float delta) {
        if(alive) {
        this.delta = delta;
        setAngle();
        move();
        }
    }

    @Override
    public void render() {
        Shader shader = Shader.shader;
        if (alive) {
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
            texture.bind(0);
            model.render();
        }
    }

    public void setTarget(Vector3f targetCord) {
        if (alive) {
            this.targetCord = targetCord;
        }
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setAngle() {

        Vector3f vector3f = new Vector3f();

        float distanceEnPl = getPosition().distance(targetCord);
        float k = force / distanceEnPl;
        vector3f.x = (getPosition().x + (targetCord.x - getPosition().x) * k);
        vector3f.y = (getPosition().y + (targetCord.y - getPosition().y) * k);

        Vector3f rotateTo = new Vector3f((getPosition().x - vector3f.x), (getPosition().y - vector3f.y), 0);
        Vector3f yVector = new Vector3f(0, -1, 0);

        boolean changeAngleSign = false;
        if (rotateTo.x < 0) {
            rotateTo.x = -rotateTo.x;
            changeAngleSign = true;
        }

        double skaljar = yVector.x * rotateTo.x + yVector.y * rotateTo.y;
        double cosA = skaljar / (yVector.length() * rotateTo.length());

        double angle = (Math.acos(cosA));

        if (changeAngleSign) angle = -angle;
        this.angle = (float) angle;
    }

    @Override
    public void move() {
        Vector3f vectorF = new Vector3f(0, force, 0);
        vectorF = transform.rotate(vectorF, angle);

        float k = (float) 0.0;

        speedY = speedY + ((vectorF.y - speedY*k) * delta);
        speedX = speedX + ((vectorF.x - speedX*k) * delta);

        transform.pos.add((speedX) * delta, speedY * delta, 0);

        float[] vertices = new float[this.vertices.length];
        System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);

        model.setVertices(Transform.rotate(vertices, angle));
    }

    public Vector3f getPosition() {
       return transform.getPosition();
    }

    public Vector3f getTargetCord() {
        return new Vector3f(targetCord);
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive() {
        alive = true;
    }

    @Override
    public void setDead(Enemy enemy) {
        alive = false;
    }
}
