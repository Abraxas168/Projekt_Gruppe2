package thu.robots.components;

import thu.robots.components.AbstractRobotFactory;
import thu.robots.components.IRobot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotFactory extends AbstractRobotFactory{

    @Override
    public IRobot createRobot(){
        Sensor sensor1=new Sensor(-Math.PI/3, Math.PI/3, 40);
        Sensor sensor2=new Sensor(0, Math.PI/3, 40);
        Sensor sensor3=new Sensor(Math.PI/3, Math.PI/3, 40);
        Sensor sensor4=new Sensor(0, Math.PI/5, 30);
        String name= "hotWheels";
        int init_velocity=50;
        int init_radius=20;
        Color color= Color.MAGENTA;
        Steuerung steuerung=new Steuerung();
        Roboter hotWheels= new Roboter(sensor1, sensor2, sensor3, sensor4, name,  init_velocity,  init_radius, color, steuerung);
        return hotWheels;
    }
}
