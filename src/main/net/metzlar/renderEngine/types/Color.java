package net.metzlar.renderEngine.types;

import java.io.Serializable;

public class Color implements Serializable {
    public final static Color RED = new Color(1, 0, 0);
    public final static Color GREEN = new Color(0, 1, 0);
    public final static Color BLUE = new Color(0, 0, 1);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color WHITE = new Color(1, 1, 1);

    private final float r;
    private final float g;
    private final float b;

    public Color() {
        this(0, 0, 0);
    }

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color(String hex) {
        this(
                Integer.valueOf( hex.substring( 1, 3 ), 16 ) / 255f,
                Integer.valueOf( hex.substring( 3, 5 ), 16 ) / 255f,
                Integer.valueOf( hex.substring( 5, 7 ), 16 ) / 255f
        );

        if (hex.charAt(0) != '#'){
            throw new IllegalArgumentException("String color (hex) must start with #");
        }
    }

    public Color(double r, double g, double b) {
        this((float) r, (float) g, (float) b);
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public Color add(Color color) {
        return new Color(
                this.r + color.r,
                this.g + color.g,
                this.b + color.b
        );
    }

    public Color multiply(double amount) {
        return new Color(
                this.r * amount,
                this.g * amount,
                this.b * amount
        );
    }

    public Color multiply(Color color) {
        return new Color(
                this.r * color.r,
                this.g * color.g,
                this.b * color.b
        );
    }

    public Color divide(double amount) {
        return new Color(
                this.r / amount,
                this.g / amount,
                this.b / amount
        );
    }

    @Override
    public String toString() {
        return String.format("Color[r: %.0f, g: %.0f, b: %.0f]", r * 255, g * 255, b * 255);
    }


}
