package RoboPack;

import java.util.List;

public interface IObserver {

    public void update(List<SensorData> sd);
}
