package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.types.Color;

import java.util.Stack;

public class Render {
    private static final int MAX_DEPTH = 2;

    private Scene scene;
    private Sample cameraSample;
    private Stack<Sample> sampleStack;
    private Statistics statistics;

    public Render(Scene scene, Sample cameraSample) {
        this.scene = scene;
        this.cameraSample = cameraSample;

        this.sampleStack = new Stack<>();
        this.sampleStack.push(cameraSample);

        this.statistics = new Statistics();
    }

    public void addSample(Sample sample) {
        if (sample.getDepth() > MAX_DEPTH) return;

        this.sampleStack.push(sample);
    }

    private void renderSample() {
        sampleStack.pop().render(this);
    }

    public Color render() {
        long startTimestamp = System.currentTimeMillis();

        while (!sampleStack.empty()) {
            renderSample();
        }

        statistics.addRunTime(System.currentTimeMillis() - startTimestamp);

        return cameraSample.getColor();
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public String toString() {
        return String.format("Render, stack_depth = %d", this.sampleStack.size());
    }
}
