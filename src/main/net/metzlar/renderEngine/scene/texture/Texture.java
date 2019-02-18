package net.metzlar.renderEngine.scene.texture;

import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Vec2;

import java.io.Serializable;

public interface Texture extends Serializable {
    /**
     * @param position UV position of hit
     * @return Color at the UV position
     */
    Color getColorAtPosition(Vec2 position);
}
