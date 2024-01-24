package thu.robots.components;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.locks.ReentrantLock;


public class AutonomousSteering extends Steering implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();

    private int read;
    private long lastAlignmentTime;
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordata;
    private int countZeros;
    private int stuckCountdown;
    private int steps;
    private ReentrantLock targetlock = new ReentrantLock();

    public AutonomousSteering() {
        this.read = 0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.countSensordata = 0;
        this.countZeros = 0;
        this.stuckCountdown = 0;
        this.steps = 0;
    }


    public void goalAlignment(Robot robo) {
        int velocity = robo.getVelocity();
        long currentTime = System.currentTimeMillis();
        if (((velocity == IRobot.MAX_VELOCITY) && ((currentTime - lastAlignmentTime) >= ALIGNMENT_INTERVAL)) && (countZeros >= 80)) {
            robo.setOrientation(0.0);
            //System.out.println("zum Ziel ausgerichtet. zeros:  " + countZeros);
            lastAlignmentTime = currentTime;
        }
    }

    //&&
    public void EvaluateSensorData(Robot robo) {
        boolean steered = false;
        int n = this.read;
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
                    steered = steer(relation_toRobo, distance, angle, beamwidth, robo);

                    if ((distance <= ((robo.getVelocity()) / 3 + robo.getRadius())) && (robo.getVelocity() > 20 && countSensordata >= 1)) {// && !abbremsvorgang && !beschleunigungsvorgang) {
                        //if (!targetlock.isLocked()) {

                        //  targetlock.lock();
                        //  try {
                        int targetVelocity1 = 20;
                        decelerate(robo, targetVelocity1);
                        //System.out.println("abgebremst auf:  " + robo.getVelocity());
                        // } finally {
                        //     targetlock.unlock();
                        // }
                    } //else {
                    // steps = 0;
                    // }
                    if (steered) {
                        break;
                    }
                }
                // if (gelenkt) {
                //   break;
                // }
                n = n + 1;
            }
        }
        this.read = sensorData.size();
    }


    public boolean reactionDataSize(int size, Robot robo) {
        double orientation = robo.getOrientation();
        if (size == 1) {
            countSensordata += 1;
            countZeros = 0;
        } else {
            countSensordata = 0;
            countZeros += 1;
            if (countZeros >= 4) {
                if (robo.getVelocity() < 50) {

                    int targetVelocity2 = 50;
                    accelerate(robo, targetVelocity2);
                    //System.out.println("beschleunigt auf: " + robo.getVelocity());

                }

            }
        }

        if (countSensordata >= 50) {
            robo.setOrientation(orientation + Math.PI);
            System.out.println("emergency Steuerung");
            countSensordata = 0;
            return false;
        }
        return true;
    }


    public boolean steer(double relation_toRobo, double distance, double angle, double beamwidth, Robot robo) {
        double orientation = robo.getOrientation();
        String relationRobot = Double.toString(relation_toRobo);
        int velocity = robo.getVelocity();
        System.out.println(distance);
        System.out.println(angle);
        boolean stuck = stuckCountdown(robo, orientation);
        if (stuck) return true;
        double orientation1 = robo.getOrientation() + ((beamwidth / 2) - Math.abs(angle));
        double orientation2 = robo.getOrientation() - ((beamwidth / 2) - Math.abs(angle));
        switch (relationRobot) {
            case "0.0":
                if ((angle >= 0.0) && (distance <= (velocity / 4 + robo.getRadius()))) {
                    robo.setOrientation(orientation2 - 0.3);
                    System.out.println(" sensor 0.0 gedreht um:  " + orientation2);

                    return true;

                } else if ((angle < 0.0) && (distance <= (velocity / 4 + robo.getRadius()))) {
                    robo.setOrientation(orientation1 + 0.3);
                    System.out.println("sensor 0.0 gedreht um:  " + orientation1);
                    return true;
                }

            case "1.0471975511965976":
                if ((angle >= 0.0) && (distance <= (7 + robo.getRadius()))) {
                    robo.setOrientation(orientation2 - 0.15);
                    System.out.println("+ pi/3 gedreht um:  " + orientation2);
                    return true;
                } else if ((angle < 0.0) && (distance <= (7 + robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() - ((beamwidth / 2) + Math.abs(angle)) - 0.15);
                    System.out.println("+ pi/3 gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                }
            case "-1.0471975511965976":
                if ((angle >= 0.0) && (distance <= (7 + robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + ((beamwidth / 2) + Math.abs(angle)) + 0.15);
                    System.out.println("- pi/3 gedreht um:  " + "halbe beamwidth und winkel");
                    return true;
                } else if ((angle < 0.0) && (distance <= (7 + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("- pi/3 gedreht um:  " + orientation1 + 0.15);
                    return true;
                }
        }
        return false;
    }

    private boolean stuckCountdown(Robot robo, double orientation) {
        int velocity = robo.getVelocity();
        if (velocity == 20) {
            stuckCountdown += 1;
        } else {
            stuckCountdown = 0;
        }
        if (stuckCountdown >= 400) {
            robo.setOrientation(orientation + Math.PI);
            stuckCountdown = 0;
            System.out.println("EmergencyStuckOperation");
            return true;
        }
        return false;
    }


    /**
     * @param robo
     */
    public void accelerate(Robot robo, int targetVelocity) {
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
    public void decelerate(Robot robo, int targetVelocity) {
        int velocity = robo.getVelocity();
        if (velocity > targetVelocity + 20 * robo.getDeltaTimeSec() * steps) {
            robo.setVelocity((int) (velocity - (20 * robo.getDeltaTimeSec() * steps)));
            System.out.println("wirklich abgebremst auf:  ");
            steps = 0;
        } else {
            robo.setVelocity(targetVelocity);
            steps = 0;
        }
    }


    @Override
    public void steer(Robot robo) {
        steps += 1;
        //System.out.println("Schritte" + steps);
        goalAlignment(robo);
        EvaluateSensorData(robo);

    }


    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
