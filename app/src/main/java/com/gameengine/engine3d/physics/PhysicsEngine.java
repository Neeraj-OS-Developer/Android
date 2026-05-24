package com.gameengine.engine3d.physics;

import java.util.ArrayList;
import java.util.List;
import com.gameengine.engine3d.core.Vector3;
import com.gameengine.engine3d.gameobject.GameObject;

public class PhysicsEngine {
    private List<GameObject> gameObjects = new ArrayList<>();
    private Vector3 gravity = new Vector3(0, -9.81f, 0);
    private static final float FIXED_DELTA_TIME = 0.016f;
    private int solverIterations = 4;

    public PhysicsEngine() {}

    public void addGameObject(GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
        }
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void setGravity(Vector3 gravity) {
        this.gravity = new Vector3(gravity);
    }

    public void update(float deltaTime) {
        for (GameObject obj : gameObjects) {
            if (obj.rigidBody != null && !obj.rigidBody.isKinematic) {
                obj.rigidBody.update(FIXED_DELTA_TIME);
                Vector3 displacement = obj.rigidBody.getVelocity().multiply(FIXED_DELTA_TIME);
                obj.transform.translate(displacement);
                
                if (obj.collider != null) {
                    obj.collider.offset = new Vector3(obj.transform.position);
                }
            }
        }

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

        Vector3 normal = objB.transform.position.subtract(objA.transform.position);
        float dist = normal.magnitude();
        if (dist < 0.001f) normal = new Vector3(0, 1, 0);
        else normal = normal.normalize();
        
        if (objA.rigidBody != null && !objA.rigidBody.isKinematic && 
            objB.rigidBody != null && !objB.rigidBody.isKinematic) {
            
            Vector3 relativeVel = objB.rigidBody.velocity.subtract(objA.rigidBody.velocity);
            float velAlongNormal = relativeVel.dot(normal);

            if (velAlongNormal < 0) {
                float totalMass = objA.rigidBody.mass + objB.rigidBody.mass;
                float aRatio = objB.rigidBody.mass / totalMass;
                float bRatio = objA.rigidBody.mass / totalMass;
                float restitution = Math.min(objA.rigidBody.restitution, objB.rigidBody.restitution);

                objA.rigidBody.velocity = objA.rigidBody.velocity.subtract(normal.multiply(velAlongNormal * aRatio * (1 + restitution)));
                objB.rigidBody.velocity = objB.rigidBody.velocity.add(normal.multiply(velAlongNormal * bRatio * (1 + restitution)));
            }
        } else if (objA.rigidBody != null && !objA.rigidBody.isKinematic) {
            objA.rigidBody.velocity = objA.rigidBody.velocity.subtract(normal.multiply(2 * objA.rigidBody.velocity.dot(normal)));
        } else if (objB.rigidBody != null && !objB.rigidBody.isKinematic) {
            objB.rigidBody.velocity = objB.rigidBody.velocity.subtract(normal.multiply(2 * objB.rigidBody.velocity.dot(normal)));
        }
    }
}
