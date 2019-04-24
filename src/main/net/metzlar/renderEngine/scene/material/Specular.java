package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.SampleDirect;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

public class Specular extends Material {
    private String inputMaterialName;

    private Material baseMaterial;
    private double kd;
    private double kr;

    public Specular(String inputMaterialName, double kd, double kr) {
        super(null);
        this.inputMaterialName = inputMaterialName;
        this.kd = kd;
        this.kr = kr;
    }


    @Override
    public void init(Scene scene) {
        baseMaterial = scene.materials.get(inputMaterialName);

        if (baseMaterial == null) {
            baseMaterial = DEFAULT;
        }
    }

    @Override
    public Color render(Intersection intersection, Render render, SampleDirect sample) {
        Ray reflectedRay = new Ray(intersection.hitPos, reflect(intersection));

        SampleDirect reflectionSample = new SampleDirect(reflectedRay, sample, this.kr);

        render.addSample(reflectionSample);

        return baseMaterial.render(intersection, render, sample).multiply(this.kd);
    }

    @Override
    public Color getSurfaceColor(Intersection intersection) {
        return baseMaterial.getSurfaceColor(intersection).multiply(this.kd);
    }

    @Override
    public Color getIndirectDiffuse(Intersection intersection) {
        return baseMaterial.getIndirectDiffuse(intersection)
                .multiply(this.kd)
                .add(Color.WHITE.multiply(this.kr));
    }

    private Vec3 reflect(Intersection intersection) {
        Vec3 toCamera = intersection.ray.direction.multiply(-1);

        return intersection.normal
                .multiply(toCamera)
                .multiply(2)
                .multiply(intersection.normal)
                .subtract(toCamera);
    }
}
