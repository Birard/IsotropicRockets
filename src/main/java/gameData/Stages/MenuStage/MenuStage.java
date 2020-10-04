package gameData.Stages.MenuStage;

import engine.game.Main;
import engine.game.MainGameStage;
import engine.gui.Gui;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Camera;
import gameData.Stages.IsotropicRocketsStage.IsotropicRocketsStage;
import gameData.Stages.TestStage.TestStage;
import gameData.entity.controller.enemy.EnemyFactory;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import gameData.entity.controller.player.Player;
import gameData.world.TileFactory;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class MenuStage extends MainGameStage {
    public final double frame_cap = 1.0 / 60.0; // в одной секунде 30 кадров
    private MenuGui gui;
    private boolean again = true, pause = true , stop = false;
    private double startTime;

    public MenuStage() {

    }

    public void main() {
        gui = new MenuGui();
        gui.setSize(Window.windows.getWidth(), Window.windows.getHeight());
        glClearColor(0, 255, 255, 0);

        double frame_time = 0;
        int frames = 0;

        double time_2;
        double passed;

        double timeWastLogik = 0, timeWastRender = 0, savedtime;

        TileFactory tiles = new TileFactory(300);

        double time = Timer.getTime(), unprocessed = 0, timeInGame = 0;
        startTime = Timer.getTime();
        boolean can_render;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);



        MenuRender menuRender = new MenuRender();


        //while (!Window.windows.shouldClose()) {
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
                tiles.setAgain();
            }


            while (unprocessed >= frame_cap) {
                savedtime = Timer.getTime();
                unprocessed -= frame_cap;
                can_render = true;
                if (!pause) {
                    gui.update();
                    tiles.updateTiles();
                    startTime = Timer.getTime();
                } else startTime = Timer.getTime();
                Window.windows.update();
                timeWastLogik += Timer.getTime() - savedtime;

            }

            if (frame_time >= 1.0) {
                frame_time = 0;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
            if (can_render) {
                savedtime = Timer.getTime();

                glClear(GL_COLOR_BUFFER_BIT);

                menuRender.render();

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
            case GLFW_KEY_N:
                again = true;
                break;
            case GLFW_KEY_I:
                if(Input.isKeyPressed(key)) {
                    Main.setGameStage( new IsotropicRocketsStage());
                }
                break;
            case GLFW_KEY_O:
                if(Input.isKeyPressed(key)) {
                    pause = !pause;
                }
                break;
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
                        switch (buttonId){
                            case 0:
                                Main.setGameStage(new IsotropicRocketsStage());
                                break;
                            case 1:
                                Main.setGameStage(new TestStage());
                                break;
                        }
                    }
                }
                break;
        }
    }

}
