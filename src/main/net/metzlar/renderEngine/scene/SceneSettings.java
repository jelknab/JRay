package net.metzlar.renderEngine.scene;

import net.metzlar.renderEngine.Render;
import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.scene.light.Light;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SceneSettings implements Serializable {
    public ArrayList<Intersectable> renderables;
    public HashMap<String, Material> materials;
    public Camera camera;
    public ArrayList<Light> lights;

    public SceneSettings() {
        this.renderables = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.materials = new HashMap<>();
    }

    /**
     * @param ray ray to check intersections with
     * @param render render for updating intersection totals
     * @return null for no intersection or the closest intersected object.
     */
    public Intersection intersectScene(Ray ray, Render render) {
        return Intersectable.closestIntersection(ray, this.renderables);
    }

    public void addRenderable(Intersectable obj) {
        this.renderables.add(obj);
    }

    public void addMaterial(String name, Material material) {
        this.materials.put(name, material);
    }

    public void addLight(Light light) {
        lights.add(light);
    }
}
