package gameData.Stages.MenuStage;

import engine.gui.Button;
import engine.io.Window;
import engine.render.Camera;
import engine.render.Shader;
import engine.texture.Texture;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class MenuRender {

    Button[] buttons;

    public MenuRender() {
       buttons = new Button[4000];

       for(int i = 0; i < 4000; i ++) {
           buttons[i] = new Button(new Vector3f((float) Math.random() * 1366 - 683, (float) Math.random() * 766 - 383, 0), new Vector3f(192, 64, 0));
           buttons[i].setText(" кнопка " + i);
       }
    }
    public void render() {
        Shader shader = new Shader("shader");
        shader.bind();
        shader.setUniform("sampler", 1);
        Texture texture = new Texture("src/main/resources/Textures.png");
        texture.bind(1);
      for(Button button: buttons) {
        shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(button.pos).mul(button.transform));
        button.model.render();
          //  button.render();
      }
    }



//    public void render() {
//        Shader shader = Shader.shader;
//        shader.bind();
//        shader.setUniform("sampler", 0);
//        shader.setUniform("projection", Camera.camera.getUntransformedProjection().translate(pos).mul(transform));
//        texture.bind(0);
//        model.render();
//    }
}