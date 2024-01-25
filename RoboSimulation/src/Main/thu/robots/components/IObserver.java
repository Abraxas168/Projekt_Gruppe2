package thu.robots.components;

import thu.robots.components.SensorData;

import java.util.List;

public interface IObserver {

    /**
     * Funktion, die die Sensordaten aktualisiert. Wird von den Sensoren aufgerufen, sobald neue Sensordaten simuliert wurden.
     * Diese Daten werden der List<List<SensorData> angehängt.
     * @param sd List<SensorData> Liste der Simulierten Sensordaten vom entsprechenden Sensor.
     */
    public void update(List<SensorData> sd);
}
