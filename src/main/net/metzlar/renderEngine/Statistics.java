package net.metzlar.renderEngine;

public class Statistics {
    private long intersections;
    private long runTimeMS;
    private long samples;

    public synchronized void merge(Statistics other) {
        this.intersections += other.intersections;
        this.runTimeMS += other.runTimeMS;
        this.samples += other.samples;
    }

    public long getIntersections() {
        return intersections;
    }

    public long getRunTimeMS() {
        return runTimeMS;
    }

    public long getSamples() {
        return samples;
    }

    public synchronized void addIntersections(long amount) {
        this.intersections += amount;
    }

    public synchronized void addRunTime(long millis) {
        this.runTimeMS += millis;
    }

    public synchronized void addSamples(long amount) {
        this.samples += amount;
    }

    @Override
    public String toString() {
        return String.format("Ran for %.2f seconds doing:\n\t%d Intersection tests (%.1f per second);\n\t%d samples.", runTimeMS/1000d, intersections, intersections / (runTimeMS/1000d), samples);
    }
}
