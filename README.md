# JavaFX Roguelike Dungeon Crawler

A 2D roguelike dungeon crawler single-player game built with JavaFX with community dungeon sharing capabilities.

## 🎮 Features

### Core Gameplay
- **Tile-based Movement**: WASD controls with continuous movement system
- **Combat System**: Projectile-based combat with space bar attacks and explosion effects
- **Multiple Dungeon Types**: Various dungeon layouts and environments to explore
- **Rich Visuals**: Extensive sprite animations and visual effects
- **Interactive Elements**: Wells, ladders, exits, and various environmental objects

### Community Features
- **Dungeon Sharing**: Upload and download custom dungeons through the server
- **Community Dungeons**: Players can create and share their own dungeon layouts
- **Server Architecture**: Client-server system for dungeon distribution

### Technical Features
- **Thread-safe Architecture**: Concurrent collision detection with proper synchronization
- **Efficient Rendering**: JavaFX-based graphics with sprite animation system
- **Modular Design**: Clean MVC architecture with separated concerns
- **Asset Management**: Centralized image and resource handling

## 🛠️ Technology Stack

- **Language**: Java
- **GUI Framework**: JavaFX
- **Architecture**: Model-View-Controller (MVC)
- **Networking**: TCP Socket-based client-server communication
- **Concurrency**: ExecutorService with thread-safe collision detection
- **Graphics**: 2D sprite-based rendering with animation support

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- JavaFX runtime (included in JDK 8, separate module for JDK 11+)
- Terminal/Command Prompt access

## 🚀 Installation & Setup

### 1. Clone or Download the Project
```bash
git clone <repository-url>
cd roguelike_new_project
```

### 2. Compile the Project
```bash
javac -d bin -cp "lib/*" src/**/*.java src/Serverapp/*.java
```

### 3. Run the Game
```bash
java -cp "bin:lib/*" Main
```

### 4. Run the Server (Optional - for dungeon sharing)
```bash
java -cp "bin:lib/*" Serverapp.ServerApp
```

## 🎯 How to Play

### Controls
- **Movement**: WASD keys
- **Attack**: Space bar
- **Navigation**: Use mouse for menu interactions

### Gameplay Flow
1. **Start Game**: Launch from the main menu
2. **Navigate Dungeons**: Move through tile-based environments
3. **Combat**: Fight enemies using projectile attacks
4. **Progress**: Navigate between different dungeon types using exits or ladders
5. **Community Features**: Upload/download dungeons via the community menu

### Community Dungeon Sharing
1. Start the server application (`ServerApp.java`)
2. Server runs on port 8888 by default
3. Connect clients through the community menu
4. Upload and download community-created dungeons

## 📁 Project Structure

```
src/
├── Main.java                    # Application entry point
├── rougelike/
│   ├── Controller.java          # Main MVC controller
│   ├── Model.java              # Central data model
│   ├── View.java               # View orchestrator
│   ├── Global.java             # Global constants (512x512 resolution)
│   ├── game/                   # Core game engine
│   │   ├── Game.java           # Main game loop with JavaFX AnimationTimer
│   │   ├── GameModel.java      # Game state management
│   │   ├── Loader.java         # Dungeon loading system
│   │   ├── entities/           # Game entities and collision system
│   │   ├── graphics/           # Animation and sprite management
│   │   └── dungeon/            # Dungeon data structures
│   ├── menu/                   # Menu system (MVC pattern)
│   └── networking/             # Client-side networking
└── Serverapp/                  # Server application
    ├── ServerApp.java          # Server entry point
    ├── Server.java             # Multi-threaded server (port 8888)
    ├── ClientHandler.java      # Per-client connection handling
    └── ServerDungeonDatabase.java # Server-side dungeon storage
```

## 🏗️ Architecture Overview

### Design Patterns
- **MVC Architecture**: Clear separation between Model, View, and Controller
- **Entity System**: Modular entity design with collision detection
- **Observer Pattern**: Event-driven updates between components
- **Factory Pattern**: Asset and entity creation management

### Threading Model
- **Main Thread**: JavaFX Application Thread for UI and rendering
- **Game Loop**: AnimationTimer for consistent frame updates
- **Concurrent Processing**: ExecutorService for collision detection
- **Network Threads**: Separate threads for client-server communication

### Key Components
- **Collision System**: Thread-safe AABB collision detection with resolution
- **Animation System**: Sprite-based animations with timing control
- **Asset Management**: Centralized image loading and caching
- **Network Protocol**: Custom TCP-based communication for dungeon sharing

## 🔧 Development & Contributing

### Code Style
- Follow Java naming conventions
- Maintain MVC separation of concerns
- Use proper JavaDoc comments for public APIs
- Ensure thread safety for concurrent operations

### Testing

The project includes a comprehensive test suite with JUnit 5 and Mockito:

**Test Structure:**
```
test/
├── java/
│   ├── rougelike/
│   │   ├── game/
│   │   │   ├── entities/          # Entity unit tests
│   │   │   ├── dungeon/           # Dungeon loading tests
│   │   │   └── GameModelTest.java # Game state tests
│   │   ├── networking/            # Client networking tests
│   │   └── ModelTest.java         # Main model tests
│   ├── Serverapp/                 # Server tests
│   └── utils/                     # Test utilities and mocks
└── resources/                     # Test resources
```

**Running Tests:**
```bash
# Compile and run all tests
./test-all.sh

# Compile tests only
./test-compile.sh

# Run specific test categories
./test-run.sh entities    # Entity tests only
./test-run.sh network     # Network tests only
./test-run.sh server      # Server tests only
./test-run.sh all         # All tests (default)
```

**Test Coverage:**
- ✅ **Unit Tests**: Core game entities (Player, Enemy, Projectile)
- ✅ **Integration Tests**: Dungeon loading and game flow  
- ✅ **System Tests**: Collision detection and physics
- ✅ **Network Tests**: Client-server communication
- ✅ **Model Tests**: Application state management
- ✅ **Utility Tests**: Helper classes and mock objects

**Test Dependencies:**
- JUnit 5 (Jupiter) - Testing framework
- Mockito - Mocking framework for isolated testing
- JUnit Platform Launcher - Test execution engine

### Building
The project uses standard Java compilation without build automation tools like Maven or Gradle.

## 🐛 Known Issues & Limitations

- Asset paths are located at `assets/`
- Fixed resolution of 512x512 pixels
- Server runs on fixed port 8888
- Limited to TCP-based networking

## 📄 License

This project is provided as-is for educational and development purposes.

## 🤝 Support

For issues, questions, or contributions, please refer to the project documentation.

---

**Game Resolution**: 512x512 pixels  
**Server Port**: 8888  
**JavaFX Version**: Compatible with JDK 8+