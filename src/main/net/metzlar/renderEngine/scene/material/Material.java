package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.texture.SolidColor;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.Random;

/**
 * Material classes determine the appearance of an object given an intersection
 * All Materials should implement this class
 */
public abstract class Material implements Serializable {
    private static final int SAMPLES = 16;
    public static final Material DEFAULT = new Lambert(new SolidColor(Color.RED));

    private Texture texture;
    private String name;
    private Random random = new Random();

    public Material(Texture texture) {
        this.texture = texture;
    }

    public abstract void init(SceneSettings sceneSettings);

    public abstract Color render(Intersection intersection, Render render, Sample sample);

    public void illuminate(Intersection intersection, Render render, Sample sample) {
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


            render.addSample(
                    new Sample(
                            new Ray(intersection.hitPos, sampleWorld),
                            sample,
                            1d / SAMPLES
                    )
            );
        }
    }

    private Vec3 uniformSampleHemisphere(double r1, double r2) {
        double sinTheta = Math.sqrt(1 - r1 * r1);
        double phi = 2 * Math.PI * r2;
        double x = sinTheta * Math.cos(phi);
        double z = sinTheta * Math.sin(phi);
        return new Vec3(x, r1, z);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
