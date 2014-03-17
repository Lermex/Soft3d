package net.lermex.soft3d.math;

public class Matrix {

    private final int m;
    private final int n;
    private final double[][] data;

    public Matrix(final int m, final int n) {
        this.m = m;
        this.n = n;
        data = new double[m][n];
    }

    public double[][] getData() {
        return data;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.n != B.m) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.m, B.n);
        for (int i = 0; i < C.m; i++)
            for (int j = 0; j < C.n; j++)
                for (int k = 0; k < A.n; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }

    public static Matrix LookAtLH(Vector3 cameraPosition, Vector3 cameraTarget, Vector3 cameraUpVector) {
        final Vector3 zaxis = cameraTarget.subtract(cameraPosition).normalize();
        final Vector3 xaxis = Vector3.cross(cameraUpVector, zaxis).normalize();
        final Vector3 yaxis = Vector3.cross(zaxis, xaxis);

        final Matrix result = new Matrix(4, 4);
        result.getData()[0][0] = xaxis.getX();
        result.getData()[0][1] = yaxis.getX();
        result.getData()[0][2] = zaxis.getX();
        result.getData()[0][3] = 0.0f;
        result.getData()[1][0] = xaxis.getY();
        result.getData()[1][1] = yaxis.getY();
        result.getData()[1][2] = zaxis.getY();
        result.getData()[1][3] = 0.0f;
        result.getData()[2][0] = xaxis.getZ();
        result.getData()[2][1] = yaxis.getZ();
        result.getData()[2][2] = zaxis.getZ();
        result.getData()[2][3] = 0.0f;

        result.getData()[3][0] = - Vector3.dot(xaxis, cameraPosition);
        result.getData()[3][1] = - Vector3.dot(yaxis, cameraPosition);
        result.getData()[3][2] = - Vector3.dot(zaxis, cameraPosition);
        result.getData()[3][3] = 1.0f;
        return result;
    }

    public static Matrix PerspectiveFovRH(float fovY, float aspect, float zn, float zf) {
        final double yScale = 1.0 / Math.tan( Math.toRadians(fovY/2.0) );
        final double xScale = yScale / aspect;

        final Matrix result = new Matrix(4, 4);
        result.getData()[0][0] = xScale;
        result.getData()[0][1] = 0.0f;
        result.getData()[0][2] = 0.0f;
        result.getData()[0][3] = 0.0f;
        result.getData()[1][0] = 0.0f;
        result.getData()[1][1] = yScale;
        result.getData()[1][2] = 0.0f;
        result.getData()[1][3] = 0.0f;
        result.getData()[2][0] = 0.0f;
        result.getData()[2][1] = 0.0f;
        result.getData()[2][2] = zf / (zn - zf);
        result.getData()[2][3] = -1;
        result.getData()[3][0] = 0.0f;
        result.getData()[3][1] = 0.0f;
        result.getData()[3][2] = zn * zf / (zn - zf);
        result.getData()[3][3] = 0.0f;
        return result;

//        final Matrix result = new Matrix(4, 4);
//        result.getData()[0][0] = xScale;
//        result.getData()[0][1] = 0.0f;
//        result.getData()[0][2] = 0.0f;
//        result.getData()[0][3] = 0.0f;
//        result.getData()[1][0] = 0.0f;
//        result.getData()[1][1] = yScale;
//        result.getData()[1][2] = 0.0f;
//        result.getData()[1][3] = 0.0f;
//        result.getData()[2][0] = 0.0f;
//        result.getData()[2][1] = 0.0f;
//        result.getData()[2][2] = zf / (zf - zn);
//        result.getData()[2][3] = 1;
//        result.getData()[3][0] = 0.0f;
//        result.getData()[3][1] = 0.0f;
//        result.getData()[3][2] = (-zn) * zf / (zf - zn) ;
//        result.getData()[3][3] = 0.0f;
//        return result;

//        final Matrix result = new Matrix(4, 4);
//        result.getData()[0][0] = 0.2f;
//        result.getData()[0][1] = 0.0f;
//        result.getData()[0][2] = 0.0f;
//        result.getData()[0][3] = 0.0f;
//        result.getData()[1][0] = 0.0f;
//        result.getData()[1][1] = 0.2f;
//        result.getData()[1][2] = 0.0f;
//        result.getData()[1][3] = 0.0f;
//        result.getData()[2][0] = 0.0f;
//        result.getData()[2][1] = 0.0f;
//        result.getData()[2][2] = 1 / (zf - zn);
//        result.getData()[2][3] = 0.0f;
//        result.getData()[3][0] = 0.0f;
//        result.getData()[3][1] = 0.0f;
//        result.getData()[3][2] = (-zn) / (zf - zn) ;
//        result.getData()[3][3] = 1.0f;
//        return result;
    }

    public static Matrix RotationYawPitchRoll(float x, float y, float z) { //TODO: not doing a proper rotation yet
        return getRotationX(x).times(getRotationY(y));
    }

    public static Matrix getRotationX(float angle) {
        final Matrix result = new Matrix(4, 4);
        float cos = (float)( Math.cos( Math.toRadians(angle) ) );
        float sin = (float)( Math.sin( Math.toRadians(angle) ) );

        result.getData()[0][0] = 1.0f;
        result.getData()[0][1] = 0.0f;
        result.getData()[0][2] = 0.0f;
        result.getData()[0][3] = 0.0f;
        result.getData()[1][0] = 0.0f;
        result.getData()[1][1] = cos;
        result.getData()[1][2] = sin;
        result.getData()[1][3] = 0.0f;
        result.getData()[2][0] = 0.0f;
        result.getData()[2][1] = -sin;
        result.getData()[2][2] = cos;
        result.getData()[2][3] = 0.0f;
        result.getData()[3][0] = 0.0f;
        result.getData()[3][1] = 0.0f;
        result.getData()[3][2] = 0.0f;
        result.getData()[3][3] = 1.0f;

        return result;
    }

    public static Matrix getRotationY(float angle) {
        final Matrix result = new Matrix(4, 4);
        float cos = (float)( Math.cos( Math.toRadians(angle) ) );
        float sin = (float)( Math.sin( Math.toRadians(angle) ) );

        result.getData()[0][0] = cos;
        result.getData()[0][1] = 0.0f;
        result.getData()[0][2] = -sin;
        result.getData()[0][3] = 0.0f;
        result.getData()[1][0] = 0.0f;
        result.getData()[1][1] = 1.0f;
        result.getData()[1][2] = 0.0f;
        result.getData()[1][3] = 0.0f;
        result.getData()[2][0] = sin;
        result.getData()[2][1] = 0.0f;
        result.getData()[2][2] = cos;
        result.getData()[2][3] = 0.0f;
        result.getData()[3][0] = 0.0f;
        result.getData()[3][1] = 0.0f;
        result.getData()[3][2] = 0.0f;
        result.getData()[3][3] = 1.0f;

        return result;
    }

    public static Matrix getTranslationMatrix(final Vector3 position) {
        Matrix result = new Matrix(4, 4);
        result.getData()[0][0] = 1.0f;
        result.getData()[0][1] = 0.0f;
        result.getData()[0][2] = 0.0f;
        result.getData()[0][3] = 0.0f;
        result.getData()[1][0] = 0.0f;
        result.getData()[1][1] = 1.0f;
        result.getData()[1][2] = 0.0f;
        result.getData()[1][3] = 0.0f;
        result.getData()[2][0] = 0.0f;
        result.getData()[2][1] = 0.0f;
        result.getData()[2][2] = 1.0f;
        result.getData()[2][3] = 0.0f;
        result.getData()[3][0] = position.getX();
        result.getData()[3][1] = position.getY();
        result.getData()[3][2] = position.getZ();
        result.getData()[3][3] = 1.0f;
        return result;

    }

    public static Matrix getIdentity(int n) {
        final Matrix I = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            I.data[i][i] = 1;
        }
        return I;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%9.4f ", data[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
