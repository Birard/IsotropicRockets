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

    public static float[] rotate(float[] vertices, float angle) {

        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);


        for (int i = 0; i < vertices.length / 3; i++) {
            double x = vertices[i * 3];
            double y = vertices[1 + i * 3];

            vertices[i * 3] = (float) (x * cosA - y * sinA);
            vertices[1 + i * 3] = (float) (y * cosA + x * sinA);
//          vertices[2+i*3] = vertices[2+i*3];
        }

        return vertices;
    }

    public Vector3f rotate(Vector3f vector, float angle) {
        float[] vertices = new float[3];
        vertices[0] = vector.x;
        vertices[1] = vector.y;
        vertices[2] = vector.z;
        vertices = rotate(vertices, angle);
        return new Vector3f(vertices[0], vertices[1], vertices[2]);
    }

    public Matrix4f getProjection(Matrix4f target) {
        target.translate(pos);
        target.mul(scale);
        return target;
    }

    public Vector3f getPosition() {
        return new Vector3f(pos);
    }
}
