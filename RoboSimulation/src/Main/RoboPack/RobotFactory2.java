package RoboPack;

public class RobotFactory2 extends AbstractRobotFactory{

    @Override
    public IRobot createRobot(){
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
        Sensor sensor1=new Sensor(orientationToRobo1, beamWidth1, measurementRate1);
        Sensor sensor2=new Sensor(orientationToRobo2, beamWidth2, measurementRate2);
        Sensor sensor3=new Sensor(orientationToRobo3, beamWidth3, measurementRate3);
        //Robotereigenschaften:
        String name= "coolerName";
        IRobot hotWheels= new Roboter(sensor1, sensor2, sensor3, name);
        return hotWheels;
    }
}
