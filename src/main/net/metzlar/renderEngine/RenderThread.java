package net.metzlar.renderEngine;

import net.metzlar.Settings;
import net.metzlar.network.client.RenderClient;
import net.metzlar.renderEngine.types.Color;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec2;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.Camera;
import net.metzlar.renderEngine.scene.Scene;

import java.util.Random;

public class RenderThread extends Thread {
    private Statistics statistics;
    private RenderClient renderclient;

    Random random = new Random();

    public RenderThread(RenderClient client) {
        this.renderclient = client;
    }

    @Override
    public void run() {
        super.run();

        renderclient.requestTile();

        while (renderclient.hasActiveTile()) {
            this.statistics = new Statistics();

            render();

            renderclient.submitTile();
            renderclient.requestTile();
        }
    }

    private void render() {
        Settings settings = this.renderclient.getClient().getSettings();
        RenderTile tile = this.renderclient.getActiveTile();
        Scene scene = settings.getScene();
        Camera camera = scene.getCamera();

        long renderStartTimestamp = System.currentTimeMillis();

        double fov = camera.getFov();
        double fovMulRatio = fov * settings.getAspectRatio();

        int position; // Represents position of pixel RELATIVE to the tile
        while ((position = this.renderclient.getActiveTile().takePosition()) !=-1) {
            Color color = new Color();

            //x and y represent the GLOBAL 2d position of the pixel
            int pixelX = tile.getStartX() + position % tile.getWidth();
            int pixelY = tile.getStartY() + position / tile.getWidth();

            for (int y = 0; y < settings.getSubSamples(); y++) {
                for (int x = 0; x < settings.getSubSamples(); x++) {
                    Vec2 subSample = jitterSubsample(pixelX, pixelY, x, y, settings.getSubSamples());
                    //System.out.println(subSample.getY());

                    //px and py are camera space coordinates
                    double px = (2 * ((subSample.getX()) / settings.getImageWidth()) - 1) * fovMulRatio;
                    double py = (1 - 2 * ((subSample.getY()) / settings.getImageHeight())) * fov;

                    //Get an actual 3d direction vector of our camera space coordinates by multiplying them by the camera matrix
                    Vec3 direction = camera.getMatrix().multiply(new Vec3(px, py, -1));

                    //Create a ray from our camera
                    Ray cameraRay = new Ray(camera.getPosition(), direction);
                    Sample cameraSample = new Sample(cameraRay, null, 1);

                    //System.out.println("..");

                    color = color.add(
                            new Render(scene, cameraSample)
                                    .render()
                                    .multiply(1d / (settings.getSubSamples()*settings.getSubSamples()))
                    );
                }
            }



            tile.submit(position, color);

            //renderclient.getClient().getStatistics().merge(render.getStatistics());
        }

        //renderclient.getClient().getStatistics().addRunTime(System.currentTimeMillis() - renderStartTimestamp);
    }

    private Vec2 jitterSubsample(int originalX, int originalY, int x, int y, int subSamples) {
        float newX = originalX + ((1f / subSamples) * x) + (random.nextFloat()/subSamples);
        float newY = originalY + ((1f / subSamples) * y) + (random.nextFloat()/subSamples);

        return new Vec2(newX, newY);
    }
}
