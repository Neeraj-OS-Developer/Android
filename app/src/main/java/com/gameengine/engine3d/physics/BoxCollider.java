package com.gameengine.engine3d.physics;

import com.gameengine.engine3d.core.Vector3;

public class BoxCollider extends Collider {
    public Vector3 size = new Vector3(1, 1, 1);

    public BoxCollider(Vector3 size) {
        this.size = new Vector3(size);
    }

    @Override
    public boolean intersects(Collider other) {
        if (other instanceof BoxCollider) {
            BoxCollider box = (BoxCollider) other;
            return Math.abs(offset.x - box.offset.x) < (size.x + box.size.x) / 2 &&
                   Math.abs(offset.y - box.offset.y) < (size.y + box.size.y) / 2 &&
                   Math.abs(offset.z - box.offset.z) < (size.z + box.size.z) / 2;
        } else if (other instanceof SphereCollider) {
            SphereCollider sphere = (SphereCollider) other;
            Vector3 closest = getClosestPoint(sphere.offset);
            return closest.distance(sphere.offset) < sphere.radius;
        }
        return false;
    }

    @Override
    public Vector3 getClosestPoint(Vector3 point) {
        float x = Math.max(offset.x - size.x / 2, Math.min(point.x, offset.x + size.x / 2));
        float y = Math.max(offset.y - size.y / 2, Math.min(point.y, offset.y + size.y / 2));
        float z = Math.max(offset.z - size.z / 2, Math.min(point.z, offset.z + size.z / 2));
        return new Vector3(x, y, z);
    }
}
