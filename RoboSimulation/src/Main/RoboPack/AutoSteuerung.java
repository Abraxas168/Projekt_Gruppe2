package RoboPack;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung{
    private List<SensorData> datafromSensors;
    private Roboter robo;

    private Validator validator;


    AutoSteuerung(Roboter robo, Validator validator){
        this.robo=robo;
        this.validator=validator;
        //this.env=env;
        //*********vorsicht, mehrere Daten-Listen von verschiedenen sensoren. Funktion measurment from Environment nutzen!************
        //env.simulateSensorData(robo); --> evtl reicht es wenn GUI Daten simuliert?? möglicherweise muss es aber auch hier bzw. eher
        //in der Funktion "Steuern" gemacht werden
        //---> darf hier überhaupt environment verwendet werden?

    }
    public void steuern(){
        if (this.validator == null){
            return;
        }
        if (validator.checkTargetZone(robo)){
            System.out.println("Ziel erreicht!");
        }
        if (datafromSensors==null || datafromSensors.size()==0){
            return;
        }
                EnvironmentObject hindernis =validator.checkCollosion(robo);
        if ( hindernis != null){
            int x_hindernis = hindernis.getX();
            int y_hindernis = hindernis.getY();
            System.out.println("x-Position Hindernis: " + x_hindernis + "  y-Position Hindernis: " +y_hindernis);
            System.out.println("xPos Roboter:  " + robo.getPosX() + "  yPos Roboter: " + robo.getPosY());
            robo.setVelocity(0);
            return;
        }
        int velocity=robo.getVelocity();
        double orientation=robo.getOrientation();
        //*************Berechnung mit Sensordaten!
        //robo.setVelocity(velocity);
        //robo.setOrientation(orientation);
    }

    public List<SensorData> getDatafromSensors() {
        return datafromSensors;
    }
    public void setDatafromSensors(List<SensorData> sd){
        this.datafromSensors=sd;
    }
}
