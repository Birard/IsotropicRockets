package engine.actors;

import engine.entity.interfaces.IRender;
import gameData.entity.controller.JetTrace;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class Actor extends Thread {

    private IRender iMove;

    public void setiMove(IRender iMove) {
        this.iMove = iMove;
    }

    @Override
    public void run() {
        ((JetTrace)iMove).setPos((new Vector3f((float)Math.random()*200-100,(float)Math.random()*200-100,0)));
        iMove.render();
    }
}
