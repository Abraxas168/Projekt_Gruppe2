package RoboPack;

import java.awt.*;
import java.io.File;

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
        int init_posX=40;
        int init_posY=40;
        int init_velocity=0;
        int init_orientation=0;
        int init_radius=20;
        Color color= Color.MAGENTA;
        Roboter hotWheels= new Roboter(sensor1, sensor2, sensor3, name,  init_velocity,  init_radius, color);
        hotWheels.setInitialPose(init_posX, init_posY,init_orientation);
        EnvironmentLoader env= new EnvironmentLoader();
        File file= new File("C:\\Users\\linda\\Studium_THU\\MT3\\Software_Entwicklung\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\RoboPack\\Umgebung.txt");
        Environment environment= env.loadFromFile(file);
        Validator validator=new Validator(environment);
        // Steuerung erstellen: Roboter übergeben: wie realisiere ich Aktivierung?
        ManuelleSteuerung manuelleSteuerung=new ManuelleSteuerung(hotWheels, validator);
        hotWheels.setManuelleSteuerung(manuelleSteuerung);
        //hotWheels.activateAutonomousStearing();

        return hotWheels;
    }
}
