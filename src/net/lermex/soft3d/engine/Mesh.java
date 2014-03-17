package net.lermex.soft3d.engine;

import net.lermex.soft3d.math.Vector3;

public class Mesh {
    private String name;
    private Vector3[] vertices;
    private Face[] faces;
    private Vector3 position = Vector3.ZERO;
    private Vector3 rotation = Vector3.ZERO;

    public Mesh (final String name, final int verticesCount, final int facesCount) {
        this.name = name;
        this.vertices = new Vector3[verticesCount];
        this.faces = new Face[facesCount];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3[] getVertices() {
        return vertices;
    }

    public Face[] getFaces() {
        return faces;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public static class Face {
        private final int a;
        private final int b;
        private final int c;

        public Face(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getC() {
            return c;
        }
    }
}
