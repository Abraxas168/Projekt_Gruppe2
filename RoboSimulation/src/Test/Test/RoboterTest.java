package Test;

//import org.junit.jupiter.api.Test;
import thu.robots.components.IRobot;
import thu.robots.components.Roboter;
import thu.robots.components.Sensor;
import thu.robots.components.Steuerung;

import java.awt.*;

//import static org.junit.jupiter.api.Assertions.*;

class RoboterTest {

   /* @Test
    void normalizeOrientation(){
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 30, 20, Color.RED, new Steuerung());

        double normalizedOrientation = robot.normalizeOrientation(Math.PI + 0.5);
        assertEquals(-Math.PI + 0.5, normalizedOrientation, 0.001);

        normalizedOrientation = robot.normalizeOrientation(-3 * Math.PI / 2);
        assertEquals(Math.PI / 2, normalizedOrientation, 0.001);
    }

    @Test
    void accelerate() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        robot.accelerate(30);
        assertEquals(30, robot.getVelocity());

        robot.accelerate(IRobot.MAX_VELOCITY + 10);
        assertEquals(IRobot.MAX_VELOCITY, robot.getVelocity());
    }

    @Test
    void decelerate() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        robot.decelerate(30);
        assertEquals(30, robot.getVelocity());

        robot.decelerate(-10);
        assertEquals(0, robot.getVelocity());
    }

    @Test
    void setRadius() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        robot.setRadius(20);
        assertEquals(20, robot.getRadius());
        assertThrows(IllegalStateException.class, () -> robot.setRadius(150));
    }

    @Test
    void move() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());
        robot.setVelocity(30);
        robot.move(1.0);

        assertEquals(30, robot.getPosX());
        assertEquals(0, robot.getPosY());
    }*/
}