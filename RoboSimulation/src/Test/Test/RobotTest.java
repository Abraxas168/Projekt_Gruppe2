package Test;

import org.junit.jupiter.api.Test;
import thu.robots.components.BaseSensor;
import thu.robots.components.Robot;
import thu.robots.components.Steering;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RobotTest {

   @Test
    void normalizeOrientation(){
       List<BaseSensor> sensors = new ArrayList<>();
       Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

       Random random = new Random();
       for (int i = 0; i < 3; i++) {
           double randomOrientation = 2 * Math.PI * (random.nextDouble() - 0.5);
           double normalizedOrientation = robot.normalizeOrientation(randomOrientation);
           assertEquals(randomOrientation, normalizedOrientation);
       }
    }

    @Test
    void setRadius() {
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());
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
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

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
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int randomVelocity = random.nextInt(50);
            robot.setVelocity(randomVelocity);
            assertEquals(randomVelocity, robot.getVelocity());
        }
    }
    @Test
    void move() {
        List<BaseSensor> sensors = new ArrayList<>();
        Robot robot = new Robot(sensors, "Testrobot", 30, 20, Color.RED, new Steering());

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            double initialPosX = robot.getPosX();
            double initialPosY = robot.getPosY();
            double initialVelocity = robot.getVelocity();
            double randomDeltaTimeSec = random.nextDouble();
            robot.move(randomDeltaTimeSec);
            double expectedPosX = initialPosX + randomDeltaTimeSec * initialVelocity * Math.cos(Math.ceil(robot.getOrientation() * 100.0) / 100.0);
            double expectedPosY = initialPosY + randomDeltaTimeSec * initialVelocity * Math.sin(Math.ceil(robot.getOrientation() * 100.0) / 100.0);
            int newPosX = (int) Math.round(expectedPosX);
            int newPosY = (int) Math.round(expectedPosY);
            assertEquals(newPosX, robot.getPosX());
            assertEquals(newPosY, robot.getPosY());
        }
    }
}
