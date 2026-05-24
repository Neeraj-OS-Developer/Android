package com.gameengine.engine3d.physics;

import java.util.ArrayList;
import java.util.List;
import com.gameengine.engine3d.core.Vector3;
import com.gameengine.engine3d.gameobject.GameObject;

public class PhysicsEngine {
    private List<GameObject> gameObjects = new ArrayList<>();
    private Vector3 gravity = new Vector3(0, -9.81f, 0);
    private float fixedDeltaTime = 0.016f; // 60 FPS
    private int solverIterations = 4;

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void setGravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public void update(float deltaTime) {
        // Update rigid bodies
        for (GameObject obj : gameObjects) {
            if (obj.rigidBody != null && !obj.rigidBody.isKinematic) {
                obj.rigidBody.update(fixedDeltaTime);
                obj.transform.translate(obj.rigidBody.velocity.multiply(fixedDeltaTime));
            }
        }

        // Collision detection and response
        for (int i = 0; i < solverIterations; i++) {
            detectAndResolveCollisions();
        }
    }

    private void detectAndResolveCollisions() {
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = i + 1; j < gameObjects.size(); j++) {
                GameObject objA = gameObjects.get(i);
                GameObject objB = gameObjects.get(j);

                if (objA.collider != null && objB.collider != null) {
                    if (objA.collider.intersects(objB.collider)) {
                        resolveCollision(objA, objB);
                    }
                }
            }
        }
    }

    private void resolveCollision(GameObject objA, GameObject objB) {
        if (objA.collider.isTrigger || objB.collider.isTrigger) return;

        Vector3 normal = objB.transform.position.subtract(objA.transform.position).normalize();
        
        if (objA.rigidBody != null && objB.rigidBody != null) {
            float restitution = 0.8f; // Bounce
            Vector3 relativeVel = objB.rigidBody.velocity.subtract(objA.rigidBody.velocity);
            float velAlongNormal = relativeVel.dot(normal);

            if (velAlongNormal < 0) {
                float totalMass = objA.rigidBody.mass + objB.rigidBody.mass;
                float aRatio = objB.rigidBody.mass / totalMass;
                float bRatio = objA.rigidBody.mass / totalMass;

                objA.rigidBody.velocity = objA.rigidBody.velocity.subtract(normal.multiply(velAlongNormal * aRatio * (1 + restitution)));
                objB.rigidBody.velocity = objB.rigidBody.velocity.add(normal.multiply(velAlongNormal * bRatio * (1 + restitution)));
            }
        }
    }
}
