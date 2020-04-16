package engine.game;

import engine.io.Input;
import engine.io.Window;
import gameData.Stages.MenuStage.MenuStage;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static MainGameStage main;
    public static final double frame_cap = 1.0 / 60.0;

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

        main = new MenuStage();
        Input.input.start();
        gameLoop();
    }

    public static void gameLoop() {
       while (!Window.windows.shouldClose())main.main();
       Input.setAlive(false);
        glfwTerminate();
    }

    public static void keyIsPressed(int key) {
        main.keyIsPressed(key);
    }


    public static void mouseButtonIsPressed(int mouseButton) {
        main.mouseButtonIsPressed(mouseButton);
    }

    public static void setGameStage(MainGameStage stage) {
        main.stop();
        main = stage;
    }
}