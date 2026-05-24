package com.gameengine.engine3d.rendering;

import java.util.ArrayList;
import java.util.List;
import com.gameengine.engine3d.core.Vector3;

public class Mesh {
    public List<Vertex> vertices = new ArrayList<>();
    public List<Integer> indices = new ArrayList<>();
    public List<Integer> colors = new ArrayList<>();

    public Mesh() {}

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addIndex(int index) {
        indices.add(index);
    }

    public void addColor(int color) {
        colors.add(color);
    }

    public static Mesh createCube(float size) {
        Mesh mesh = new Mesh();
        float half = size / 2f;
        int[] cubeColors = {
            0xFFFF6B6B, 0xFF4ECDC4, 0xFFFFE66D, 0xFF95E1D3, 0xFFC7CEEA, 0xFFB5EAD7
        };

        // Front face (red)
        mesh.addVertex(new Vertex(-half, -half, half));
        mesh.addVertex(new Vertex(half, -half, half));
        mesh.addVertex(new Vertex(half, half, half));
        mesh.addVertex(new Vertex(-half, half, half));

        // Back face (cyan)
        mesh.addVertex(new Vertex(-half, -half, -half));
        mesh.addVertex(new Vertex(half, -half, -half));
        mesh.addVertex(new Vertex(half, half, -half));
        mesh.addVertex(new Vertex(-half, half, -half));

        // Front
        mesh.addIndex(0);
        mesh.addIndex(1);
        mesh.addIndex(2);
        mesh.addIndex(2);
        mesh.addIndex(3);
        mesh.addIndex(0);

        // Back
        mesh.addIndex(4);
        mesh.addIndex(6);
        mesh.addIndex(5);
        mesh.addIndex(4);
        mesh.addIndex(7);
        mesh.addIndex(6);

        // Top
        mesh.addIndex(3);
        mesh.addIndex(2);
        mesh.addIndex(6);
        mesh.addIndex(3);
        mesh.addIndex(6);
        mesh.addIndex(7);

        // Bottom
        mesh.addIndex(4);
        mesh.addIndex(5);
        mesh.addIndex(1);
        mesh.addIndex(4);
        mesh.addIndex(1);
        mesh.addIndex(0);

        // Right
        mesh.addIndex(1);
        mesh.addIndex(5);
        mesh.addIndex(6);
        mesh.addIndex(1);
        mesh.addIndex(6);
        mesh.addIndex(2);

        // Left
        mesh.addIndex(4);
        mesh.addIndex(0);
        mesh.addIndex(3);
        mesh.addIndex(4);
        mesh.addIndex(3);
        mesh.addIndex(7);

        return mesh;
    }

    public static Mesh createPyramid(float size) {
        Mesh mesh = new Mesh();
        float half = size / 2f;

        // Base vertices
        mesh.addVertex(new Vertex(-half, 0, -half));
        mesh.addVertex(new Vertex(half, 0, -half));
        mesh.addVertex(new Vertex(half, 0, half));
        mesh.addVertex(new Vertex(-half, 0, half));
        // Apex
        mesh.addVertex(new Vertex(0, size, 0));

        // Base (yellow)
        mesh.addIndex(0);
        mesh.addIndex(2);
        mesh.addIndex(1);
        mesh.addIndex(0);
        mesh.addIndex(3);
        mesh.addIndex(2);

        // Sides
        mesh.addIndex(0);
        mesh.addIndex(4);
        mesh.addIndex(1);
        mesh.addIndex(1);
        mesh.addIndex(4);
        mesh.addIndex(2);
        mesh.addIndex(2);
        mesh.addIndex(4);
        mesh.addIndex(3);
        mesh.addIndex(3);
        mesh.addIndex(4);
        mesh.addIndex(0);

        return mesh;
    }

    public static Mesh createSphere(float radius, int segments) {
        Mesh mesh = new Mesh();
        float PI = (float) Math.PI;

        for (int i = 0; i <= segments; i++) {
            float lat = PI * i / segments;
            for (int j = 0; j <= segments; j++) {
                float lon = 2 * PI * j / segments;
                float x = (float) (radius * Math.sin(lat) * Math.cos(lon));
                float y = (float) (radius * Math.cos(lat));
                float z = (float) (radius * Math.sin(lat) * Math.sin(lon));
                mesh.addVertex(new Vertex(x, y, z));
            }
        }

        for (int i = 0; i < segments; i++) {
            for (int j = 0; j < segments; j++) {
                int a = i * (segments + 1) + j;
                int b = a + segments + 1;
                mesh.addIndex(a);
                mesh.addIndex(b);
                mesh.addIndex(a + 1);
                mesh.addIndex(b);
                mesh.addIndex(b + 1);
                mesh.addIndex(a + 1);
            }
        }

        return mesh;
    }

    public static Mesh createPlane(float width, float height) {
        Mesh mesh = new Mesh();
        float hw = width / 2f;
        float hh = height / 2f;

        mesh.addVertex(new Vertex(-hw, 0, -hh));
        mesh.addVertex(new Vertex(hw, 0, -hh));
        mesh.addVertex(new Vertex(hw, 0, hh));
        mesh.addVertex(new Vertex(-hw, 0, hh));

        mesh.addIndex(0);
        mesh.addIndex(1);
        mesh.addIndex(2);
        mesh.addIndex(2);
        mesh.addIndex(3);
        mesh.addIndex(0);

        return mesh;
    }
}
