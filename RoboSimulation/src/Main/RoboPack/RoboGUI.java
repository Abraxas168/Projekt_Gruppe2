package RoboPack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class RoboGUI extends JFrame {
    private JSlider sRadius;
    private JLabel lRadius;
    private JButton bOben;
    private JButton bUnten;
    private JButton bLinks;
    private JButton rechtsButton;
    private JTextArea tRoboinfo;
    private JPanel pDrawPanel;
    private JPanel pInfoPanel;
    private Roboter robot;
    private final int velocityIncrement = 10;
    private final int orientationIncrement = 5;
    private Thread updateThread;
    private Environment environment;
    private int width;
    private int hight;
    private List<EnvironmentObject> objects;


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
            public void stateChanged (ChangeEvent e){
                int newValue = sRadius.getValue();

                if (newValue < 1 || newValue > 100) {
                    JOptionPane.showMessageDialog(null, "Der Wert des Radius muss zwischen 1 und 100 liegen!");
                }
                robot.setRadius(newValue);
            }
        });
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
                double orientation = robot.getOrientation() + orientationIncrement;
                robot.setOrientation(normalizeOrientation(orientation));
                evt.consume();
            } else if (key == KeyEvent.VK_RIGHT) {
                double orientation = robot.getOrientation() - orientationIncrement;
                robot.setOrientation(normalizeOrientation(orientation));
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

        private double normalizeOrientation (double orientation){
            if (orientation <= -180) {
                orientation = 360 + orientation;
            } else if (orientation > 180) {
                orientation = (orientation - 360);
            }
            return orientation;
        }


    private void startCalculating() {
        double deltaT = 0.2;
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    robot.move(deltaT);
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

    public void setEnv(EnvironmentLoader env){
        File file= new File("C:\\Users\\linda\\Studium_THU\\MT3\\Software_Entwicklung\\Projekt_Gruppe2\\RoboSimulation\\src\\Main\\RoboPack\\Umgebung.txt");
        this.environment= env.loadFromFile(file);
        this.width= environment.getWidth();
        this.hight= environment.getHeight();
        this.objects= environment.getObjects();
        createUIComponents();
    }

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
                posY = environment.getHeight() - posY;
                double orientation = robot.getOrientation();
                int velocity = robot.getVelocity();
                int radius = robot.getRadius();


                String statusStr = "X: " + posX + "\n";
                statusStr += "Y: " + posY + "\n";
                statusStr += "Orientierung: " + orientation + "°\n";
                statusStr += "Geschwindigkeit: " + velocity + " Pixel/s\n";
                //statusStr += "Höhe Frame: " +pDrawPanel.getHeight() +"\n";
                //statusStr += "Breite Frame: " + pDrawPanel.getWidth() + "\n";
                statusStr += "Höhe Bild: " +environment.getHeight() +"\n";
                statusStr += "Breite Bild: " + environment.getWidth() + "\n";


                tRoboinfo.setText(statusStr);
                int listengr = objects.size();
                //System.out.println(listengr);

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
                g.fillOval(posX - radius, posY -radius,radius*2, radius*2);
                g.setColor(Color.BLACK);
                g.fillArc(posX -radius, posY -radius, radius*2, radius*2, (int)orientation - 45, 90);

                g.drawRect(0, 0, environment.getWidth(), environment.getHeight());

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
        //pDrawPanel.setPreferredSize(new Dimension(800, 600));
        //repaint();
    }
}
