package net.metzlar.renderEngine.scene;

import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;

public class Object3D implements Serializable {
    private Vec3 position;

    public Object3D(Vec3 position) {
        this.position = position;
    }


    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }
}
