package RoboPack;

import thu.robots.components.BaseSensor;
import thu.robots.components.SensorData;

import java.util.List;

public class Sensor extends BaseSensor {

private List<IObserver> register;

    public Sensor(double orientationToRobot, double beamWidth, int measurementRate) {
        super(orientationToRobot, beamWidth, measurementRate);
    }

    //public void setAutoSteuerung(AutoSteuerung auto){
     //   this.autoSteuerung=auto;
    //}

    //public void setManuelleSteuerung(ManuelleSteuerung manu){
     //   this.manuelleSteuerung=manu;
   // }
   // public AutoSteuerung getAutoSteuerung(){
    //    return this.autoSteuerung;
   // }
    //public ManuelleSteuerung getManuelleSteuerung(){
     //   return this.manuelleSteuerung;
    //}

    //*********Observer Regestrieren!!
    public void setRegister(List<IObserver> register){
        this.register=register;
    }
    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        for(int n=0; n<register.size(); n++){
            register.get(n).update(data);
        }
    }

}
