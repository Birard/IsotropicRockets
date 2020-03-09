package engine.io;

import engine.game.Main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input extends Thread {
    private static boolean alive = true;
    public static final Input input = new Input();
    private long window;

    private boolean[] keys;

    public Input() {
        this.window = Window.windows.getWindow();
        this.keys = new boolean[GLFW_KEY_LAST];
        for (int i = 0; i < GLFW_KEY_LAST; i++)
            keys[i] = false;
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while (alive) {
            for (int i = 32; i < GLFW_KEY_LAST; i++) {
                keys[i] = isKeyDown(i);
                if (keys[i]) Main.thisMain.keyIsPressed(i);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void setAlive(boolean alive) {
        Input.alive = alive;
    }
}
