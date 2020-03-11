package engine.game;

import engine.render.*;
import gameData.entity.controller.enemy.EnemyFactory;
import engine.io.Input;
import engine.world.TileRenderer;
import gameData.entity.controller.enemy.Enemy;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import gameData.entity.controller.player.Player;
import engine.io.Timer;
import engine.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static final Main thisMain = new Main();
    public static final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private static Player player;
//    private static Enemy[] enemies;
    private static EnemyFactory enemyFactory;
    private static boolean immortal;
    private static boolean again = true, pause = true;
    private static double startTime;
    private static JetTraceFactory jetTraceFactory;

    private Main() {
    }

    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }
        Window.setCallbacks();

        Window.windows.setFullscreen(false);
        Window.windows.setSize(1376/2, 768/2);
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
        jetTraceFactory = new JetTraceFactory(200);
        player.getPosition();
        player = new Player();
        Texture tex = TextureManager.getTexture("src/main/resources/checker.png");

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
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        enemyFactory = new EnemyFactory(500);

        while (!Window.windows.shouldClose()) {

                can_render = false;
                time_2 = Timer.getTime();
                passed = time_2 - time;
                unprocessed += passed;
                frame_time += passed;
                time = time_2;

                if (again) {
                    again = false;
                    startTime = Timer.getTime();
                    player.setAlive();// = new JetPlayer();
                    enemyFactory.setAllAlive();
                }


                while (unprocessed >= frame_cap) {
                    savedtime = Timer.getTime();
                    unprocessed -= frame_cap;
                    can_render = true;
                    if (!pause) {
                        player.update();
                        enemyFactory.joinThread();
                        enemyFactory.setTarget(player);
                        enemyFactory.calculate();
                        jetTraceFactory.createJetTrace(player.getPosition());
                        Camera.camera.setPosition(player.getPosition());
                        if (!immortal) {
                            int hit = enemyFactory.hit(player);
                            if (hit != -1) {
                                player.setDead(enemyFactory.getEnemies().get(hit));
                                enemyFactory.setAllDead();
                                System.out.println("Your time:" + (Timer.getTime() - startTime));
                                break;
                            }
                        }
                    }
                        Window.windows.update();
                        timeWastLogik += Timer.getTime() - savedtime;

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
                    shader.setUniform("projection", Camera.camera.getProjection().translate(new Vector3f(0, 0, 0)).mul(scale));
                    tex.bind(0);
                    model.render();

                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            tiles.renderTile((byte) 0, i, j, scale);
                        }
                    }

                    jetTraceFactory.render();
                    player.render();
                    enemyFactory.render();
                    Window.windows.swapBuffers();
                    frames++;
                    timeWastRender += Timer.getTime() - savedtime;
                }

//            System.out.println("logic==  "+timeWastLogik);
//            System.out.println("render=  " +timeWastRender);
            }
            Input.setAlive(false);
            glfwTerminate();

    }

    public void keyIsPressed(int key) {

        switch (key) {
            case GLFW_KEY_ESCAPE:
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
                again = true;
                break;
            case GLFW_KEY_I:
                if(Input.isKeyPressed(key)) {
                immortal = !immortal;
                }
                break;
            case GLFW_KEY_O:
                if(Input.isKeyPressed(key)) {
                    pause = !pause;
                }
                break;
        }
    }
}
