package com.gameengine.engine3d.core;

public class Matrix4 {
    public float[] data = new float[16];

    public Matrix4() {
        identity();
    }

    public Matrix4(float[] data) {
        System.arraycopy(data, 0, this.data, 0, 16);
    }

    public void identity() {
        for (int i = 0; i < 16; i++) {
            data[i] = 0f;
        }
        data[0] = data[5] = data[10] = data[15] = 1f;
    }

    public static Matrix4 translation(Vector3 pos) {
        Matrix4 m = new Matrix4();
        m.data[12] = pos.x;
        m.data[13] = pos.y;
        m.data[14] = pos.z;
        return m;
    }

    public static Matrix4 scale(Vector3 scale) {
        Matrix4 m = new Matrix4();
        m.data[0] = scale.x;
        m.data[5] = scale.y;
        m.data[10] = scale.z;
        return m;
    }

    public static Matrix4 rotationX(float angle) {
        Matrix4 m = new Matrix4();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        m.data[5] = cos;
        m.data[6] = -sin;
        m.data[9] = sin;
        m.data[10] = cos;
        return m;
    }

    public static Matrix4 rotationY(float angle) {
        Matrix4 m = new Matrix4();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        m.data[0] = cos;
        m.data[2] = sin;
        m.data[8] = -sin;
        m.data[10] = cos;
        return m;
    }

    public static Matrix4 rotationZ(float angle) {
        Matrix4 m = new Matrix4();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        m.data[0] = cos;
        m.data[1] = -sin;
        m.data[4] = sin;
        m.data[5] = cos;
        return m;
    }

    public static Matrix4 multiply(Matrix4 a, Matrix4 b) {
        Matrix4 result = new Matrix4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.data[i * 4 + j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.data[i * 4 + j] += a.data[i * 4 + k] * b.data[k * 4 + j];
                }
            }
        }
        return result;
    }

    public Vector3 transformPoint(Vector3 point) {
        float x = data[0] * point.x + data[4] * point.y + data[8] * point.z + data[12];
        float y = data[1] * point.x + data[5] * point.y + data[9] * point.z + data[13];
        float z = data[2] * point.x + data[6] * point.y + data[10] * point.z + data[14];
        float w = data[3] * point.x + data[7] * point.y + data[11] * point.z + data[15];
        if (w != 0) return new Vector3(x / w, y / w, z / w);
        return new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%.2f ", data[i * 4 + j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
