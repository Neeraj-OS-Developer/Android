package com.gameengine.engine3d.core;

public class Vector3 {
    public float x, y, z;

    public Vector3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3 multiply(float scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3 divide(float scalar) {
        if (scalar == 0) return new Vector3();
        return new Vector3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public float dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        float cx = this.y * other.z - this.z * other.y;
        float cy = this.z * other.x - this.x * other.z;
        float cz = this.x * other.y - this.y * other.x;
        return new Vector3(cx, cy, cz);
    }

    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalize() {
        float mag = magnitude();
        if (mag == 0) return new Vector3();
        return this.divide(mag);
    }

    @Override
    public String toString() {
        return String.format("Vector3(%.2f, %.2f, %.2f)", x, y, z);
    }
}
