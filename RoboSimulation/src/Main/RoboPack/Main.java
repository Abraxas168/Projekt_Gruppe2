package RoboPack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            RoboGUI guiFrame = new RoboGUI("RoboGUI");
            guiFrame.setVisible(true);
            RobotFactory2 factory2=new RobotFactory2();
            IRobot HotWheels= factory2.createRobot();
            HotWheels.activateAutonomousStearing();
            List<IObserver> register=new ArrayList<>();
            Roboter hotWheels=(Roboter)HotWheels;
            register.add(hotWheels.getSteuerung());
            register.add(guiFrame);
            List <BaseSensor> sensoren= hotWheels.getSensors();
            for(int n=0; n<sensoren.size(); n++){
            Sensor sensor= (Sensor) sensoren.get(n);
            sensor.setRegister(register);}
            guiFrame.setRobot(hotWheels);
            EnvironmentLoader env= new EnvironmentLoader();
            guiFrame.setEnv(env);

            //********hier noch um Validator kümmern!!
        }
    }