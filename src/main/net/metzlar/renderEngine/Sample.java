package net.metzlar.renderEngine;

import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;

/**
 * Class that helps with setting up the render queue by avoiding stack overflows from recursive function calling
 * Every time a bunch of rays need to be cast it should add a sample with said ray to the render queue
 */
public class Sample {
    private Ray ray;
    private Sample parent;
    private double contribution;
    private Color color = Color.BLACK;
    private int depth = 0;

    public Sample(Ray ray, Sample parent, double contribution) {
        this.ray = ray;
        this.parent = parent;
        this.contribution = contribution;

        if (parent != null) {
            depth = parent.depth + 1;
        }
    }

    public void render(Render render) {
        Intersection intersection = render.sceneSettings.intersectScene(this.ray, render);

        if (intersection != null) {
            this.color = intersection.renderable
                    .material
                    .render(intersection, render, this)
                    .multiply(this.contribution);
        }
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public void addColorToParent() {
        if (this.parent == null) return;

        this.parent.addColor(this.color);
    }

    private void addColor(Color color) {
        this.color = this.color.add(color);
    }
}
