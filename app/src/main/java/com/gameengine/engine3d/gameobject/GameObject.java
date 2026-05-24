package com.gameengine.engine3d.gameobject;

import com.gameengine.engine3d.core.Transform;
import com.gameengine.engine3d.physics.RigidBody;
import com.gameengine.engine3d.physics.Collider;
import com.gameengine.engine3d.rendering.Mesh;

public class GameObject {
    public String name;
    public Transform transform;
    public RigidBody rigidBody;
    public Collider collider;
    public Mesh mesh;
    public boolean active = true;

    private GameComponent[] components = new GameComponent[10];
    private int componentCount = 0;

    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
    }

    public void addComponent(GameComponent component) {
        if (componentCount < components.length && component != null) {
            components[componentCount] = component;
            component.gameObject = this;
            componentCount++;
        }
    }

    public void removeComponent(GameComponent component) {
        for (int i = 0; i < componentCount; i++) {
            if (components[i] == component) {
                components[i] = components[componentCount - 1];
                components[componentCount - 1] = null;
                componentCount--;
                break;
            }
        }
    }

    public void start() {
        for (int i = 0; i < componentCount; i++) {
            if (components[i] != null) {
                components[i].start();
            }
        }
    }

    public void update(float deltaTime) {
        if (!active) return;

        for (int i = 0; i < componentCount; i++) {
            if (components[i] != null) {
                components[i].update(deltaTime);
            }
        }
    }

    public void destroy() {
        active = false;
    }
}
