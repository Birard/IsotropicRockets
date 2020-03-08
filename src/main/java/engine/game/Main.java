package engine.game;

import engine.actors.ActorFactory;
import engine.io.Input;
import engine.world.TileRenderer;
import gameData.entity.controller.Enemy;
import gameData.entity.controller.JetTraceFactory;
import gameData.entity.controller.Player;
import engine.io.Timer;
import engine.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import engine.render.Camera;
import engine.render.Model;
import engine.render.Shader;
import engine.render.Texture;

import java.sql.Time;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static final Main thisMain = new Main();
    public static final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private static Player player;
//    private static Enemy[] enemies;
    private static boolean immortal;
    private static double startTime;
    private static double timeFromLastPressImmortal;
    private static JetTraceFactory jetTraceFactory;

    public Main() {
    }

    public static void main(String[] args) {
        timeFromLastPressImmortal = Timer.getTime() ;
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }
        Window.setCallbacks();

        Window.windows.setFullscreen(true);
        Window.windows.setSize(1376, 768);
        Window.windows.createWindow("Game");

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

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
        Shader shader = Shader.shader;

        immortal = true;
        player = Player.player;
        jetTraceFactory = new JetTraceFactory(1);
        player.getPosition();
//        enemies = new Enemy[100];

        ActorFactory actorFactory = new ActorFactory(1);
//        for (int i = 0; i < enemies.length; i++) {
//            enemies[i] = new Enemy(new Vector3f(i * 10f + 1000f, i * 10f + 500f, 0));
//        }
        Texture tex = new Texture("src/main/resources/checker.png");

        Matrix4f scale = new Matrix4f().scale(32);

        glClearColor(0, 255, 255, 0);

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;

        double timeWastLogik =0, timeWastRender =0, savedtime;

        TileRenderer tiles = new TileRenderer();

        double time = Timer.getTime();
        double unprocessed = 0;
        startTime = Timer.getTime();
        boolean can_render;
        Input.input.start();
        while (!Window.windows.shouldClose()) {
            can_render = false;
            time_2 = Timer.getTime();
            passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            time = time_2;

            while (unprocessed >= frame_cap) {
                savedtime = Timer.getTime();
                unprocessed -= frame_cap;
                can_render = true;

                player.update();
                actorFactory.joinActor();
                actorFactory.calculate();
                jetTraceFactory.createJetTrace(player.getPosition());
                Camera.camera.setPosition(player.getPosition());
//                for (Enemy enemy : ActorFactory.iRender) {
//                    if (!immortal && enemy.isAlive() && enemy.getPosition().distance(player.getPosition()) < 13) {
//                        player.setDead(enemy);
//                        enemy.setDead(enemy);
//                        System.out.println("Your time:" + ( Timer.getTime() - startTime));
//                        break;
//                    }
//                    jetTraceFactory.createJetTrace(enemy.getPosition());
//                    Vector3f playerCord = new Vector3f(player.getPosition());
//                    float distance = enemy.getPosition().distance(player.getPosition());
//                    if (distance < 88) {
//                        playerCord.x = playerCord.x + player.getSpeedX() / 5;
//                        playerCord.y = playerCord.y + player.getSpeedY() / 5;
//                    }
//                    if (distance > 110) {
//                        playerCord.x = playerCord.x + player.getSpeedX() / 3 - enemy.getSpeedX() / 3;
//                        playerCord.y = playerCord.y + player.getSpeedY() / 3 - enemy.getSpeedY() / 3;
//                    }
//                    if (distance > 220) {
//                        playerCord.x = playerCord.x + player.getSpeedX()/2 - enemy.getSpeedX()/2;
//                        playerCord.y = playerCord.y + player.getSpeedY()/2 - enemy.getSpeedY()/2;
//                    }
                   // enemy.update(playerCord);
 //               }

                Window.windows.update();
                timeWastLogik+= Timer.getTime() - savedtime;
            }

            if (frame_time >= 1.0) {
                frame_time = 0;
               // System.out.println("FPS: " + frames + "   Logic: " + logic_frames);
                frames = 0;
            }
            if (can_render) {
                savedtime = Timer.getTime();
                glClear(GL_COLOR_BUFFER_BIT);

                shader.bind();
                shader.setUniform("sampler", 0);
                shader.setUniform("projection", Camera.camera.getProjection().translate(new Vector3f(0,0,0)).mul(scale));
                tex.bind(0);
                model.render();

                for(int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        tiles.renderTile((byte) 0, i, j, scale);
                    }
                }

                jetTraceFactory.render();
 //               actorFactory.calculate();
                player.render();
                for (Enemy enemy : ActorFactory.iRender) {
                    enemy.render();
                }
                Window.windows.swapBuffers();
                frames++;
                timeWastRender+= Timer.getTime() - savedtime;
            }

//            System.out.println("logic==  "+timeWastLogik);
//            System.out.println("render=  " +timeWastRender);
        }



        glfwTerminate();
    }

    public void keyIsPressed(int key) {

        switch (key) {
            case GLFW_KEY_ESCAPE:
                Input.setAlive(false);
                glfwSetWindowShouldClose(Window.windows.getWindow(), true);
                break;
            case GLFW_KEY_A:
                player.setSpeedX(-220f);
                break;
            case GLFW_KEY_D:
                player.setSpeedX(220f);
                break;
            case GLFW_KEY_S:
                player.setSpeedY(-220f);
                break;
            case GLFW_KEY_W:
                player.setSpeedY(220f);
                break;
            case GLFW_KEY_N:
                startTime = Timer.getTime();
                player.setAlive();// = new JetPlayer();

               // enemies = new Enemy[1];
//                for (int i = 0; i < enemies.length; i++) {
//                    enemies[i] = new Enemy(new Vector3f(i * 0.05f + 200f, i * 0.05f + 200f, 0));
//                }
                break;

            case GLFW_KEY_I:
                if((timeFromLastPressImmortal- Timer.getTime()) < 1) {
                immortal = !immortal;
                timeFromLastPressImmortal = Timer.getTime();
                }

                break;
        }
    }
}
