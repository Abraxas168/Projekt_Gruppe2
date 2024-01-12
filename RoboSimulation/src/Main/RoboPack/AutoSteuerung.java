package RoboPack;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung{
    private List<List<SensorData>> sensorData=new ArrayList<>();
    private Roboter robo;

    private Validator validator;
    private int gelesen;


    AutoSteuerung(Roboter robo, Validator validator){
        this.robo=robo;
        this.validator=validator;
        this.gelesen=0;
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
        int n=this.gelesen;
        if (sensorData !=null && sensorData.size()>0){
            while(n< sensorData.size()) {
                List<SensorData> daten=sensorData.get(n);
                for (int j = 0; j < daten.size(); j++) {
                    SensorData sensorData1=daten.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    //System.out.println(beamwidth);
                    //System.out.println(angle);
                    sensorData1.getX(); //distance
                    sensorData1.getY(); //distance
                    if (distance <= 35 + robo.getRadius()) {

                        if (relation_toRobo == 0) {
                            System.out.println("Sensor: 0  - " + angle);
                            if (angle >= 0) {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2-Math.abs(angle)));
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation + (beamwidth/2-Math.abs(angle)));
                                break;
                            }
                        }
                        if (relation_toRobo == Math.PI/4) {
                            System.out.println("Sensor: pi/4 - "+ angle);
                            if (angle >= 0) {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2-Math.abs(angle)));
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2+Math.abs(angle)));
                                break;
                            }
                        }
                        if (relation_toRobo == -Math.PI/4) {
                            System.out.println("Sensor: -pi/4:  -" + angle);
                            if (angle >= 0) {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation + (beamwidth/2-Math.abs(angle)));
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation + (beamwidth/2+Math.abs(angle)));
                                break;
                            }
                        }
                    }
                }
            n=n+1;}
        } this.gelesen=sensorData.size();

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
        return sensorData;
    }
    public void setDatafromSensors(List<SensorData> sd){
        this.sensorData.add(sd);
    }
}
