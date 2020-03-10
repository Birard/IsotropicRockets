package engine.render;


import java.util.HashMap;

public class TextureManager {
    public static final TextureManager textureManager = new TextureManager();
    private static final HashMap<String, Texture> textures  = new HashMap<String, Texture>();

    private TextureManager() {
    }

    public static Texture getTexture(String filename) {
        if(textures.containsKey(filename)) return textures.get(filename);

        Texture t = new Texture(filename);
        textures.put(filename, t);
        return t;
    }
}
