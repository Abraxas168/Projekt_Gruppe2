package RoboPack;

import thu.robots.components.BaseSensor;
import thu.robots.components.IRobot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Roboter implements IRobot {
    private Sensor sensor1;
    private Sensor sensor2;
    private Sensor sensor3;
    private Sensor sensor4;
    private String name;
    private int radius;
    private int posX;
    private int posY;
    private double orientation;
    private int velocity;
    private Color color;
    private Steuerung steuerung;
    private double deltaTimeSec;


    public Roboter(Sensor sensor1, Sensor sensor2, Sensor sensor3, Sensor sensor4, String name, int velocity, int radius, Color color, Steuerung steuerung) {
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.name = name;
        this.velocity = velocity;
        this.radius = radius;
        this.color = color;
        this.steuerung = steuerung;
    }

    @Override
    public List<BaseSensor> getSensors() {
        List<BaseSensor> sensoren = new ArrayList<BaseSensor>();
        sensoren.add(sensor1);
        sensoren.add(sensor2);
        sensoren.add(sensor3);
        sensoren.add(sensor4);
        return sensoren;
    }

    @Override
    public void activateAutonomousStearing() {
        //Roboter robo= manuelleSteuerung.getRobo();
        //Validator validator=manuelleSteuerung.getValidator();
        this.steuerung = new AutoSteuerung();
        //steuerung=(AutoSteuerung)steuerung;
    }

    @Override
    public void setInitialPose(int posX, int posY, double orientation) {
        this.posX = posX;
        this.posY = posY;
        this.orientation = orientation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPosX() {
        return this.posX;
    }

    @Override
    public int getPosY() {
        return this.posY;
    }

    @Override
    public double getOrientation() {
        return this.orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = normalizeOrientation(orientation);
    }

    private double normalizeOrientation(double orientation) {
        if (orientation <= -Math.PI) {
            orientation = 2 * Math.PI + orientation;
        } else if (orientation > Math.PI) {
            orientation = (orientation - Math.PI * 2);
        }
        return orientation;
    }

    @Override
    public int getVelocity() {
        return this.velocity;
    }

    public void setVelocity(int velocity) {
        if (velocity <= MAX_VELOCITY && velocity>=-MAX_VELOCITY) {
            this.velocity = velocity;
        }
    }

    public void accelerate(int targetVelocity) {
        int acceleration = targetVelocity - velocity;
        acceleration = Math.min(acceleration, MAX_ACCELERATE);
        velocity += acceleration;
        velocity = Math.min(velocity, MAX_VELOCITY);
    }
    public void decelerate(int targetVelocity) {
        int deceleration = velocity - targetVelocity;
        deceleration = Math.min(deceleration, MAX_ACCELERATE);
        velocity -= deceleration;
        velocity = Math.max(velocity, -MAX_VELOCITY);
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

    public Color getColor() {
        return this.color;
    }

    public Steuerung getSteuerung() {
        return steuerung;
    }

    public double getDeltaTimeSec() {
        return deltaTimeSec;
    }

    @Override
    public void move(double deltaTimeSec) {
            this.deltaTimeSec=deltaTimeSec;
            double deltaX = deltaTimeSec * velocity * Math.cos(orientation);
            double deltaY = deltaTimeSec * velocity * Math.sin(orientation);
            double x_neu = posX + deltaX;
            double y_neu = posY + deltaY;
            posX = (int) x_neu;
            posY = (int) y_neu;
        if (steuerung instanceof AutoSteuerung) {
            //System.out.println("AutoSteuerung aktiviert");
            steuerung.steuern(this);
        }
    }
}