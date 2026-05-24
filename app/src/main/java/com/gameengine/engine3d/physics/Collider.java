package com.gameengine.engine3d.physics;

import com.gameengine.engine3d.core.Vector3;

public abstract class Collider {
    public Vector3 offset = new Vector3();
    public boolean isTrigger = false;

    public abstract boolean intersects(Collider other);
    public abstract Vector3 getClosestPoint(Vector3 point);
}
