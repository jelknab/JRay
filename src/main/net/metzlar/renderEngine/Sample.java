package net.metzlar.renderEngine;

import javafx.scene.Parent;
import net.metzlar.renderEngine.types.Ray;

public abstract class Sample {
    Ray ray;
    Sample parent;
    int depth;

    public Sample(Ray ray, Sample parent, int depth) {
        this.ray = ray;
        this.parent = parent;
        this.depth = depth;
    }

    public abstract void render(Render render);

    public abstract void mergeToParent();

}
