package net.metzlar.renderEngine.scene.material;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.Sample;
import net.metzlar.renderEngine.scene.texture.Texture;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.scene.light.Light;


public class Lambert extends Material {

    public Lambert(Texture texture) {
        super(texture);
    }

    @Override
    public void init(SceneSettings sceneSettings) {

    }

    @Override
    public Color render(Intersection intersection, Render render, Sample sample) {
        Color color = Color.BLACK;

        Color surfaceColor = this.getTexture().getColorAtPosition(intersection.texturePos);

        for (Light light : render.sceneSettings.lights) {
            Color intensity = light.getNormIntensity(render, intersection);

            if (intensity != Color.BLACK) {
                Vec3 light2PointDir = light.position.subtract(intersection.hitPos).getNormalized();

                double shading = light2PointDir.dot(intersection.normal);


                if (shading > 0)
                    color = color.add(surfaceColor.multiply(shading).multiply(intensity));
            }
        }

        return color;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }
}
