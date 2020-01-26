package world;

import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;

public class World {
    private  byte[] tiles;
    private int width;
    private int height;
    private int scale;

    private  Matrix4f world;

    public World() {
        width = 18;
        height = 18;
        scale = 32;

        tiles = new byte[width * height];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    public void render(TileRenderer render, Shader shader, Camera camera) {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                render.renderTile(tiles[j + i * width], j, -i, shader, world, camera);
            }
        }
    }

    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        if(pos.x > -(window.getWidth()/2)+scale/2);
            pos.x = -(window.getWidth()/2)+scale/2;
        if(pos.x < w +(window.getWidth()*2)-scale*4)
            pos.x = w +(window.getWidth()*2)-scale*4;

        if(pos.y < (window.getHeight()/2)-scale/2)
            pos.y = (window.getHeight()/2)-scale/2;
        if(pos.y > h-window.getHeight()*2-scale*1.65f)
            pos.y = h-(window.getHeight()*2)-scale*1.65f;
    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
    }

    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
