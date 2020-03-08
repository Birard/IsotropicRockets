package gameData.entity.controller;

import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import engine.render.*;

public class Player implements IMove, IRender, IAlive {
    private Model model;
//    private Texture texture;
    private Animation texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private boolean alive;
    private Scrap[] scraps;

    public Player() {
        float[] vertices = new float[]{
                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[]{
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
        model = new Model(vertices, texture, indices);
        this.texture = new Animation(5, 15, "scrap");
//       // this.texture = new Texture("src/main/resources/player/player.png");
        transform = new Transform();
        transform.scale = new Matrix4f().scale(16);
        speedX = -220;
        speedY = 0;
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
            transform.pos.add(speedX * delta, speedY * delta, 0);
        }
    }
    @Override
    public boolean isAlive() {
        return alive;
    }
    @Override
    public void setAlive() {
        float[] vertices = new float[]{
                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[]{
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
        model = new Model(vertices, texture, indices);
        this.texture = new Animation(5, 15, "scrap");
//        // this.texture = new Texture("src/main/resources/player/player.png");
        transform = new Transform();
        transform.scale = new Matrix4f().scale(16);
        speedX = -220;
        speedY = 0;
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

    public void setSpeedX(float speedX) {
        this.speedX = speedX; speedY = 0;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY; speedX = 0;
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
