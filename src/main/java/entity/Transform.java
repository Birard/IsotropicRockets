package entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    public Vector3f pos;
    public Vector3f scale;

    public static float[] rotate(float[] vertices, Vector3f xVector, Vector3f rotateTo) {

        boolean changeAngleSign = false;
        if ( rotateTo.x < 0){rotateTo.x = -rotateTo.x;changeAngleSign = true; }

        double skaljar = xVector.x*rotateTo.x + xVector.y*rotateTo.y;
        double cosA = skaljar/ (xVector.length() * rotateTo.length());

        double angle = (Math.acos(cosA)) ;

        if(changeAngleSign) angle = -angle;

        cosA = Math.cos(angle);
        double sinA = Math.sin(angle);


        for(int i = 0; i < vertices.length/3; i++) {
            double x = vertices[i*3];
            double y = vertices[1+i*3];

            vertices[i*3] = (float)(x*cosA - y*sinA);
            vertices[1+i*3] = (float)(y*cosA+x*sinA);
//          vertices[2+i*3] = vertices[2+i*3];
        }

        return  vertices;
    }


    public Transform() {
        pos = new Vector3f(0,0,0);
        scale = new Vector3f(16, 16, 1);

    }

    public Matrix4f getProjection(Matrix4f target) {
        target.scale(scale);
        target.translate(pos);
        return target;
    }

    public Vector3f getPosition() {
        return  pos;
    }
}
