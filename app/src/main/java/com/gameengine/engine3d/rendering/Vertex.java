package com.gameengine.engine3d.rendering;

import com.gameengine.engine3d.core.Vector3;

public class Vertex {
    public Vector3 position;
    public Vector3 normal;
    public int color;

    public Vertex(Vector3 position, Vector3 normal, int color) {
        this.position = new Vector3(position);
        this.normal = new Vector3(normal);
        this.color = color;
    }

    public Vertex(float x, float y, float z) {
        this.position = new Vector3(x, y, z);
        this.normal = new Vector3(0, 1, 0);
        this.color = 0xFFFFFFFF;
    }

    public Vertex(Vertex other) {
        this.position = new Vector3(other.position);
        this.normal = new Vector3(other.normal);
        this.color = other.color;
    }
}
