package net.metzlar.renderEngine;

import net.metzlar.Settings;
import net.metzlar.network.client.RenderClient;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;
import net.metzlar.renderEngine.scene.Camera;
import net.metzlar.renderEngine.scene.Scene;

public class RenderThread extends Thread {
    private Statistics statistics;
    private RenderClient renderclient;

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
            //x and y represent the GLOBAL 2d position of the pixel
            int x = tile.getStartX() + position % tile.getWidth();
            int y = tile.getStartY() + position / tile.getWidth();

            //px and py are camera space coordinates
            double px = (2 * ((x + 0.5) / settings.getImageWidth()) - 1) * fovMulRatio;
            double py = (1 - 2 * ((y + 0.5) / settings.getImageHeight())) * fov;

            //Get an actual 3d direction vector of our camera space coordinates by multiplying them by the camera matrix
            Vec3 direction = camera.getMatrix().multiply(new Vec3(px, py, -1));

            //Create a ray from our camera
            Ray cameraRay = new Ray(camera.getPosition(), direction);
            Sample cameraSample = new Sample(cameraRay, null, 1);

            Render render = new Render(scene, cameraSample);

            tile.submit(position, render.render());

            renderclient.getClient().getStatistics().merge(render.getStatistics());
        }

        renderclient.getClient().getStatistics().addRunTime(System.currentTimeMillis() - renderStartTimestamp);
    }
}
