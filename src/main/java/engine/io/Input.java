package engine.io;

import engine.game.Main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input extends Thread {
    public static final Input input = new Input();
    private static long window;
    private static boolean[] keys;
    private static boolean alive;

    private Input() {
        window = Window.windows.getWindow();
        alive = true;
        keys = new boolean[GLFW_KEY_LAST];
        for (int i = 0; i < GLFW_KEY_LAST; i++)
            keys[i] = false;
    }

    public static boolean isKeyDown(int key){return glfwGetKey(window, key) == 1;}

    public static boolean isKeyPressed(int key){return (isKeyDown(key) && !keys[key]);}

    public static boolean isKeyReleased(int key){return (isKeyDown(key) && keys[key]);}

    public static boolean isKeyUnpressed(int key){return (!isKeyDown(key) && keys[key]);}

    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while (alive) {
            for (int i = 32; i < GLFW_KEY_LAST; i++) {
                if (isKeyDown(i)) Main.thisMain.keyIsPressed(i);
                keys[i] = isKeyDown(i);
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
