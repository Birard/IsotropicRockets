package gameData.entity.controller;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    public Vector3f pos;
    public Matrix4f scale;

    public Transform() {
        pos = new Vector3f(0, 0, 0);
        scale = new Matrix4f().scale(16);
    }

    public Transform(Vector3f pos1) {
        pos = pos1;
        scale = new Matrix4f().scale(16);
    }


    public static float[] rotate(float[] vertices, float angle) {
        double cosA = Math.cos(angle), sinA = Math.sin(angle), x , y;
        int length = vertices.length / 3;
        for (int i = 0; i < length; i++) {
            x = vertices[i * 3];
            y = vertices[1 + i * 3];
            vertices[i * 3] = (float) (x * cosA - y * sinA);
            vertices[1 + i * 3] = (float) (y * cosA + x * sinA);
        }
        return vertices;
    }

    public static Vector3f rotate(Vector3f vector, float angle) {
        double cosA = Math.cos(angle), sinA = Math.sin(angle), x = vector.x, y = vector.y;
        vector.x = (float)(x * cosA - y * sinA);
        vector.y = (float)(y * cosA + x * sinA);
        return vector;
    }

    public Matrix4f getProjection(Matrix4f target) {
        return target.translate(pos).mul(scale);
    }

    public Vector3f getPosition() {
        return new Vector3f(pos);
    }
}
