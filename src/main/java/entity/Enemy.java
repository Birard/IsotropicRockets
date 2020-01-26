package entity;

import io.Window;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

public class Enemy {

    private Model model;
    private Texture texture;
    private Transform transform;
    private float delta;
    private float force;
    private float speedX;
    private float speedY;

    public  Enemy() {
        float[] vertices = new float[]{

                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[] {
                0,0, // 0
                1,0, // 1
                1,1, // 2
                0,1, // 3
        };

        int[] indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texture, indices);
        force = 10;
        speedX = 0;
        speedY = 0;
        this.texture = new Texture("src/main/resources/test.png");
        transform = new Transform();
        transform.pos = new Vector3f(25, 25, 0);
        transform.scale = new Vector3f(16, 16, 1);
    }

    public void update(float delta) {
        this.delta = delta;
    }

    public void render(Shader shader, Camera camera) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection",transform.getProjection(camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void move(Vector3f playCord) {
        Vector3f vector3f = new Vector3f();
       // System.out.println(getPosition().distance(playCord));

        double distanceEnPl = getPosition().distance(playCord);//= Math.sqrt(Math.pow((playCord.x - getPosition().x),2) + Math.pow((playCord.y - getPosition().y),2));

        double k = force / distanceEnPl;
        vector3f.x = (getPosition().x + (playCord.x-getPosition().x)*(float)k);
        vector3f.y = (getPosition().y + (playCord.y-getPosition().y)*(float)k);

        speedY = speedY + (getPosition().y-vector3f.y)*delta;
        speedX = speedX + (getPosition().x-vector3f.x)*delta;

        transform.pos.add(-speedX*delta,-speedY*delta,0);

        //transform.pos.add((getPosition().x-speedX)*delta,(getPosition().y-speedY)*delta,0);
    }

    public  Vector3f getPosition() {
        return  transform.getPosition();
    }
}
