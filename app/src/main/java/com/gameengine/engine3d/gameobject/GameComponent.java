package com.gameengine.engine3d.gameobject;

public abstract class GameComponent {
    public GameObject gameObject;

    public abstract void start();
    public abstract void update(float deltaTime);
}
