package net.metzlar.parsers;

import net.metzlar.parsers.lights.LightParserController;
import net.metzlar.parsers.materials.MaterialParserController;
import net.metzlar.parsers.renderables.RenderableParserController;
import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.Camera;
import net.metzlar.renderEngine.scene.light.Light;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Jelle-laptop on 19-Feb-17.
 */
public class SceneParser {
    private Scene scene;

    public SceneParser() {
        this.scene = new Scene();
    }

    public Scene parse(Element sceneElement) {

        // Get Materials
        Elements materialElements = sceneElement.select("materials > material");
        for (Element materialElement : materialElements) {
            Material material = new MaterialParserController(this.scene).parse(materialElement);

            if (material != null) {
                String name = materialElement.attr("name");
                scene.addMaterial(name, material);
                material.setName(name);
            }
        }

        //Get renderables
        Elements objectElements = sceneElement.select("objects > renderable");
        for (Element objectElement : objectElements) {
            Intersectable renderable = new RenderableParserController(this.scene).parse(objectElement);

            if (renderable != null) {
                scene.addRenderable(renderable);
            }
        }

        //Get Lights
        Elements lightElements = sceneElement.select("lights > light");
        for (Element lightElement : lightElements) {
            Light light = new LightParserController(this.scene).parse(lightElement);

            if (light != null) {
                scene.addLight(light);
            }
        }

        //Get camera settingsXML
        this.scene.camera = new Camera(
                new Vec3(
                        Double.parseDouble(sceneElement.select("camera > position > x").html()),
                        Double.parseDouble(sceneElement.select("camera > position > y").html()),
                        Double.parseDouble(sceneElement.select("camera > position > z").html())
                ),
                Double.parseDouble(sceneElement.select("camera > angle > pitch").html()),
                Double.parseDouble(sceneElement.select("camera > angle > yaw").html()),
                Double.parseDouble(sceneElement.select("camera > angle > roll").html()),
                Double.parseDouble(sceneElement.select("camera > fov").html())
        );

        //Init materials
        scene.materials.forEach((s, material) -> material.init(scene));

        return this.scene;
    }
}