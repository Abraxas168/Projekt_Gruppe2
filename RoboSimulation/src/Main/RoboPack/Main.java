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

        HotWheels.activateAutonomousStearing();
        RoboGUI guiFrame = new RoboGUI("RoboGUI");
        guiFrame.setVisible(true);
        guiFrame.setRobot((Roboter)HotWheels);
        guiFrame.setRegister();
        guiFrame.setEnv(env);

        }
    }