package Test;

import org.junit.jupiter.api.BeforeEach;
import thu.robots.components.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import thu.robots.components.Robot;

import static java.lang.Math.ceil;
import static org.junit.jupiter.api.Assertions.*;

public class AutonomousSteeringTest {
    private Robot robot;
    private AutonomousSteering autosteering;

    @BeforeEach
    public void beforeEach() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();
        sensors.add(sensor1);
        sensors.add(sensor2);
        sensors.add(sensor3);

        robot = new Robot(sensors, "Testrobot", 30, 10, Color.RED, new Steering());
        robot.activateAutonomousSteering();
        autosteering = (AutonomousSteering) robot.getSteering();
    }

    @Test
    public void goalAlignment() {
       /* Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();

        Robot robot = new Robot(sensors, "Testrobot", 30, 10, Color.RED, new Steering());*/
        robot.setOrientation(Math.PI / 4);

        assertEquals(0.0, robot.getOrientation(), 0.001);
    }

    @Test
    public void navigate() {
        double testAngle = (Math.PI / 6) - 0.1;
        //double testAngle=

        assertTrue(autosteering.navigate(0.0, 14.0, -(testAngle), Math.PI / 3, robot));
        assertEquals(Math.PI / 2, robot.getOrientation());

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(0.0, 39, -(testAngle), Math.PI / 3, robot));
        assertEquals(Math.PI / 2 + 0.1 + 0.25, robot.getOrientation());

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(Math.PI / 3, 19.0, -(testAngle), Math.PI / 3, robot));
        double turnangle2 = ceil(((Math.PI / 6) + (testAngle) + 0.05) * 10.0) / 10.0;
        double actualOrientation = (Math.PI / 2 + 0.1 + 0.25) - turnangle2;
        assertEquals(actualOrientation, robot.getOrientation());

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(-Math.PI / 3, 19.0, -(testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation + 0.1, robot.getOrientation());

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(0.0, 14.0, (testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation + 0.1 - Math.PI / 2, robot.getOrientation());

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(Math.PI / 3, 19.0, (testAngle), Math.PI / 3, robot));
        assertEquals((actualOrientation + 0.1 - Math.PI / 2) - 0.1, robot.getOrientation());

        double actualOrientation2 = ((actualOrientation + 0.1 - Math.PI / 2) - 0.1) + turnangle2;

        assertTrue(((AutonomousSteering) robot.getSteering()).navigate(-Math.PI / 3, 19.0, (testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(0.0, 41.0, -(testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(Math.PI / 3, 21.0, -(testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(-Math.PI / 3, 21.0, -(testAngle), Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(0.0, 41.0, testAngle, Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(Math.PI / 3, 21.0, testAngle, Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

        assertFalse(((AutonomousSteering) robot.getSteering()).navigate(-Math.PI / 3, 21.0, testAngle, Math.PI / 3, robot));
        assertEquals(actualOrientation2, robot.getOrientation());

    }

    @Test
    void reactionDataSize() {
        assertFalse(autosteering.reactionDataSize(1, robot));
        assertEquals(1, autosteering.getCountSensordata());
        assertEquals(0, autosteering.getCountZeros());

        autosteering.reactionDataSize(0, robot);
        assertEquals(0, autosteering.getCountSensordata());
        assertEquals(1, autosteering.getCountZeros());

        autosteering.setGreenLight(25);
        autosteering.reactionDataSize(60, robot);
        assertEquals(60, autosteering.getCountSensordata());
        assertEquals(0, autosteering.getCountZeros());
        assertEquals(robot.MAX_VELOCITY, autosteering.getTargetVelocity());
        autosteering.setGreenLight(0);
        autosteering.setCountZeros(60);
        assertEquals(robot.MAX_VELOCITY, autosteering.getTargetVelocity());

        assertTrue(autosteering.reactionDataSize(140, robot));
        assertEquals(Math.PI, robot.getOrientation());
        assertEquals(0, autosteering.getCountSensordata());

    }

    @Test
    void stuckCountdown() {
        
    }

    @Test
    public void velocityRegulation() {
       /* Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();

        Robot robot = new Robot(sensors, "Testrobot", 30, 10, Color.RED, new Steering());*/


        AutonomousSteering autoSteering = new AutonomousSteering();

        boolean velocityChanged = autoSteering.velocityRegulation(robot);
        autoSteering.targetVelocity = 40;

        assertTrue(velocityChanged);
        assertEquals(40, robot.getVelocity());

        autoSteering.targetVelocity = 40;

        velocityChanged = autoSteering.velocityRegulation(robot);

        assertFalse(velocityChanged);
        assertEquals(40, robot.getVelocity());


    }
}