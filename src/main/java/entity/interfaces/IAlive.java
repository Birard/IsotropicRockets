package entity.interfaces;

import entity.Enemy;

public interface IAlive {

    boolean isAlive();
    void setAlive();
    void setDead(Enemy enemy);

}
