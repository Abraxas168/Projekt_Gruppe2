package thu.robots.components;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static thu.robots.components.IRobot.MAX_VELOCITY;


public class AutonomousSteering extends Steering implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();
    private int read = 0;
    private long lastAlignmentTime = System.currentTimeMillis();
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordata = 0;
    private int countZeros = 0;
    private int stuckCount = 0;
    public int targetVelocity = 50;
    private int greenLight=0;


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
        if (((velocity == MAX_VELOCITY) && ((currentTime - lastAlignmentTime) >= ALIGNMENT_INTERVAL)) && (countZeros >= 80)) {
            robo.setOrientation(0.0);
            lastAlignmentTime = currentTime;
        }
    }

    /**
     * Liest die jeweiligen Listenlängen der Sensordaten und ruft mit dieser Länge die Funktion reactionDataSize() auf.
     * Liest die gesammelten Sensordaten der Liste Sensordata aus und ruft, mit diesen Daten die Funktion navigate() auf
     * und setzt abhängig von der Eigenschaft distance  und der jeweiligen Orientierung des zugehörigen Sensors ,
     * der aktuellen Sensordate die Zielgeschwindigkeit (targetVelocity) auf 20p/s.
     * Dabei, wird auch gezählt, wie oft der Sensor mit der Orientierung 0.0 nicht betroffen ist und speichert diese Anzahl in der globalen
     * freieFahrt Variable. Diese wird zurück gesetzt, sobald der 0.0- orientierte Sensor Daten sendet).
     * Wertet die Wahrheitswerte der einzelnen Funktionen aus und bricht mit der Analyse der Sensordaten ab sobald gesteuert wurde.
     *
     * @param robo Roboter der Klasse Robot
     */
    public void EvaluateSensorData(Robot robo) {
        boolean steered;
        int n = this.read;
        if ((sensorData != null) && (sensorData.size() > 0)) {
            while (n < sensorData.size()) {
                List<SensorData> data = sensorData.get(n);
                boolean reacted = reactionDataSize(data.size(), robo);
                if (reacted) {
                    break;
                }
                for (SensorData sensorData1 : data) {
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    double beamwidth = relatedSensor.getBeamWidth();
                    double distance = sensorData1.getDistance();
                    double angle = sensorData1.getAngle();
                    steered = navigate(relation_toRobo, distance, angle, beamwidth, robo);
                    if ((distance <= ((5 * robo.getVelocity()) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && (Double.toString(relation_toRobo).equals("0.0"))) {
                        this.targetVelocity = 20;
                        greenLight = 0;
                    } else if ((distance <= (((robo.getVelocity()) / 3.0) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && !(Double.toString(relation_toRobo).equals("0.0"))) {
                        this.targetVelocity = 20;
                    }
                    if ((distance > ((robo.getVelocity() / 3.0) + robo.getRadius())) || !(Double.toString(relation_toRobo).equals("0.0"))) {
                        greenLight += 1;
                    }
                    if (steered) {
                        n = sensorData.size();
                        break;
                    }
                }
                n = n + 1;
            }
        }
        if (sensorData != null) {
            this.read = sensorData.size();
        }
    }


    /**
     * Funktion, die auf die Anzahl der eingegangenen Sensordaten reagiert und belegt abhängig von dieser Anzahl die Variable
     * targetVelocity mit dem Zielwert 50 oder löst eine Notfallsteuerung aus.
     *
     * @param size int, Länge der Liste der seit dem letzten Zeitschritt übermittelten Sensordaten
     * @param robo der Klasse Robot
     * @return gibt einen Wahrheitswert zurück. True falls mit der Notfallsteuerung reagiert wurde.
     */
    public boolean reactionDataSize(int size, Robot robo) {
        double orientation = robo.getOrientation();
        if (size >= 1) {
            countSensordata += 1;
            countZeros = 0;
        } else {
            countSensordata = 0;
            countZeros += 1;
            if (greenLight >= 25 || countZeros >= 60) {
                if (robo.getVelocity() < MAX_VELOCITY) {
                    this.targetVelocity = MAX_VELOCITY;
                }
            }
        }

        if (countSensordata >= 200) {
            robo.setOrientation(orientation + Math.PI);
            countSensordata = 0;
            return true;
        }
        return false;
    }


    /**
     * Erhält jeweils ausgelesene Sensordate der Klasse SensorData und lenkt den Roboter abhängig von den empfangenen Daten,
     * mindestens bis das Hindernis außerhalb des Sichtfeldes des betroffenen Sensors ist.
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
        double turnAngle1 = ((ceil(((beamwidth / 2.0) - Math.abs(angle)) * 10.0)) / 10.0);
        double turnAngle2 = ceil(((beamwidth / 2.0) + Math.abs(angle) + 0.05) * 10.0) / 10.0;
        double orientation1 = robo.getOrientation() + turnAngle1;
        double orientation2 = robo.getOrientation() - turnAngle1;

        switch (relationRobot) {
            case "0.0" -> {
                if ((angle >= 0.0) && (distance <= (5 + robo.getRadius()))) {
                    robo.setOrientation(orientation - Math.PI / 2);
                    return true;
                } else if ((angle >= 0.0) && (distance <= (velocity + robo.getRadius()))) {
                    robo.setOrientation(orientation2 - 0.25);
                    return true;
                } else if ((angle < 0.0) && (distance <= (5 + robo.getRadius()))) {
                    robo.setOrientation(orientation + Math.PI / 2);
                    return true;
                } else if ((angle < 0.0) && (distance <= (velocity + robo.getRadius()))) {
                    robo.setOrientation(orientation1 + 0.25);
                    return true;
                } else {
                    return false;
                }
            }
            case "1.0471975511965976" -> {
                if ((angle >= 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(orientation2);
                    return true;
                } else if ((angle < 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() - turnAngle2);
                    return true;
                } else {
                    return false;
                }
            }
            case "-1.0471975511965976" -> {
                if ((angle >= 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(robo.getOrientation() + turnAngle2);
                    return true;
                } else if ((angle < 0.0) && (distance <= (2 * robo.getRadius()))) {
                    robo.setOrientation(orientation1);
                    return true;
                } else {
                    return false;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + relationRobot);
        }
    }

    /**
     * Funktion die den Roboter unabhängig von den Sensordaten lenkt, sobald dieser eine bestimmte Zeit lang die Minimale Zielgeschwindigkeit von
     * 20P/s unterschreitet.
     *
     * @param robo        Roboter der Klasse Robot
     * @param orientation Orientierung des Roboters als double in Radiant
     * @return Boolean, true, wenn gelenkt wurde
     */
    private boolean stuckCountdown(Robot robo, double orientation) {
        int velocity = robo.getVelocity();
        if (velocity <= 20) {
            stuckCount += 1;
        } else {
            stuckCount = 0;
        }
        if (stuckCount >= 700) {
            robo.setOrientation(orientation + Math.PI);
            stuckCount = 0;
            return true;
        }
        return false;
    }


    /**
     * Reguliert die Geschwindigkeit durch beschleunigen oder abbremsen um den jeweils höchtens zulässigen Beschleunigungswert
     * (lt. Projektbeschreibung Punkt 11 und 12), je nach aktuellem Zielwert der Geschwindigkeit (targetVelocity), wobei höchstens eine
     * Maximalgeschwindigkeit von 50 Pixel/s und eine Mindestgeschwindigkeit von 10 Pixel/s erreicht werden kann.
     *
     * @param robo der Klasse Robot
     * @return gibt einen Wahrheitswert zurück. True falls beschleunigt oder abbgebremst wurde.
     */
    public boolean velocityRegulation(Robot robo) {
        System.out.println(targetVelocity);
        int velocity = robo.getVelocity();
        if (velocity < targetVelocity) {
            robo.setVelocity((velocity + robo.MAX_ACCELERATE));
            return true;
        }
        if (velocity > targetVelocity) {
            robo.setVelocity((velocity - robo.MAX_DECCELERATE));
            return true;
        }
        return false;
    }


    /**
     * Koodiniert die Steuerung, durch den Aufruf der einzelnen Steuerungsfunktionen
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
