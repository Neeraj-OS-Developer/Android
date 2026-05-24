package com.gameengine.engine3d.core;

public class Transform {
    public Vector3 position = new Vector3(0, 0, 0);
    public Vector3 rotation = new Vector3(0, 0, 0);
    public Vector3 scale = new Vector3(1, 1, 1);

    private Matrix4 modelMatrix;
    private boolean isDirty = true;

    public Transform() {
        updateMatrix();
    }

    public Transform(Vector3 position) {
        this.position = new Vector3(position);
        updateMatrix();
    }

    public void setPosition(Vector3 pos) {
        this.position = new Vector3(pos);
        isDirty = true;
    }

    public void setRotation(Vector3 rot) {
        this.rotation = new Vector3(rot);
        isDirty = true;
    }

    public void setScale(Vector3 scl) {
        this.scale = new Vector3(scl);
        isDirty = true;
    }

    public void translate(Vector3 offset) {
        this.position = this.position.add(offset);
        isDirty = true;
    }

    public void rotate(Vector3 angle) {
        this.rotation = this.rotation.add(angle);
        isDirty = true;
    }

    public Matrix4 getModelMatrix() {
        if (isDirty) {
            updateMatrix();
        }
        return modelMatrix;
    }

    private void updateMatrix() {
        Matrix4 trans = Matrix4.translation(position);
        Matrix4 rotX = Matrix4.rotationX(rotation.x);
        Matrix4 rotY = Matrix4.rotationY(rotation.y);
        Matrix4 rotZ = Matrix4.rotationZ(rotation.z);
        Matrix4 scl = Matrix4.scale(scale);

        modelMatrix = Matrix4.multiply(trans, Matrix4.multiply(rotZ, Matrix4.multiply(rotY, Matrix4.multiply(rotX, scl))));
        isDirty = false;
    }
}
