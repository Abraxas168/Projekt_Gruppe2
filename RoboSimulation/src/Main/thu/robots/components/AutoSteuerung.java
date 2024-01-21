package thu.robots.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AutoSteuerung extends Steuerung implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();

    private int gelesen;
    private long lastAlignmentTime;
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordaten;
    private int countZeros;
    public boolean abbremsvorgang;
    public boolean beschleunigungsvorgang;
    private int stuckCountdown;
    public int countAbbremsversuch;
    private ReentrantLock emergencyLock = new ReentrantLock();

    public AutoSteuerung() {
        this.gelesen = 0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.countSensordaten = 0;
        this.countZeros = 0;
        this.abbremsvorgang = false;
        this.stuckCountdown = 0;
        this.countAbbremsversuch = 0;
        this.beschleunigungsvorgang = false;
    }


    public void zielAusrichtung(Roboter robo) {
        emergencyLock.lock();
        try {
            int velocity = robo.getVelocity();
            long currentTime = System.currentTimeMillis();
            if (((velocity == IRobot.MAX_VELOCITY) && ((currentTime - lastAlignmentTime) >= ALIGNMENT_INTERVAL)) || (countZeros >= 20)) {
                robo.setOrientation(0.0);
                System.out.println("zum Ziel ausgerichtet. zeros:  " + countZeros);
                lastAlignmentTime = currentTime;
            }
        } finally {
            emergencyLock.unlock();
        }

    }

    //&&
    public void sensorDatenAuswerten(Roboter robo) {
        int n = this.gelesen;
        double orientation = robo.getOrientation();
        int velocity = robo.getVelocity();
        if ((sensorData != null) && (sensorData.size() > 0)) {
            while (n < sensorData.size()) {
                List<SensorData> daten = sensorData.get(n);
                System.out.println(daten.size());
                boolean count = reactionDataSize(daten.size(), robo);
                if (!count) {
                    break;
                }
                //System.out.println(daten.size()+ "");
                for (int j = 0; j < daten.size(); j++) {
                    SensorData sensorData1 = daten.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    // Thread lenkenThread= new Thread(new Runnable() {
                    // @Override
                    // public void run() {
                    boolean gelenkt = lenken(relation_toRobo, distance, angle, beamwidth, robo);
                    // }
                    // });
                    Thread geschwindigkeitRegulieren = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if ((distance <= ((robo.getVelocity() / 2) + robo.getRadius())) && (robo.getVelocity() > 20) && !abbremsvorgang && !beschleunigungsvorgang) {
                                abbremsvorgang = true;
                                abbremsvorgang = decelerate(20, robo);
                                System.out.println("abbremsen gestartet");
                            }
                        }
                    });

                    //lenkenThread.start();
                    geschwindigkeitRegulieren.start();
                    if (gelenkt) {
                        break;
                    }
                    // if (distance <= 10+robo.getRadius()) {
                    //boolean gelenkt=lenken(relation_toRobo, distance, angle,beamwidth, robo);
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
                    //        robo.decelerate(20);
                    //System.out.println(gelenkt);
                    //if(gelenkt){
                    //break;
                    // }
                    // }
                }
                n = n + 1;
            }
        }
        this.gelesen = sensorData.size();
    }


    public boolean reactionDataSize(int size, Roboter robo) {
        double orientation = robo.getOrientation();
        if (size == 1) {
            countSensordaten += 1;
            countZeros = 0;
        } else {
            countSensordaten = 0;
            countZeros += 1;
            if (countZeros >= 20) {
                System.out.println("gezählte Nullen" + countZeros);
                Thread geschwindigkeitRegulieren2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!beschleunigungsvorgang) {
                            beschleunigungsvorgang = true;
                            beschleunigungsvorgang = accelerate(50, robo);
                            System.out.println("beschleunigen gestartet");
                        }
                    }
                });
                geschwindigkeitRegulieren2.start();
                countZeros = 0;
            }
        }
        emergencyLock.lock();
        try {
            if (countSensordaten >= 20) {
                robo.setOrientation(orientation + Math.PI);
                System.out.println("emergency Steuerung");
                countSensordaten = 0;
                return false;
            }
        } finally {
            emergencyLock.unlock();
        }
        return true;
    }


    public boolean lenken(double relation_toRobo, double distance, double angle, double beamwidth, Roboter robo) {
        double orientation = robo.getOrientation();
        String relationRobot = Double.toString(relation_toRobo);
        //System.out.println(relationRobot);
        int velocity = robo.getVelocity();
        if (velocity == 10) {
            stuckCountdown += 1;
            System.out.println(velocity);
        } else {
            stuckCountdown = 0;
        }
        if (stuckCountdown >= 100) {
            emergencyLock.lock();
            try {
                robo.setOrientation(orientation + Math.PI);
                System.out.println(stuckCountdown);
                stuckCountdown = 0;
                System.out.println("EmergencyStuckOperation");
                return true;
            } finally {
                emergencyLock.unlock();
            }
        }
        double orientation1 = orientation + ((beamwidth / 2) - Math.abs(angle));
        double orientation2 = orientation - ((beamwidth / 2) - Math.abs(angle));
        switch (relationRobot) {
            case "0.0":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println("gedreht um:  " + orientation2);
                    return true;
                } else if ((angle >= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    robo.setOrientation(robo.getOrientation() - ((2 * Math.PI) / 5));
                    return true;
                } else if ((angle >= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (velocity + robo.getRadius())) && (distance > (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    robo.setOrientation(robo.getOrientation() - (Math.PI / 20));
                    System.out.println("gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("gedreht um:  " + orientation1);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + ((2 * Math.PI) / 5));
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (velocity + robo.getRadius())) && (distance > (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    robo.setOrientation(robo.getOrientation() + (Math.PI / 20));
                    System.out.println("gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    return true;
                }
            case "1.0471975511965976":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println("gedreht um:  " + orientation2);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation - ((beamwidth / 2) + Math.abs(angle)));
                    System.out.println("gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                }
            case "-1.0471975511965976":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("gedreht um:  " + orientation1);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation + ((beamwidth / 2) + Math.abs(angle)));
                    System.out.println("gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                }
        }
        return false;
    }

    /**
     * beschleunigt den Roboter abhängig vom übergebenen Ziel-Wert
     *
     * @param targetVelocity int zielwert für die Geschwindigkeit auf die beschleunigt werden soll
     */
    public boolean accelerate(int targetVelocity, Roboter robo) {
        int velocity = robo.getVelocity();
        if (velocity >= targetVelocity) {
            return false;
        }
        int acceleration = targetVelocity - velocity;
        acceleration = Math.min(acceleration, Roboter.MAX_ACCELERATE);

        Timer accelerationTimer = new Timer();
        int finalAcceleration = acceleration;
        accelerationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int velocity = robo.getVelocity();
                velocity += finalAcceleration;
                velocity = Math.min(velocity, Roboter.MAX_VELOCITY);
                robo.setVelocity(velocity);
                System.out.println("beschleunigt run() auf: " + robo.getVelocity());

                if (velocity >= targetVelocity) {
                    accelerationTimer.cancel();
                    System.out.println("beschleunigung beendet bei " + robo.getVelocity());
                }
            }
        }, 0, 100);
        return false;
    }

    public boolean decelerate(int targetVelocity, Roboter robo) {
        int velocity = robo.getVelocity();

        if (velocity <= targetVelocity) {
            System.out.println("abbremsen nicht durchgeführt, zu langsam");
            countAbbremsversuch += 1;
            return false;
        }
        System.out.println("Abbremsvorgang try-Block");
        int deceleration = velocity - targetVelocity;
        deceleration = Math.min(deceleration, Roboter.MAX_ACCELERATE);

        Timer decelerationTimer = new Timer();
        int finalDeceleration = deceleration;
        decelerationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int velocity = robo.getVelocity();
                velocity -= finalDeceleration;
                velocity = Math.max(velocity, 10);
                robo.setVelocity(velocity);
                System.out.println("Abbremsvorgang run() auf:  " + robo.getVelocity());

                if (velocity <= targetVelocity) {
                    decelerationTimer.cancel();
                    System.out.println("abbremsen beendet");
                    countAbbremsversuch = 0;
                }
            }
        }, 0, 100);

        return false;
    }


    @Override
    public void steuern(Roboter robo) {
        zielAusrichtung(robo);
        System.out.println("ZielAusrichten() aufgerufen");
        sensorDatenAuswerten(robo);
        System.out.println(robo.getVelocity());

    }


    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
