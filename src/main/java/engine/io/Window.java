package engine.io;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.Callbacks;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    public static final Window windows = new Window();
    private long window;
    private int width, height;
    private boolean fullscreen, hasResized;
    private GLFWWindowSizeCallback windowSizeCallback;

    private Window() {
        setSize(640, 480);
        setFullscreen(false);
        hasResized = false;
    }

    public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallback() {

            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    private void setLocalCallbacks() {
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long argWindow, int argWidth, int argHeight) {
                width = argWidth;
                height = argHeight;
                hasResized = true;
            }
        };

        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    public void createWindow(String title) {

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        if (!fullscreen) {
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert videoMode != null;
            glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        setLocalCallbacks();
    }

    public void cleanUp() {
        windowSizeCallback.close();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void update() {
        glfwPollEvents();
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setHasResized(boolean hasResized) {
        this.hasResized = hasResized;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public long getWindow() {
        return window;
    }

    public boolean hasResized(){return hasResized; }
}
