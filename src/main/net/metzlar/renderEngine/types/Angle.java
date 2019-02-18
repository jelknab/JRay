package net.metzlar.renderEngine.types;

public class Angle {
    private final double pitch, yaw, roll;

    public Angle() {
        this(0, 0, 0);
    }

    public Angle(double pitch, double yaw, double roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public double getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public double getRoll() {
        return roll;
    }
}
