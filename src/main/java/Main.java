import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

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

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glClearColor(0,255,255,0);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            glBegin(GL_QUADS);
                glColor4f(1,0,0,0);
                glVertex2f(-0.5f,0.5f);

                glColor4f(0,1,0,0);
                glVertex2f(0.5f,0.5f);

                glColor4f(0,0,1,0);
                glVertex2f(0.5f,-0.5f);

                glColor4f(0,1,0,0);
                glVertex2f(-0.5f,-0.5f);
            glEnd();

            glfwSwapBuffers(window);
        }

        glfwTerminate();

    }
}
