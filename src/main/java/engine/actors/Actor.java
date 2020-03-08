package engine.actors;

import gameData.entity.controller.Enemy;
import gameData.entity.controller.Player;
import org.joml.Vector3f;

public class Actor extends Thread {

    private int numberiMove;

    public Actor(int iMove) {
        this.numberiMove = iMove;
    }

    @Override
    public void run() {

        Vector3f playerCord = new Vector3f(Player.player.getPosition());
                    float distance = ActorFactory.iRender[numberiMove].getPosition().distance(Player.player.getPosition());
                    if (distance < 88) {
                        playerCord.x = playerCord.x + Player.player.getSpeedX() / 5;
                        playerCord.y = playerCord.y + Player.player.getSpeedY() / 5;
                    }
                    if (distance > 110) {
                        playerCord.x = playerCord.x + Player.player.getSpeedX() / 3 - ActorFactory.iRender[numberiMove].getSpeedX() / 3;
                        playerCord.y = playerCord.y + Player.player.getSpeedY() / 3 - ActorFactory.iRender[numberiMove].getSpeedY() / 3;
                    }
                    if (distance > 220) {
                        playerCord.x = playerCord.x + Player.player.getSpeedX()/2 - ActorFactory.iRender[numberiMove].getSpeedX()/2;
                        playerCord.y = playerCord.y + Player.player.getSpeedY()/2 - ActorFactory.iRender[numberiMove].getSpeedY()/2;
                    }
        ActorFactory.iRender[numberiMove].update(playerCord);

    }
}
