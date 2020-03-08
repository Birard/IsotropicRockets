package engine.actors;


import gameData.entity.controller.Enemy;
import org.joml.Vector3f;

public class ActorFactory {
    public static final int numberIMoves = 100;
    public static final Enemy[] iRender = new Enemy[numberIMoves];
    private Actor[] actors;
    private int numberActors = 100;

    public ActorFactory(int numberEnemy) {
        for(int i = 0; i < numberIMoves; i++){
            iRender[i] = new Enemy(new Vector3f(i * 10f + 1000f, i * 10f + 500f, 0));
        }
        actors = new Actor[numberActors];
    }

    public void calculate() {
        int nowIMoves = 0;
        while (true){
            for(int i = 0; i < numberActors; i++){
                if(nowIMoves == numberIMoves) break;
                actors[i] = new Actor(nowIMoves);
                actors[i].start();
                nowIMoves++;
            }
            for(int i = 0; i < numberActors; i++){
                try {
                    actors[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(nowIMoves == numberIMoves) return;
        }
    }

}
