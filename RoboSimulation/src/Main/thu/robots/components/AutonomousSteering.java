package thu.robots.components;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.ceil;
import static thu.robots.components.IRobot.MAX_VELOCITY;


/**
 * Autonome Steuerung welche durch die Regulierung der Geschwindigkeit des Roboters und durch dessen Lenkung diesen selbstständig um Hindernisse herum fahren lässt
 */
public class AutonomousSteering extends Steering implements IObserver {
    private List<List<SensorData>> sensorData = new ArrayList<>();
    private int read = 0;
    private long lastAlignmentTime = System.currentTimeMillis();
    private static final long ALIGNMENT_INTERVAL = 4000;
    private int countSensordata = 0;
    private int countZeros = 0;
    private int stuckCount = 0;
    private int targetVelocity = 50;
    private int greenLight = 0;


    /**
     * Instanziiert eine autonome Steuerung der Klasse AutonomousSteering mit Default-Werten für ihre Eigenschaften
     */
    public AutonomousSteering() {
    }

    public int getCountSensordata() {
        return countSensordata;
    }

    public int getCountZeros() {
        return countZeros;
    }

    public int getStuckCount() {
        return stuckCount;
    }

    public int getTargetVelocity() {
        return targetVelocity;
    }

    public void setLastAlignmentTime(long lastAlignmentTime) {
        this.lastAlignmentTime = lastAlignmentTime;
    }

    public void setCountZeros(int countZeros) {
        this.countZeros = countZeros;
    }

    public void setStuckCount(int stuckCount) {
        this.stuckCount = stuckCount;
    }

    public void setGreenLight(int greenLight) {
        this.greenLight = greenLight;
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
     * und ändert die Zielgeschwindigkeit (targetVelocity) abhängig von den Sensordaten auf 20p/s.
     * Zählt wie oft der Sensor mit der Orientierung 0.0 (nicht) betroffen ist und passt die Variable greenLight entsprechend an.
     * Wertet die Wahrheitswerte der einzelnen Funktionen aus und bricht mit der Analyse der Sensordaten ab, sobald gesteuert wurde.
     *
     * @param robo Roboter der Klasse Robot
     */
    public void evaluateSensorData(Robot robo) {
        boolean steered;
        int n = this.read;
        int i = this.read;
        if ((sensorData != null) && (sensorData.size() > 0)) {
            while (i < sensorData.size()) {
                List<SensorData> data = sensorData.get(i);
                boolean reacted = reactionDataSize(data.size(), robo);
                if (reacted) {
                    break;
                }
                for (SensorData sensorData1 : data) {
                    BaseSensor relatedSensor = sensorData1.getRelatedSensor();
                    double relation_toRobo = relatedSensor.getOrientationToRobot();
                    if (relation_toRobo == 0.0) {
                        steered = navigate(sensorData1, robo);
                        if (steered) {
                            i = sensorData.size();
                            n = sensorData.size();
                            this.read = sensorData.size();
                            break;
                        }
                    }
                }
                i = i + 1;
            }
            while (n < sensorData.size()) {
                List<SensorData> data = sensorData.get(n);
                for (SensorData sensorData1 : data) {
                    steered = navigate(sensorData1, robo);
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
     * @param size int, Länge der Liste der aktuell übergebenen Liste von Sensordaten
     * @param robo der Klasse Robot
     * @return gibt einen Wahrheitswert zurück. True, falls mit der Notfallsteuerung reagiert wurde.
     */
    public boolean reactionDataSize(int size, Robot robo) {
        double orientation = robo.getOrientation();
        if (size >= 1) {
            countSensordata += size;
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

        if (countSensordata >= 50) {
            robo.setVelocity(10);
            if (countSensordata >= 200) {
                robo.setOrientation(orientation + Math.PI);
            }
            countSensordata = 0;
            return true;
        }
        return false;
    }


    /**
     * Erhält jeweils ausgelesene Sensordaten der Klasse SensorData und lenkt den Roboter abhängig von den empfangenen Daten,
     * mindestens bis das Hindernis außerhalb des Sichtfeldes des betroffenen Sensors ist.
     *
     * @param sensorData1 SensorData
     * @param robo        Dazugehöriger Roboter Robo
     * @return Boolean    wenn gelenkt wurde, true
     */
    public boolean navigate(SensorData sensorData1, Robot robo) {
        double orientation = robo.getOrientation();
        int velocity = robo.getVelocity();
        BaseSensor relatedSensor = sensorData1.getRelatedSensor();
        double relation_toRobo = relatedSensor.getOrientationToRobot();
        double beamwidth = relatedSensor.getBeamWidth();
        double distance = sensorData1.getDistance();
        double angle = sensorData1.getAngle();
        String relationRobot = Double.toString(relation_toRobo);
        if (stuckCountdown(robo)) {
            return true;
        }
        if ((distance <= ((5 * robo.getVelocity()) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && relationRobot.equals("0.0")) {
            this.targetVelocity = 20;
            greenLight = 0;
        } else if ((distance <= (((robo.getVelocity()) / 3.0) + robo.getRadius())) && (robo.getVelocity() > 20) && (countSensordata >= 1) && !relationRobot.equals("0.0")) {
            this.targetVelocity = 20;
        }
        if ((distance > ((robo.getVelocity() / 3.0) + robo.getRadius())) || !relationRobot.equals("0.0")) {
            greenLight += 1;
        }
        double turnAngle1 = ((ceil(((beamwidth / 2.0) - Math.abs(angle)) * 10.0)) / 10.0);
        double turnAngle2 = ceil(((beamwidth / 2.0) + Math.abs(angle) + 0.05) * 10.0) / 10.0;
        double orientation1 = robo.getOrientation() + turnAngle1;
        double orientation2 = robo.getOrientation() - turnAngle1;

        switch (relationRobot) {
            case "0.0" -> {
                if ((angle >= 0.0) && (distance <= (8 + robo.getRadius()))) {
                    robo.setOrientation(orientation - Math.PI / 2);
                    return true;
                } else if ((angle >= 0.0) && (distance <= (velocity + robo.getRadius()))) {
                    robo.setOrientation(orientation2 - 0.25);
                    return true;
                } else if ((angle < 0.0) && (distance <= (8 + robo.getRadius()))) {
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
     * Funktion die den Roboter unabhängig von den Sensordaten lenkt, sobald dieser eine bestimmte Zeit lang die minimale Zielgeschwindigkeit von
     * 20P/s unterschreitet.
     *
     * @param robo Roboter der Klasse Robot
     * @return Boolean, true, wenn gelenkt wurde
     */
    public boolean stuckCountdown(Robot robo) {
        int velocity = robo.getVelocity();
        if (velocity <= 20) {
            stuckCount += 1;
        } else {
            stuckCount = 0;
        }
        if (stuckCount >= 700) {
            robo.setOrientation(robo.getOrientation() + Math.PI);
            stuckCount = 0;
            return true;
        }
        return false;
    }


    /**
     * Reguliert die Geschwindigkeit durch Beschleunigen oder Abbremsen um den jeweils höchtens zulässigen Beschleunigungswert
     * (lt. Projektbeschreibung Punkt 11 und 12), je nach aktuellem Zielwert der Geschwindigkeit (targetVelocity), wobei höchstens eine
     * Maximalgeschwindigkeit von 50 Pixel/s und eine Mindestgeschwindigkeit von 10 Pixel/s erreicht werden kann.
     *
     * @param robo der Klasse Robot
     * @return gibt einen Wahrheitswert zurück. True, falls beschleunigt oder abgebremst wurde.
     */
    public boolean velocityRegulation(Robot robo) {
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
     * Koordiniert die Steuerung, durch den Aufruf der einzelnen Steuerungsfunktionen
     *
     * @param robo Roboter der Klasse Robot
     */
    @Override
    public void steer(Robot robo) {
        velocityRegulation(robo);
        goalAlignment(robo);
        evaluateSensorData(robo);

    }


    /**
     * Funktion, die die Sensordaten aktualisiert. Wird von den Sensoren aufgerufen, sobald neue Sensordaten simuliert wurden.
     * Diese Daten werden der Liste von Listen an Sensordaten angehängt.
     *
     * @param sd  Liste der simulierten Sensordaten vom entsprechenden Sensor.
     */
    @Override
    public void update(List<SensorData> sd) {
        this.sensorData.add(sd);
    }
}
