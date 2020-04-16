package gameData.Stages.TestStage;

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

public class TestGui extends Gui {
    private Matrix4f temp;
    private Vector3f pos = new Vector3f(0,0,0);
    private int needUpdate = 0;
    private double timeInGame;
    private static Texture texture;
    private GetTextPictureThread thread;
    private BufferedImage bufferedImage;
    private boolean needReCreateTexture;

    private Model model = ModelManager.getModel("standartQuad");



    public TestGui() {
        temp = new Matrix4f().scale(256);
        timeInGame = 0.0;

        bufferedImage = new BufferedImage(Window.windows.getWidth(), Window.windows.getHeight(), 2);
        TextureManager.getTextImage( "00.00",bufferedImage);
        texture = new Texture(bufferedImage);
        needReCreateTexture = false;
        needUpdate = 40;
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
         //   texture = new Texture(bufferedImage);
        }
        Shader shader = Shader.shader;
        shader.bind();

        shader.setUniform("sampler", 0);
        // shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(new Vector3f((float) (-Window.windows.getWidth()*0.5+256/2),(float) (Window.windows.getHeight()*0.5-256/2),0)).mul(temp));
        shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(pos).mul(temp));

        texture.bind(0);
        model.render();
    }


    public void updateTime(double timeInGame) {
        this.timeInGame = timeInGame;
        needUpdate++;
    }

    public int updateLeftMouseButtonPress(Vector2f mousePos) {
        return  -1;
    }

    public void update() {
        if (needUpdate > 30) {
            needUpdate = 0;
            String s2 = Double.toString(timeInGame);
            if (s2.length() > 5) s2 = s2.substring(0, 5);

            thread = new GetTextPictureThread(s2, bufferedImage);
            thread.start();
            needReCreateTexture = true;
            //   texture.reCreate(TextureManager.getTextImage(s1 + '\n' + s2));

        }
    }
    public void setSize(int width, int height) {
        bufferedImage = new BufferedImage(width, height, 2);
        TextureManager.getTextImage( "00.00",bufferedImage);
        temp.set(width,0,0,0,0,height,0,0,0,0,1,0,0,0,0,1);
    }
}
