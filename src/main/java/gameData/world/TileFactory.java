package gameData.world;



import engine.io.Window;
import engine.render.Camera;
import org.joml.Vector3f;



public class TileFactory {

    private Tile[] tiles;
    private int maxTile;

    public TileFactory(int maxTile) {
        this.maxTile = maxTile;
        tiles = new Tile[maxTile];
        for(int i = 0; i<maxTile; i++) {
            tiles[i] = new Tile(new Vector3f((float)(Math.random()*Window.windows.getWidth()*4 - Window.windows.getWidth()*2),
                    (float)(Math.random()*Window.windows.getHeight()*4 - Window.windows.getHeight()*2),
                    0), "src/main/resources/cloud.png");
        }
    }

    public void setAgain() {
        float width = Window.windows.getWidth(), height = Window.windows.getHeight();
        Vector3f cameraPos = Camera.camera.getPosition();
        for(int i = 0; i<maxTile; i++) {
            tiles[i].setPos(new Vector3f((float)(cameraPos.x + Math.random()*width*4 - width*2),
                    (float)(cameraPos.y + Math.random()*height*4 - height*2),
                    0));
        }
    }

    public void updateTiles() {
        float width = Window.windows.getWidth(), height = Window.windows.getHeight();
        //float width = 200, height = 200;
        Vector3f pos, cameraPos = Camera.camera.getPosition();

        for(int i = 0; i<maxTile; i++) {
            pos = tiles[i].getPos();

                if(pos.x > cameraPos.x + width*2) {
                   tiles[i].setPos(new Vector3f((float)(cameraPos.x - Math.random()*width - width),
                            (float)(cameraPos.y + Math.random()*height*4 - height*2), 0)); }
                else
                if(pos.x < cameraPos.x - width*2) {
                    tiles[i].setPos(new Vector3f((float)(cameraPos.x + Math.random()*width + width),
                            (float)(cameraPos.y + Math.random()*height*4 - height*2), 0)); }
                else
                if(pos.y > cameraPos.y + height*2) {
                    tiles[i].setPos(new Vector3f((float)(cameraPos.x + Math.random()*width*4 - width*2),
                            (float)(cameraPos.y - Math.random()*height - height), 0));}
                else
                if(pos.y < cameraPos.y - height*2) {
                    tiles[i].setPos(new Vector3f((float)(cameraPos.x + Math.random()*width*4 - width*2),
                            (float)(cameraPos.y + Math.random()*height + height), 0)); }
        }
    }

    public void render() {
        for (int i = 0; i < maxTile; i++) {
            tiles[i].render();
        }
    }
}
