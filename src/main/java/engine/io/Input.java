package engine.io;

import engine.game.Main;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input extends Thread {
    public static final Input input = new Input();
    private static long window;
    private static boolean[] keys, mouseButtons;
    private static boolean alive;

    private Input() {
        window = Window.windows.getWindow();
        alive = true;
        keys = new boolean[GLFW_KEY_LAST];
        for (int i = 0; i < GLFW_KEY_LAST; i++)
            keys[i] = false;

        mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++)
            mouseButtons[i] = false;
    }

    public static boolean isKeyDown(int key){return glfwGetKey(window, key) == 1;}

    public static boolean isKeyPressed(int key){return (isKeyDown(key) && !keys[key]);}

    public static boolean isKeyReleased(int key){return (isKeyDown(key) && keys[key]);}

    public static boolean isKeyUnpressed(int key){return (!isKeyDown(key) && keys[key]);}

    public static Vector2f getMousePos () {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xBuffer, yBuffer);
        return  new Vector2f((float)xBuffer.get(0), (float) yBuffer.get(0));
    }
    public static boolean isMouseButtonDown(int MouseButton){return glfwGetMouseButton(window, MouseButton) == 1;}

    public static boolean isMouseButtonPressed(int MouseButton){return (isMouseButtonDown(MouseButton) && !mouseButtons[MouseButton]);}

    public static boolean isMouseButtonReleased(int MouseButton){return (isMouseButtonDown(MouseButton) && mouseButtons[MouseButton]);}

    public static boolean isMouseButtonUnpressed(int MouseButton){return (!isMouseButtonDown(MouseButton) && mouseButtons[MouseButton]);}

    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while (alive) {
            for (int i = GLFW_KEY_SPACE; i < GLFW_KEY_LAST; i++) {
                if (isKeyDown(i)) Main.keyIsPressed(i);
                keys[i] = isKeyDown(i);
            }

            for (int i = GLFW_MOUSE_BUTTON_LEFT; i < GLFW_MOUSE_BUTTON_LAST; i++){
                if (isMouseButtonDown(i)) Main.mouseButtonIsPressed(i);
                mouseButtons[i] = isMouseButtonDown(i);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void setAlive(boolean alive) {
        Input.alive = alive;
    }
}
