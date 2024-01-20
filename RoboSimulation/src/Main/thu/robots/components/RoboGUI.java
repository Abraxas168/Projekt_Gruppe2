package thu.robots.components;

import thu.robots.components.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RoboGUI extends JFrame implements IObserver{
    private JSlider sRadius;
    private JButton bschneller;
    private JButton blangsamer;
    private JButton bLinks;
    private JButton bRechts;
    private JTextArea tRoboinfo;
    private JLabel lRadius;
    private JPanel pDrawPanel;
    private JPanel pInfoPanel;
    private Roboter robot;
    private final int velocityIncrement = 10;
    private final double orientationIncrement = 5./180.*Math.PI;
    private Thread updateThread;
    private Environment environment;
    private int width;
    private int hight;
    private List<EnvironmentObject> objects;
    private List<SensorData> sensorData;
    private Validator validator;

    public RoboGUI(String title) {
        setTitle(title);
        setContentPane(pInfoPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        pack();

        sRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!sRadius.isFocusOwner()) {
                    return;
                }
                if (robot == null) {
                    return;
                }
                int newValue = sRadius.getValue();

                if (newValue < 1 || newValue > 100) {
                    JOptionPane.showMessageDialog(null, "Der Wert des Radius muss zwischen 1 und 100 liegen!");
                }
                robot.setRadius(newValue);
                int xPose= newValue;
                int yPose= newValue;
                robot.setInitialPose(xPose, yPose, robot.getOrientation());
            }
        });
        bschneller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (robot == null) {
                    return;
                }
                int velocity = robot.getVelocity();
                robot.setVelocity(velocity + velocityIncrement);
            }
        });
        blangsamer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (robot == null) {
                    return;
                }
                int velocity = robot.getVelocity();
                robot.setVelocity(velocity - velocityIncrement);
            }
        });
        bLinks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (robot == null) {
                    return;
                }

                double orientation = robot.getOrientation() - orientationIncrement;
                robot.setOrientation(orientation);
            }
        });
        bRechts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (robot == null) {
                    return;
                }
                double orientation = robot.getOrientation() + orientationIncrement;
                robot.setOrientation(orientation);
            }
        });


    KeyboardFocusManager.getCurrentKeyboardFocusManager().

    addKeyEventDispatcher(new KeyEventDispatcher() {
        public boolean dispatchKeyEvent (KeyEvent e){
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN -> {
                        formKeyPressed(e);
                        break;
                    }
                }
            }
            return false;
        }
    });
        createUIComponents();
    }


    @Override
    public boolean isFocusable() {
        return true;
    }

    public void setRobot(Roboter robot) {
        this.robot = robot;
        startCalculating();
    }


    private void formKeyPressed(KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
            if (robot == null) {
                return;
            }
            int key = evt.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                double orientation = robot.getOrientation() - orientationIncrement;
                robot.setOrientation(orientation);
                evt.consume();
            } else if (key == KeyEvent.VK_RIGHT) {
                double orientation = robot.getOrientation() + orientationIncrement;
                robot.setOrientation(orientation);
                evt.consume();
            } else if (key == KeyEvent.VK_UP) {
                int velocity = robot.getVelocity();
                robot.setVelocity(velocity + velocityIncrement);
                evt.consume();
            } else if (key == KeyEvent.VK_DOWN) {
                int velocity = robot.getVelocity();
                robot.setVelocity(velocity - velocityIncrement);
                evt.consume();
            }

        }

       /* private double normalizeOrientation (double orientation){
            if (orientation <= -Math.PI) {
                orientation = 2*Math.PI + orientation;
            } else if (orientation > Math.PI) {
                orientation = (orientation - Math.PI*2);
            }
            return orientation;
        }*/


    private void startCalculating() {
        double deltaT = 0.1;
        updateThread = new Thread(new Runnable() {
            @Override

            public void run() {
                while (true) {
                    if(environment != null){
                    environment.simulateSensorData(robot);}
                    robot.move(deltaT);
                    if (validator != null) {
                        EnvironmentObject hindernis = validator.checkCollosion(robot);
                        if (hindernis != null) {
                            robot.setVelocity(0);
                            showCollisionMessage();
                        }
                        if (validator.checkTargetZone(robot)){
                            robot.setVelocity(0);
                            showTargetReachedMessage();
                             }
                    }
                    repaint();

                    try {
                        Thread.sleep((long) (deltaT * 1000));
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        updateThread.start();
    }

    /*public void setRegister(){
        List<IObserver> register=new ArrayList<>();
        register.add(robot.getSteuerung());
        register.add(this);
        List <BaseSensor> sensoren= robot.getSensors();
        for(int n=0; n<sensoren.size(); n++){
            Sensor sensor= (Sensor) sensoren.get(n);
            sensor.setRegister(register);}
    }*/

    public void setEnv(EnvironmentLoader env){
        File file3= new File("C:\\Users\\linda\\Studium_THU\\MT3\\Software_Entwicklung\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\thu\\robots\\components\\Umgebung.txt");
        File file2= new File("C:\\Users\\linda\\Studium_THU\\MT3\\Software_Entwicklung\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\thu\\robots\\components\\Umgebung2.txt");
        File file1= new File("C:\\Users\\linda\\Studium_THU\\MT3\\Software_Entwicklung\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\thu\\robots\\components\\Umgebung3.txt");
        this.environment= env.loadFromFile(file2);
        environment.simulateSensorData(robot);
        this.width= environment.getWidth();
        this.hight= environment.getHeight();
        this.objects= environment.getObjects();
        this.validator=new Validator(environment);
        createUIComponents();
    }

    public int getHight() {
        return hight;
    }

    @Override
    public int getWidth() {
        return width;
    }


   // public List<List<SensorData>> getDatafromSensors() {
     //   return robot.getAutoSteuerung().getDatafromSensors();
    //}

    private void createUIComponents() {
        pDrawPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (robot == null || environment==null) {
                    return;
                }

                Color color = robot.getColor();
                int posX = robot.getPosX();
                int posY = robot.getPosY();
                double orientation = robot.getOrientation();
                int velocity = robot.getVelocity();
                int radius = robot.getRadius();


                String statusStr = "X: " + posX + "\n";
                statusStr += "Y: " + posY + "\n";
                statusStr += "Orientierung: " + (orientation*180/Math.PI) + "°\n";
                statusStr += "Geschwindigkeit: " + velocity + " Pixel/s\n";


                tRoboinfo.setText(statusStr);

                int listengr = objects.size();

                for(int n=0; n<listengr; n++){
                    EnvironmentObject obj= objects.get(n);
                    Color objColor= obj.getColor();
                    Rectangle2D rechteck = obj.getRectangle();
                    double objOrientation=obj.getOrientation();
                    Graphics2D g2d= (Graphics2D) g;
                    AffineTransform transform = new AffineTransform();
                    transform.rotate(objOrientation, rechteck.getX() + rechteck.getWidth() / 2, rechteck.getY() + rechteck.getHeight() / 2);
                    g2d.setTransform(transform);
                    g.setColor(objColor);
                    g.fillRect((int)rechteck.getX(), (int)rechteck.getY(), (int)rechteck.getWidth(), (int)rechteck.getHeight());
                    g2d.setTransform(new AffineTransform());
                }


                g.setColor(color);
                g.fillOval(posX - radius, posY - radius,radius*2, radius*2);
                g.setColor(Color.BLACK);

                int startAngle = (int)(orientation/Math.PI*180.) - 45;
                g.fillArc(posX -radius, posY - radius, radius*2, radius*2, -startAngle, -90);

                g.drawRect(0, 0, environment.getWidth(), environment.getHeight());

                for(int k=0; k<sensorData.size(); k=k+1){
                    SensorData sensorData1=sensorData.get(k);
                    BaseSensor sensor = sensorData1.getRelatedSensor();
                    double orientationToRobot = sensor.getOrientationToRobot();
                    double hindernisOrientation=sensorData1.getAngle();
                    double distanceToRobo=sensorData1.getDistance();
                    double gesamtOrientation= orientation+orientationToRobot+hindernisOrientation;
                    double laserX=posX+Math.cos(gesamtOrientation)*distanceToRobo;
                    double laserY=posY+Math.sin(gesamtOrientation)*distanceToRobo;
                    int laserXi=(int)laserX;
                    int laserYi=(int)laserY;
                    g.setColor(Color.RED);
                    g.fillOval(laserXi-3,laserYi-3 , 6,6);
                }

                int maxX = environment.getWidth() +radius;
                int minX = -radius;
                int maxY = environment.getHeight() +radius;
                int minY = -radius;

                if (posX > maxX || posX < minX || posY > maxY || posY < minY) {
                    updateThread.interrupt();
                    JOptionPane.showMessageDialog(this, "Der Roboter ist verschwunden!", "Roboter ist verschwunden", JOptionPane.ERROR_MESSAGE);
                }
            }

        };
    }
    private void showCollisionMessage() {
        JOptionPane.showMessageDialog(this, "Der Roboter ist kollidiert!", "Kollision", JOptionPane.ERROR_MESSAGE);
    }

    private void showTargetReachedMessage() {
        JOptionPane.showMessageDialog(this, "Ziel erreicht!", "Ziel erreicht", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void update(List<SensorData> sd) {
            this.sensorData=sd;

            }
        }

