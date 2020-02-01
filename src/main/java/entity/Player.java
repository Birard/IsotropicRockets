package entity;

import io.Window;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Player {
    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private boolean alive;

    public  Player() {
        float[] vertices = new float[]{
                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[] {
                0,0, // 0
                1,0, // 1
                1,1, // 2
                0,1, // 3
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        alive = true;
        model = new Model(vertices, texture, indices);
        this.texture = new Texture("src/main/resources/player.png");
        transform = new Transform();
        transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta, Window window, Camera camera) {
        this.delta = delta;
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection",transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void move(Vector3f vector3f) {
        if (alive) {
            vector3f.mul(delta);
            transform.pos.add(vector3f);
        }
    }

    public void setDead(Vector3f enemy) {
        alive = false;
    }

    public  Vector3f getPosition() {
        return  transform.getPosition();
    }
}