package com.gameengine.engine3d.physics;

import com.gameengine.engine3d.core.Vector3;

public class RigidBody {
    public Vector3 velocity = new Vector3();
    public Vector3 acceleration = new Vector3();
    public Vector3 force = new Vector3();
    
    public float mass = 1f;
    public float drag = 0.1f;
    public float angularDrag = 0.1f;
    public boolean useGravity = true;
    public boolean isKinematic = false;

    private Vector3 angularVelocity = new Vector3();
    private static final Vector3 GRAVITY = new Vector3(0, -9.81f, 0);

    public RigidBody() {}

    public RigidBody(float mass) {
        this.mass = mass;
    }

    public void addForce(Vector3 force) {
        this.force = this.force.add(force);
    }

    public void addForce(Vector3 force, Vector3 point) {
        addForce(force);
    }

    public void addImpulse(Vector3 impulse) {
        velocity = velocity.add(impulse.divide(mass));
    }

    public void setVelocity(Vector3 vel) {
        this.velocity = vel;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setAngularVelocity(Vector3 angVel) {
        this.angularVelocity = angVel;
    }

    public Vector3 getAngularVelocity() {
        return angularVelocity;
    }

    public void update(float deltaTime) {
        if (isKinematic) return;

        // Apply gravity
        if (useGravity) {
            addForce(GRAVITY.multiply(mass));
        }

        // Calculate acceleration
        acceleration = force.divide(mass);

        // Apply drag
        velocity = velocity.multiply(1 - drag * deltaTime);
        angularVelocity = angularVelocity.multiply(1 - angularDrag * deltaTime);

        // Update velocity
        velocity = velocity.add(acceleration.multiply(deltaTime));

        // Reset forces
        force = new Vector3();
    }

    public void reset() {
        force = new Vector3();
        acceleration = new Vector3();
    }
}
