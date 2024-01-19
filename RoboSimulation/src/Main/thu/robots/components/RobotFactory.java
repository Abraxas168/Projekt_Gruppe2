package thu.robots.components;

import java.awt.*;

public class RobotFactory extends AbstractRobotFactory{

    @Override
    public IRobot createRobot(){
        //Sensoreigenschaften:
        double orientationToRobo1=-Math.PI/3;
        double orientationToRobo2=0;
        double orientationToRobo3=Math.PI/3;
        double beamWidth1=Math.PI/3;
        double beamWidth2=Math.PI/3;
        double beamWidth3=Math.PI/3;
        double beamWidth4=Math.PI/5;
        int measurementRate1=40;
        int measurementRate2=40;
        int measurementRate3=40;
        Sensor sensor1=new Sensor(orientationToRobo1, beamWidth1, measurementRate1);
        Sensor sensor2=new Sensor(orientationToRobo2, beamWidth2, measurementRate2);
        Sensor sensor3=new Sensor(orientationToRobo3, beamWidth3, measurementRate3);
        Sensor sensor4=new Sensor(orientationToRobo2, beamWidth4, 30);
        //Robotereigenschaften:
        String name= "coolerName";
        int init_posX=40;
        int init_posY=300;
        int init_velocity=0;
        int init_orientation=0;
        int init_radius=20;
        Color color= Color.MAGENTA;
        Steuerung steuerung=new Steuerung();
        Roboter hotWheels= new Roboter(sensor1, sensor2, sensor3, sensor4, name,  init_velocity,  init_radius, color, steuerung);
        hotWheels.setInitialPose(init_posX, init_posY,init_orientation);
        return hotWheels;
    }
}
