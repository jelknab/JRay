package net.metzlar.renderEngine.scene;

import net.metzlar.renderEngine.types.Mat4;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;


/**
 * Created by Jelle-laptop on 15-Feb-17.
 */
public class Camera implements Serializable {
    private Vec3 position;
    private double fov;

    private Mat4 matrix;

    public Camera() {
        this(new Vec3(), 0, 0, 0, 90);
    }

    public Camera(Vec3 position, double pitch, double yaw, double roll, double fov) {
        this.position = position;

        this.fov = Math.tan(fov / 2 * Math.PI / 180);

        this.matrix = new Mat4();
        matrix.makeIdentity();
        matrix.rotateY(yaw);
        matrix.rotateX(-pitch);
        matrix.rotateZ(roll);
    }

    public Vec3 getPosition() {
        return position;
    }

    public double getFov() {
        return fov;
    }

    public Mat4 getMatrix() {
        return matrix;
    }
}
