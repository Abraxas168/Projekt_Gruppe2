package Test;

import thu.robots.components.AutoSteuerung;
import thu.robots.components.Roboter;
import thu.robots.components.Sensor;
import thu.robots.components.Steuerung;

import java.awt.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*public class AutoSteuerungTest {

    @Test
 public void testStuckEmergencyOperation() {
        AutoSteuerung autoSteuerung = new AutoSteuerung();
        Roboter robot = new Roboter(new Sensor(-Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/3, 40),
                new Sensor(Math.PI/3, Math.PI/3, 40), new Sensor(0, Math.PI/5, 30),
                "TestRoboter", 0, 20, Color.RED, new Steuerung());

        for (int i = 0; i < 100; i++) {
            autoSteuerung.steuern(robot);
        }

        assertFalse(robot.getOrientation() != 0.0);
    }
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
    public void testSensorDatenAuswerten() {

    }
}*/