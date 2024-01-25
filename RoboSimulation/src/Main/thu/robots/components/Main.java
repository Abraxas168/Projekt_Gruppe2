package thu.robots.components;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello World");
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
        AbstractRobotFactory factory2= new RobotFactory();
        IRobot hotWheels = factory2.createRobot();
        
        int init_posX=50;
        int init_posY=450;
        int init_orientation=0;
        hotWheels.setInitialPose(init_posX, init_posY,init_orientation);
        hotWheels.activateAutonomousSteering();
        RoboGUI guiFrame = new RoboGUI("RoboGUI");
        Robot christine= (Robot)hotWheels;
        christine.addToRegister(guiFrame);
        guiFrame.setVisible(true);
        guiFrame.setRobot(christine);
        guiFrame.setEnv(env);



        }
    }