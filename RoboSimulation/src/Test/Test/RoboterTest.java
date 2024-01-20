package Test;

import org.junit.jupiter.api.Test;
import thu.robots.components.Roboter;
import thu.robots.components.Sensor;
import thu.robots.components.Steuerung;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class RoboterTest {

    @Test
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
    }

    @Test
    void decelerate() {
    }

    @Test
    void setRadius() {
    }

    @Test
    void move() {
    }
}