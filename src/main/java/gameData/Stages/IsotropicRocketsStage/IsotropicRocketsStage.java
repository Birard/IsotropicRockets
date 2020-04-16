package gameData.Stages.IsotropicRocketsStage;

import engine.game.Main;
import engine.game.MainGameStage;
import engine.gui.Gui;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Camera;
import gameData.Stages.MenuStage.MenuStage;
import gameData.entity.controller.enemy.EnemyFactory;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import gameData.entity.controller.player.Player;
import gameData.world.TileFactory;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class IsotropicRocketsStage extends MainGameStage {

    public final double frame_cap = 1.0 / 60.0; // в одной секунде 30 кадров
    private Player player;
    private EnemyFactory enemyFactory;
    private boolean immortal;
    private IsotropicRocketsGui gui;
    private boolean again = true, pause = true , stop = false;
    private double startTime;
    private JetTraceFactory jetTraceFactory;

    public IsotropicRocketsStage() {

    }

    public void main() {
        immortal = false;
        player = Player.player;
        jetTraceFactory = new JetTraceFactory(200);
        enemyFactory = new EnemyFactory(1);
        gui = new IsotropicRocketsGui();
        gui.setSize(Window.windows.getWidth(), Window.windows.getHeight());
        glClearColor(0, 255, 255, 0);

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;

        double timeWastLogik =0, timeWastRender =0, savedtime;

        TileFactory tiles = new TileFactory(300);

        double time = Timer.getTime(), unprocessed = 0, timeInGame = 0;
        startTime = Timer.getTime();
        boolean can_render;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        while (!stop && !Window.windows.shouldClose()) {
            can_render = false;
            time_2 = Timer.getTime();
            passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;
            time = time_2;

            if(Window.windows.hasResized()){
                Camera.camera.setSize(Window.windows.getWidth(), Window.windows.getHeight());
                glViewport(0,0,Window.windows.getWidth(), Window.windows.getHeight());
                Window.windows.setHasResized(false);
                tiles.setAgain();
                gui.setSize(Window.windows.getWidth(), Window.windows.getHeight());
            }

            if (again) {
                timeInGame = 0;
                again = false;
                startTime = Timer.getTime();
                player.setAlive();// = new JetPlayer();
                Camera.camera.setPosition(player.getPosition());
                tiles.setAgain();
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
                    gui.updateTime(enemyFactory.getEnemies().get(0).getTstar(),timeInGame);
                    gui.update();
                    tiles.updateTiles();
                    jetTraceFactory.createJetTrace(player.getPosition());
                    Camera.camera.setPosition(player.getPosition());
                    if (!immortal) {
                        int hit = enemyFactory.hit(player);
                        if (hit != -1) {
                            player.setDead(enemyFactory.getEnemies().get(hit));
                            enemyFactory.setAllDead();
                            System.out.println("Your time:" + timeInGame);
                            break;
                        }
                    }
                    if(player.isAlive())timeInGame+= Timer.getTime() - startTime;
                    startTime = Timer.getTime();
                } else startTime = Timer.getTime();
                Window.windows.update();
                timeWastLogik += Timer.getTime() - savedtime;

            }

            if (frame_time >= 1.0) {
                frame_time = 0;
                frames = 0;
            }
            if (can_render) {
                savedtime = Timer.getTime();
                glClear(GL_COLOR_BUFFER_BIT);

                jetTraceFactory.render();
                tiles.render();
                player.render();
                enemyFactory.render();
                gui.render();
                Window.windows.swapBuffers();
                frames++;
                timeWastRender += Timer.getTime() - savedtime;
            }
        }
    }

    public void keyIsPressed(int key) {

        switch (key) {
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(Window.windows.getWindow(), true);
                stop = true;
                break;
            case GLFW_KEY_A:
                player.setSpeedX(-200f);
                break;
            case GLFW_KEY_D:
                player.setSpeedX(200f);
                break;
            case GLFW_KEY_S:
                player.setSpeedY(-200f);
                break;
            case GLFW_KEY_W:
                player.setSpeedY(200f);


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
            case GLFW_KEY_BACKSPACE:
                Main.setGameStage( new MenuStage());
        }
    }

    public void stop(){
        stop = true;
    }

    public void mouseButtonIsPressed(int mouseButton) {

        switch (mouseButton) {
            case GLFW_MOUSE_BUTTON_LEFT:
                    if(Input.isMouseButtonPressed(mouseButton)) {
                        int buttonId = gui.updateLeftMouseButtonPress(Input.getMousePos());
                        if (buttonId > -1) {
                            switch (buttonId) {
                                case 0:
                                    player.setAuto(!player.isAuto(), enemyFactory.getEnemies().get(0));
                                    break;
                            }
                        }
                    }
                break;
            case GLFW_MOUSE_BUTTON_RIGHT:
                Vector3f newPos = new Vector3f(Input.getMousePos(),0);
                newPos.y = -newPos.y;
                newPos.add(Camera.camera.getPosition());
                newPos.x = (float) (newPos.x - Window.windows.getWidth()*0.5);
                newPos.y = (float) (newPos.y + Window.windows.getHeight()*0.5);
                enemyFactory.getEnemies().get(0).setPosition(newPos);
                enemyFactory.getEnemies().get(0).setSpeedX(0);
                enemyFactory.getEnemies().get(0).setSpeedY(0);
                break;
        }
    }


}
