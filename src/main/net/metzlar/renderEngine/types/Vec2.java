package net.metzlar.renderEngine.types;

public class Vec2 {
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
}
