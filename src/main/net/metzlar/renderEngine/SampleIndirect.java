package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.light.Photon;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleIndirect extends Sample {
    public static final int SAMPLES = 4;
    public static final int MAX_DEPTH = 3;

    List<Photon> photons = new ArrayList<>((int) Math.pow(SAMPLES, (MAX_DEPTH+1) - depth));

    Intersection intersection;

    public SampleIndirect(Ray ray, Sample parent, int depth) {
        super(ray, parent, depth);
    }

    @Override
    public void render(Render render) {
        this.intersection = render.scene.intersect(ray, render);

        if (intersection != null) {
            render.scene.lights.forEach(
                    light -> {
                        Photon photon = light.getIntensity(render, intersection);

                        if (photon != null) {
                            photon.color = photon.color.multiply(intersection.renderable.material.getIndirectDiffuse(intersection));
                            this.photons.add(photon);
                        }
                    }
            );

            if (depth < MAX_DEPTH) {
                generateSamples(intersection, this, this.depth + 1).forEach(render::addSample);
            }
        }
    }


    @Override
    public void mergeToParent() {
        if (this.parent == null || photons.isEmpty()) return;

        ((SampleIndirect) parent).addPhotons(this.photons, this.intersection.distance);
    }

    public void addPhotons(List<Photon> photons, double distance) {
        photons.forEach(photon -> {
            photon.distanceTraveled += distance;

            if (photon.distanceTraveled < photon.light.intensity) {
                photon.color = photon.color.multiply(this.intersection.renderable.material.getIndirectDiffuse(intersection));
                this.photons.add(photon);
            }
        });
    }

    public static List<Sample> generateSamples(Intersection intersection, Sample parent, int depth) {
        Random random = new Random();
        List<Sample> samples = new ArrayList<>();

        Vec3 n = intersection.normal;
        Vec3 nt; // Vector perpendicular to normal
        if (n.getX() > n.getY()) {
            nt = new Vec3(n.getZ(), 0, -n.getX())
                    .divide( Math.sqrt( n.getX()*n.getX() + n.getZ()*n.getZ() ) );
        } else {
            nt = new Vec3(0, -n.getZ(), n.getY())
                    .divide( Math.sqrt( n.getY()*n.getY() + n.getZ()*n.getZ() ) );
        }
        Vec3 nb = n.cross(nt);

        for (int i = 0; i < SAMPLES; i++) {
            double r1 = random.nextDouble();
            double r2 = random.nextDouble();

            Vec3 sh = uniformSampleHemisphere(r1, r2);
            Vec3 sampleWorld = new Vec3(
                    sh.getX() * nb.getX() + sh.getY() * n.getX() + sh.getZ() * nt.getX(),
                    sh.getX() * nb.getY() + sh.getY() * n.getY() + sh.getZ() * nt.getY(),
                    sh.getX() * nb.getZ() + sh.getY() * n.getZ() + sh.getZ() * nt.getZ()
            );


            samples.add(
                    new SampleIndirect(
                            new Ray(intersection.hitPos, sampleWorld),
                            parent,
                            depth
                    )
            );
        }

        return samples;
    }

    private static Vec3 uniformSampleHemisphere(double r1, double r2) {
        double sinTheta = Math.sqrt(1 - r1 * r1);
        double phi = 2 * Math.PI * r2;
        double x = sinTheta * Math.cos(phi);
        double z = sinTheta * Math.sin(phi);
        return new Vec3(x, r1, z);
    }
}
