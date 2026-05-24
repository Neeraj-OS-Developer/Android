package com.gameengine.engine3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;
import com.gameengine.engine3d.gameobject.GameObject;
import com.gameengine.engine3d.rendering.Mesh;
import com.gameengine.engine3d.physics.RigidBody;
import com.gameengine.engine3d.physics.SphereCollider;
import com.gameengine.engine3d.physics.BoxCollider;
import com.gameengine.engine3d.core.Vector3;

public class MainActivity extends Activity {
    private SurfaceView surfaceView;
    private GameEngine gameEngine;
    private GameLoopThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initGame();
                gameThread = new GameLoopThread(gameEngine, holder);
                gameThread.start();
                gameEngine.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameEngine.stop();
                gameThread.stopGame();
            }
        });
    }

    private void initGame() {
        gameEngine = new GameEngine(null, surfaceView.getWidth(), surfaceView.getHeight());

        // Create floor
        GameObject floor = new GameObject("Floor");
        floor.transform.setPosition(new Vector3(0, -5, 0));
        floor.transform.setScale(new Vector3(10, 1, 10));
        floor.mesh = Mesh.createCube(1);
        floor.collider = new BoxCollider(new Vector3(10, 1, 10));
        floor.rigidBody = new RigidBody(0);
        floor.rigidBody.isKinematic = true;
        gameEngine.addGameObject(floor);

        // Create falling cube
        GameObject cube = new GameObject("Cube");
        cube.transform.setPosition(new Vector3(0, 5, 0));
        cube.mesh = Mesh.createCube(1);
        cube.collider = new BoxCollider(new Vector3(1, 1, 1));
        cube.rigidBody = new RigidBody(1f);
        gameEngine.addGameObject(cube);

        // Create pyramid
        GameObject pyramid = new GameObject("Pyramid");
        pyramid.transform.setPosition(new Vector3(3, 5, 0));
        pyramid.mesh = Mesh.createPyramid(1);
        pyramid.collider = new SphereCollider(0.5f);
        pyramid.rigidBody = new RigidBody(1f);
        gameEngine.addGameObject(pyramid);

        // Create sphere
        GameObject sphere = new GameObject("Sphere");
        sphere.transform.setPosition(new Vector3(-3, 5, 0));
        sphere.mesh = Mesh.createSphere(0.5f, 16);
        sphere.collider = new SphereCollider(0.5f);
        sphere.rigidBody = new RigidBody(1f);
        gameEngine.addGameObject(sphere);
    }

    private class GameLoopThread extends Thread {
        private GameEngine engine;
        private SurfaceHolder holder;
        private boolean running = true;

        GameLoopThread(GameEngine engine, SurfaceHolder holder) {
            this.engine = engine;
            this.holder = holder;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        engine.update(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void stopGame() {
            running = false;
        }
    }
}
