package gameData.entity.controller.particles.JetTrace;


import engine.assets.Model;
import engine.assets.ModelManager;
import engine.entity.interfaces.IRender;
import engine.render.Camera;
import engine.render.Shader;
import engine.texture.Texture;
import engine.texture.TextureManager;
import gameData.entity.controller.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class JetTrace implements IRender {

    private static final Model model = ModelManager.getModel("standartQuad");
    private static final Texture texture = TextureManager.getTexture("src/main/resources/cloud.png");
    private Transform transform;

    public JetTrace(Vector3f pos) {
        transform = new Transform();
        transform.pos = pos;
        transform.scale = new Matrix4f().scale(4);
    }

    @Override
    public void render() {
        Shader shader = Shader.shader;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(Camera.camera.getProjection()));
        texture.bind(0);
        model.render();
    }

    public void setPos(Vector3f pos) {
        transform.pos = pos;
    }
}
