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

public class JetPlayer extends Player implements IMove, IRender, IAlive {
    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private float angle;
    private float force;
    private boolean alive;
    private float[] vertices;
    private float[] texturef;
    private int[] indices;
    private Scrap[] scraps;

    public JetPlayer() {
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

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        scraps = new Scrap[0];
        alive = true;
        model = new Model(vertices, texturef, indices);
        this.texture = new Texture("src/main/resources/enemy.png");
        transform = new Transform();
        transform.scale = new Matrix4f().scale(16);
        force = 10;
        speedX = -10;
        speedY = 0;
        angle = 0;
    }

    public void update(float delta) {
        this.delta = delta;
        for (Scrap scrap : scraps) {
            scrap.update(delta);
        }
        move();
//        speedX = 0;
//        speedY = 0;
    }

    @Override
    public void render() {
        Shader shader = Shader.shader;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
        texture.bind(0);
        model.render();
        for (Scrap scrap : scraps) {
            scrap.render();
        }
    }

    @Override
    public void move() {
        if (alive) {
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
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive() {
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

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        scraps = new Scrap[0];
        alive = true;
        model = new Model(vertices, texturef, indices);
        this.texture = new Texture("src/main/resources/enemy.png");
        transform = new Transform();
        transform.scale = new Matrix4f().scale(16);
        force = 10;
        speedX = -10;
        speedY = 0;
        angle = 0;
    }

    @Override
    public void setDead(Enemy enemy) {
        alive = false;

        scraps = new Scrap[5];

        scraps[0] = new Scrap(getPosition(), (float) Math.random() * 2 - 1 - enemy.getSpeedX() / 3, (float) Math.random() * 2 - 1 - enemy.getSpeedY() / 3, "0");
        scraps[1] = new Scrap(getPosition(), (float) Math.random() * 2 - 1 - enemy.getSpeedX() / 3, (float) Math.random() * 2 - 1 - enemy.getSpeedY() / 3, "1");
        scraps[2] = new Scrap(getPosition(), (float) Math.random() * 2 - 1 - enemy.getSpeedX() / 3, (float) Math.random() * 2 - 1 - enemy.getSpeedY() / 3, "2");
        scraps[3] = new Scrap(getPosition(), (float) Math.random() * 2 - 1 - enemy.getSpeedX() / 3, (float) Math.random() * 2 - 1 - enemy.getSpeedY() / 3, "3");
        scraps[4] = new Scrap(getPosition(), (float) Math.random() * 2 - 1 - enemy.getSpeedX() / 3, (float) Math.random() * 2 - 1 - enemy.getSpeedY() / 3, "4");

        model.setVertices(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    public void moveAngleToLeft() {
        angle += 0.2;
      /////////////
    }

    public void moveAngleToRight() {
       angle -= 0.2;
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
