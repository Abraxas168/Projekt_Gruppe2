package thu.robots.components;

import thu.robots.components.SensorData;
import thu.robots.components.Validator;

import java.util.List;

public class ManuelleSteuerung extends Steuerung{
    private List<SensorData> datafromSensors;
    private Roboter robo;

    private Validator validator;


    ManuelleSteuerung(Roboter robo, Validator validator){
        this.validator=validator;
        this.robo=robo;

    }
    public void steuern(){
        int velocity=robo.getVelocity();
        double orientation=robo.getOrientation();
        robo.setVelocity(velocity);
        robo.setOrientation(orientation);
    }

    public List<SensorData> getDatafromSensors() {
        return datafromSensors;
    }
    public void setDatafromSensors(List<SensorData> sd){
        this.datafromSensors=sd;
    }
    public Roboter getRobo(){
        return robo;
    }

    public Validator getValidator() {
        return validator;
    }
}

