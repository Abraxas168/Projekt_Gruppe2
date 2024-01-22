package thu.robots.components;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.locks.ReentrantLock;


public class AutoSteuerung extends Steuerung implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();

    private int gelesen;
    private long lastAlignmentTime;
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordaten;
    private int countZeros;
    private int stuckCountdown;
    private int steps;
    private ReentrantLock targetlock = new ReentrantLock();

    public AutoSteuerung() {
        this.gelesen = 0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.countSensordaten = 0;
        this.countZeros = 0;
        this.stuckCountdown = 0;
        this.steps = 0;
    }


    public void zielAusrichtung(Roboter robo) {
        int velocity = robo.getVelocity();
        long currentTime = System.currentTimeMillis();
        if (((velocity == IRobot.MAX_VELOCITY) && ((currentTime - lastAlignmentTime) >= ALIGNMENT_INTERVAL)) && (countZeros >= 80)) {
            robo.setOrientation(0.0);
            //System.out.println("zum Ziel ausgerichtet. zeros:  " + countZeros);
            lastAlignmentTime = currentTime;
        }
    }

    //&&
    public void sensorDatenAuswerten(Roboter robo) {
        int n = this.gelesen;
        if ((sensorData != null) && (sensorData.size() > 0)) {
            while (n < sensorData.size()) {
                List<SensorData> daten = sensorData.get(n);
                boolean count = reactionDataSize(daten.size(), robo);
                if (!count) {
                    break;
                }
                for (int j = 0; j < daten.size(); j++) {
                    SensorData sensorData1 = daten.get(j);
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    double beamwidth = relatedSensor.getBeamWidth();
                    boolean gelenkt = lenken(relation_toRobo, distance, angle, beamwidth, robo);

                    if ((distance <= ((robo.getVelocity()) + robo.getRadius())) && (robo.getVelocity() > 20 && countSensordaten >= 1)) {// && !abbremsvorgang && !beschleunigungsvorgang) {
                        //if (!targetlock.isLocked()) {

                        //  targetlock.lock();
                        //  try {
                        int targetVelocity1 = 20;
                        abbremsen(robo, targetVelocity1);
                        System.out.println("abgebremst auf:  " + robo.getVelocity());
                        // } finally {
                        //     targetlock.unlock();
                        // }
                    } //else {
                       // steps = 0;
                   // }
                    if (gelenkt) {
                        break;
                    }
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
            if (countZeros >= 4) {
                if (robo.getVelocity() < 50) {

                    int targetVelocity2 = 50;
                    beschleunigen(robo, targetVelocity2);
                    System.out.println("beschleunigt auf: " + robo.getVelocity());

                }

            }
        }

        if (countSensordaten >= 20) {
            robo.setOrientation(orientation + Math.PI);
            //System.out.println("emergency Steuerung");
            countSensordaten = 0;
            return false;
        }
        return true;
    }


    public boolean lenken(double relation_toRobo, double distance, double angle, double beamwidth, Roboter robo) {
        double orientation = robo.getOrientation();
        String relationRobot = Double.toString(relation_toRobo);
        int velocity = robo.getVelocity();
        if (velocity == 10) {
            stuckCountdown += 1;
        } else {
            stuckCountdown = 0;
        }
        if (stuckCountdown >= 100) {
            robo.setOrientation(orientation + Math.PI);
            //System.out.println(stuckCountdown);
            stuckCountdown = 0;
            //System.out.println("EmergencyStuckOperation");
            return true;
        }
        double orientation1 = orientation + ((beamwidth / 2) - Math.abs(angle));
        double orientation2 = orientation - ((beamwidth / 2) - Math.abs(angle));
        switch (relationRobot) {
            case "0.0":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println(" sensor 0.0 gedreht um:  " + orientation2);
                    //System.out.println("gedreht um:  " + orientation2);
                    return true;
                } else if ((angle >= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    robo.setOrientation(robo.getOrientation() - (Math.PI / 5));
                    System.out.println("sensor 0.0 gedreht um:  " + beamwidth);
                    //System.out.println("gedreht um:  " + beamwidth);
                    return true;
                } else if ((angle >= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (velocity + robo.getRadius())) && (distance > (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    robo.setOrientation(robo.getOrientation() - (Math.PI / 20));
                    System.out.println("sensor 0.0 gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    //System.out.println("gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (20 + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("sensor 0.0 gedreht um:  " + orientation1);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + ((Math.PI) / 5));
                    System.out.println("sensor 0.0 gedreht um:  " + beamwidth);
                    //System.out.println("gedreht um:  " + beamwidth);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 5)) && (distance <= (velocity + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    robo.setOrientation(robo.getOrientation() + (Math.PI / 20));
                    System.out.println("sensor 0.0 gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    //System.out.println("gedreht um:  " + orientation2 + "und 1/20 Pi" + "  bei  " + distance);
                    return true;
                }
            case "1.0471975511965976":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println("+ pi/3 gedreht um:  " + orientation2);
                    //System.out.println("gedreht um:  " + orientation2);
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation - ((beamwidth / 2) + Math.abs(angle)));
                    System.out.println("+ pi/3 gedreht um:  " + "beamwidth/2 und winkel");
                    //System.out.println("gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                }
            case "-1.0471975511965976":
                if ((angle >= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + ((beamwidth / 2) + Math.abs(angle)));
                    System.out.println("- pi/3 gedreht um:  " + "halbe beamwidth und winkel");
                    robo.setOrientation(orientation + ((beamwidth / 2) + Math.abs(angle)));
                    //System.out.println("gedreht um:  " + "halbe beamwidth und winkel");
                    return true;
                } else if ((angle <= 0.0) && (beamwidth == (Math.PI / 3)) && (distance <= (10 + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("- pi/3 gedreht um:  " + orientation1);
                    //System.out.println("gedreht um:  " + orientation1);
                    return true;
                }
        }
        return false;
    }


    /**
     * @param robo
     */
    public void beschleunigen(Roboter robo, int targetVelocity) {
        int velocity = robo.getVelocity();
        if (velocity < targetVelocity - robo.MAX_ACCELERATE * robo.getDeltaTimeSec() * steps) {
            robo.setVelocity((int) (velocity + robo.MAX_ACCELERATE * robo.getDeltaTimeSec() * steps));
            steps = 0;
        } else {
            robo.setVelocity(targetVelocity);
            steps = 0;
        }
    }

    /**
     * @param robo
     */
    public void abbremsen(Roboter robo, int targetVelocity) {
        int velocity = robo.getVelocity();
        if (velocity > targetVelocity + robo.MAX_ACCELERATE * robo.getDeltaTimeSec() * steps) {
            robo.setVelocity((int) (velocity - (robo.MAX_ACCELERATE * robo.getDeltaTimeSec() * steps)));
            System.out.println("wirklich abgebremst auf:  ");
            steps = 0;
        } else {
            robo.setVelocity(targetVelocity);
            steps = 0;
        }
    }


    @Override
    public void steuern(Roboter robo) {
        steps += 1;
        //System.out.println("Schritte" + steps);
        zielAusrichtung(robo);
        sensorDatenAuswerten(robo);

    }


    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
