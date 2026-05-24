package com.gameengine.engine3d.rendering;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.gameengine.engine3d.core.Matrix4;
import com.gameengine.engine3d.core.Vector3;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private Canvas canvas;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;

    // Camera properties
    private Vector3 cameraPosition = new Vector3(0, 5, 10);
    private Vector3 cameraTarget = new Vector3(0, 0, 0);
    private float fov = 45f;
    private float nearPlane = 0.1f;
    private float farPlane = 1000f;

    // Light properties
    private Vector3 lightDirection = new Vector3(1, 1, 1).normalize();
    private int ambientLight = 0xFF444444;

    public Renderer(Canvas canvas, int screenWidth, int screenHeight) {
        this.canvas = canvas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
    }

    public void setCameraPosition(Vector3 pos) {
        this.cameraPosition = pos;
    }

    public void setCameraTarget(Vector3 target) {
        this.cameraTarget = target;
    }

    public void setLightDirection(Vector3 dir) {
        this.lightDirection = dir.normalize();
    }

    public void renderMesh(Mesh mesh, Matrix4 modelMatrix) {
        if (mesh == null || canvas == null) return;

        List<ProjectedVertex> projectedVertices = new ArrayList<>();

        // Project vertices
        for (Vertex vertex : mesh.vertices) {
            Vector3 worldPos = modelMatrix.transformPoint(vertex.position);
            Vector3 screenPos = projectVertex(worldPos);
            
            // Calculate lighting
            int color = calculateLighting(vertex, modelMatrix);
            projectedVertices.add(new ProjectedVertex(screenPos, color));
        }

        // Draw triangles
        for (int i = 0; i < mesh.indices.size(); i += 3) {
            int idx0 = mesh.indices.get(i);
            int idx1 = mesh.indices.get(i + 1);
            int idx2 = mesh.indices.get(i + 2);

            if (idx0 < projectedVertices.size() && idx1 < projectedVertices.size() && idx2 < projectedVertices.size()) {
                ProjectedVertex v0 = projectedVertices.get(idx0);
                ProjectedVertex v1 = projectedVertices.get(idx1);
                ProjectedVertex v2 = projectedVertices.get(idx2);

                // Back-face culling
                if (isBackFace(v0, v1, v2)) continue;

                drawTriangle(v0, v1, v2);
            }
        }
    }

    private Vector3 projectVertex(Vector3 worldPos) {
        // Simple perspective projection
        float distance = worldPos.z;
        if (distance <= 0) distance = 0.1f;

        float scale = (screenHeight / 2f) / (float) Math.tan(Math.toRadians(fov / 2f));
        
        float screenX = (worldPos.x / distance) * scale + (screenWidth / 2f);
        float screenY = (worldPos.y / distance) * scale + (screenHeight / 2f);
        float screenZ = distance;

        return new Vector3(screenX, screenY, screenZ);
    }

    private int calculateLighting(Vertex vertex, Matrix4 modelMatrix) {
        Vector3 normal = vertex.normal;
        float light = Math.max(0.3f, normal.dot(lightDirection));
        
        int r = (int) (255 * light);
        int g = (int) (255 * light);
        int b = (int) (255 * light);

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private boolean isBackFace(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2) {
        float area = (v1.position.x - v0.position.x) * (v2.position.y - v0.position.y) -
                     (v2.position.x - v0.position.x) * (v1.position.y - v0.position.y);
        return area < 0;
    }

    private void drawTriangle(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2) {
        Path path = new Path();
        path.moveTo(v0.position.x, v0.position.y);
        path.lineTo(v1.position.x, v1.position.y);
        path.lineTo(v2.position.x, v2.position.y);
        path.close();

        paint.setColor(v0.color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
        canvas.drawPath(path, paint);
    }

    public void clear(int color) {
        canvas.drawColor(color);
    }

    // Helper class for projected vertices
    private static class ProjectedVertex {
        Vector3 position;
        int color;

        ProjectedVertex(Vector3 pos, int col) {
            this.position = pos;
            this.color = col;
        }
    }
}
