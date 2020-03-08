package engine.actors;

import engine.entity.interfaces.IMove;
import gameData.entity.controller.Transform;

public class MoveMe implements IMove {
    private Transform transform;

    public MoveMe() {
        transform = new Transform();
    }

    @Override
    public void move() {
        transform.pos.add(1, 0, 0);
    }
}

