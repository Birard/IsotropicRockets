import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;

    private int width, height;

    public Window() {
        setSize(640, 480);
    }

    public void createWindow(String title) {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width,height,title, 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width)/2, (videoMode.height() - height)/2);

        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }
}
