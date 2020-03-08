package gameData.entity.controller;

import engine.entity.interfaces.IRender;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import engine.render.Camera;
import engine.render.Model;
import engine.render.Shader;
import engine.render.Texture;

public class JetTrace implements IRender {

    private static final float[] vertices = new float[]{
            // верхний правый треугольник
            0, 0.5f, 0, //TOP LEFT      0
            -0.5f, 0.5f, 0,  //TOP RIGHT     1
            -0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
            //  -0.5f, -0.5f, 0, //BOTTOM LEFT  3
    };

    private static final float[] texturef = new float[]{
            0, 0, // 0
            1, 0, // 1
            1, 1, // 2
            //  0,1, // 3
    };

    private static final int[] indices = new int[]{
            0, 1, 2,
            //    2,3,0
    };

    private static final Model model = new Model(vertices, texturef, indices);
    private static final Texture texture = new Texture("src/main/resources/jetTrace.png");
    private Transform transform;

    public JetTrace(Vector3f pos) {
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Matrix4f().scale(4);
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

    public void setPos(Vector3f pos) {
        transform.pos = pos;
    }
}
