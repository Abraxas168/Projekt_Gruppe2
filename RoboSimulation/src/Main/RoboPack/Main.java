package RoboPack;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

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
        EnvironmentLoader env = new EnvironmentLoader();
        RobotFactory2 factory2 = new RobotFactory2();
        IRobot HotWheels = factory2.createRobot();
        //File file= new File("C:\\Users\\sarah\\Documents\\Hochschule\\3. Semester\\Software Engineering\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\RoboPack\\Umgebung.txt");
        //Environment environment= env.loadFromFile(file);
        //Validator validator=new Validator(environment);
        HotWheels.activateAutonomousStearing();
        RoboGUI guiFrame = new RoboGUI("RoboGUI");
        guiFrame.setVisible(true);
        List<IObserver> register=new ArrayList<>();
        Roboter hotWheels=(Roboter)HotWheels;
        register.add(hotWheels.getSteuerung());
        register.add(guiFrame);
        List <BaseSensor> sensoren= hotWheels.getSensors();
        for(int n=0; n<sensoren.size(); n++){
            Sensor sensor= (Sensor) sensoren.get(n);
            sensor.setRegister(register);}
            // kann das innerhalb der GUI gemacht werden?:
        guiFrame.setRobot(hotWheels);
        guiFrame.setEnv(env);
        }
    }