package gameData.entity.controller.player;

import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import engine.game.Main;
import gameData.entity.controller.particles.Scrap;
import gameData.entity.controller.Transform;
import gameData.entity.controller.enemy.Enemy;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import engine.render.*;

public class Player implements IMove, IRender, IAlive {
    public static final Player player = new Player();
    private Model model;
//    private Texture texture;
    private Animation texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private boolean alive;
    private Scrap[] scraps;

    public Player() {
        float[] vertices = new float[]{
                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[]{
                0, 0, // 0
                1, 0, // 1
                1, 1, // 2
                0, 1, // 3
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        scraps = new Scrap[0];
        alive = true;
        model = new Model(vertices, texture, indices);
        this.texture = new Animation(5, 15, "scrap");
//       // this.texture = new Texture("src/main/resources/player/player.png");
        transform = new Transform();
        speedX = -22;
        speedY = 0;
        this.delta = (float)Main.frame_cap;

        scraps = new Scrap[5];
        scraps[0] = new Scrap(getPosition(), 0, 0, "0");
        scraps[1] = new Scrap(getPosition(), 0, 0, "1");
        scraps[2] = new Scrap(getPosition(), 0, 0, "2");
        scraps[3] = new Scrap(getPosition(), 0, 0, "3");
        scraps[4] = new Scrap(getPosition(), 0, 0, "4");
    }

    public void update() {
        if(alive)  move(); else
        for (Scrap scrap : scraps) {
            scrap.update(delta);
        }
    }

    @Override
    public void render() {
        if(alive) {
            Shader shader = Shader.shader;
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
            texture.bind(0);
            model.render();
        } else
        for (Scrap scrap : scraps) {
            scrap.render();
        }
    }

    @Override
    public void move() {
        if (alive) {
            transform.pos.add(speedX * delta, speedY * delta, 0);
        }
    }
    @Override
    public boolean isAlive() {
        return alive;
    }
    @Override
    public void setAlive() {
        float[] vertices = new float[]{
                // верхний правый треугольник
                -0.5f, 0.5f, 0, //TOP LEFT      0
                0.5f, 0.5f, 0,  //TOP RIGHT     1
                0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                -0.5f, -0.5f, 0, //BOTTOM LEFT  3
        };

        float[] texture = new float[]{
                0, 0, // 0
                1, 0, // 1
                1, 1, // 2
                0, 1, // 3
        };

        int[] indices = new int[]{
                0, 1, 2,
                2, 3, 0
        };
        alive = true;
        transform.pos = (new Vector3f(0,0,0));
        speedX = -220;
        speedY = 0;
    }

    @Override
    public void setDead(Enemy enemy) {
        alive = false;
        scraps[0].setPhysics(getPosition(), (float)(Math.random() * 20 - 10 - enemy.getSpeedX() / 3), (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[1].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[2].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[3].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
        scraps[4].setPhysics(getPosition(),(float) Math.random() * 20 - 10 - enemy.getSpeedX() / 3, (float) Math.random() * 20 - 10 - enemy.getSpeedY() / 3);
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX; speedY = 0;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY; speedX = 0;
    }

    public Vector3f getPosition() {
        return transform.getPosition();
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeedX() {
        return speedX;
    }
}
