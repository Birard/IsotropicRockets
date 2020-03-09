package engine.entity.interfaces;

import gameData.entity.controller.enemy.Enemy;

public interface IAlive {

    boolean isAlive();
    void setAlive();
    void setDead(Enemy enemy);

}
