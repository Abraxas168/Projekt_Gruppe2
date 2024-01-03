package RoboPack;

import java.util.List;

public class Sensor extends BaseSensor{

    private AutoSteuerung autoSteuerung;
    private ManuelleSteuerung manuelleSteuerung;
    private RoboGUI gui;

    public Sensor(double orientationToRobot, double beamWidth, int measurementRate) {
        super(orientationToRobot, beamWidth, measurementRate);
    }

    public void setAutoSteuerung(AutoSteuerung auto){
        this.autoSteuerung=auto;
    }

    public void setManuelleSteuerung(ManuelleSteuerung manu){
        this.manuelleSteuerung=manu;
    }
    public AutoSteuerung getAutoSteuerung(){
        return this.autoSteuerung;
    }
    public ManuelleSteuerung getManuelleSteuerung(){
        return this.manuelleSteuerung;
    }

    @Override
    public void measurementFromEnvironment(List<SensorData> data) {
        if(autoSteuerung != null){
            autoSteuerung.setDatafromSensors(data);}
        else if (manuelleSteuerung!= null) {
            manuelleSteuerung.setDatafromSensors(data);}
        if(gui != null){
            gui.setDatafromSensors(data);
        }
        //this.gui.setDatafromSensors(data); ***** wie komme ich an Gui ran?
    }

}
