package net.lermex.soft3d.math;

public class Vector2 {
    public static final Vector2 ZERO = new Vector2(0, 0);

    private final float x;
    private final float y;

    public Vector2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 subtract(final Vector2 v) {
        return new Vector2(x - v.getX(), y - v.getY());
    }

    public Vector2 add(final Vector2 v) {
        return new Vector2(x + v.getX(), y + v.getY());
    }

    public double norm() {
        return Math.sqrt(x*x+y*y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
