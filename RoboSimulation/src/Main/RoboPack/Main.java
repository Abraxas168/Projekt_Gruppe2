package RoboPack;

import javax.swing.*;

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
            guiFrame.setRobot((Roboter) HotWheels);
            // kann das innerhalb der GUI gemacht werden?:
            EnvironmentLoader env= new EnvironmentLoader();
            guiFrame.setEnv(env);
            HotWheels.activateAutonomousStearing();
            //********hier noch um Validator kümmern!!
        }
    }