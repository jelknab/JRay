package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.light.Light;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Vec3;

public class Phong extends Material {

    private double kd; // Diffuse strength
    private double ks; // Specular strength
    private double n; // Shininess

    public Phong(Texture texture, double kd, double ks, double n) {
        super(texture);
        this.kd = kd;
        this.ks = ks;
        this.n = n;
    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        Color color = Color.BLACK;

        Color surfaceColor = getTexture().getColorAtPosition(intersection.getTexturePos());

        for (Light light : render.getScene().getLights()) {
            Color intensity = light.getIntensity(render, intersection);

            if (intensity != Color.BLACK) {
                Vec3 light2PointDir = light.getPosition().subtract(intersection.getHitPos()).getNormalized();

                // Diffuse
                double shading = light2PointDir.dot(intersection.getNormal());
                Color diffuse = surfaceColor.multiply(shading).multiply(intensity);

                // Specular highlight
                // specular += vis * lightIntensity * std::pow(std::max(0.f, R.dotProduct(-dir)), isect.hitObject->n);
                Vec3 r = reflect(intersection, light2PointDir);
                Color specular = light.getColor().multiply(Math.pow(Math.max(0, r.dot(intersection.getRay().getDirection())), n));

                if (shading > 0)
                    color = color
                            .add(diffuse.multiply(this.kd))
                            .add(specular.multiply(this.ks));
            }
        }

        return color;
    }

    private Vec3 reflect(Intersection intersection, Vec3 lightDirection) {
        return lightDirection.subtract(
                intersection.getNormal().multiply(
                        2 * lightDirection.dot(intersection.getNormal())
                )
        );
    }
}
