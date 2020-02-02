package game;

import entity.Enemy;
import entity.JetTraceFactory;
import entity.Player;
import io.Timer;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    private static Main thisMain;
    private static Window window;
    private static Camera camera;
    private static Player player;
    private static Enemy[] enemies;

    public Main() {

    }

    public static void main(String[] args) {
        thisMain = new Main();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }
        window = new Window(thisMain);
        Window.setCallbacks();
        window.setFullscreen(true);
        window.setSize(1376, 768);
        window.createWindow("Game");

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        camera = new Camera(window.getWidth(), window.getHeight());
        glEnable(GL_TEXTURE_2D);

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

        Model model = new Model(vertices, texture, indices);
        Shader shader = new Shader("shader");

        JetTraceFactory jetTraceFactory = new JetTraceFactory(100);
        player = new Player();
        enemies = new Enemy[1];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(new Vector3f(i * 0.05f + 10f, i * 0.05f + 5f, 0));
        }
        Texture tex = new Texture("src/main/resources/checker.png");

        Matrix4f scale = new Matrix4f().scale(100);

        camera.setPosition(player.getPosition());

        glClearColor(0, 255, 255, 0);

        double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
        double frame_time = 0;
        int frames = 0;
        int logic_frames = 0;

        double time_2;
        double passed;

        double time = Timer.getTime();
        double unprocessed = 0;

        boolean can_render;
        while (!window.shouldClose()) {
            can_render = false;
            time_2 = Timer.getTime();
            passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            time = time_2;

            while (unprocessed >= frame_cap) {
                logic_frames++;
                unprocessed -= frame_cap;
                can_render = true;

                player.update((float) frame_cap, window, camera);
                for (Enemy enemy : enemies) {
                    if (enemy.getPosition().distance(player.getPosition()) < 1) {
                        player.setDead(enemy);
                        enemies = new Enemy[0];
                        jetTraceFactory.deleteAll();
                        break;
                    }
                    jetTraceFactory.createJetTrace(enemy.getPosition().mul(4, new Vector3f()));
                    enemy.update((float) frame_cap, new Vector3f(player.getPosition()));
                }
                window.update();
            }

            if (frame_time >= 1.0) {
                frame_time = 0;
                System.out.println("FPS: " + frames + "   Logic: " + logic_frames);
                logic_frames = 0;
                frames = 0;
            }

            //new UpdateLogic("updateLogic", thisMain, window, camera, player, enemies).start();

            if (can_render) {
                glClear(GL_COLOR_BUFFER_BIT);

                shader.bind();
                shader.setUniform("sampler", 0);
                shader.setUniform("projection", camera.getProjection().mul(scale));
                tex.bind(0);
                model.render();
                jetTraceFactory.render(shader, camera);

                player.render(shader, camera);
                for (Enemy enemy : enemies) {
                    enemy.render(shader, camera);
                }
                window.swapBuffers();
                frames++;
            }
        }

        glfwTerminate();
    }

    public void keyIsPressed(int key) {

        switch (key) {
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window.getWindow(), true);
                break;
            case GLFW_KEY_A:
                player.setSpeedX(-10f);
                break;
            case GLFW_KEY_D:
                player.setSpeedX(10f);
                break;
            case GLFW_KEY_S:
                player.setSpeedY(-10f);
                break;
            case GLFW_KEY_W:
                player.setSpeedY(10f);
                break;
            case GLFW_KEY_N:
                player = new Player();
                enemies = new Enemy[1];
                for (int i = 0; i < enemies.length; i++) {
                    enemies[i] = new Enemy(new Vector3f(i * 0.05f + 10f, i * 0.05f + 5f, 0));
                }
                break;
        }
        camera.setPosition(player.getPosition().mul(-16, new Vector3f()));
    }
}
