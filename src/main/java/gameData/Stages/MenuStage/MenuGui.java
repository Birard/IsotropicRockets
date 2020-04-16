package gameData.Stages.MenuStage;

import engine.Threads.GetTextPictureThread;
import engine.gui.Button;
import engine.gui.Gui;
import engine.texture.Texture;
import engine.texture.TextureManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class MenuGui extends Gui {

    private Button buttonIR, buttonTG;

    public MenuGui() {

        buttonIR = new Button(new Vector3f(0,40,0),new Vector3f(192*2,64,0));
        buttonIR.setText(" Симуляцiя iзотопних ракет");
        buttonTG = new Button(new Vector3f(0,-40,0),new Vector3f(192,64,0));
        buttonTG.setText(" Тестова гра");
    }
    public void render() {
        buttonTG.render();
        buttonIR.render();
    }

    public int updateLeftMouseButtonPress(Vector2f mousePos) {
        if(buttonTG.isCollisionWith(mousePos)) {return 1;}
        if(buttonIR.isCollisionWith(mousePos)) {return 0;}

        return -1;
    }

    public void update() {
    }

    public void setSize(int width, int height) {
    }
}
