package thu.robots.components;


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


    public void register(IObserver component){
        register.add(component);
    }

    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        for (IObserver current : register) {
            current.update(data);
        }
    }

}
  //      for(int n=0; n<register.size(); n++){
   //     register.get(n).update(data);