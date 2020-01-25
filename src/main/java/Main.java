import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main (String[] args){
        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long window = glfwCreateWindow(640,480,"My LWJGL", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - 640)/2, (videoMode.height() - 640)/2);

        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        Camera camera = new Camera(640,480);
        glEnable(GL_TEXTURE_2D);

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

        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");

        Texture tex = new Texture("src/main/resources/test.png");

        Matrix4f scale = new Matrix4f().scale(100);
        Matrix4f target;

        camera.setPosition(new Vector3f(0, 0, 0));

        glClearColor(0,255,255,0);

        while (!glfwWindowShouldClose(window)) {
            target = scale;
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", camera.getProjection().mul(target));
            tex.bind(0);
            model.render();

            glfwSwapBuffers(window);
        }

        glfwTerminate();

    }
}
