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
    private boolean abbremsvorgang;

    AutoSteuerung(){
        this.gelesen=0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.countSensordaten=0;
        this.countZeros=0;
        this.abbremsvorgang=false;
    }


    public void zielAusrichtung(Roboter robo){
        int velocity=robo.getVelocity();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAlignmentTime >= ALIGNMENT_INTERVAL && velocity == IRobot.MAX_VELOCITY && countZeros>=20) {
            robo.setOrientation(0.0);
            lastAlignmentTime = currentTime;
        }
    }


    public void sensorDatenAuswerten(Roboter robo){
        int n=this.gelesen;
        double orientation=robo.getOrientation();
        int velocity =robo.getVelocity();
        if (sensorData !=null && sensorData.size()>0){
            while(n< sensorData.size()) {
                List<SensorData> daten=sensorData.get(n);
                boolean count = reactionDataSize(daten.size(), robo);
                if(!count){break;}
                //System.out.println(daten.size()+ "");
                for (int j = 0; j < daten.size(); j++) {
                    SensorData sensorData1=daten.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    if (distance <= robo.getVelocity()*2+robo.getRadius()) {
                    boolean gelenkt=lenken(relation_toRobo, distance, angle,beamwidth, robo);
                    //*************logik zum abbremsen muss noch vervollständigt werden
                        //**********momentan werden keine weiteren daten mehr ausgelesen sobald robo zu nah am
                         //************hindernis und abbremsen (mehrfach) ausgelöst wurde zudem bleibt er stehen.
                            //**********bis auf das problem funktioniert alles gut. bitte nur an dieser stelle
                                //*********oder in den beschleunigungsfunktionen oder höchstens noch bei reactionToSize()
                                    //********beim beschleunigen etwas ändern wenn nötig.

                    //if(velocity>20 && !abbremsvorgang){
                    //    this.abbremsvorgang=true;
                    //    this.abbremsvorgang=robo.decelerate(20);
                   // }
                            robo.decelerate(20);
                    if(gelenkt){
                        break;
                        }
                    }
                }
                n=n+1;}
        } this.gelesen=sensorData.size();}


    public boolean reactionDataSize(int size, Roboter robo){
        double orientation=robo.getOrientation();
        if(size==1){
            countSensordaten+=1;
            countZeros=0;}
        else{countSensordaten=0;
            countZeros+=1;
            if (countZeros>=4){
                robo.accelerate(50);}}
        if(countSensordaten>=4){
            robo.setOrientation(orientation+Math.PI);
            countSensordaten=0;
            return false;
        }
        return true;
    }



    public boolean lenken(double relation_toRobo, double distance, double angle,double beamwidth, Roboter robo){
            double orientation=robo.getOrientation();
            String relationRobot=Double.toString(relation_toRobo);
            System.out.println(relationRobot);
            double orientation1 = orientation + ((beamwidth / 2) - Math.abs(angle));
            double orientation2 = orientation - ((beamwidth / 2) - Math.abs(angle));
            switch (relationRobot) {
                case "0.0":
                if (angle >= 0.0) {
                    robo.setOrientation(orientation2);
                    if (beamwidth==Math.PI/5 && distance<=robo.getVelocity()+robo.getRadius()){
                        robo.setOrientation(robo.getOrientation()-(2*Math.PI)/5);
                    }
                    return true;
                } else {
                    robo.setOrientation(orientation1);
                    if (beamwidth==Math.PI/5 && distance<=robo.getVelocity()+robo.getRadius()){
                        robo.setOrientation(robo.getOrientation()+(2*Math.PI)/5);
                    }
                    return true;
                }
                case "1.0471975511965976":
                if (angle >= 0.0) {
                    robo.setOrientation(orientation2);
                    return true;
                } else {
                    robo.setOrientation(orientation - ((beamwidth/2)+Math.abs(angle)));
                    return true;
                }
                case "-1.0471975511965976":
                if (angle >= 0.0) {
                    robo.setOrientation(orientation1);
                    return true;
                } else {
                    robo.setOrientation(orientation + ((beamwidth/2)+Math.abs(angle)));
                    return true;
                }
            }
        return false;
        }



    @Override
    public void steuern(Roboter robo){
        zielAusrichtung(robo);
        sensorDatenAuswerten(robo);
    }


    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
