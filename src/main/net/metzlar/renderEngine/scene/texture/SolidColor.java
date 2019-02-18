package net.metzlar.renderEngine.scene.texture;

import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Vec2;

public class SolidColor implements Texture {
    private Color color;

    public SolidColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColorAtPosition(Vec2 position) {
        return new Color(this.color.getR(), this.color.getG(), this.color.getB());
    }
}
