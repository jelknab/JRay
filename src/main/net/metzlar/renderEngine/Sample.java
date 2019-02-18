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
    private int depth;

    public Sample(Ray ray, Sample parent, double contribution) {
        this.ray = ray;
        this.parent = parent;
        this.contribution = contribution;

        if (parent != null) {
            depth = parent.depth + 1;
        } else {
            depth = 0;
        }
    }

    public void render(Render render) {
        Intersection intersection = render.getScene().intersectScene(this.ray, render);

        if (intersection != null) {
            this.color = this.color.add(intersection.getRenderable().getMaterial().render(intersection, render, this));
        }

        if (parent != null) {
            parent.color = parent.getColor().add(this.color.multiply(this.contribution));
        }

        render.getStatistics().addSamples(1);
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }
}
