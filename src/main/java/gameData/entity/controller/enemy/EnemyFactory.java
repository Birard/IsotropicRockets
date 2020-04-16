package gameData.entity.controller.enemy;


import engine.Threads.UpdateThread;
import engine.entity.interfaces.IMove;
import engine.io.Window;
import engine.render.Camera;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import org.joml.Vector3f;

import java.util.ArrayList;

public class EnemyFactory {
    private int numberOfEnemy;
    private ArrayList enemies;
    private UpdateThread thread;
    private JetTraceFactory jetTraceFactory = new JetTraceFactory(500);

    public int getNumberOfEnemy() {
        return numberOfEnemy;
    }

    public EnemyFactory(int numberEnemy, int jetTrace) {
        this(numberEnemy);
        jetTraceFactory = new JetTraceFactory(jetTrace);
    }

    public void checkForHitHimself() {
        int i , j = 0;
        while (j < numberOfEnemy -1) {
            i = j+1;
            while (i < numberOfEnemy) {
                if (((Enemy) enemies.get(i)).getPosition().distance(((Enemy) enemies.get(j)).getPosition()) < 15) {
                    enemies.remove(i);
                    numberOfEnemy--;
                    enemies.remove(j);
                    numberOfEnemy--;
                    return;
                }
                i++;
            }
            j++;
        }
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

    public void setAllAlive() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy)enemies.get(i)).setSpeedX(0);
            ((Enemy)enemies.get(i)).setSpeedY(0);
            ((Enemy)enemies.get(i)).setPosition(new Vector3f(i * 10f + 500f, i * 10f + 500f, 0));
            ((Enemy)enemies.get(i)).setAlive();
        }
    }

    public void setAllDead() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).setDead(((Enemy) enemies.get(i)));
        }
    }

    public void setTarget(IMove target) {

        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).setTarget(target);
        }
    }

    public void render() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).render();
        }
        jetTraceFactory.render();
    }

    public void setAllNotSmart() {
        for (int i = 0; i < numberOfEnemy; i++) {
            ((Enemy) enemies.get(i)).setNotSmart(true);
            ((Enemy) enemies.get(i)).setK((float) 0.8);
            ((Enemy) enemies.get(i)).setForce(400);
        }
    }

    public void createEnemyOnRandomPlace() {
        float width = Window.windows.getWidth(), height = Window.windows.getHeight();
        Vector3f cameraPos = Camera.camera.getPosition();
            enemies.add(new Enemy(new Vector3f((float)(cameraPos.x + Math.random()*width*4 - width*2),
                    (float)(cameraPos.y + Math.random()*height*4 - height*2),
                    0)));
numberOfEnemy++;
    }

    public void deleteEnemy(int i) {
        enemies.remove(i);
        numberOfEnemy--;
    }

    public int hit(IMove target) { return hit(target, 25);}

    public int hit(IMove target, int size) {
        for (int i = 0; i < numberOfEnemy; i++) {
            if (((Enemy) enemies.get(i)).isAlive() && ((Enemy) enemies.get(i)).getPosition().distance(target.getPosition()) < size)
                return i;
        }
        return -1;
    }
}