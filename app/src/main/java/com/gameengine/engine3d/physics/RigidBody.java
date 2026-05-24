package com.gameengine.engine3d.physics;

import com.gameengine.engine3d.core.Vector3;

public class RigidBody {
    public Vector3 velocity = new Vector3();
    public Vector3 acceleration = new Vector3();
    public Vector3 force = new Vector3();
    
    public float mass = 1f;
    public float drag = 0.05f;
    public float angularDrag = 0.1f;
    public boolean useGravity = true;
    public boolean isKinematic = false;
    public float restitution = 0.6f;

    private Vector3 angularVelocity = new Vector3();
    private static final Vector3 GRAVITY = new Vector3(0, -9.81f, 0);
    private static final float MIN_VELOCITY = 0.001f;

    public RigidBody() {
        this(1f);
    }

    public RigidBody(float mass) {
        this.mass = Math.max(0.1f, mass);
    }

    public void addForce(Vector3 force) {
        if (!isKinematic) {
            this.force = this.force.add(force);
        }
    }

    public void addImpulse(Vector3 impulse) {
        if (!isKinematic) {
            velocity = velocity.add(impulse.divide(mass));
        }
    }

    public void setVelocity(Vector3 vel) {
        this.velocity = new Vector3(vel);
    }

    public Vector3 getVelocity() {
        return new Vector3(velocity);
    }

    public void setAngularVelocity(Vector3 angVel) {
        this.angularVelocity = new Vector3(angVel);
    }

    public Vector3 getAngularVelocity() {
        return new Vector3(angularVelocity);
    }

    public void update(float deltaTime) {
        if (isKinematic) return;

        if (useGravity) {
            addForce(GRAVITY.multiply(mass));
        }

        acceleration = force.divide(mass);
        
        float dragFactor = 1f - (drag * deltaTime);
        dragFactor = Math.max(0f, Math.min(1f, dragFactor));
        velocity = velocity.multiply(dragFactor);
        
        angularVelocity = angularVelocity.multiply(1 - angularDrag * deltaTime);
        velocity = velocity.add(acceleration.multiply(deltaTime));

        if (velocity.magnitude() < MIN_VELOCITY) {
            velocity = Vector3.zero();
        }

        force = Vector3.zero();
    }
}
