package gameData.entity.controller.enemy;

import engine.assets.Model;
import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import engine.entity.interfaces.IUpdate;
import engine.game.Main;
import engine.render.*;
import engine.texture.Texture;
import engine.texture.TextureManager;
import gameData.entity.controller.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Enemy implements IMove, IRender, IAlive, IUpdate {

    private Vector3f targetCord;
    private IMove target;
    private static final Texture texture = TextureManager.getTexture("src/main/resources/enemy.png");
    private Transform transform;
    private float delta = (float) Main.frame_cap;
    private float force = 23000;
    private float speedX;
    private float speedY;
    private float angle;
    private float k;
    private double Tstar;
    private boolean notSmart = false;

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
        k = (float) 100;
        speedX = 0;
        speedY = 0;
        transform = new Transform();
        transform.pos = pos;
        targetCord = new Vector3f(0, 0, 0);
        angle = (float) 3.14;
        alive = true;
        Tstar = 0;
    }

    public void update(Vector3f targetCord) {
        if(alive) {
            this.delta = (float) Main.frame_cap;
            setTarget(targetCord);
            setAngle();
            move();
        }
    }

    public void update() {
        if(alive) {
        this.delta = (float)Main.frame_cap;
        setAngle();
        move();
        }
    }

    @Override
    public void render() {
        if (alive) {
        Shader shader = Shader.shader;
        float[] vertices = new float[this.vertices.length];
        System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);
        model.setVertices(Transform.rotate(vertices, angle));
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

    public void setTarget(IMove target) {
        if (alive) {
            this.target = target;
        }
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public void setK(float k) {
        this.k = k;
    }

    public void setNotSmart(boolean notSmart) {
        this.notSmart = notSmart;
    }

    public void setAngle() {
     //   if(target == null) target = this;
        if (notSmart) {
            targetCord = target.getPosition();
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

        } else {

            double W = target.getSpeedX() + target.getSpeedY();
            if(W < 0) W = -W;

            targetCord = target.getPosition();
           // targetCord = new Vector3f( 0,0,0);
            Tstar = (Math.sqrt(Math.pow(targetCord.x-transform.getPosition().x,2) + Math.pow(targetCord.y-transform.getPosition().y,2) + ((Math.pow(speedX,2)+Math.pow(speedY,2))/Math.pow(k,2)) - (2/k)*((targetCord.x-transform.getPosition().x)*speedX + (targetCord.y-transform.getPosition().y)*speedY)))/ ((force/k)-W);

            double QTstar = ((force/k)-W) * Tstar;

            double  S = ((targetCord.x-transform.getPosition().x)-speedX/k)/QTstar,
                    C = ((targetCord.y-transform.getPosition().y)-speedY/k)/QTstar;

            if(S < -1) S = -0.2;
             if(S>1) S = 1;
            if(C<0) this.angle = (float)(Math.asin(S)+3.1415);
              else  this.angle = (float)(-Math.asin(S));
        }
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public void move() {
//        delta = (float) (1.0/60.0);
//        float force = this.force * 2;
        Vector3f vectorF = new Vector3f(0, force, 0);
  //      vectorF = transform.rotate(vectorF, angle);
        transform.rotate(vectorF, angle);

//        speedY = speedY + ((vectorF.y - speedY*k) * delta);
//        speedX = speedX + ((vectorF.x - speedX*k) * delta);
//
        speedY = speedY + ((vectorF.y - speedY*k) * delta);
        speedX = speedX + ((vectorF.x - speedX*k) * delta);

        transform.pos.add(speedX * delta, speedY * delta, 0);
//        float[] vertices = new float[this.vertices.length];
//        System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);
//
//        model.setVertices(Transform.rotate(vertices, angle));
    }

    public Vector3f getPosition() {
       return transform.getPosition();
    }

    public void setPosition (Vector3f pos) {
        transform.pos = pos;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
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
        Tstar = 0;
    }

    public double getTstar() {
        return Tstar;
    }
}
