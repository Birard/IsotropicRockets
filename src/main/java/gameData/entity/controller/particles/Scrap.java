package gameData.entity.controller.particles;

import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import engine.render.*;
import gameData.entity.controller.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
        this.texture = TextureManager.getTexture("src/main/resources/player/scraps/scrap" + name + ".png");
        transform = new Transform(pos);
    }

    public void setPhysics(Vector3f pos, float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        transform.pos = pos;
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

    @Override
    public float getSpeedX() {
        return speedX;
    }

    @Override
    public float getSpeedY() {
        return speedY;
    }

    public Vector3f getPosition() {
        return transform.getPosition();
    }
}
