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
     * Ersetzt das aktuelle Register für Komponenten des Typs IObserver, welche Sensordaten empfangen sollen durch ein neues.
     * @param register Liste von Komponenten der Klasse IObserver
     */
    public void setRegister(List<IObserver> register) {
        this.register = register;
    }


    /**
     * Fügt dem Register einen weiteren Komponenten des Typs IObserver hinzu
     * @param component Komponent für Register des Typs IObserver
     */
    public void register(IObserver component) {
        register.add(component);
    }

    /**
     * Erhält die Messdaten aus der Umgebung und gibt diese an die registrierten Komponenten, die Sensordaten erhalten sollen weiter.
     * @param data die empfangenen / simulierten Messdaten aus der Umgebung des Roboters für diesen Sensor
     */
    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        for (IObserver current : register) {
            current.update(data);
        }
    }

}