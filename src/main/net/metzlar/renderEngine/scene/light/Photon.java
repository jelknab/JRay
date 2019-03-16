package net.metzlar.renderEngine.scene.light;

import net.metzlar.renderEngine.types.Color;

public class Photon {
    public Color color;
    public double distanceTraveled;

    public Photon(Color color) {
        this.color = color;
    }
}
