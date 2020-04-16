package engine.assets;

import engine.io.Window;
import engine.texture.Texture;
import engine.texture.TextureManager;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class ModelManager {
    public static final ModelManager modelManager = new ModelManager();
    private static final HashMap<String, Model> modeles  = new HashMap<String, Model>();

    private ModelManager() {
    }

    public static Model getModel(String nameOfModel) {
        if(modeles.containsKey(nameOfModel)) return modeles.get(nameOfModel);

        float[] vertices, texturef;
        int[] indices;

                vertices = new float[]{
                        // верхний правый треугольник
                        -0.5f, 0.5f, 0, //TOP LEFT      0
                        0.5f, 0.5f, 0,  //TOP RIGHT     1
                        0.5f, -0.5f, 0,  //BOTTOM RIGHT 2
                        -0.5f, -0.5f, 0, //BOTTOM LEFT  3
                };
                texturef = new float[]{
                        0, 0, // 0
                        1, 0, // 1
                        1, 1, // 2
                        0, 1, // 3
                };
                indices = new int[]{
                        0, 1, 2,
                        2, 3, 0
                };

        switch (nameOfModel) {
            case "standartQuad":
                break;
        }

        Model m = new Model(vertices, texturef, indices);;
        modeles.put(nameOfModel, m);
        return m;
    }
}
