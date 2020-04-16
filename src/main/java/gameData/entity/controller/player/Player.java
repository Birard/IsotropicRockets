package gameData.entity.controller.player;

import engine.assets.Model;
import engine.assets.ModelManager;
import engine.entity.interfaces.IAlive;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import engine.game.Main;
import engine.texture.Animation;
import engine.texture.Texture;
import gameData.entity.controller.particles.Scrap;
import gameData.entity.controller.Transform;
import gameData.entity.controller.enemy.Enemy;
import org.joml.Vector3f;
import engine.render.*;

public class Player implements IMove, IRender, IAlive {
    public static final Player player = new Player();
    private Model model;
    private Enemy enemy;
    private Texture texture;
   // private Animation texture;
    private Transform transform;
    private float delta;
    private float speedX;
    private float speedY;
    private boolean alive ,auto;
    private Scrap[] scraps;

    public Player() {

        alive = true;
        model = ModelManager.getModel("standartQuad");
  //      this.texture = new Animation(5, 15, "scrap");
        this.texture = new Texture("src/main/resources/player/player.png");
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

    public void setAuto(boolean auto, Enemy enemy) {
        this.enemy = enemy;
        this.auto = auto;
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
            if (!auto) {
                transform.pos.add(speedX * delta, speedY * delta, 0);
            } else {
                Vector3f vectorF = new Vector3f(0, 220, 0);
               // vectorF = transform.rotate(vectorF, enemy.getAngle());
                transform.rotate(vectorF, enemy.getAngle());
                transform.pos.add(vectorF.x * delta, vectorF.y * delta, 0);
            }
        }
    }

    public boolean isAuto() {
        return auto;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
    @Override
    public void setAlive() {

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
