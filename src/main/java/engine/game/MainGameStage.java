package engine.game;

import engine.gui.Gui;
import engine.io.Input;
import engine.io.Timer;
import engine.io.Window;
import engine.render.Camera;
import gameData.entity.controller.enemy.EnemyFactory;
import gameData.entity.controller.particles.JetTrace.JetTraceFactory;
import gameData.entity.controller.player.Player;
import gameData.world.TileFactory;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainGameStage {
    public final double frame_cap = 1.0 / 60.0; // в одной секунде 60 кадров
    private boolean stop = false;


    public MainGameStage() {
    }

    public void main() {
    }

    public void keyIsPressed(int key) {
    }

    public void stop(){
        stop = true;
    }

    public void mouseButtonIsPressed(int mouseButton) {
    }

}
