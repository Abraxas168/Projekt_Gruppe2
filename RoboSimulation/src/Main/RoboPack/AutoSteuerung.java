package RoboPack;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung{
    private List<List<SensorData>> datafromSensors=new ArrayList<>();
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
            robo.setVelocity(0);
        }
        int velocity=robo.getVelocity();
        double orientation=robo.getOrientation();
        //System.out.println(orientation);

        if (datafromSensors!=null && datafromSensors.size()>0){
            for(int n=0; n<datafromSensors.size(); n++) {
                List<SensorData> sensorData=datafromSensors.get(n);
                for (int j = 0; j < sensorData.size(); j++) {
                    SensorData sensorData1=sensorData.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    //System.out.println(distance);
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    //System.out.println(beamwidth);
                    System.out.println(angle);
                    sensorData1.getX(); //distance
                    sensorData1.getY(); //distance
                    if (distance <= 30 + robo.getRadius()) {
                        robo.setVelocity(10);
                        if (relation_toRobo == 0) {
                            if (angle >= 0) {
                                robo.setOrientation(orientation - (angle));
                            } else {
                                robo.setOrientation(orientation + (angle));
                            }
                        }
                        //  if(angle>=0){
                        //     robo.setOrientation(orientation-5);}
                        // else{robo.setOrientation(orientation+5);}
                    }
                }
            }
        }

        EnvironmentObject hindernis =validator.checkCollosion(robo);
        if ( hindernis != null){
            int x_hindernis = hindernis.getX();
            int y_hindernis = hindernis.getY();
            //System.out.println("x-Position Hindernis: " + x_hindernis + "  y-Position Hindernis: " +y_hindernis);
            //System.out.println("xPos Roboter:  " + robo.getPosX() + "  yPos Roboter: " + robo.getPosY());
            robo.setVelocity(0);
        }

        //*************Berechnung mit Sensordaten!

        //robo.setVelocity(velocity);
        //robo.setOrientation(orientation);
    }

    public List<List<SensorData>> getDatafromSensors() {
        return datafromSensors;
    }
    public void setDatafromSensors(List<SensorData> sd){
        this.datafromSensors.add(sd);
    }
}
