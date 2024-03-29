package thu.robots.components;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die mit der Funktion createRobot einen Roboter erstellt, der das Interface IRobot implementiert
 */
public class RobotFactory extends AbstractRobotFactory {

    /**
     * Instanziiert einen speziellen Roboter (customrobot). Dabei muss dieser auch mit Sensoren ausgestattet werden.
     * @return der erstellte Roboter
     */
    @Override
    public IRobot createRobot() {
        Sensor sensor1 = new Sensor(-Math.PI / 3, Math.PI / 3, 50);
        Sensor sensor2 = new Sensor(0, Math.PI / 3, 50);
        Sensor sensor3 = new Sensor(Math.PI / 3, Math.PI / 3, 50);
        List<BaseSensor> sensoren = new ArrayList<>();
        sensoren.add(sensor1);
        sensoren.add(sensor2);
        sensoren.add(sensor3);
        String name = "christine";
        int init_velocity = 30;
        int init_radius = 10;
        Color color = Color.MAGENTA;
        Steering steering = new Steering();
        Robot hotWheels = new Robot(sensoren, name, init_velocity, init_radius, color, steering);
        return hotWheels;
    }
}
