package engine.world;

import engine.render.Camera;
import engine.render.Model;
import engine.render.Shader;
import engine.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class TileRenderer {

    private static float[] vertices = new float[]{
            // верхний правый треугольник
            -0.5f, 0.5f, 0, //TOP LEFT      0
            0.5f, 0.5f, 0,  //TOP RIGHT     1
            0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
            -0.5f, -0.5f, 0, //BOTTOM LEFT  3
    };

    private static float[] texture = new float[]{
            0, 0, // 0
            1, 0, // 1
            1, 1, // 2
            0, 1, // 3
    };

    private static int[] indices = new int[]{
            0, 1, 2,
            2, 3, 0
    };

    private HashMap<String, Texture> tile_textures;
    private static final Model model = new Model(vertices, texture, indices);

    public TileRenderer() {
        tile_textures = new HashMap<String, Texture>();

        for(int i = 0; i < Tile.tiles.length; i++) {
            if(Tile.tiles[i] != null) {
                if (!tile_textures.containsKey(Tile.tiles[i].getTexture())) {
                    String tex = Tile.tiles[i].getTexture();
                    tile_textures.put(tex, new Texture(tex + ".png"));
                }
            }
        }
    }

    public void renderTile(byte id, int x, int y, Matrix4f world) {
        Shader.shader.bind();
        if(tile_textures.containsKey(Tile.tiles[id].getTexture()))
            tile_textures.get(Tile.tiles[id].getTexture()).bind(0);

        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x, y,0));
        Matrix4f target = new Matrix4f();

        Camera.camera.getProjection().mul(world,target);
        target.mul(tile_pos);

        Shader.shader.setUniform("sampler", 0);
        Shader.shader.setUniform("projection",target);
 //       tex.bind(0);

        model.render();
    }
}
