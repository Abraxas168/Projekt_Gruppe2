package thu.robots.components;

import java.util.List;

public interface IObserver {

    public void update(List<SensorData> sd);
}
