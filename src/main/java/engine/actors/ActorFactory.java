package engine.actors;

import engine.entity.interfaces.IMove;
import engine.entity.interfaces.IRender;
import gameData.entity.controller.JetTrace;
import org.joml.Vector3f;

public class ActorFactory {
    private IRender[] iRender;
    private int numberIMoves = 1000;
    private Actor[] actors;
    private int numberActors = 1;

    public ActorFactory() {
        iRender = new IRender[numberIMoves];
        for(int i = 0; i < numberIMoves; i++){
            iRender[i] = new JetTrace(new Vector3f((float)Math.random()*200-100,(float)Math.random()*200-100,0));
        }

        actors = new Actor[numberActors];
        for(int i = 0; i < numberActors; i++){
            actors[i] = new Actor();
        }
    }

    public void calculate() {
        int nowIMoves = 0;
        while (true){
            for(int i = 0; i < numberActors; i++){
                if(nowIMoves == numberIMoves) break;
                actors[i].setiMove(iRender[nowIMoves]);
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
