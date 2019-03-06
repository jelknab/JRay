package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.SceneSettings;
import net.metzlar.renderEngine.types.Color;

import java.util.Stack;

public class Render {
    private static final int MAX_DEPTH = 255;

    public SceneSettings sceneSettings;
    private Sample cameraSample;
    private Stack<Sample> sampleStack = new Stack<>();
    private Stack<Sample> finishedSampleStack = new Stack<>();
    private Statistics statistics;

    public Render(SceneSettings sceneSettings, Sample cameraSample) {
        this.sceneSettings = sceneSettings;
        this.cameraSample = cameraSample;

        this.sampleStack.push(cameraSample);

        this.statistics = new Statistics();
    }

    public void addSample(Sample sample) {
        if (sample.getDepth() > MAX_DEPTH) return;

        this.sampleStack.push(sample);
    }

    private void renderSample() {
        Sample next = sampleStack.pop();
        next.render(this);
        finishedSampleStack.push(next);
    }

    public Color render() {
        //long startTimestamp = System.currentTimeMillis();

        while (!sampleStack.empty()) {
            renderSample();
        }

        //statistics.addRunTime(System.currentTimeMillis() - startTimestamp);

        while (!finishedSampleStack.empty()) {
            finishedSampleStack.pop().addColorToParent();
        }

        return cameraSample.getColor();
    }

    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public String toString() {
        return String.format("Render, stack_depth = %d", this.sampleStack.size());
    }
}
