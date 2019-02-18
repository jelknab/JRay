package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
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
        baseMaterial = scene.getMaterial(inputMaterialName);

        if (baseMaterial == null) {
            baseMaterial = DEFAULT;
        }
    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        Ray reflectedRay = new Ray(intersection.getHitPos().add(intersection.getNormal()), reflect(intersection));
        Sample reflectionSample = new Sample(reflectedRay, sample, this.kr);
        render.addSample(reflectionSample);

        return baseMaterial.render(intersection, render, sample).multiply(this.kd);
    }

    private Vec3 reflect(Intersection intersection) {
        Vec3 toCamera = intersection.getRay().getDirection().multiply(-1);

        return intersection.getNormal()
                .multiply(toCamera)
                .multiply(2)
                .multiply(intersection.getNormal())
                .subtract(toCamera);
    }
}
