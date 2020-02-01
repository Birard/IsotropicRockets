package entity;

import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Scrap {

    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private float[] vertices;
    private float[] texturef;
    private int[] indices;

    public  Scrap(Vector3f pos, float speedX, float speedY, String name) {
        vertices = new float[]{

                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        texturef = new float[] {
                0,0, // 0
                1,0, // 1
                1,1, // 2
                0,1, // 3
        };

        indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texturef, indices);
        this.speedX = speedX;
        this.speedY = speedY;
        this.texture = new Texture("src/main/resources/player/scraps/scrap"+name+".png");
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta) {
        this.delta = delta;
        move();
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection",transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void move() {
        transform.pos.add(-speedX*delta,-speedY*delta,0);
    }

    public  Vector3f getPosition() {
        return  transform.getPosition();
    }
}
