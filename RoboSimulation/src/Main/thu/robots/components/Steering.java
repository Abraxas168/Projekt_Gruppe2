package thu.robots.components;

import thu.robots.components.SensorData;

import java.util.List;

/**
 * Oberklasse der Steuerung
 */
public class Steering implements IObserver {

    /**
     * erstellt eine Steuerung der Klasse Steering
     */
    public Steering() {

    }

    /**
     * Funktion, die die Lenkung Koordiniert
     *
     * @param robo Roboter der Klasse Robot
     */
    public void steer(Robot robo) {
    }

    /**
     * Funktion, die die Sensordaten aktualisiert. Wird von den Sensoren aufgerufen, sobald neue Sensordaten simuliert wurden. Diese Daten werden der List von Listen an Sensordaten angehaengt.
     *
     * @param sd Liste der simulierten Sensordaten vom entsprechenden Sensor.
     */
    @Override
    public void update(List<SensorData> sd) {

    }
}
