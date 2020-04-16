package gameData.entity.controller.player;

import engine.assets.Model;
import engine.assets.ModelManager;
import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import engine.render.*;
import engine.texture.Texture;
import engine.texture.TextureManager;
import gameData.entity.controller.particles.Scrap;
import gameData.entity.controller.Transform;
import gameData.entity.controller.enemy.Enemy;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class JetPlayer implements IMove, IRender, IAlive {
    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta = (float) (1.0/60.0);
    private float speedX;
    private float speedY;
    private float angle;
    private float force;
    private boolean alive;
    private Scrap[] scraps;

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

    public JetPlayer() {
        alive = true;
        model = new Model(vertices, texturef, indices);
        this.texture = TextureManager.getTexture("src/main/resources/enemy.png");
        transform = new Transform();
        force = 300;
        speedX = 0;
        speedY = 0;
        angle = 0;

        scraps = new Scrap[5];
        scraps[0] = new Scrap(getPosition(), 0, 0, "0");
        scraps[1] = new Scrap(getPosition(), 0, 0, "1");
        scraps[2] = new Scrap(getPosition(), 0, 0, "2");
        scraps[3] = new Scrap(getPosition(), 0, 0, "3");
        scraps[4] = new Scrap(getPosition(), 0, 0, "4");
    }

    public void update() {
        if(alive)  move(); else
            for (Scrap scrap : scraps) {
                scrap.update(delta);
            }
//        speedX = 0;
//        speedY = 0;
    }

    @Override
    public void render() {
        if(alive) {
            Shader shader = Shader.shader;
            float[] vertices = new float[this.vertices.length];
            System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);
            model.setVertices(Transform.rotate(vertices, angle));
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
            texture.bind(0);
            model.render();
        } else
        for (Scrap scrap : scraps) {
            scrap.render();
        }
    }

    @Override
    public void move() {
        if (alive) {
            Vector3f vectorF = new Vector3f(0, force, 0);
//            vectorF = transform.rotate(vectorF, angle);
            transform.rotate(vectorF, angle);

            float k = (float) 0.8;

            speedY = speedY + ((vectorF.y - speedY*k) * delta);
            speedX = speedX + ((vectorF.x - speedX*k) * delta);

            transform.pos.add((speedX) * delta, speedY * delta, 0);
//            float[] vertices = new float[this.vertices.length];
//            System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);
//
//            model.setVertices(Transform.rotate(vertices, angle));
        }
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive() {

        alive = true;
        transform.pos = (new Vector3f(0,0,0));
        speedX = 0;
        speedY = 0;
        angle = 0;
    }

    @Override
    public void setDead(Enemy enemy) {
        alive = false;
        scraps[0].setPhysics(getPosition(), (float)(Math.random() * 20 - 10 - enemy.getSpeedX() / 3), (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[1].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[2].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[3].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[4].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
    }

    public void moveAngleToLeft() {
        angle += 0.02;
      /////////////
    }

    public void moveAngleToRight() {
       angle -= 0.02;
        ////////////
    }

    public void setSpeedX(float speedX) {
       if(speedX < 0) moveAngleToLeft();
       else moveAngleToRight();
        //this.speedX = speedX; speedY = 0;
    }

    public void setSpeedY(float speedY) {
        //this.speedY = speedY; speedX = 0;
    }

    public Vector3f getPosition() {
        return new Vector3f(transform.getPosition());
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeedX() {
        return speedX;
    }
}
