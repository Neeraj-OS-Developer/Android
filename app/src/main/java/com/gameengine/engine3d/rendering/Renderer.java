package com.gameengine.engine3d.rendering;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.gameengine.engine3d.core.Matrix4;
import com.gameengine.engine3d.core.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private Canvas canvas;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;

    private Vector3 cameraPosition = new Vector3(0, 5, 15);
    private Vector3 cameraTarget = new Vector3(0, 2, 0);
    private float fov = 45f;
    private float nearPlane = 0.1f;
    private float farPlane = 1000f;

    private Vector3 lightDirection = new Vector3(1, 2, 1).normalize();
    private int ambientLight = 0xFF444444;

    public Renderer(Canvas canvas, int screenWidth, int screenHeight) {
        this.canvas = canvas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setFilterBitmap(true);
    }

    public void setCameraPosition(Vector3 pos) {
        this.cameraPosition = new Vector3(pos);
    }

    public void setCameraTarget(Vector3 target) {
        this.cameraTarget = new Vector3(target);
    }

    public void setLightDirection(Vector3 dir) {
        this.lightDirection = dir.normalize();
    }

    public void renderMesh(Mesh mesh, Matrix4 modelMatrix) {
        if (mesh == null || canvas == null) return;

        List<ProjectedTriangle> triangles = new ArrayList<>();
        List<ProjectedVertex> projectedVertices = new ArrayList<>();

        for (Vertex vertex : mesh.vertices) {
            Vector3 worldPos = modelMatrix.transformPoint(vertex.position);
            Vector3 screenPos = projectVertex(worldPos);
            int color = calculateLighting(vertex);
            projectedVertices.add(new ProjectedVertex(screenPos, color, worldPos.z));
        }

        for (int i = 0; i < mesh.indices.size(); i += 3) {
            int idx0 = mesh.indices.get(i);
            int idx1 = mesh.indices.get(i + 1);
            int idx2 = mesh.indices.get(i + 2);

            if (idx0 < projectedVertices.size() && idx1 < projectedVertices.size() && idx2 < projectedVertices.size()) {
                ProjectedVertex v0 = projectedVertices.get(idx0);
                ProjectedVertex v1 = projectedVertices.get(idx1);
                ProjectedVertex v2 = projectedVertices.get(idx2);

                if (!isBackFace(v0, v1, v2)) {
                    float avgZ = (v0.depth + v1.depth + v2.depth) / 3f;
                    triangles.add(new ProjectedTriangle(v0, v1, v2, avgZ));
                }
            }
        }

        Collections.sort(triangles, new Comparator<ProjectedTriangle>() {
            @Override
            public int compare(ProjectedTriangle a, ProjectedTriangle b) {
                return Float.compare(a.avgDepth, b.avgDepth);
            }
        });

        for (ProjectedTriangle tri : triangles) {
            drawTriangle(tri.v0, tri.v1, tri.v2);
        }
    }

    private Vector3 projectVertex(Vector3 worldPos) {
        Vector3 relPos = worldPos.subtract(cameraPosition);
        float distance = relPos.magnitude();
        if (distance <= 0.1f) distance = 0.1f;

        float scale = (screenHeight / 2f) / (float) Math.tan(Math.toRadians(fov / 2f));
        float screenX = (relPos.x / Math.max(relPos.z, 0.1f)) * scale + (screenWidth / 2f);
        float screenY = (relPos.y / Math.max(relPos.z, 0.1f)) * scale + (screenHeight / 2f);

        return new Vector3(screenX, screenY, distance);
    }

    private int calculateLighting(Vertex vertex) {
        Vector3 normal = vertex.normal;
        float light = Math.max(0.4f, Math.abs(normal.dot(lightDirection)));
        
        int r = Math.min(255, (int) (((vertex.color >> 16) & 0xFF) * light));
        int g = Math.min(255, (int) (((vertex.color >> 8) & 0xFF) * light));
        int b = Math.min(255, (int) ((vertex.color & 0xFF) * light));

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private boolean isBackFace(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2) {
        float area = (v1.position.x - v0.position.x) * (v2.position.y - v0.position.y) -
                     (v2.position.x - v0.position.x) * (v1.position.y - v0.position.y);
        return area < 0;
    }

    private void drawTriangle(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2) {
        try {
            Path path = new Path();
            path.moveTo(v0.position.x, v0.position.y);
            path.lineTo(v1.position.x, v1.position.y);
            path.lineTo(v2.position.x, v2.position.y);
            path.close();

            paint.setColor(v0.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);

            paint.setColor(0xFF222222);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(0.5f);
            canvas.drawPath(path, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(int color) {
        canvas.drawColor(color);
    }

    private static class ProjectedVertex {
        Vector3 position;
        int color;
        float depth;

        ProjectedVertex(Vector3 pos, int col, float d) {
            this.position = pos;
            this.color = col;
            this.depth = d;
        }
    }

    private static class ProjectedTriangle {
        ProjectedVertex v0, v1, v2;
        float avgDepth;

        ProjectedTriangle(ProjectedVertex v0, ProjectedVertex v1, ProjectedVertex v2, float avgDepth) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.avgDepth = avgDepth;
        }
    }
}
