package net.metzlar.renderEngine.scene.light;

import net.metzlar.renderEngine.types.Color;

public class Photon {
    public Light light;
    public Color color;
    public double distanceTraveled;

    public Photon(Light light, double distanceTraveled) {
        this.light = light;
        this.color = Color.WHITE;
        this.distanceTraveled = distanceTraveled;
    }
}
