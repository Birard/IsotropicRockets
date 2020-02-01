package entity;

import org.joml.Vector3f;
import render.Camera;
import render.Shader;

public class JetTraceFactory {
    private boolean full;
    private JetTrace[] jetTraces;
    private int nowJetTrace;
    private int maxJetTrace;

    public JetTraceFactory(int maxJetTrace) {
        full = false;
        this.maxJetTrace = maxJetTrace;
        jetTraces = new JetTrace[maxJetTrace];
        nowJetTrace = 0;
    }

    public void createJetTrace(Vector3f pos) {
        if(full) {
            if(nowJetTrace == maxJetTrace) {
                nowJetTrace = 0;
            }
            jetTraces[nowJetTrace].setPos(pos);
            nowJetTrace++;

            return;
        }


        if(nowJetTrace < maxJetTrace) {
            jetTraces[nowJetTrace] = new JetTrace(pos);
            nowJetTrace++;
        } else {
            full = true;
            nowJetTrace = 0;
            createJetTrace(pos);
        }
    }

    public void render(Shader shader, Camera camera) {
        if(full) {
            for(int i = 0; i < maxJetTrace; i++) {
                jetTraces[i].render(shader, camera);
            }
        } else
        for(int i = 0; i < nowJetTrace; i++) {
            jetTraces[i].render(shader, camera);
        }
    }
}
