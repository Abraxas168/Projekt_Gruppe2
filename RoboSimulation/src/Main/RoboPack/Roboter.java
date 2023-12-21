package RoboPack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Roboter implements IRobot{
    private Sensor sensor1;
    private Sensor sensor2;
    private Sensor sensor3;
    private String name;
    private int radius;


    public Roboter(Sensor sensor1, Sensor sensor2, Sensor sensor3, String name) {
        this.sensor1=sensor1;
        this.sensor2=sensor2;
        this.sensor3=sensor3;
        this.name=name;
    }

    @Override
    public List<BaseSensor> getSensors() {
        List<BaseSensor> sensoren = new ArrayList<BaseSensor>();
        sensoren.add(sensor1);
        sensoren.add(sensor2);
        sensoren.add(sensor3);
        return sensoren;
    }

    @Override
    public void activateAutonomousStearing() {

    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public void setInitialPose(int posX, int posY, double orientation) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPosX() {
        return 0;
    }

    @Override
    public int getPosY() {
        return 0;
    }

    @Override
    public double getOrientation() {
        return 0;
    }

    @Override
    public int getVelocity() {
        return 0;
    }
    public void setRadius(int newRadius) {
        if (newRadius < 1 || newRadius > 100) {
            throw new IllegalStateException("Radius muss zwischen 1 und 100 liegen.");
        }
        this.radius = newRadius;

    }
    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void move(double deltaTimeSec) {

    }
}
