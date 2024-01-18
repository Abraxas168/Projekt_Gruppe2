package RoboPack;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung implements IObserver{
    private List<List<SensorData>> sensorData=new ArrayList<>();

    private int gelesen;


    AutoSteuerung(){
        this.gelesen=0;
    }
    @Override
    public void steuern(Roboter robo){
        int velocity=robo.getVelocity();
        robo.setVelocity(50);
        double orientation= robo.getOrientation();
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

                    if (distance <= robo.getVelocity()*3+robo.getRadius()) {
                            robo.setVelocity(20);
                        if (relation_toRobo == 0) {
                            System.out.println("Sensor: 0  - " + angle + "Distance:" + distance);
                            if (angle >= 0.0) {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2-Math.abs(angle)));
                                if (beamwidth==Math.PI/5){
                                    robo.setOrientation(robo.getOrientation()-(2*Math.PI)/6);
                                }
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation + (beamwidth/2-Math.abs(angle)));
                                if (beamwidth==Math.PI/5){
                                    robo.setOrientation(robo.getOrientation()+(2*Math.PI)/6);
                                }
                                break;
                            }

                        }
                        if (relation_toRobo == Math.PI/3) {
                            System.out.println("Sensor: pi/3 - "+ angle+ "Distance:" + distance);
                            if (angle >= 0.0) {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2-Math.abs(angle)));
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                robo.setOrientation(orientation - (beamwidth/2+Math.abs(angle)));
                                break;
                            }
                        }
                        if (relation_toRobo == -Math.PI/3) {

                           System.out.println("Sensor: -pi/3:  -" + angle + "Distance:" + distance);
                            if (angle >= 0.0) {
                               orientation=robo.getOrientation();
                                //robo.setOrientation(orientation+Math.PI/4);
                                robo.setOrientation(orientation + (beamwidth/2-Math.abs(angle)));
                                break;
                            } else {
                                orientation=robo.getOrientation();
                                //robo.setOrientation(orientation+Math.PI/2);
                                robo.setOrientation(orientation + (beamwidth/2+Math.abs(angle)));
                                break;
                            }
                        }
                    }
                }
            n=n+1;}
        } this.gelesen=sensorData.size();
        
        //*************Berechnung mit Sensordaten!

        //robo.setVelocity(velocity);
        //robo.setOrientation(orientation);
    }

    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }

    public List<List<SensorData>> getDatafromSensors() {
        return sensorData;
    }
  //  public void setDatafromSensors(List<SensorData> sd){
   //     this.sensorData.add(sd);
    //}
}
