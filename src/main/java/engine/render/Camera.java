package engine.render;

import engine.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public static final Camera camera = new Camera(Window.windows.getWidth(), Window.windows.getHeight());
    private Vector3f position;
    private Matrix4f projection;

    private Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f);
    }

    public Vector3f getPosition() {
        return new Vector3f(this.position);
    }

    public void setSize(int width, int height) {
        projection = projection.setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Matrix4f getProjection() {
        return projection.translate(new Vector3f(-position.x, -position.y, -position.z), new Matrix4f());
    }
}
