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
    private int posX;
    private int posY;
    private double orientation;
    private int velocity;
    private Color color;


    public Roboter(Sensor sensor1, Sensor sensor2, Sensor sensor3, String name, int velocity, int radius, Color color) {
        this.sensor1=sensor1;
        this.sensor2=sensor2;
        this.sensor3=sensor3;
        this.name=name;
        this.velocity=velocity;
        this.radius=radius;
        this.color=color;
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
        this.posX= posX;
        this.posY=posY;
        this.orientation=orientation;
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

    public void setOrientation(double orientation){
        this.orientation=orientation;
    }

    @Override
    public int getVelocity() {
        return this.velocity;
    }
    public void setVelocity(int velocity){
        this.velocity=velocity;
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

    public Color getColor(){return this.color;}

    @Override
    public void move(double deltaTimeSec) {
        if (posX>=572 | posX<=26 | posY>=373 | posY<=25 ) {
            orientation=orientation+45;
        }
        double x_neu= posX+deltaTimeSec*velocity*Math.cos(orientation*Math.PI/180.0);
        double y_neu= posY+deltaTimeSec*velocity*Math.sin(orientation*Math.PI/180.0);
        posX=(int) x_neu;
        posY=(int) y_neu;
    }
    }