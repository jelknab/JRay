package net.metzlar.renderEngine.scene.renderable.mesh;

import net.metzlar.renderEngine.scene.renderable.Intersectable;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class KDNode implements Serializable {
    @SuppressWarnings({"unchecked"})
    private static final ToDoubleFunction<Vec3>[] VECTOR_AXIS_TO_DOUBLE_METHODS = new ToDoubleFunction[] {
            vector -> ((Vec3)vector).getX(),
            vector -> ((Vec3)vector).getY(),
            vector -> ((Vec3)vector).getZ(),
    };

    public boolean leaf = false;
    public int depth;
    public AABB boundingBox;

    public List<Face> faces;
    public KDNode leftNode;
    public KDNode rightNode;

    public KDNode(int depth, List<Face> faces) {
        this.depth = depth;
        this.faces = faces;

        this.boundingBox = new AABB(faces);

        if (this.faces.size() > 4) {
            split();
        } else {
            leaf = true;
        }
    }

    public void split() {
        int fails = 0;
        List<Face> leftNodes;
        List<Face> rightNodes;

        do {
            int axis = (this.depth + fails) % 3;
            if (fails++ == 3) {
                leaf = true;
                return;
            }

            ToDoubleFunction<Vec3> toDoubleFunction = VECTOR_AXIS_TO_DOUBLE_METHODS[axis]; // Array is static and always filed with ToDoubleFunction<Vec3> objects.

            double median = findMedian(toDoubleFunction);
            leftNodes = faces.stream().parallel().filter(face -> leftOfMedian(face, median, toDoubleFunction)).collect(Collectors.toList());
            rightNodes = faces.stream().parallel().filter(face -> rightOfMedian(face, median, toDoubleFunction)).collect(Collectors.toList());
        } while (leftNodes.size() == 0 || rightNodes.size() == 0);

        this.leftNode = new KDNode(this.depth + 1, leftNodes);
        this.rightNode = new KDNode(this.depth + 1, rightNodes);
    }

    public Intersection traverse(Ray r) {
        if (leaf) return Intersectable.closestIntersection(r, this.faces);


        Intersection near = null;
        Intersection far = null;
        if (this.leftNode.boundingBox.intersectRay(r) != null) {
            near = this.leftNode.traverse(r);
        }
        if (this.rightNode.boundingBox.intersectRay(r) != null) {
            far = this.rightNode.traverse(r);
        }

        if (near != null) {
            if (far != null) {
                if (far.distance < near.distance) {
                    return far;
                } else {
                    return near;
                }
            }
            return near;
        }

        return far;
    }

    private double findMedian(ToDoubleFunction<Vec3> toDoubleFunction) {
        double[] sorted = faces.stream().parallel().mapToDouble(face -> Arrays.stream(face.getVertices()).mapToDouble(toDoubleFunction).average().getAsDouble()).sorted().toArray();
        return sorted[sorted.length/2];
    }

    private boolean leftOfMedian(Face face, double median, ToDoubleFunction<Vec3> toDoubleFunction) {
        return Arrays.stream(face.getVertices()).mapToDouble(toDoubleFunction).average().getAsDouble() <= median;
    }

    private boolean rightOfMedian(Face face, double median, ToDoubleFunction<Vec3> toDoubleFunction) {
        return Arrays.stream(face.getVertices()).mapToDouble(toDoubleFunction).average().getAsDouble() > median;
    }
}
