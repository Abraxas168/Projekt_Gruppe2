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
    private int targetVelocity;

    /**
     * Instanziiert eine Autonome Steuerung der Klasse AutonomousSteering mit Default-Werten für ihre Eigenschaften
     */
    public AutonomousSteering() {
        this.read = 0;
        this.countSensordata = 0;
        this.countZeros = 0;
        this.stuckCountdown = 0;
        this.steps = 0;
        this.lastAlignmentTime = System.currentTimeMillis();
        this.targetVelocity = 50;
    }


    /**
     * Richtet die Orientierung des Roboters, nach einer Zeitspanne von 4sec und abhängig von der Anzahl seiner Sensordaten sowie seiner Geschwindigkeit,
     * auf 0.0 und somit in Richtung des Zieles aus.
     *
     * @param robo Roboter der Klasse Robot
     */
    public void goalAlignment(Robot robo) {
        int velocity = robo.getVelocity();
        long currentTime = System.currentTimeMillis();
        if (((velocity == IRobot.MAX_VELOCITY) && ((currentTime - lastAlignmentTime) >= ALIGNMENT_INTERVAL)) && (countZeros >= 80)) {
            robo.setOrientation(0.0);
            lastAlignmentTime = currentTime;
        }
    }

    /**
     * Liest die gesammelten Sensordaten der Liste sensordata aus und ruft, abhängig von diesen Daten, weitere Funktionen
     * der Klasse Steering oder Robot auf.
     *
     * @param robo Roboter der Klasse Robot
     */
    //&&
    public void EvaluateSensorData(Robot robo) {
        boolean steered = false;
        int n = this.read;
        if ((sensorData != null) && (sensorData.size() > 0)) {
            while (n < sensorData.size()) {
                List<SensorData> data = sensorData.get(n);
                boolean count = reactionDataSize(data.size(), robo);
                if (!count) {
                    break;
                }
                for (SensorData sensorData1 : data) {
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double beamwidth = relatedSensor.getBeamWidth();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    steered = steer(relation_toRobo, distance, angle, beamwidth, robo);
                    System.out.println(countSensordata);
                    if ((distance <= ((robo.getVelocity()) + robo.getRadius())) && (robo.getVelocity() > 20 && countSensordata >= 1)) {
                        this.targetVelocity = 20;
                        // decelerate(robo, targetVelocity1);

                    }
                    if (steered) {
                        n = sensorData.size();
                        break;
                    }
                }
                n = n + 1;
            }
        }
        this.read = sensorData.size();
    }


    /**
     * Funktion, die auf die Anzahl der eingegangenen Sensordaten reagiert. Sie zählt dafür mit Hilfe von Zählvariablen wie oft
     * Sensordaten übermittelt wurden bzw. keine Sensordaten übermittelt wurden und ruft abhängig davon die Funktion
     * accelerate auf oder löst eine Notfallsteuerung aus, wobei sich der Roboter um PI dreht.
     *
     * @param size int, länge der Liste der seit dem letzten Zeitschritt übermittelten Sensordaten
     * @param robo
     * @return
     */
    public boolean reactionDataSize(int size, Robot robo) {
        System.out.println(size);
        double orientation = robo.getOrientation();
        if (size >= 1) {
            countSensordata += 1;
            countZeros = 0;
        } else {
            countSensordata = 0;
            countZeros += 1;
            System.out.println("gezählteNullen:" + countZeros);
            if (countZeros >= 100) {
                if (robo.getVelocity() < 50) {
                    this.targetVelocity = 50;
                    //accelerate(robo, targetVelocity2);
                }
            }
        }

        if (countSensordata >= 400) {
            robo.setOrientation(orientation + Math.PI);
            countSensordata = 0;
            System.out.println("EmergencySteering");
            return false;
        }
        return true;
    }


    /**
     * Erhält ausgelesene Sensor Daten der Klasse SensorData und lenkt den Roboter abhängig von den empfangenen Daten, bis das Hindernis außerhalb
     * seines Sichtfeldes ist.
     *
     * @param relation_toRobo Orientierung des Sensors relativ zur Orientierung des Roboters
     * @param distance        Entfernung des Datenpunktes in Pixel vom Roboter
     * @param angle           Orientierung des Datenpunktes relativ zum Sensor, der den Datenpunkt empfangen hat
     * @param beamwidth       Strahlbreite des Sensors, der den Datenpunkt empfangen hab
     * @param robo            Dazugehöriger Roboter Robo
     * @return Boolean, wenn gelenkt wurde, true
     */
    public boolean steer(double relation_toRobo, double distance, double angle, double beamwidth, Robot robo) {
        double orientation = robo.getOrientation();
        int velocity = robo.getVelocity();
        String relationRobot = Double.toString(relation_toRobo);
        System.out.println(distance);
        System.out.println(angle);
        boolean stuck = stuckCountdown(robo, orientation);
        if (stuck) return true;
        double turnAngle1 = (beamwidth / 2.0) - Math.abs(angle) + 0.15;
        double orientation1 = robo.getOrientation() + (turnAngle1);
        double orientation2 = robo.getOrientation() - (turnAngle1);
        switch (relationRobot) {
            case "0.0":
                if ((angle >= 0.0) && (distance <= ((velocity / 4.0) + robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println(" sensor 0.0 gedreht um:  " + orientation2);

                    return true;

                } else if ((angle < 0.0) && (distance <= ((velocity / 4.0) + robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("sensor 0.0 gedreht um:  " + orientation1);
                    return true;
                } else {
                    return false;
                }

            case "1.0471975511965976":
                if ((angle >= 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    System.out.println("+ pi/3 gedreht um:  " + orientation2);
                    return true;
                } else if ((angle < 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() - ((beamwidth / 2.0) + Math.abs(angle) + 0.15));
                    System.out.println("+ pi/3 gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                } else {
                    return false;
                }
            case "-1.0471975511965976":
                if ((angle >= 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + ((beamwidth / 2.0) + Math.abs(angle) + 0.15));
                    System.out.println("- pi/3 gedreht um:  " + "halbe beamwidth und winkel");
                    return true;
                } else if ((angle < 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    System.out.println("- pi/3 gedreht um:  " + orientation1);
                    return true;
                } else {
                    return false;
                }
                //break;
            default:
                throw new IllegalStateException("Unexpected value: " + relationRobot);
        }
    }

    /**
     * Funktion die den Roboter unabhängig von den Sensordaten lenkt, sobald dieser eine bestimmte Zeit lang 20P/s fährt
     *
     * @param robo        Roboter der Klasse Robot
     * @param orientation Orientierung des Roboters als double in Radiant
     * @return Boolean, true, wenn gelenkt wurde
     */
    private boolean stuckCountdown(Robot robo, double orientation) {
        int velocity = robo.getVelocity();
        if (velocity == 20) {
            stuckCountdown += 1;
        } else {
            stuckCountdown = 0;
        }
        if (stuckCountdown >= 700) {
            robo.setOrientation(orientation + Math.PI);
            stuckCountdown = 0;
            System.out.println("EmergencyStuckOperation");
            return true;
        }
        return false;
    }


    /**
     * Funktion die den Roboter beschleunigt, falls die Geschwindigkeit nicht nach der Beschleunigung die Zielgeschwindigkeit überschreiten würde.
     * Beschleunigt um 10Pixel/s^2 Pro Zeitdifferenz für Berechnung der Bewegung, pro Zeitschritte, die seit dem letzten Abbremsvorgang vergangen sind.
     *
     * @param robo           Roboter der Klasse Robot
     * @param targetVelocity int Zielgeschwindigkeit
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
     * Funktion die den Roboter abbremst, falls die Geschwindigkeit nicht nach der Beschleunigung die Zielgeschwindigkeit unterschreiten würde.
     * bremast um 20Pixel/s^2 Pro Zeitdifferenz für Berechnung der Bewegung, pro Zeitschritte, die seit dem letzten Beschleunigungsvorgang vergangen sind.
     *
     * @param robo           Roboter der Klasse Robot
     * @param targetVelocity int Zielgeschwindigkeit
     */
    public void decelerate(Robot robo, int targetVelocity) {
        int velocity = robo.getVelocity();
        if (velocity > (targetVelocity + (20 * robo.getDeltaTimeSec() * steps))) {
            robo.setVelocity((int) (velocity - (20 * robo.getDeltaTimeSec() * steps)));
            steps = 0;
        } else {
            robo.setVelocity(targetVelocity);
            steps = 0;
        }
    }

    public boolean velocityRegulation(Robot robo, int targetVelocity) {
        System.out.println(targetVelocity);
        int velocity = robo.getVelocity();
        if (velocity < targetVelocity) {
            robo.setVelocity((velocity + robo.MAX_ACCELERATE));
            return true;
        }
        if (velocity > targetVelocity) {
            robo.setVelocity((velocity - 20));
            return true;
        }
        return false;
    }


    /**
     * Koodiniert die Steuerung indem sie weitere Funktionen aufruft und die Durchläufe Zählt
     *
     * @param robo Roboter der Klasse Robot
     */
    @Override
    public void steer(Robot robo) {
        //steps += 1;
        velocityRegulation(robo, targetVelocity);
        goalAlignment(robo);
        EvaluateSensorData(robo);

    }


    /**
     * Funktion, die die Sensordaten aktualisiert. Wird von den Sensoren aufgerufen, sobald neue Sensordaten simuliert wurden.
     * Diese Daten werden der List<List<SensorData> angehängt.
     *
     * @param sd List<SensorData> Liste der Simulierten Sensordaten vom entsprechenden Sensor.
     */
    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
