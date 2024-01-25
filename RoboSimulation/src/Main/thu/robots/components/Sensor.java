package thu.robots.components;


import java.util.ArrayList;
import java.util.List;

public class Sensor extends BaseSensor {

    private List<IObserver> register;

    /**
     * Erstellt einen neuen Sensor
     *
     * @param orientationToRobot Orientierung (Blickrichtung) des Sensors relativ zur Roboter-Ausrichtung
     * @param beamWidth          Strahlbreite in rad
     * @param measurementRate    Messrate in Hertz
     */
    public Sensor(double orientationToRobot, double beamWidth, int measurementRate) {
        super(orientationToRobot, beamWidth, measurementRate);
        this.register=new ArrayList<>();
    }


    /**
     * Legt ein Register von Komponenten an, welche die SensorDaten empfangen sollen.
     * @param register Liste von Komponenten der Klasse IObserver
     */
    public void setRegister(List<IObserver> register) {
        this.register = register;
    }


    /**
     * Fügt dem Register einen weiteren Komponenten der Klasse IObserver hinzu
     * @param component Komponent für Register der Klasse IObserver
     */
    public void register(IObserver component) {
        register.add(component);
    }

    /**
     * Erhält die Messdaten aus der Umgebung und gibt diese an die regestrierten Komponenten, die Sensordaten erhalten sollen weiter.
     * @param data die empfangenen / simulierten Messdaten aus der Umgebung des Roboters für diesen Sensor
     */
    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        for (IObserver current : register) {
            current.update(data);
        }
    }

}