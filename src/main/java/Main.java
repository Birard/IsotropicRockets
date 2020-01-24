import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public static void main (String[] args){
        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        long window = glfwCreateWindow(640,480,"My LWJGL", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - 640)/2, (videoMode.height() - 640)/2);

        glfwShowWindow(window);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }

        glfwTerminate();

    }
}
