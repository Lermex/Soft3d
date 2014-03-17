package net.lermex.soft3d.math;

public class Vector3 {
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 UNIT_X = new Vector3(1, 0, 0);
    public static final Vector3 UNIT_Y = new Vector3(0, 1, 0);
    public static final Vector3 UNIT_Z = new Vector3(0, 0, 1);

    private final float x;
    private final float y;
    private final float z;

    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double norm() {
        return Math.sqrt(x*x+y*y+z*z);
    }

    public Vector3 normalize() {
        final double d = norm();
        return new Vector3((float) (x / d), (float) (y / d), (float) (z / d));
    }

    public Vector3 subtract(final Vector3 v) {
        return new Vector3(x - v.getX(), y - v.getY(), z - v.getZ());
    }

    public static Vector3 cross (final Vector3 v1, final Vector3 v2) {
        return new Vector3 ((v1.getY()*v2.getZ()) - (v1.getZ()*v2.getY()),
                (v1.getZ()*v2.getX()) - (v1.getX()*v2.getZ()),
                (v1.getX()*v2.getY()) - (v1.getY()*v2.getX()));
    }

    public static double dot(final Vector3 v1, final Vector3 v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public static Vector3 transformCoordinate(Vector3 coord, Matrix transform) {
        final double X = (((coord.getX() * transform.getData()[0][0]) + (coord.getY() * transform.getData()[1][0])) + (coord.getZ() * transform.getData()[2][0])) + transform.getData()[0][0];
        final double Y = (((coord.getX() * transform.getData()[0][1]) + (coord.getY() * transform.getData()[1][1])) + (coord.getZ() * transform.getData()[2][1])) + transform.getData()[3][1];
        final double Z = (((coord.getX() * transform.getData()[0][2]) + (coord.getY() * transform.getData()[1][2])) + (coord.getZ() * transform.getData()[2][2])) + transform.getData()[3][2];
        final double W = 1 / ((((coord.getX() * transform.getData()[0][3]) + (coord.getY() * transform.getData()[1][3])) + (coord.getZ() * transform.getData()[2][3])) + transform.getData()[3][3]);

        Vector3 result = new Vector3( (float) (X * W), (float) (Y * W), (float) (Z * W));
        //System.out.println("x: " + X + " y: " + Y + "z: " + Z + "w: " + W);

        return result;
    }

    @Override
    public String toString() {
        return "\n[" + getX() + " " + getY() + " " + getZ() + "]\n";
    }
}
