package gameData.entity.controller;

import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import engine.render.Camera;
import engine.render.Model;
import engine.render.Shader;
import engine.render.Texture;

public class Scrap implements IMove, IRender {

    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private float[] vertices;
    private float[] texturef;
    private int[] indices;

    public Scrap(Vector3f pos, float speedX, float speedY, String name) {
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
        this.speedX = speedX;
        this.speedY = speedY;
        this.texture = new Texture("src/main/resources/player/scraps/scrap" + name + ".png");
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Matrix4f().scale(16);
    }

    public void update(float delta) {
        this.delta = delta;
        move();
    }

    @Override
    public void render() {
        Shader shader = Shader.shader;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    @Override
    public void move() {
        transform.pos.add(-speedX * delta, -speedY * delta, 0);
    }

    public Vector3f getPosition() {
        return transform.getPosition();
    }
}
