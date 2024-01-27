package Test;

import thu.robots.components.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import thu.robots.components.Robot;

import static org.junit.jupiter.api.Assertions.*;

public class AutonomousSteeringTest {

    @Test
 public void testStuckEmergencyOperation() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();

        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

    }
    @Test
    public void testevaluateSensorData() {

    }

    @Test
    void goalAlignment() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();

        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());
        robot.setOrientation(Math.PI / 4);

        AutonomousSteering autoSteering = (AutonomousSteering) robot.getSteering();
        autoSteering.goalAlignment(robot);
        assertEquals(0.0, robot.getOrientation(), 0.001);
    }

    @Test
    void evaluateSensorData() {
    }

    @Test
    void reactionDataSize() {
    }

    @Test
    void velocityRegulation() {
    }
}