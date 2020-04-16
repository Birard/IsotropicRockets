package gameData.Stages.IsotropicRocketsStage;

import engine.Threads.GetTextPictureThread;
import engine.assets.Model;
import engine.assets.ModelManager;
import engine.gui.Button;
import engine.gui.Gui;
import engine.io.Window;
import engine.render.Camera;
import engine.render.Shader;
import engine.texture.Texture;
import engine.texture.TextureManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class IsotropicRocketsGui extends Gui {
    private Matrix4f temp;
    private Vector3f pos = new Vector3f(0,0,0);
    private int needUpdate = 0;
    private double time;
    private double timeInGame;
    private static Texture texture;
    private GetTextPictureThread thread;
    private BufferedImage bufferedImage;
    private boolean needReCreateTexture;

    private Model model = ModelManager.getModel("standartQuad");

    private Button casualButton;

    public IsotropicRocketsGui() {
        temp = new Matrix4f().scale(256);
        time = 0.0;
        timeInGame = 0.0;

        bufferedImage = new BufferedImage(Window.windows.getWidth(), Window.windows.getHeight(), 2);
        TextureManager.getTextImage("00.00" + '\n' + "00.00",bufferedImage);
        texture = new Texture(bufferedImage);
        needReCreateTexture = false;
        needUpdate = 40;

        casualButton = new Button(new Vector3f(0,0,0),new Vector3f(192,48,0));
    }

    public void render() {
        if(needReCreateTexture) {
            needReCreateTexture = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            texture.reCreate(bufferedImage);
        }
        Shader shader = Shader.shader;
        shader.bind();

        shader.setUniform("sampler", 0);
        // shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(new Vector3f((float) (-Window.windows.getWidth()*0.5+256/2),(float) (Window.windows.getHeight()*0.5-256/2),0)).mul(temp));
        shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(pos).mul(temp));

        texture.bind(0);
        model.render();

        casualButton.render();
    }


    public void updateTime(double newTime, double timeInGame) {
        time = newTime;
        this.timeInGame = timeInGame;
        needUpdate++;
    }

    public int updateLeftMouseButtonPress(Vector2f mousePos) {
        if(casualButton.isCollisionWith(mousePos)){
            if(casualButton.getState()==0) {
                casualButton.setText("Авт");

            } else {
                casualButton.setText("Руч");
            }
            return 0;
        }

        return  -1;
    }

    public void update() {
        if (needUpdate > 30) {
            needUpdate = 0;
            String s1 = String.valueOf(time);
            if (s1.length() > 5) s1 = s1.substring(0, 5);

            String s2 = Double.toString(timeInGame);
            if (s2.length() > 5) s2 = s2.substring(0, 5);

            s1 = s1 + '\n' + s2;
            thread = new GetTextPictureThread(s1, bufferedImage);
            thread.start();
            needReCreateTexture = true;
            //   texture.reCreate(TextureManager.getTextImage(s1 + '\n' + s2));

        }
    }
    public void setSize(int width, int height) {
        bufferedImage = new BufferedImage(width, height, 2);
        TextureManager.getTextImage("00.00" + '\n' + "00.00",bufferedImage);
        casualButton.setPos(new Vector3f((float) (width*0.5)-96, (float) (height*0.5)-24,0));
        temp.set(width,0,0,0,0,height,0,0,0,0,1,0,0,0,0,1);
    }
}
