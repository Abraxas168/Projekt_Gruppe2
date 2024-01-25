package thu.robots.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Robot implements IRobot {
    private final List<BaseSensor> sensors;
    private final String name;
    private int radius;
    private int posX;
    private int posY;
    private double orientation;
    private int velocity;
    private final Color color;
    private Steering steering;
    private double deltaTimeSec;



    /**
     * Erstellt einen Roboter, der auf einer passenden Benutzeroberfläche mithilfe von Sensoren einen Parkour selbstständig überwinden kann
     *
     * @param sensors  Liste an Sensoren, der Klasse Sensor. Übermitteln jeweils Umgebungsdaten an registrierte Komponenten abhängig von seinen Eigenschaften
     * @param name      Name des Roboters als String
     * @param velocity  Geschwindigkeit des Roboters in pixel/s
     * @param radius    Radius des Roboters in pixel
     * @param color     Farbe des Roboters in Color
     * @param steering Steuerung des Roboters der Klasse Steuerung bzw. seiner Unterklasse AutonomeSteuerung
     */
    public Robot(List<BaseSensor> sensors, String name, int velocity, int radius, Color color, Steering steering) {
        this.sensors = sensors;
        this.name = name;
        this.velocity = velocity;
        this.radius = radius;
        this.color = color;
        this.steering = steering;
        buildRegister();

    }

    /**
     * Gibt eine Liste aller Sensoren des Typs BaseSensor zurück
     *
     * @return Liste der Sensoren
     */
    @Override
    public List<BaseSensor> getSensors() {
        return sensors;
    }

    /**
     * Erstellt ein default Register von Empfänger-Klassen für die Sensordaten, sobald die Autonome Steuerung aktiviert wurde und
     * gibt diese an die Sensoren weiter
     */
    public void buildRegister(){
    java.util.List<IObserver> register=new ArrayList<>();
        register.add(this.steering);
    List<BaseSensor> sensoren= getSensors();
        for (BaseSensor baseSensor : sensoren) {
            Sensor sensor = (Sensor) baseSensor;
            sensor.setRegister(register);
        }
    }


    /**
     * Fügt dem Register für Empfänger-Klassen für die Sensordaten eine Komponente nachträglich hinzu und übermittelt diese an die Sensoren
     * @param component vom Typ IObserver, der sich bei den Sensoren für Daten regestrieren soll
     */
    public void addToRegister(IObserver component) {
        List<BaseSensor> sensoren = getSensors();
        for (BaseSensor baseSensor : sensoren) {
            Sensor sensor = (Sensor) baseSensor;
            try {
                sensor.register(component);
            } catch (NullPointerException e) {
                System.out.println("Die Autonome Steuerung muss aktiviert sein, bevor sich weitere Komponenten für die Sensordaten registrieren können.");
            }
        }
    }


    /**
     * aktiviert die autonome Steuerung des Roboters durch eine neue Instanz der Klasse AutoSteuerung und initiiert das Erstellen eines Registers von Empfänger-Klassen für die Sensordaten
     */
    @Override
    public void activateAutonomousSteering() {
        this.steering = new AutonomousSteering();
        addToRegister(this.steering);
    }

    /**
     * legt bei Aufruf einmalig die x und y-Positionen des Roboters mit gewünschten int- Werten sowie dessen Orientierung mit double-Werten in Radiant fest
     *
     * @param posX        int Position des Roboters in x-Richtung
     * @param posY        int Position des Roboters in y-Richtung
     * @param orientation double Orientierung in Radiant
     */
    @Override
    public void setInitialPose(int posX, int posY, double orientation) {
        this.posX = posX;
        this.posY = posY;
        this.orientation = normalizeOrientation(orientation);
    }

    /**
     * gibt den Namen des Roboters als String zurück
     * @return String Name des Roboters
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * gibt die aktuelle x-Position des Roboters zurück
     * @return int posX
     */
    @Override
    public int getPosX() {
        return this.posX;
    }

    /**
     * gibt die aktuelle y-Position des Roboters zurück
     * @return int posY
     */
    @Override
    public int getPosY() {
        return this.posY;
    }

    /**
     * gibt die aktuelle orientierung des Roboters in Radiant zurück
     * @return double orientation aktuelle Orientierung des Roboters in Radiant
     */
    @Override
    public double getOrientation() {
        return this.orientation;
    }

    /**
     * legt bei Aufruf einmalig die Orientierung des Roboters mit gewünschten double-Werten in Radiant fest
     * und initiiert dabei deren Normalisierung
     * @param orientation gewünschte Orientierung des Roboters in Radiant
     */
    public void setOrientation(double orientation) {
        this.orientation = normalizeOrientation(orientation);
    }

    /**
     * Normalisiert übergebene double-Werte, sodass diese nicht über PI oder unter -PI steigen oder fallen, indem bei Werten über PI, 2*PI
     * abgezogen werden und bei Werten unter -PI, 2*PI dazu gezählt werden.
     * @param orientation double gewünschte Orientierung des Roboters in Radiant
     * @return orientation double normalisierte Orientierung des Roboters in Radiant
     */
    public double normalizeOrientation(double orientation) {
        if (orientation <= -Math.PI) {
            orientation = 2 * Math.PI + orientation;
        } else if (orientation > Math.PI) {
            orientation = (orientation - Math.PI * 2);
        }
        return orientation;
    }

    /**
     * gibt die aktuelle Geschwindigkeit des Roboters als int-Wert zurück
     * @return velocity int Geschwindigkeit in pixel/s
     */
    @Override
    public int getVelocity() {
        return this.velocity;
    }

    /**
     * legt bei Aufruf einmalig die Geschwindigkeit des Roboters fest
     * @param velocity int Geschwindigkeit in m/s
     */
    public void setVelocity(int velocity) {
        if (velocity <= MAX_VELOCITY && velocity >= -MAX_VELOCITY) {
            this.velocity = velocity;
        }
    }


    /**
     * legt den Radius des Roboters fest
     *
     * @param newRadius int Wert für Radius des Roboters
     */
    public void setRadius(int newRadius) {
        if (newRadius < 1 || newRadius > 100) {
            throw new IllegalStateException("Radius muss zwischen 1 und 100 liegen.");
        }
        this.radius = newRadius;

    }

    /**
     * gibt den aktuellen Radius des Roboters zurück
     * @return int Radius des Roboters
     */
    @Override
    public int getRadius() {
        return this.radius;
    }

    /**
     * gibt die aktuelle Farbe des Roboters zurück
     * @return Color Farbe des Roboters
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * gibt die aktuelle Steuerung des Roboters zurück
     * @return Steuerung des Roboters
     */
    public Steering getsteering() { return steering; }

    /**
     * gibt die Zeitdifferenz zurück, mit welcher der Roboter in den Forbewegungs-Methoden rechnet
     * @return double Zeitdifferenz, für die die Bewegung berechnet werden soll
     */
    public double getDeltaTimeSec() {
        return deltaTimeSec;
    }

    /**
     * bei Aufruf bewegt sich der Roboter einmalig in Abhängigkeit seiner Orientierung und aktuellen Position, sowie Geschwindigkeit, und
     * der Zeitdifferenz und initiiert zudem dessen Steuerung, falls die autonome Steuerung aktiviert ist
     * @param deltaTimeSec Zeitdifferenz, für die die Bewegung berechnet werden soll
     */
    @Override
    public void move(double deltaTimeSec) {
            this.deltaTimeSec=deltaTimeSec;
            double deltaX = deltaTimeSec * velocity * Math.cos(orientation);
            double deltaY = deltaTimeSec * velocity * Math.sin(orientation);
            double x_neu = posX + deltaX;
            double y_neu = posY + deltaY;
            posX = (int) x_neu;
            posY = (int) y_neu;
        if (steering instanceof AutonomousSteering) {
            steering.steer(this);
        }
    }
}