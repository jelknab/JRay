package net.metzlar.renderEngine;

import net.metzlar.renderEngine.scene.Scene;
import net.metzlar.renderEngine.scene.light.Photon;
import net.metzlar.renderEngine.types.Color;

import java.util.Stack;

public class Render {
    private static final int MAX_DEPTH = 16;

    public Scene scene;
    private Sample rootSample;
    private Stack<Sample> sampleStack = new Stack<>();
    private Stack<Sample> finishedSampleStack = new Stack<>();

    public Render(Scene scene, Sample rootSample) {
        this.scene = scene;
        this.rootSample = rootSample;

        this.sampleStack.push(rootSample);
    }

    public void addSample(Sample sample) {
        if (sample.depth > MAX_DEPTH) return;

        this.sampleStack.push(sample);
    }

    private void renderSample() {
        Sample next = sampleStack.pop();
        next.render(this);
        finishedSampleStack.push(next);
    }

    public Sample render() {
        while (!sampleStack.empty()) renderSample();
        while (!finishedSampleStack.empty()) finishedSampleStack.pop().mergeToParent();

        return rootSample;
    }

    @Override
    public String toString() {
        return String.format("Render, stack_depth = %d", this.sampleStack.size());
    }
}
