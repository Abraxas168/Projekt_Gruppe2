package thu.robots.components;

import thu.robots.components.SensorData;

import java.util.List;

public interface IObserver {

    public void update(List<SensorData> sd);
}
