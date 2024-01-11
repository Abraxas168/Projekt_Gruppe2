package RoboPack;

import javax.swing.*;
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
        Roboter HotWheels = factory2.createRobot();
        File file= new File("C:\\Users\\sarah\\Documents\\Hochschule\\3. Semester\\Software Engineering\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\RoboPack\\Umgebung.txt");
        Environment environment= env.loadFromFile(file);
        Validator validator=new Validator(environment);
        ManuelleSteuerung manuelleSteuerung=new ManuelleSteuerung(HotWheels, validator);
        HotWheels.setManuelleSteuerung(manuelleSteuerung);
        HotWheels.activateAutonomousStearing();

            RoboGUI guiFrame = new RoboGUI("RoboGUI");
            guiFrame.setVisible(true);
            guiFrame.setRobot(HotWheels);
            // kann das innerhalb der GUI gemacht werden?:
            guiFrame.setEnv(env);
        }
    }