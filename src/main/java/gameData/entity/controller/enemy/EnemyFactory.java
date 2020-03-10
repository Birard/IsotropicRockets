package gameData.entity.controller.enemy;


import engine.Threads.UpdateThread;
import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IUpdate;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import org.joml.Vector3f;

import java.util.ArrayList;

public class EnemyFactory {
    private int numberOfEnemy;
    private  ArrayList enemies;
    private UpdateThread thread;
    private JetTraceFactory jetTraceFactory = new JetTraceFactory(500);

    public int getNumberOfEnemy() {
        return numberOfEnemy;
    }

    public EnemyFactory(int numberEnemy) {
        enemies = new ArrayList(numberEnemy);
        numberOfEnemy = numberEnemy;
        for (int i = 0; i < numberOfEnemy; i++) {
            enemies.add(new Enemy(new Vector3f(i * 10f + 1000f, i * 10f + 500f, 0)));
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void joinThread() {
        if (thread != null)
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void calculate() {
        for (int i = 0; i < numberOfEnemy; i++) {
            jetTraceFactory.createJetTrace(((Enemy) enemies.get(i)).getPosition());
        }
        thread = new UpdateThread(enemies);
        thread.start();
    }

    public void setDead(int i) {
        ((Enemy) enemies.get(i)).setDead(((Enemy) enemies.get(i)));
    }

    public void setAllDead() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).setDead(((Enemy) enemies.get(i)));
        }
    }

    public void setTarget(IMove target) {

        for (int i = 0; i < numberOfEnemy; i++) {

            Vector3f targetCord = new Vector3f(target.getPosition());
            float distance = ((Enemy) enemies.get(i)).getPosition().distance(target.getPosition());
            if (distance < 88) {
                targetCord.x = targetCord.x + target.getSpeedX() / 5;
                targetCord.y = targetCord.y + target.getSpeedY() / 5;
            }
            if (distance > 110) {
                targetCord.x = targetCord.x + target.getSpeedX() / 3 - ((Enemy) enemies.get(i)).getSpeedX() / 3;
                targetCord.y = targetCord.y + target.getSpeedY() / 3 - ((Enemy) enemies.get(i)).getSpeedY() / 3;
            }
            if (distance > 220) {
                targetCord.x = targetCord.x + target.getSpeedX() / 2 - ((Enemy) enemies.get(i)).getSpeedX() / 2;
                targetCord.y = targetCord.y + target.getSpeedY() / 2 - ((Enemy) enemies.get(i)).getSpeedY() / 2;
            }

            ((Enemy) enemies.get(i)).setTarget(targetCord);
        }
    }

    public void render() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).render();
        }
        jetTraceFactory.render();
    }

    public int hit(IMove target) {
        for (int i = 0; i < numberOfEnemy; i++) {
            if (((Enemy) enemies.get(i)).isAlive() && ((Enemy) enemies.get(i)).getPosition().distance(target.getPosition()) < 13)
                return i;
        }
        return -1;
    }
}