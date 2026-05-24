package com.gameengine.engine3d;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import com.gameengine.engine3d.gameobject.GameObject;
import com.gameengine.engine3d.physics.PhysicsEngine;
import com.gameengine.engine3d.rendering.Renderer;

public class GameEngine {
    private List<GameObject> gameObjects = new ArrayList<>();
    private PhysicsEngine physicsEngine;
    private Renderer renderer;
    private boolean isRunning = false;
    private long lastUpdateTime = 0;
    private float deltaTime = 0f;
    private int fps = 0;
    private int frameCount = 0;
    private long lastFpsTime = 0;

    public GameEngine(Canvas canvas, int screenWidth, int screenHeight) {
        this.renderer = new Renderer(canvas, screenWidth, screenHeight);
        this.physicsEngine = new PhysicsEngine();
        this.lastFpsTime = System.currentTimeMillis();
    }

    public void addGameObject(GameObject gameObject) {
        if (gameObject != null && !gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
            if (physicsEngine != null) {
                physicsEngine.addGameObject(gameObject);
            }
            gameObject.start();
        }
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        if (physicsEngine != null) {
            physicsEngine.removeGameObject(gameObject);
        }
        gameObject.destroy();
    }

    public void start() {
        isRunning = true;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void stop() {
        isRunning = false;
    }

    public void update(Canvas canvas) {
        if (!isRunning || canvas == null) return;

        try {
            long currentTime = System.currentTimeMillis();
            deltaTime = Math.min((currentTime - lastUpdateTime) / 1000f, 0.033f);
            lastUpdateTime = currentTime;

            renderer.clear(0xFF1a1a1a);

            if (physicsEngine != null) {
                physicsEngine.update(deltaTime);
            }

            for (GameObject obj : gameObjects) {
                if (obj.active) {
                    obj.update(deltaTime);
                }
            }

            for (GameObject obj : gameObjects) {
                if (obj.active && obj.mesh != null) {
                    renderer.renderMesh(obj.mesh, obj.transform.getModelMatrix());
                }
            }

            frameCount++;
            if (currentTime - lastFpsTime >= 1000) {
                fps = frameCount;
                frameCount = 0;
                lastFpsTime = currentTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public int getFPS() {
        return fps;
    }
}
