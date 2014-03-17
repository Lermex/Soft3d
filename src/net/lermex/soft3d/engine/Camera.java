package net.lermex.soft3d.engine;

import net.lermex.soft3d.math.Vector3;

public class Camera {

    private Vector3 position;
    private Vector3 target;

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getTarget() {
        return target;
    }

    public void setTarget(Vector3 target) {
        this.target = target;
    }
}
