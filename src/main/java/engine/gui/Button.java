package engine.gui;

import engine.assets.Model;
import engine.assets.ModelManager;
import engine.io.Window;
import engine.render.Camera;
import engine.render.Shader;
import engine.texture.Texture;
import engine.texture.TextureManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class Button {

    public Texture texture;
    public Model model = ModelManager.getModel("standartQuad");
    public Vector3f pos, scale;
    private float halfX , halfY;
    public Matrix4f transform;
    private int state;
    private BufferedImage bufferedImage;

    public Button(Vector3f pos, Vector3f scale) {
        this.state = 0;
        this.pos = pos;
        this.scale = scale;
        halfX = (float) (scale.x*0.5);
        halfY = (float)(scale.y*0.5);
        this.transform = new Matrix4f();
        transform.set(scale.x,0,0,0,0,scale.y,0,0,0,0,1,0,0,0,0,1);

        state = 1;
        bufferedImage = TextureManager.getTextImage("Руч");
        texture = new Texture(bufferedImage);
    }

    public boolean isCollisionWith(Vector2f point) {
        Vector2f pointPos = new Vector2f(point);
        pointPos.y = -pointPos.y;
        pointPos.x = (float) (pointPos.x - Window.windows.getWidth()*0.5);
        pointPos.y = (float) (pointPos.y + Window.windows.getHeight()*0.5);

        if(pos.x - halfX < pointPos.x && pos.x + halfX > pointPos.x)
            if(pos.y - halfY < pointPos.y && pos.y + halfY > pointPos.y) {
                if(state==0) {
                    state = 1;

                } else {
                    state = 0;

                }
                return true;
            }
        return false;
    }

    public int getState() {
        return state;
    }

    public void setText(String text)
    {
        bufferedImage = TextureManager.getTextImage(String.valueOf(text));
        texture.reCreate(bufferedImage);
    }

    public void render() {
        Shader shader = Shader.shader;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(pos).mul(transform));
        texture.bind(0);
        model.render();
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void setScale(Vector3f scale) {
        halfX = (float) (scale.x*0.5);
        halfY = (float)(scale.y*0.5);
        this.scale = scale;
    }
}
