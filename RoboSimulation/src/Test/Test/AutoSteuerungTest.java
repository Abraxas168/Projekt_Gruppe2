package Test;

import org.junit.jupiter.api.Test;

import thu.robots.components.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class AutoSteuerungTest {

    @Test
    public void testZielAusrichtung() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        robot.setOrientation(Math.PI / 4);

        AutoSteuerung autoSteuerung = (AutoSteuerung) robot.getSteuerung();
        autoSteuerung.zielAusrichtung(robot);
        assertEquals(0.0, robot.getOrientation(), 0.001);
    }
    @Test
    public void testLenken() {
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        robot.setOrientation(0.0);

        AutoSteuerung autoSteuerung = (AutoSteuerung) robot.getSteuerung();

        assertTrue(autoSteuerung.lenken(0.0, 10.0, 0.1, Math.PI / 5, robot));
        assertEquals(-Math.PI / 5, robot.getOrientation(), 0.001); // Adjust delta as needed
    }
    @Test
    public void testSensorDatenAuswerten() {

    }
}