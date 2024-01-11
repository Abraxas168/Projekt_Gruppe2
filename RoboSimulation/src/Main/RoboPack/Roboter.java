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
    private ManuelleSteuerung manuelleSteuerung;
    private AutoSteuerung autoSteuerung;


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
        Roboter robo= manuelleSteuerung.getRobo();
        Validator validator=manuelleSteuerung.getValidator();
        this.autoSteuerung=new AutoSteuerung(robo, validator);
        sensor1.setAutoSteuerung(autoSteuerung);
        sensor2.setAutoSteuerung(autoSteuerung);
        sensor3.setAutoSteuerung(autoSteuerung);
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
        this.orientation=normalizeOrientation(orientation);
    }

    private double normalizeOrientation (double orientation){
        if (orientation <= -Math.PI) {
            orientation = 2*Math.PI + orientation;
        } else if (orientation > Math.PI) {
            orientation = (orientation - Math.PI*2);
        }
        return orientation;
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
    public void setManuelleSteuerung(ManuelleSteuerung manu){
        this.manuelleSteuerung=manu;
        sensor1.setManuelleSteuerung(manuelleSteuerung);
        sensor2.setManuelleSteuerung(manuelleSteuerung);
        sensor3.setManuelleSteuerung(manuelleSteuerung);
    }

    public ManuelleSteuerung getManuelleSteuerung(){return this.manuelleSteuerung;}

    public AutoSteuerung getAutoSteuerung(){return this.autoSteuerung;}

    @Override
    public void move(double deltaTimeSec) {
        if(velocity >0){
        double deltaX=deltaTimeSec*velocity*Math.cos(orientation);
        double deltaY=deltaTimeSec*velocity*Math.sin(orientation);
            double x_neu= posX+deltaX;
            double y_neu= posY+deltaY;
            posX=(int) x_neu;
            posY=(int) y_neu;}
        else{
            double future_deltaX=deltaTimeSec*velocity*Math.cos(orientation);
            double future_deltaY=deltaTimeSec*velocity*Math.sin(orientation);
            double x_neu= posX+future_deltaX;
            double y_neu= posY+future_deltaY;
            posX=(int) x_neu;
            posY=(int) y_neu;}
        //******test**** steuern() muss jeweils noch geschrieben werden!
        if(autoSteuerung != null){
            //System.out.println("AutoSteuerung aktiviert");
            autoSteuerung.steuern();
        }else if (manuelleSteuerung != null){
            //System.out.println("ManuelleSteuerung");
            manuelleSteuerung.steuern();
            }
        }
    }