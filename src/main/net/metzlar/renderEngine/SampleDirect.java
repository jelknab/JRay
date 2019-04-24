package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.light.Photon;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;

/**
 * Class that helps with setting up the render queue by avoiding stack overflows from recursive function calling
 * Every time a bunch of rays need to be cast it should add a sample with said ray to the render queue
 */
public class SampleDirect extends Sample {
    private double contribution;
    public double contributionToRoot;

    public Color color = Color.BLACK;
    public Intersection intersection;

    public SampleDirect(Ray ray, SampleDirect parent, double contribution) {
        super(ray, parent, 1);

        this.contribution = contribution;

        if (parent != null) {
            this.contributionToRoot = parent.contributionToRoot * contribution;
            this.depth = parent.depth + 1;
        }
    }

    @Override
    public void render(Render render) {
        this.intersection = render.scene.intersect(this.ray, render);

        if (intersection != null) {
            this.color = intersection.renderable.material.render(intersection, render, this);

            Render indirectRender = new Render(render.scene, new SampleIndirect(this.ray, null, 0));
            SampleIndirect indirectSample = (SampleIndirect) indirectRender.render();

            Color surfaceDiffuse = this.intersection.renderable.material.getSurfaceColor(this.intersection);
            Color indirectColor = Color.BLACK;

            for (Photon photon : indirectSample.photons) {

                indirectColor = indirectColor.add(surfaceDiffuse.multiply(photon.light.getColor().multiply(photon.color).multiply(1 - (photon.distanceTraveled / photon.light.intensity))));
            }

            this.color = this.color.add(indirectColor.divide(Math.pow(SampleIndirect.SAMPLES, SampleIndirect.MAX_DEPTH)));
        }
    }

    @Override
    public void mergeToParent() {
        if (this.parent == null) return;

        ((SampleDirect) this.parent).addColor(this.color.multiply(this.contribution));
    }

    private void addColor(Color color) {
        this.color = this.color.add(color);
    }
}
