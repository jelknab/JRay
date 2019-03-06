package net.metzlar.renderEngine.scene;

import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;

public class Object3D implements Serializable {
    public Vec3 position;
    public Vec3 orientation;

    public Object3D(Vec3 position, Vec3 orientation) {
        this.position = position;
        this.orientation = orientation;
    }
}
