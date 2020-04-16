package gameData.entity.controller.particles.JetTrace;

import org.joml.Vector3f;

public class JetTraceFactory {
    private JetTrace[] jetTraces;
    private int nowJetTrace;
    private int maxJetTrace;

    public JetTraceFactory(int maxJetTrace) {
        this.maxJetTrace = maxJetTrace;
        jetTraces = new JetTrace[maxJetTrace];
        for(int i = 0; i<maxJetTrace; i++) {
            jetTraces[i] = new JetTrace(new Vector3f(0,0,-100));
        }
        nowJetTrace = 0;
    }

    public void createJetTrace(Vector3f pos) {
        if(maxJetTrace == 0) return;
            if (nowJetTrace == maxJetTrace) {
                nowJetTrace = 0;
            }
            jetTraces[nowJetTrace].setPos(pos);
            nowJetTrace++;
    }

    public void render() {
        if(maxJetTrace == 0) return;
            for (int i = 0; i < maxJetTrace; i++) {
                jetTraces[i].render();
            }
    }
}
