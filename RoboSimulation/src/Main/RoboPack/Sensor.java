package RoboPack;

import java.util.List;

public class Sensor extends BaseSensor{
    public Sensor(double orientationToRobot, double beamWidth, int measurementRate) {
        super(orientationToRobot, beamWidth, measurementRate);
    }

    @Override
    public void measurementFromEnvironment(List<SensorData> data) {

    }

}
