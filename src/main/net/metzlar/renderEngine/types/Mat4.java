package net.metzlar.renderEngine.types;

import java.io.Serializable;

public class Mat4 implements Serializable {
    public double[] m;

    public Mat4() {
        m = new double[16];
    }

    public Mat4(double[] m) {
        this.m = m;
    }

    public void makeIdentity() {
        this.m[0] = 1.0f; this.m[1] = 0.0f; this.m[2] = 0.0f; this.m[3] = 0.0f;
        this.m[4] = 0.0f; this.m[5] = 1.0f; this.m[6] = 0.0f; this.m[7] = 0.0f;
        this.m[8] = 0.0f; this.m[9] = 0.0f; this.m[10] = 1.0f; this.m[11] = 0.0f;
        this.m[12] = 0.0f; this.m[13] = 0.0f; this.m[14] = 0.0f; this.m[15] = 1.0f;
    }

    public Mat4 multiply(Mat4 other) {
        return new Mat4(
                new double[] {
                        this.m[0]*other.m[0] + this.m[4]*other.m[1] + this.m[8]*other.m[2] + this.m[12]*other.m[3],
                        this.m[1]*other.m[0] + this.m[5]*other.m[1] + this.m[9]*other.m[2] + this.m[13]*other.m[3],
                        this.m[2]*other.m[0] + this.m[6]*other.m[1] + this.m[10]*other.m[2] + this.m[14]*other.m[3],
                        this.m[3]*other.m[0] + this.m[7]*other.m[1] + this.m[11]*other.m[2] + this.m[15]*other.m[3],

                        this.m[0]*other.m[4] + this.m[4]*other.m[5] + this.m[8]*other.m[6] + this.m[12]*other.m[7],
                        this.m[1]*other.m[4] + this.m[5]*other.m[5] + this.m[9]*other.m[6] + this.m[13]*other.m[7],
                        this.m[2]*other.m[4] + this.m[6]*other.m[5] + this.m[10]*other.m[6] + this.m[14]*other.m[7],
                        this.m[3]*other.m[4] + this.m[7]*other.m[5] + this.m[11]*other.m[6] + this.m[15]*other.m[7],

                        this.m[0]*other.m[8] + this.m[4]*other.m[9] + this.m[8]*other.m[10] + this.m[12]*other.m[11],
                        this.m[1]*other.m[8] + this.m[5]*other.m[9] + this.m[9]*other.m[10] + this.m[13]*other.m[11],
                        this.m[2]*other.m[8] + this.m[6]*other.m[9] + this.m[10]*other.m[10] + this.m[14]*other.m[11],
                        this.m[3]*other.m[8] + this.m[7]*other.m[9] + this.m[11]*other.m[10] + this.m[15]*other.m[11],

                        this.m[0]*other.m[12] + this.m[4]*other.m[13] + this.m[8]*other.m[14] + this.m[12]*other.m[15],
                        this.m[1]*other.m[12] + this.m[5]*other.m[13] + this.m[9]*other.m[14] + this.m[13]*other.m[15],
                        this.m[2]*other.m[12] + this.m[6]*other.m[13] + this.m[10]*other.m[14] + this.m[14]*other.m[15],
                        this.m[3]*other.m[12] + this.m[7]*other.m[13] + this.m[11]*other.m[14] + this.m[15]*other.m[15],
                }
        );
    }

    public Vec3 multiply(Vec3 rhs) {
        double a, b, c, w;

        a = rhs.getX() * m[0] + rhs.getY() * m[4] + rhs.getZ() * m[8] + m[12];
        b = rhs.getX() * m[1] + rhs.getY() * m[5] + rhs.getZ() * m[9] + m[13];
        c = rhs.getX() * m[2] + rhs.getY() * m[6] + rhs.getZ() * m[10] + m[14];
        w = rhs.getX() * m[3] + rhs.getY() * m[7] + rhs.getZ() * m[11] + m[15];

        return new Vec3(a/w, b/w, c/w);
    }

    private Mat4 rotate(float angle, double x, double y, double z) {
        double c = Math.cos(Math.toRadians(angle));
        double s = Math.sin(Math.toRadians(angle));
        double xx = x * x;
        double xy = x * y;
        double xz = x * z;
        double yy = y * y;
        double yz = y * z;
        double zz = z * z;

        // build rotation matrix
        Mat4 rotation = new Mat4();
        double[] m = rotation.m;
        m[0] = xx * (1 - c) + c;
        m[1] = xy * (1 - c) - z * s;
        m[2] = xz * (1 - c) + y * s;
        m[3] = 0;
        m[4] = xy * (1 - c) + z * s;
        m[5] = yy * (1 - c) + c;
        m[6] = yz * (1 - c) - x * s;
        m[7] = 0;
        m[8] = xz * (1 - c) - y * s;
        m[9] = yz * (1 - c) + x * s;
        m[10] = zz * (1 - c) + c;
        m[11] = 0;
        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;

        // multiply it
        Mat4 res = rotation.multiply(this);
        this.m = res.m;

        return this;
    }

    public Mat4 rotate(float angle, Vec3 axis) {
        return rotate(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    public Mat4 translate(float x, float y, float z) {
        m[12] += x;
        m[13] += y;
        m[14] += z;

        return this;
    }

    public void translate(Vec3 xyz) {
        m[12] += xyz.getX();
        m[13] += xyz.getY();
        m[14] += xyz.getZ();

    }

    public void rotateX(double angle) {
        double c = Math.cos(Math.toRadians(angle));
        double s = Math.sin(Math.toRadians(angle));
        double m4 = m[4], m5 = m[5], m6 = m[6], m7 = m[7],
                m8 = m[8], m9 = m[9], m10 = m[10], m11 = m[11];

        m[4] = m4 * c + m8 * -s;
        m[5] = m5 * c + m9 * -s;
        m[6] = m6 * c + m10 * -s;
        m[7] = m7 * c + m11 * -s;
        m[8] = m4 * s + m8 * c;
        m[9] = m5 * s + m9 * c;
        m[10] = m6 * s + m10 * c;
        m[11] = m7 * s + m11 * c;

    }

    public void rotateY(double angle) {
        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));

        double m0 = m[0], m1 = m[1], m2 = m[2], m3 = m[3],
                m8 = m[8], m9 = m[9], m10 = m[10], m11 = m[11];

        m[0] = m0 * c + m8 * s;
        m[1] = m1 * c + m9 * s;
        m[2] = m2 * c + m10 * s;
        m[3] = m3 * c + m11 * s;
        m[8] = m0 * -s + m8 * c;
        m[9] = m1 * -s + m9 * c;
        m[10] = m2 * -s + m10 * c;
        m[11] = m3 * -s + m11 * c;

    }

    public void rotateZ(double angle) {
        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));

        double m0 = m[0], m1 = m[1], m2 = m[2], m3 = m[3],
                m4 = m[4], m5 = m[5], m6 = m[6], m7 = m[7];

        m[0] = m0 * c + m4 * -s;
        m[1] = m1 * c + m5 * -s;
        m[2] = m2 * c + m6 * -s;
        m[3] = m3 * c + m7 * -s;
        m[4] = m0 * s + m4 * c;
        m[5] = m1 * s + m5 * c;
        m[6] = m2 * s + m6 * c;
        m[7] = m3 * s + m7 * c;

    }

    @Override
    public String toString() {
        String ret = "Matrix[\n\t";

        for (int i = 0; i < 4; i++) {
            for (int a = 0; a < 4; a++) {
                ret += Math.round(m[i * 4 + a] * 100) / 100f;

                if (a < 3) {
                    ret += ",\t";
                } else {
                    ret += "\n";
                }
            }

            if (i < 3) {
                ret += "\t";
            } else {
                ret += "]";
            }
        }

        return ret;
    }
}
