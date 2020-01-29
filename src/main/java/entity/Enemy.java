package entity;

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
    private float[] vertices;
    private float[] texturef;
    private int[] indices;

    public  Enemy(Vector3f pos) {
        vertices = new float[]{

                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        texturef = new float[] {
                0,0, // 0
                1,0, // 1
                1,1, // 2
                0,1, // 3
        };

        indices = new int[] {
                0,1,2,
                2,3,0
        };

        model = new Model(vertices, texturef, indices);
        force = 11;
        speedX = 0;
        speedY = 0;
        this.texture = new Texture("src/main/resources/test.png");
        transform = new Transform();
        transform.pos = pos;
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

        double distanceEnPl = getPosition().distance(playCord);//= Math.sqrt(Math.pow((playCord.x - getPosition().x),2) + Math.pow((playCord.y - getPosition().y),2));

        double k = force / distanceEnPl;
        vector3f.x = (getPosition().x + (playCord.x-getPosition().x)*(float)k);
        vector3f.y = (getPosition().y + (playCord.y-getPosition().y)*(float)k);

        speedY = speedY + (getPosition().y-vector3f.y)*delta;
        speedX = speedX + (getPosition().x-vector3f.x)*delta;

        transform.pos.add(-speedX*delta,-speedY*delta,0);

        float[] vertices = new float[this.vertices.length];
        System.arraycopy(this.vertices, 0, vertices, 0, this.vertices.length);
        model.cleanUp();
        model = new Model(Transform.rotate(vertices,new Vector3f(0, -1, 0), new Vector3f((getPosition().x-vector3f.x), (getPosition().y-vector3f.y), 0)), texturef, indices);
    }

    public  Vector3f getPosition() {
        return  transform.getPosition();
    }
}
