package thu.robots.components;

import java.util.ArrayList;
import java.util.List;


public class AutonomousSteering extends Steering implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();
    private int read = 0;
    private long lastAlignmentTime = System.currentTimeMillis();
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordata = 0;
    private int countZeros = 0;
    private int stuckCount = 0;
    private int steps = 0;
    private int targetVelocity = 50;
    private int freieFahrt=0;


    /**
     * Instanziiert eine autonome Steuerung der Klasse AutonomousSteering mit Default-Werten für ihre Eigenschaften
     */
    public AutonomousSteering() {
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
                    steered = navigate(relation_toRobo, distance, angle, beamwidth, robo);
                    System.out.println(countSensordata);
                    System.out.println("sensor:  " + relation_toRobo);
                    if ((distance <= ((5*robo.getVelocity()) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && (Double.toString(relation_toRobo).equals("0.0"))) {
                        this.targetVelocity = 20;
                        System.out.println("gebremst bei winkel null");
                        freieFahrt=0;
                    }else if((distance <= (((robo.getVelocity())/3) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && !(Double.toString(relation_toRobo).equals("0.0"))){
                        this.targetVelocity=20;
                        System.out.println("gebremst bei winkel nicht null");
                    }
                    if ((distance>((robo.getVelocity()/3) + robo.getRadius())) || !(Double.toString(relation_toRobo).equals("0.0"))){
                        freieFahrt+=1;
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
     * Funktion, die auf die Anzahl der eingegangenen Sensordaten reagiert. Sie zählt dafür mithilfe von Zählvariablen wie oft
     * Sensordaten übermittelt wurden bzw. keine Sensordaten übermittelt wurden und ruft abhängig davon die Funktion
     * accelerate auf oder löst eine Notfallsteuerung aus, wobei sich der Roboter um PI dreht.
     *
     * @param size int, Länge der Liste der seit dem letzten Zeitschritt übermittelten Sensordaten
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
            System.out.println("freieFahrt:  " + freieFahrt);
            if (freieFahrt >= 25 || countZeros>=60) {
                if (robo.getVelocity() < 50) {
                    this.targetVelocity = 50;
                }
            }
        }

        if (countSensordata >= 400) {
            robo.setOrientation(orientation + Math.PI);
            countSensordata = 0;
            return false;
        }
        return true;
    }


    /**
     * Erhält ausgelesene Sensor Daten der Klasse SensorData und lenkt den Roboter abhängig von den empfangenen Daten, bis das Hindernis außerhalb
     * seines Sichtfeldes ist.
     *
     * @param relation_Robot Orientierung des Sensors relativ zur Orientierung des Roboters
     * @param distance       Entfernung des Datenpunktes in Pixel vom Roboter
     * @param angle          Orientierung des Datenpunktes relativ zum Sensor, der den Datenpunkt empfangen hat
     * @param beamwidth      Strahlbreite des Sensors, der den Datenpunkt empfangen hab
     * @param robo           Dazugehöriger Roboter Robo
     * @return Boolean, wenn gelenkt wurde, true
     */
    public boolean navigate(double relation_Robot, double distance, double angle, double beamwidth, Robot robo) {
        double orientation = robo.getOrientation();
        int velocity = robo.getVelocity();
        String relationRobot = Double.toString(relation_Robot);
        boolean stuck = stuckCountdown(robo, orientation);
        if (stuck) {
            return true;
        }
        double turnAngle1 = (beamwidth / 2.0) - Math.abs(angle) + 0.05;
        double orientation1 = robo.getOrientation() + ((Math.ceil(turnAngle1*100))/100);
        double orientation2 = robo.getOrientation() - ((Math.ceil(turnAngle1*100))/100);
        switch (relationRobot) {
            case "0.0":
                if ((angle >= 0.0) && (distance <= ((velocity / 4.0) + robo.getRadius()))) {
                    robo.setOrientation(orientation2-0.25);
                    System.out.println(" sensor 0.0 gedreht um:  " + orientation2);

                    return true;

                } else if ((angle < 0.0) && (distance <= ((velocity / 4.0) + robo.getRadius()))) {
                    robo.setOrientation(orientation1+0.25);
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
                    robo.setOrientation(robo.getOrientation() - (Math.ceil(((beamwidth / 2.0) + Math.abs(angle) + 0.05)*100)/100));
                    System.out.println("+ pi/3 gedreht um:  " + "beamwidth/2 und winkel");
                    return true;
                } else {
                    return false;
                }
            case "-1.0471975511965976":
                if ((angle >= 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + (Math.ceil(((beamwidth / 2.0) + Math.abs(angle) + 0.05)*100)/100));
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
            stuckCount += 1;
        } else {
            stuckCount = 0;
        }
        if (stuckCount >= 700) {
            robo.setOrientation(orientation + Math.PI);
            stuckCount = 0;
            System.out.println("EmergencyStuckOperation");
            return true;
        }
        return false;
    }


    public boolean velocityRegulation(Robot robo) {
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
     * Koodiniert die Steuerung, indem sie weitere Funktionen aufruft und die Durchläufe Zählt
     *
     * @param robo Roboter der Klasse Robot
     */
    @Override
    public void steer(Robot robo) {
        velocityRegulation(robo);
        goalAlignment(robo);
        EvaluateSensorData(robo);

    }


    /**
     * Funktion, die die Sensordaten aktualisiert. Wird von den Sensoren aufgerufen, sobald neue Sensordaten simuliert wurden.
     * Diese Daten werden der List<List<SensorData> angehängt.
     *
     * @param sd List<SensorData> Liste der simulierten Sensordaten vom entsprechenden Sensor.
     */
    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
