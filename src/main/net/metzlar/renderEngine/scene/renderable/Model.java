package net.metzlar.renderEngine.scene.renderable;

import net.metzlar.renderEngine.scene.material.Material;
import net.metzlar.renderEngine.types.Angle;
import net.metzlar.renderEngine.types.Intersection;
import net.metzlar.renderEngine.types.Ray;
import net.metzlar.renderEngine.types.Vec3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Model extends Renderable {
    private List<Face> faces = new ArrayList<>();
    private KDTree tree;

    public Model(Vec3 position, Angle orientation, Material material) {
        super(position, orientation, material);
    }

    public void addFaces(List<Face> faces) {
        this.faces.addAll(faces);
    }

    @Override
    public Intersection intersectRay(Ray r) {
        List<Face> faceToIntersect = tree.traverseTree(r);

        if (faceToIntersect != null) {
            return Renderable.closestIntersection(r, this.faces);
        }

        return null;
    }

    public void makeTree() {
        this.tree = new KDTree(getBoundingBox());
    }

    private AABB getBoundingBox() {
        double xMin, yMin, zMin;
        xMin = yMin = zMin = Double.MAX_VALUE;
        double xMax, yMax, zMax;
        xMax = yMax = zMax = Double.MIN_VALUE;

        for (Face face : faces) {
            for (Vec3 vertex : face.getVertices()) {
                xMin = Math.min(xMin, vertex.getX());
                yMin = Math.min(yMin, vertex.getY());
                zMin = Math.min(zMin, vertex.getZ());

                xMax = Math.max(xMax, vertex.getX());
                yMax = Math.max(yMax, vertex.getY());
                zMax = Math.max(zMax, vertex.getZ());
            }
        }

        return new AABB(
                new Vec3(xMin, yMin, zMin),
                new Vec3(xMax, yMax, zMax)
        );
    }

    private class KDTree implements Serializable {
        private KDNode root;

        private KDTree(AABB boundingBox) {
            this.root = new KDNode(boundingBox, 0, faces);
        }

        List<Face> traverseTree(Ray r) {
            return root.traverse(r);
        }
    }

    private class KDNode implements Serializable {
        private AABB box;
        private List<Face> faces;
        private int depth;
        private KDNode left = null;
        private KDNode right = null;

        KDNode(AABB boundingBox, int depth, List<Face> faces) {
            this.box = boundingBox;
            this.faces = faces;
            this.depth = depth;

            //System.out.printf("Depth: %d, faces: %d\n", depth, faces.size());

            if (depth < 200 && faces.size() > 1) {
                split();
            }
        }

        void split() {
            int axis = depth % 3;

            double[] faceAxisAverages;
            double median;
            AABB[] leftRight;
            List<Face> leftFaces;
            List<Face> rightFaces;

            switch (axis) {
                case 0: // split over X
                    faceAxisAverages = this.faces.stream()
                            .mapToDouble(
                                    // average of vertex X per face
                                    face -> Arrays.stream(face.getVertices())
                                            .mapToDouble(Vec3::getX).average().getAsDouble()
                            ).sorted().toArray();
                    median = faceAxisAverages[faceAxisAverages.length / 2]; // we split over the median

                    leftRight = this.box.splitOverX(median);

                    leftFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getX() <= median))
                            .collect(Collectors.toList());
                    rightFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getX() > median))
                            .collect(Collectors.toList());

                    this.left = new KDNode(leftRight[0], this.depth + 1, leftFaces);
                    this.right = new KDNode(leftRight[1], this.depth + 1, rightFaces);
                    break;

                case 1: // Split over Y
                    faceAxisAverages = this.faces.stream()
                            .mapToDouble(
                                    // average of vertex Y per face
                                    face -> Arrays.stream(face.getVertices())
                                            .mapToDouble(Vec3::getY).average().getAsDouble()
                            ).sorted().toArray();
                    median = faceAxisAverages[faceAxisAverages.length / 2]; // we split over the median

                    leftRight = this.box.splitOverY(median);

                    leftFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getY() <= median))
                            .collect(Collectors.toList());
                    rightFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getY() > median))
                            .collect(Collectors.toList());

                    this.left = new KDNode(leftRight[0], this.depth + 1, leftFaces);
                    this.right = new KDNode(leftRight[1], this.depth + 1, rightFaces);
                    break;

                case 2: // Split over Z
                    faceAxisAverages = this.faces.stream()
                            .mapToDouble(
                                    // average of vertex Y per face
                                    face -> Arrays.stream(face.getVertices())
                                            .mapToDouble(Vec3::getZ).average().getAsDouble()
                            ).sorted().toArray();
                    median = faceAxisAverages[faceAxisAverages.length / 2]; // we split over the median

                    leftRight = this.box.splitOverZ(median);

                    leftFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getZ() <= median))
                            .collect(Collectors.toList());
                    rightFaces = this.faces.stream()
                            .filter(face -> Arrays.stream(face.getVertices()).noneMatch(vec3 -> vec3.getZ() > median))
                            .collect(Collectors.toList());

                    this.left = new KDNode(leftRight[0], this.depth + 1, leftFaces);
                    this.right = new KDNode(leftRight[1], this.depth + 1, rightFaces);
                    break;
            }

            if (this.left.faces.size() == 0) this.left = null;
            if (this.right.faces.size() == 0) this.right = null;
        }

        List<Face> traverse(Ray r) {
            if (this.left == null && this.right == null) return faces;

            if (this.box.intersectRay(r) == null) return null;

            if (this.left != null) {
                List<Face> left = this.left.traverse(r);
                if (left != null)
                    return left;
            }

            return this.right.traverse(r);
        }
    }
}
