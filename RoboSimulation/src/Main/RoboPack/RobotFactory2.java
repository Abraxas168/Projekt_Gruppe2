package RoboPack;

import java.awt.*;

public class RobotFactory2 extends AbstractRobotFactory{

    @Override
    public Roboter createRobot(){
        //Sensoreigenschaften:
        double orientationToRobo1=1;
        double orientationToRobo2=2;
        double orientationToRobo3=3;
        double beamWidth1=1;
        double beamWidth2=2;
        double beamWidth3=3;
        int measurementRate1=1;
        int measurementRate2=2;
        int measurementRate3=3 ;
        Steuerung steuerung=new Steuerung();
        Sensor sensor1=new Sensor(orientationToRobo1, beamWidth1, measurementRate1);
        Sensor sensor2=new Sensor(orientationToRobo2, beamWidth2, measurementRate2);
        Sensor sensor3=new Sensor(orientationToRobo3, beamWidth3, measurementRate3);
        //Robotereigenschaften:
        String name= "coolerName";
        int init_posX=30;
        int init_posY=30;
        int init_velocity=0;
        int init_orientation=0;
        int init_radius=20;
        Color color= Color.MAGENTA;
        Roboter hotWheels= new Roboter(sensor1, sensor2, sensor3, name,  init_velocity,  init_radius, color);
        hotWheels.setInitialPose(init_posX, init_posY,init_orientation);
        //EnvironmentLoader env= new EnvironmentLoader();

        // Steuerung erstellen: Roboter übergeben: wie realisiere ich Aktivierung?
        ManuelleSteuerung manuelleSteuerung=new ManuelleSteuerung(hotWheels);
        hotWheels.setManuelleSteuerung(manuelleSteuerung);
        hotWheels.activateAutonomousStearing();

        return hotWheels;
    }
}
