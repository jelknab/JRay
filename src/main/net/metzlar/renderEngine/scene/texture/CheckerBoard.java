package net.metzlar.renderEngine.scene.texture;

import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Vec2;

public class CheckerBoard implements Texture {
    private Color color;
    private int scaleU;
    private int scaleV;

    public CheckerBoard(Color color, int scaleU, int scaleV) {
        this.color = color;
        this.scaleU = scaleU;
        this.scaleV = scaleV;
    }

    @Override
    public Color getColorAtPosition(Vec2 position) {
        if ((Math.round(position.getX() * this.scaleU) + Math.round(position.getY() * this.scaleV)) % 2 == 0) {
            return color.multiply(0.5);
        }

        return color;
    }
}
