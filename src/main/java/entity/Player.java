package entity;

import entity.interfaces.IMove;
import entity.interfaces.IRender;
import io.Window;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Player implements IMove, IRender {
    private Model model;
    private Texture texture;
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
        this.texture = new Texture("src/main/resources/player/player.png");
        transform = new Transform();
        transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta, Window window, Camera camera) {
        this.delta = delta;
        for (Scrap scrap : scraps) {
            scrap.update(delta);
        }
        move();
        speedX = 0;
        speedY = 0;
    }

    @Override
    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
        for (Scrap scrap : scraps) {
            scrap.render(shader, camera);
        }
    }

    @Override
    public void move() {
        if (alive) {
            transform.pos.add(speedX * delta, speedY * delta, 0);
        }
    }

    public boolean isAlive() {
        return alive;
    }

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
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public Vector3f getPosition() {
        return new Vector3f(transform.getPosition());
    }
}
