package thu.robots.components;

import thu.robots.components.BaseSensor;
import thu.robots.components.IRobot;
import thu.robots.components.SensorData;

import java.util.ArrayList;
import java.util.List;

public class AutoSteuerung extends Steuerung implements IObserver{
    private List<List<SensorData>> sensorData=new ArrayList<>();

    private int gelesen;
    private long lastAlignmentTime;
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordaten;
    private int countZeros;

    AutoSteuerung(){
        this.gelesen=0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.countSensordaten=0;
        this.countZeros=0;
    }
    @Override
    public void steuern(Roboter robo){
        int velocity=robo.getVelocity();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAlignmentTime >= ALIGNMENT_INTERVAL && velocity == IRobot.MAX_VELOCITY && countZeros>=20) {
            robo.setOrientation(0.0);
            lastAlignmentTime = currentTime;
        }
        double orientation = robo.getOrientation();
        int n=this.gelesen;
        if (sensorData !=null && sensorData.size()>0){
            while(n< sensorData.size()) {
                List<SensorData> daten=sensorData.get(n);
                //System.out.println(daten.size()+ "");
                if(daten.size()==1){
                    countSensordaten+=1;
                    countZeros=0;}
                else{countSensordaten=0;
                    countZeros+=1;
                    if (countZeros>=4){
                    robo.accelerate(50);}}
                if(countSensordaten>=4){
                    robo.setOrientation(orientation+Math.PI);
                    countSensordaten=0;
                    break;
                }
                for (int j = 0; j < daten.size(); j++) {
                    SensorData sensorData1=daten.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    if(distance <= robo.getVelocity()+robo.getRadius()){
                        robo.decelerate(10);
                    }
                    if (distance <= robo.getVelocity()*2+robo.getRadius()) {
                        robo.decelerate(20);
                        if (relation_toRobo == 0) {
                            //System.out.println("Sensor: 0  - " + angle + "Distance:" + distance);
                            if (angle >= 0.0) {
                                robo.setOrientation(orientation - (beamwidth/2-Math.abs(angle)));
                                if (beamwidth==Math.PI/5 && distance<=robo.getVelocity()+robo.getRadius()){
                                    robo.setOrientation(robo.getOrientation()-(2*Math.PI)/5);
                                }
                                break;
                            } else {
                                robo.setOrientation(orientation + (beamwidth/2-Math.abs(angle)));
                                if (beamwidth==Math.PI/5 && distance<=robo.getVelocity()+robo.getRadius()){
                                    robo.setOrientation(robo.getOrientation()+(2*Math.PI)/5);
                                }
                                break;
                            }
                        }
                        if (relation_toRobo == Math.PI/3) {
                            //System.out.println("Sensor: pi/3 - "+ angle+ "Distance:" + distance);
                            if (angle >= 0.0) {
                                robo.setOrientation(orientation - ((beamwidth/2)-Math.abs(angle)));
                                break;
                            } else {
                                robo.setOrientation(orientation - ((beamwidth/2)+Math.abs(angle)));
                                break;
                            }
                        }
                        if (relation_toRobo == -Math.PI/3) {

                           //System.out.println("Sensor: -pi/3:  -" + angle + "Distance:" + distance);
                            if (angle >= 0.0) {
                                robo.setOrientation(orientation + ((beamwidth/2)-Math.abs(angle)));
                                break;
                            } else {
                                robo.setOrientation(orientation + ((beamwidth/2)+Math.abs(angle)));
                                break;
                            }
                        }
                    }
                }
            n=n+1;}
        } this.gelesen=sensorData.size();}

    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }


    public List<List<SensorData>> getDatafromSensors() {
        return sensorData;
    }
}
