package entity;

import entity.interfaces.IMove;
import entity.interfaces.IRender;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Enemy implements IMove, IRender {

    private Vector3f targetCord;
    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private float force;
    private float speedX;
    private float speedY;
    private float angle;
    private float[] vertices;
    private float[] texturef;
    private int[] indices;

    public Enemy(Vector3f pos) {
        vertices = new float[]{

                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        texturef = new float[]{
                0, 0, // 0
                1, 0, // 1
                1, 1, // 2
                0, 1, // 3
        };

        indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, texturef, indices);
        force = 11;
        speedX = 0;
        speedY = 0;
        this.texture = new Texture("src/main/resources/enemy.png");
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Vector3f(16, 16, 1);
        targetCord = new Vector3f(0, 0, 0);
        angle = 0;
    }

    public void update(float delta, Vector3f targetCord) {
        this.delta = delta;
        setTarget(targetCord);
        setAngle();
        move();
    }

    public void update(float delta) {
        this.delta = delta;
        move();
    }

    @Override
    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void setTarget(Vector3f targetCord) {
        this.targetCord = targetCord;
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
        Vector3f vector3f = new Vector3f(0, force, 0);
        vector3f = transform.rotate(vector3f, angle);

        speedY = speedY + (vector3f.y) * delta;
        speedX = speedX + (vector3f.x) * delta;

        transform.pos.add(speedX * delta, speedY * delta, 0);

        float[] vertices = new float[this.vertices.length];
        System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);

        model.setVertices(Transform.rotate(vertices, angle));
    }

    public Vector3f getPosition() {
        return transform.getPosition();
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }
}
