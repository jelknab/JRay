package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.types.Color;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

public class Render {
    private static final int MAX_DEPTH = 255;

    private Scene scene;
    private Sample cameraSample;
    private Stack<Sample> sampleStack;
    private Stack<Sample> finishedSampleStack;
    private Statistics statistics;

    public Render(Scene scene, Sample cameraSample) {
        this.scene = scene;
        this.cameraSample = cameraSample;

        this.sampleStack = new Stack<>();
        this.finishedSampleStack = new Stack<>();
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

        //todo: keep a better list so sorting and reversing of samples is not needed.
        finishedSampleStack.stream().sorted(Comparator.comparingInt(Sample::getDepth).reversed()).forEach(Sample::addColorToParent);

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
