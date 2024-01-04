package RoboPack;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung{
    private List<SensorData> datafromSensors;
    private Roboter robo;
    //private Environment env;


    AutoSteuerung(Roboter robo){
        this.robo=robo;
        //this.env=env;
        //*********vorsicht, mehrere Daten-Listen von verschiedenen sensoren. Funktion measurment from Environment nutzen!************
        //env.simulateSensorData(robo); --> evtl reicht es wenn GUI Daten simuliert?? möglicherweise muss es aber auch hier bzw. eher
        //in der Funktion "Steuern" gemacht werden
        //---> darf hier überhaupt environment verwendet werden?

    }
    public void steuern(){
        int velocity=robo.getVelocity();
        double orientation=robo.getOrientation();
        //*************Berechnung mit Sensordaten!
       if (datafromSensors != null && datafromSensors.size()>0){
        //System.out.println(""+datafromSensors.get(0).getAngle());
        }
        robo.setVelocity(velocity);
        robo.setOrientation(orientation);
    }

    public List<SensorData> getDatafromSensors() {
        return datafromSensors;
    }
    public void setDatafromSensors(List<SensorData> sd){
        this.datafromSensors=sd;
    }
}
