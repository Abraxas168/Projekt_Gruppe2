/*package Test;

import thu.robots.components.AutonomousSteering;
import thu.robots.components.Robot;
import thu.robots.components.Sensor;
import thu.robots.components.Steering;

import java.awt.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;*/

/*public class AutonomousSteeringTest {

    @Test
 public void testStuckEmergencyOperation() {
        AutonomousSteering autoSteering = new AutonomousSteering();
        Robot robot = new Robot(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steering());

        for (int i = 0; i < 100; i++) {
            autoSteering.steer(robot);
        }

        assertFalse(robot.getOrientation() != 0.0);
    }
    public void testgoalAlignment() {
        Robot robot = new Robot(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steering());

        robot.setOrientation(Math.PI / 4);

        AutonomousSteering autoSteering = (AutonomousSteering) robot.getSteering();
        autoSteering.goalAlignment(robot);
        assertEquals(0.0, robot.getOrientation(), 0.001);
    }
    @Test
    public void testevaluateSensorData() {

    }
}*/