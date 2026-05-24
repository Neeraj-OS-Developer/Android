package com.gameengine.engine3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.util.Log;
import com.gameengine.engine3d.gameobject.GameObject;
import com.gameengine.engine3d.rendering.Mesh;
import com.gameengine.engine3d.physics.RigidBody;
import com.gameengine.engine3d.physics.SphereCollider;
import com.gameengine.engine3d.physics.BoxCollider;
import com.gameengine.engine3d.core.Vector3;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "GameEngine3D";
    private SurfaceView surfaceView;
    private GameEngine gameEngine;
    private GameLoopThread gameThread;
    private volatile boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            surfaceView = new SurfaceView(this);
            setContentView(surfaceView);
            surfaceView.getHolder().addCallback(this);
            Log.d(TAG, "Activity created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Log.d(TAG, "Surface created");
            initGame();
            running = true;
            gameThread = new GameLoopThread(holder);
            gameThread.start();
            if (gameEngine != null) {
                gameEngine.start();
            }
            Log.d(TAG, "Game loop started");
        } catch (Exception e) {
            Log.e(TAG, "Error in surfaceCreated: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Surface changed: " + width + "x" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            Log.d(TAG, "Surface destroyed");
            running = false;
            if (gameEngine != null) {
                gameEngine.stop();
            }
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in surfaceDestroyed: " + e.getMessage());
        }
    }

    private void initGame() {
        try {
            gameEngine = new GameEngine(null, surfaceView.getWidth(), surfaceView.getHeight());
            Log.d(TAG, "GameEngine created with dimensions: " + surfaceView.getWidth() + "x" + surfaceView.getHeight());

            // Floor
            GameObject floor = new GameObject("Floor");
            floor.transform.setPosition(new Vector3(0, -3, 0));
            floor.transform.setScale(new Vector3(20, 1, 20));
            floor.mesh = Mesh.createPlane(20, 20);
            floor.collider = new BoxCollider(new Vector3(20, 1, 20));
            floor.collider.offset = floor.transform.position;
            floor.rigidBody = new RigidBody(0);
            floor.rigidBody.isKinematic = true;
            gameEngine.addGameObject(floor);
            Log.d(TAG, "Floor created");

            // Cube
            GameObject cube = new GameObject("Cube");
            cube.transform.setPosition(new Vector3(0, 5, 0));
            cube.transform.setScale(new Vector3(1.5f, 1.5f, 1.5f));
            cube.mesh = Mesh.createCube(1);
            cube.collider = new BoxCollider(new Vector3(1.5f, 1.5f, 1.5f));
            cube.collider.offset = cube.transform.position;
            cube.rigidBody = new RigidBody(2f);
            cube.rigidBody.restitution = 0.7f;
            gameEngine.addGameObject(cube);
            Log.d(TAG, "Cube created");

            // Pyramid
            GameObject pyramid = new GameObject("Pyramid");
            pyramid.transform.setPosition(new Vector3(-4, 5, 0));
            pyramid.transform.setScale(new Vector3(1.5f, 1.5f, 1.5f));
            pyramid.mesh = Mesh.createPyramid(1);
            pyramid.collider = new SphereCollider(1f);
            pyramid.collider.offset = pyramid.transform.position;
            pyramid.rigidBody = new RigidBody(1.5f);
            pyramid.rigidBody.restitution = 0.6f;
            gameEngine.addGameObject(pyramid);
            Log.d(TAG, "Pyramid created");

            // Sphere
            GameObject sphere = new GameObject("Sphere");
            sphere.transform.setPosition(new Vector3(4, 5, 0));
            sphere.transform.setScale(new Vector3(1.5f, 1.5f, 1.5f));
            sphere.mesh = Mesh.createSphere(1, 12);
            sphere.collider = new SphereCollider(0.75f);
            sphere.collider.offset = sphere.transform.position;
            sphere.rigidBody = new RigidBody(1f);
            sphere.rigidBody.restitution = 0.8f;
            gameEngine.addGameObject(sphere);
            Log.d(TAG, "Sphere created");

            Log.d(TAG, "Game initialized with " + gameEngine.getPhysicsEngine() + " physics engine");
        } catch (Exception e) {
            Log.e(TAG, "Error in initGame: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class GameLoopThread extends Thread {
        private SurfaceHolder holder;
        private static final int TARGET_FPS = 60;
        private static final long FRAME_TIME = 1000 / TARGET_FPS;

        GameLoopThread(SurfaceHolder holder) {
            this.holder = holder;
            setName("GameLoop");
        }

        @Override
        public void run() {
            long frameStartTime;
            while (running) {
                frameStartTime = System.currentTimeMillis();
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null && gameEngine != null) {
                        synchronized (holder) {
                            gameEngine.update(canvas);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in game loop: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            holder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            Log.e(TAG, "Error unlocking canvas: " + e.getMessage());
                        }
                    }
                }

                long frameTime = System.currentTimeMillis() - frameStartTime;
                long sleepTime = FRAME_TIME - frameTime;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Game loop interrupted: " + e.getMessage());
                    }
                }
            }
        }
    }
}
