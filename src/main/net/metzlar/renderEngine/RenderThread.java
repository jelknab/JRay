package net.metzlar.renderEngine;

import net.metzlar.Settings;
import net.metzlar.network.client.RenderClient;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec2;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.Camera;
import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.settings.ImageSettings;

import java.util.Random;

public class RenderThread extends Thread {
    private RenderClient renderclient;
    private Vec2[][] subSampleJitter;

    Random random = new Random();

    public RenderThread(RenderClient client) {
        this.renderclient = client;
    }

    @Override
    public void run() {
        super.run();

        int subSamples = this.renderclient.client.imageSettings.subSamples;

        this.subSampleJitter = new Vec2[subSamples][subSamples];
        for (int y = 0; y < subSamples; y++)
            for (int x = 0; x < subSamples; x++)
                this.subSampleJitter[x][y] = jitterSubsample(x, y, subSamples);

        this.renderclient.requestTile();

        while (renderclient.hasActiveTile()) {
            render();

            this.renderclient.submitTile();
            this.renderclient.requestTile();
        }
    }

    private void render() {
        RenderTile tile = this.renderclient.activeTile;
        ImageSettings imageSettings = this.renderclient.client.imageSettings;
        SceneSettings sceneSettings = this.renderclient.client.sceneSettings;
        Camera camera = sceneSettings.camera;

        double fov = camera.getFov();
        double fovMulRatio = fov * imageSettings.aspectRatio;

        int position; // Represents position of pixel RELATIVE to the tile
        while ((position = this.renderclient.activeTile.takePosition()) !=-1) {
            Color color = new Color();

            //x and y represent the GLOBAL 2d position of the pixel
            int pixelX = tile.getStartX() + position % tile.getWidth();
            int pixelY = tile.getStartY() + position / tile.getWidth();

            for (int y = 0; y < imageSettings.subSamples; y++) {
                for (int x = 0; x < imageSettings.subSamples; x++) {
                    //Vec2 subSample = jitterSubsample(pixelX, pixelY, x, y, settingsXML.getSubSamples());

                    //px and py are camera space coordinates
                    double px = (2 * ((pixelX + subSampleJitter[x][y].getX()) / imageSettings.imageWidth) - 1) * fovMulRatio;
                    double py = (1 - 2 * ((pixelY + subSampleJitter[x][y].getY()) / imageSettings.imageHeight)) * fov;

                    //Get an actual 3d direction vector of our camera space coordinates by multiplying them by the camera matrix
                    Vec3 direction = camera.getMatrix().multiply(new Vec3(px, py, -1));

                    //Create a ray from our camera
                    Ray cameraRay = new Ray(camera.getPosition(), direction);
                    Sample cameraSample = new Sample(cameraRay, null, 1);

                    color = color.add(
                            new Render(sceneSettings, cameraSample)
                                    .render()
                                    .multiply(1d / (imageSettings.subSamples*imageSettings.subSamples))
                    );
                }
            }



            tile.submit(position, color);
        }
    }

    private Vec2 jitterSubsample(int x, int y, int subSamples) {
        float newX = ((1f / subSamples) * x) + (random.nextFloat()/subSamples);
        float newY = ((1f / subSamples) * y) + (random.nextFloat()/subSamples);

        return new Vec2(newX, newY);
    }
}
