package net.metzlar.renderEngine.types;

import java.io.Serializable;

public class Vec2 implements Serializable {
    private final double x, y;

    public Vec2() {
        this(0.0, 0.0);
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2 multiply(double b) {
        return new Vec2(this.x*b, this.y*b);
    }

    public Vec2 add(Vec2 b) {
        return new Vec2(this.x + b.x, this.y + b.y);
    }
}
