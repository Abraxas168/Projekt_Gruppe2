package thu.robots.components;

import thu.robots.components.BaseSensor;
import thu.robots.components.SensorData;

import java.util.ArrayList;
import java.util.List;

public class Sensor extends BaseSensor {

private List<IObserver> register;

    public Sensor(double orientationToRobot, double beamWidth, int measurementRate) {
        super(orientationToRobot, beamWidth, measurementRate);
    }



    public void setRegister(List<IObserver> register){
        this.register=register;
    }


    public void addRegisterComponent(IObserver component){
        register.add(component);
    }
    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        for(int n=0; n<register.size(); n++){
            register.get(n).update(data);
        }
    }

}
