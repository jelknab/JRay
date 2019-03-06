package net.metzlar.renderEngine;

public class Statistics {
    public long intersections;
    public long runTimeMS;

    @Override
    public String toString() {
        return String.format("Ran for %.2f seconds doing:\n\t%d Intersection tests (%.1f per second);", runTimeMS/1000d, intersections, intersections / (runTimeMS/1000d));
    }
}
