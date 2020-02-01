package entity;

import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class JetTrace {

    private Model model;
    private Texture texture;
    private Transform transform;

    public JetTrace(Vector3f pos) {
        float[] vertices = new float[]{
                // верхний правый треугольник
                0, 0.5f, 0, //TOP LEFT      0
                -0.5f, 0.5f, 0,  //TOP RIGHT     1
                -0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
              //  -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[] {
                0,0, // 0
                1,0, // 1
                1,1, // 2
              //  0,1, // 3
        };

        int[] indices = new int[] {
                0,1,2,
            //    2,3,0
        };

        model = new Model(vertices, texture, indices);
        this.texture = new Texture("src/main/resources/jetTrace.png");
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Vector3f(4, 4, 1);
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection",transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void setPos(Vector3f pos) {
        transform.pos = pos;
    }
}
