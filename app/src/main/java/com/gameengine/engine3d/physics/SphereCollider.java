package com.gameengine.engine3d.physics;

import com.gameengine.engine3d.core.Vector3;

public class SphereCollider extends Collider {
    public float radius = 1f;

    public SphereCollider(float radius) {
        this.radius = Math.max(0.1f, radius);
    }

    @Override
    public boolean intersects(Collider other) {
        if (other instanceof SphereCollider) {
            SphereCollider sphere = (SphereCollider) other;
            float dist = offset.distance(sphere.offset);
            return dist < (radius + sphere.radius);
        } else if (other instanceof BoxCollider) {
            BoxCollider box = (BoxCollider) other;
            Vector3 closest = box.getClosestPoint(offset);
            return offset.distance(closest) < radius;
        }
        return false;
    }

    @Override
    public Vector3 getClosestPoint(Vector3 point) {
        Vector3 dir = point.subtract(offset);
        float dist = dir.magnitude();
        if (dist < 0.001f) return new Vector3(offset);
        return offset.add(dir.normalize().multiply(radius));
    }
}
