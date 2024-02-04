package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thu.robots.components.*;
import thu.robots.components.Robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RobotTest {
    private Robot robot;
    @BeforeEach
    public void beforeEach() {
        List<BaseSensor> sensors = new ArrayList<>();
        robot = new Robot(sensors, "Testrobot", 30, 10, Color.RED, new Steering());
    }
   @Test
    void normalizeOrientation(){
       double result1 = robot.normalizeOrientation(2.0);
       assertEquals(2.0, result1);

       double result2 = robot.normalizeOrientation(-3*Math.PI);
       assertEquals(-Math.PI, result2);

       double result4 = robot.normalizeOrientation(-Math.PI);
       assertEquals(Math.PI, result4);

    }

    @Test
    void setRadius() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int randomRadius = random.nextInt(101);
            robot.setRadius(randomRadius);
            assertEquals(randomRadius, robot.getRadius());
        }

        assertThrows(IllegalStateException.class, () -> robot.setRadius(-100));
        assertThrows(IllegalStateException.class, () -> robot.setRadius(150));
    }
    @Test
    void setInitialPose(){
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int randomPosX = random.nextInt(301);
            int randomPosY = random.nextInt(301);
            double randomOrientation = 2*Math.PI*(random.nextDouble()-0.5);

            robot.setInitialPose(randomPosX, randomPosY, randomOrientation);

            assertEquals(randomPosX, robot.getPosX());
            assertEquals(randomPosY, robot.getPosY());
            assertEquals(randomOrientation, robot.getOrientation());
        }
    }
    @Test
    void setVelocity(){
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int randomVelocity = random.nextInt(50);
            robot.setVelocity(randomVelocity);
            assertEquals(randomVelocity, robot.getVelocity());
        }
    }
    @Test
    void move() {
        robot.move(1.0);
        assertEquals(robot.getVelocity(), robot.getPosX());
        assertEquals(0, robot.getPosY());

        robot.move(0.5);
        assertEquals(robot.getVelocity()+0.5*30, robot.getPosX());
        assertEquals(0, robot.getPosY());

        robot.move(0.2);
        assertEquals(robot.getVelocity()+0.5*30+0.2*30, robot.getPosX());
        assertEquals(0, robot.getPosY());
    }
}
