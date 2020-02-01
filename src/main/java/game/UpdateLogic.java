package game;

import entity.Enemy;
import io.Timer;
import io.Window;
import org.joml.Vector3f;


import entity.Enemy;
import entity.Player;
import io.Timer;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class UpdateLogic extends Thread{

    public  UpdateLogic(String name, Main main, Camera camera,Player player, Enemy[] enemies) {
        super(name);
    }

    public void run() {

        double frame_cap = 1.0/60.0; // в одной секунде 60 кадров (пауза между кадрами)
        double frame_time = 0;
        int frames = 0;
        int logic_frames = 0;
        double time_2;
        double passed;
        double time = Timer.getTime();
        double unprocessed = 0;

        boolean can_render;
        while (true) {
            time_2 = Timer.getTime();
            passed = time_2 - time;
            unprocessed += passed;
            can_render = false;
            frame_time += passed;
            time = time_2;


            while (unprocessed >= frame_cap) {
                logic_frames++;
                unprocessed -= frame_cap;
                can_render = true;
// updateLogic(frame_cap)
                if (frame_time >= 1.0) {
                    frame_time = 0;
                    System.out.println("FPсмчмS: " + frames + "   Logиисмиic: " + logic_frames);
                    logic_frames = 0;
                    frames = 0;
                }
            }
            if(can_render) {
                //Render
                frames++;
            }
        }
    }

}
