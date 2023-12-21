package RoboPack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RoboGUI {
    private JSlider sRadius;
    private JLabel lRadius;
    private JButton bOben;
    private JButton bUnten;
    private JButton bLinks;
    private JButton rechtsButton;
    private JTextArea tRoboinfo;

    public RoboGUI() {
        sRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newValue = sRadius.getValue();

                if (newValue < 1 || newValue > 100) {
                    JOptionPane.showMessageDialog(null, "Der Wert des Radius muss zwischen 1 und 100 liegen!");
                }
            }
        });
    }
}
