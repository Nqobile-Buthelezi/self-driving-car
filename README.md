# Self-Driving Car Simulation

This project is a **Java-based simulation of a self-driving car** that uses a neural network for decision-making. It is designed to demonstrate artificial intelligence principles in a portfolio-worthy format.

## Features
- **Neural Network Implementation:** A custom neural network framework with configurable layers and levels.
- **Vehicle Simulation:** Vehicles equipped with sensors for collision detection and road navigation.
- **Road and Canvas Visuals:** A real-time graphical interface to visualize car movement and network decisions.
- **Unit Testing:** Comprehensive tests for core utilities and application logic.

## Project Structure
```plaintext
self-driving-car/
├── best_brain.json            # Pre-trained neural network weights
├── neuralNetwork/             # Main project directory
    ├── pom.xml                # Maven configuration file
    ├── src/
        ├── main/java/         # Source code
            ├── za/co/bangoma/neural/
                ├── Application.java         # Entry point for the simulation
                ├── network/                 # Neural network logic
                ├── road/car/                # Vehicle and sensor classes
                ├── road/RoadCanvas.java     # Road rendering logic
                ├── Screen.java              # Main simulation screen
                └── Utils.java               # Utility functions
        ├── test/java/           # Unit tests
            ├── ApplicationTest.java
            ├── ScreenTest.java
            └── UtilsTest.java
## Prerequisites
- Java JDK 11 or higher
- Maven 3.8+
- A Java IDE (e.g., IntelliJ IDEA, Eclipse)

## Installation
1. Clone the repository: git clone https://github.com/nqobilebuthelezi/self-driving-car.git
2. Navigate to the neuralNetwork directory: cd self-driving-car/neuralNetwork
3. Build the project using maven: mvn clean install

## Usage
1. Run the application: mvn exec:java -Dexec.mainClass="za.co.bangoma.neural.Application"



