# 3D Game Engine for Android

एक पूर्ण 3D Game Engine जिसमे निम्नलिखित features हैं:

## ✅ Features

### 1. **3D Rendering System**
- Perspective projection
- Back-face culling (hidden surface removal)
- Basic lighting और shading
- Multiple mesh types (Cube, Pyramid, Sphere)
- Wireframe rendering

### 2. **Physics Engine**
- Rigid body dynamics
- Gravity simulation
- Velocity और acceleration
- Force application
- Impulse-based interactions
- Drag और air resistance

### 3. **Collision Detection & Response**
- Box Collider
- Sphere Collider
- AABB (Axis-Aligned Bounding Box) detection
- Elastic collision response
- Restitution (bounce)
- Trigger zones

### 4. **Transform System**
- Position, Rotation, Scale
- Matrix-based transformations
- Hierarchy support (parent-child)

### 5. **Math Library**
- Vector3 operations
- Matrix4 transformations
- Quaternion support

## 📁 Project Structure

```
app/src/main/java/com/gameengine/engine3d/
├── core/
│   ├── Vector3.java          # 3D Vector math
│   ├── Matrix4.java          # Matrix transformations
│   └── Transform.java        # Position, rotation, scale
├── rendering/
│   ├── Vertex.java           # Vertex data
│   ├── Mesh.java             # Mesh geometry
│   └── Renderer.java         # Rendering pipeline
├── physics/
│   ├── RigidBody.java        # Physics properties
│   ├── Collider.java         # Collision shapes
│   └── PhysicsEngine.java    # Physics simulation
├── gameobject/
│   ├── GameObject.java       # Game object entity
│   └── GameComponent.java    # Component system
├── GameEngine.java           # Main engine
└── MainActivity.java         # Android activity
```

## 🎮 How to Build & Run

### Prerequisites
- Android Studio
- Java 11+
- Android SDK 21+

### Build Steps

1. **Clone/Open the project**
   ```bash
   cd Neeraj-OS-Developer/Android
   ```

2. **Open in Android Studio**
   - File → Open → Select this repository

3. **Build APK**
   ```bash
   ./gradlew assembleDebug
   ```
   APK will be in: `app/build/outputs/apk/debug/`

4. **Install on Device/Emulator**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

5. **Or Run Directly**
   - Connect Android device
   - Click "Run" in Android Studio

## 🎯 Game Components

Default game में ये objects हैं:

1. **Floor** - Static ground (kinematic)
2. **Cube** - Dynamic rigid body with box collider
3. **Pyramid** - Dynamic rigid body with sphere collider
4. **Sphere** - Dynamic rigid body

## 📝 Usage Example

```java
// GameObject create करें
GameObject cube = new GameObject("Cube");

// Position set करें
cube.transform.setPosition(new Vector3(0, 5, 0));

// Mesh assign करें
cube.mesh = Mesh.createCube(1);

// Physics add करें
cube.rigidBody = new RigidBody(1f); // mass = 1
cube.collider = new BoxCollider(new Vector3(1, 1, 1));

// Game में add करें
gameEngine.addGameObject(cube);

// Force apply करें
cube.rigidBody.addForce(new Vector3(10, 0, 0));
```

## 🔧 Physics Properties

```java
RigidBody rb = gameObject.rigidBody;

rb.mass = 2f;                          // Mass
rb.useGravity = true;                  // Gravity enable
rb.drag = 0.1f;                        // Linear drag
rb.angularDrag = 0.1f;                 // Rotational drag
rb.isKinematic = false;                // Static/Dynamic
rb.setVelocity(new Vector3(5, 0, 0));  // Velocity
rb.addForce(force);                    // Apply force
rb.addImpulse(impulse);                // Apply impulse
```

## 🎨 Rendering Features

- **Lighting**: Direction light with Lambertian shading
- **Culling**: Automatic back-face culling
- **Projection**: Perspective projection with FOV
- **Wireframe**: Edge rendering for all objects

## 📊 Performance Notes

- **Physics**: Fixed 60 FPS
- **Rendering**: Variable FPS (device-dependent)
- **Optimization**: 
  - Spatial hashing for collision
  - Early culling
  - Minimal garbage allocation

## 🚀 Extending the Engine

### Custom Game Component

```java
public class PlayerController extends GameComponent {
    @Override
    public void start() {
        // Initialization
    }

    @Override
    public void update(float deltaTime) {
        // Game logic
    }
}

// Add to object
gameObject.addComponent(new PlayerController());
```

### Custom Mesh

```java
Mesh customMesh = new Mesh();
customMesh.addVertex(new Vertex(0, 0, 0));
customMesh.addVertex(new Vertex(1, 0, 0));
customMesh.addIndex(0);
customMesh.addIndex(1);

gameObject.mesh = customMesh;
```

## 📋 Physics Equations

- **Position**: p(t) = p₀ + v₀*t + 0.5*a*t²
- **Velocity**: v(t) = v₀ + a*t
- **Force**: F = m*a
- **Momentum**: p = m*v
- **Collision**: v' = v - (1+e)*(v·n)*n

## ⚠️ Limitations

- 2D screen rendering (perspective projected 3D)
- No texture mapping
- Simple lighting model
- No advanced physics (joints, constraints)
- No sound system
- Basic collision detection

## 📱 Android Requirements

- Minimum API: 21 (Android 5.0)
- Target API: 34 (Android 14)
- Java 11

## 📝 License

GNU General Public License v3.0

## 🎓 Learning Resources

- 3D Graphics: Matrix transformations, perspective projection
- Physics: Rigid body dynamics, collision detection
- Android: SurfaceView, threading, lifecycle

---

**बनाया गया**: Neeraj-OS-Developer  
**संस्करण**: 1.0  
**अंतिम अपडेट**: 2025
