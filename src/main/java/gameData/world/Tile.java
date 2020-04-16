package gameData.world;

import engine.assets.Model;
import engine.assets.ModelManager;
import engine.render.Camera;
import engine.render.Shader;
import engine.texture.Texture;
import engine.texture.TextureManager;
import gameData.entity.controller.Transform;
import org.joml.Vector3f;

public class Tile {

    private static final Model model = ModelManager.getModel("standartQuad");
    private Texture texture;
    private Transform transform;

    public Tile(Vector3f pos, String texture) {
            transform = new Transform();
            transform.pos = pos;
        transform.scale.scale((float) (Math.random()*6+4));
            this.texture = TextureManager.getTexture(texture);
        }


    public void render() {
        Shader shader = Shader.shader;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public Vector3f getPos() {
        return transform.getPosition();
    }

    public void setPos(Vector3f pos) {
        transform.pos = pos;
    }
}
