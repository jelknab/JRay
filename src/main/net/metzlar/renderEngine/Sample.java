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
    public double contributionToRoot;
    public Color color = Color.BLACK;
    public int depth = 0;

    public Sample(Ray ray, Sample parent, double contribution) {
        this.ray = ray;
        this.parent = parent;
        this.contribution = contribution;

        if (parent != null) {
            this.contributionToRoot = parent.contributionToRoot * contribution;
            this.depth = parent.depth + 1;
        } else {
            this.contributionToRoot = 1d;
        }
    }

    public void render(Render render) {
        Intersection intersection = render.sceneSettings.intersectScene(this.ray, render);

        if (intersection != null) {
            this.color = intersection.renderable.material
                    .render(intersection, render, this);
        }
    }

    public void addColorToParent() {
        if (this.parent == null) return;

        this.parent.addColor(this.color.multiply(this.contribution));
    }

    private void addColor(Color color) {
        this.color = this.color.add(color);
    }
}
