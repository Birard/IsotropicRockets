package entity.interfaces;

import render.Camera;
import render.Shader;

public interface IRender {
    void render(Shader shader, Camera camera);
}
