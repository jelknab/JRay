package net.metzlar.renderEngine.types;

import java.io.Serializable;
import java.util.Locale;

public class Vec3 implements Serializable {
    private float x;
    private float y;
    private float z;

    public Vec3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vec3(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public double norm() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return Math.sqrt(this.norm());
    }

    public Vec3 subtract(Vec3 b) {
        return new Vec3(x - b.x, y - b.y, z - b.z);
    }

    public Vec3 add(Vec3 b) {
        return new Vec3(x + b.x, y + b.y, z + b.z);
    }

    public Vec3 multiply(Vec3 b) {
        return new Vec3(x * b.x, y * b.y, z * b.z);
    }

    public Vec3 multiply(double b) {
        return new Vec3(x * b, y * b, z * b);
    }

    public double dot(Vec3 b) {
        return x * b.x + y * b.y + z * b.z;
    }

    public Vec3 cross(Vec3 b) {
        return new Vec3(
                y * b.z - z * b.y,
                z * b.x - x * b.z,
                x * b.y - y * b.x
        );
    }

    public Vec3 divide(double b) {
        return new Vec3(x / b, y / b, z / b);
    }

    public Vec3 getNormalized() {
        double length = length();

        return new Vec3(x / length, y / length, z / length);
    }

    /**
     * Rotates a vector by radians over the x axis
     * @param theta theta to rotate with
     * @return rotated vector
     */
    public Vec3 rotateAxisX(double theta) {
        return new Vec3(
                this.x,
                this.y*Math.cos(theta) - z*Math.sin(theta),
                this.y*Math.sin(theta) + z*Math.cos(theta)
        );
    }

    /**
     * Rotates a vector by radians over the y axis
     * @param theta theta to rotate with
     * @return rotated vector
     */
    public Vec3 rotateAxisY(double theta) {
        return new Vec3(
                this.x*Math.cos(theta) + z*Math.sin(theta),
                this.y,
                this.z*Math.cos(theta) - this.x*Math.sin(theta)
        );
    }

    /**
     * Rotates a vector by radians over the z axis
     * @param theta theta to rotate with
     * @return rotated vector
     */
    public Vec3 rotateAxisZ(double theta) {
        return new Vec3(
                this.x*Math.cos(theta) - y*Math.sin(theta),
                this.x*Math.sin(theta) - y*Math.cos(theta),
                this.z
        );
    }

    public float getX() {
        return x;
    }

    public void setX(double x) {
        this.x = (float)x;
    }
    public void setX(float x) { this.x = x; }

    public float getY() {
        return y;
    }

    public void setY(double y) {
        this.y = (float)y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = (float)z;
    }
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Vector[%.3f, %.3f, %.3f]", x, y, z);
    }
}
