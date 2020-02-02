package io;

import game.Main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input {
    private long window;
    private Main main;

    private boolean[] keys;

    public Input(long window, Main main) {
        this.main = main;
        this.window = window;
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

    public void update() {
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = isKeyDown(i);
            if (keys[i]) main.keyIsPressed(i);
        }


    }


}
