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
    private float deltaTime = 0;

    public GameEngine(Canvas canvas, int screenWidth, int screenHeight) {
        this.renderer = new Renderer(canvas, screenWidth, screenHeight);
        this.physicsEngine = new PhysicsEngine();
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        physicsEngine.addGameObject(gameObject);
        gameObject.start();
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        physicsEngine.removeGameObject(gameObject);
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
        if (!isRunning) return;

        // Calculate delta time
        long currentTime = System.currentTimeMillis();
        deltaTime = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;

        // Clear screen
        renderer.clear(0xFF1a1a1a);

        // Update physics
        physicsEngine.update(deltaTime);

        // Update game objects
        for (GameObject obj : gameObjects) {
            if (obj.active) {
                obj.update(deltaTime);
            }
        }

        // Render game objects
        for (GameObject obj : gameObjects) {
            if (obj.active && obj.mesh != null) {
                renderer.renderMesh(obj.mesh, obj.transform.getModelMatrix());
            }
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
}
