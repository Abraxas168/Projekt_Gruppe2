package Test;

import org.junit.jupiter.api.Test;
import thu.robots.components.BaseSensor;
import thu.robots.components.Robot;
import thu.robots.components.Sensor;
import thu.robots.components.Steering;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class RobotTest {

   @Test
    void normalizeOrientation(){
       Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
       Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
       Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
       List<BaseSensor> sensors = new ArrayList<>();

       Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

       double normalizedOrientation1 = robot.normalizeOrientation(3 * Math.PI);
       assertEquals(Math.PI, normalizedOrientation1, 0.001);


       double normalizedOrientation2 = robot.normalizeOrientation(-3 * Math.PI);
       assertEquals(-Math.PI, normalizedOrientation2, 0.001);


       double normalizedOrientation3 = robot.normalizeOrientation(-Math.PI / 2);
       assertEquals(-Math.PI / 2, normalizedOrientation3, 0.001);
    }

    @Test
    void setRadius() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

        robot.setRadius(20);
        assertEquals(20, robot.getRadius());

        assertThrows(IllegalStateException.class, () -> robot.setRadius(-100));

        assertThrows(IllegalStateException.class, () -> robot.setRadius(150));
    }

    @Test
    void move() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());
        robot.setVelocity(30);
        robot.move(1.0);

        assertEquals(30, robot.getPosX());
        assertEquals(0, robot.getPosY());

        robot.move(0.5);
        assertEquals(45, robot.getPosX());
        assertEquals(0, robot.getPosY());

        robot.move(0.0);
        assertEquals(45, robot.getPosX());
        assertEquals(0, robot.getPosY());

    }

}
