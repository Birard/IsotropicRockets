package game;

import io.Timer;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.TileRenderer;
import world.World;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main (String[] args){

        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }
        Window win = new Window();
        Window.setCallbacks();
        win.setFullscreen(true);
        win.setSize(1366,768);
        win.createWindow("Game");


        GL.createCapabilities();

        Camera camera = new Camera(win.getWidth(),win.getHeight());
        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();

//        float[] vertices = new float[]{
//
//                // верхний правый треугольник
//                -0.5f, 0.5f, 0, //TOP LEFT      0
//                0.5f, 0.5f, 0,  //TOP RIGHT     1
//                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
//                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
//        };
//
//        float[] texture = new float[] {
//          0,0, // 0
//          1,0, // 1
//          1,1, // 2
//          0,1, // 3
//        };
//
//        int[] indices = new int[] {
//          0,1,2,
//          2,3,0
//        };
//
//        Model model = new Model(vertices, texture, indices);
        //Texture tex = new Texture("src/main/resources/test.png");

        Shader shader = new Shader("shader");

        World world = new World();

        camera.setPosition(new Vector3f(0, 0, 0));

        glClearColor(0,255,255,0);

        double frame_cap = 1.0/60.0; // в одной секунде 60 кадров
        double frame_time = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        while (!win.shouldClose()) {
            boolean can_render = false;
            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed+=passed;
            frame_time +=passed;
            time = time_2;

            while (unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;

                if(win.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    glfwSetWindowShouldClose(win.getWindow(), true);
                }

                if(win.getInput().isKeyDown(GLFW_KEY_A)) {
                    camera.getPosition().sub(new Vector3f(-1, 0, 0));
                }
                if(win.getInput().isKeyDown(GLFW_KEY_D)) {
                    camera.getPosition().sub(new Vector3f(1, 0, 0));
                }

                if(win.getInput().isKeyDown(GLFW_KEY_W)) {
                    camera.getPosition().sub(new Vector3f(0, 1, 0));
                }
                if(win.getInput().isKeyDown(GLFW_KEY_S)) {
                    camera.getPosition().sub(new Vector3f(0, -1, 0));
                }

                win.update();
                if(frame_time >= 1.0) {
                    frame_time = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if(can_render) {
                glClear(GL_COLOR_BUFFER_BIT);

//                shader.bind();
//                shader.setUniform("sampler", 0);
//                shader.setUniform("projection", camera.getProjection().mul(target));
//                tex.bind(0);
//                model.render();
                world.render(tiles, shader, camera);
                win.swapBuffers();
                frames++;
            }
        }

        glfwTerminate();

    }
}
