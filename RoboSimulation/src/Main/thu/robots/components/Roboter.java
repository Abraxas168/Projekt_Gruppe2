package thu.robots.components;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Roboter implements IRobot {
    private final Sensor sensor1;
    private final Sensor sensor2;
    private final Sensor sensor3;
    private final Sensor sensor4;
    private final String name;
    private int radius;
    private int posX;
    private int posY;
    private double orientation;
    private int velocity;
    private final Color color;
    private Steuerung steuerung;
    private double deltaTimeSec;


    /**
     * Erstellt einen Roboter, der auf einer passenden Benutzeroberfläche mit hilfe von Sensoren einen Parkour selbstständig überwinden kann
     * @param sensor1 der Klasse Sensor übermittelt umgebungsdaten an Regestrierte Komponenten abhängig von seinen Eigenschaften
     * @param sensor2 der Klasse Sensor übermittelt umgebungsdaten an Regestrierte Komponenten abhängig von seinen Eigenschaften
     * @param sensor3 der Klasse Sensor übermittelt umgebungsdaten an Regestrierte Komponenten abhängig von seinen Eigenschaften
     * @param sensor4 der Klasse Sensor übermittelt umgebungsdaten an Regestrierte Komponenten abhängig von seinen Eigenschaften
     * @param name Name des Roboters als String
     * @param velocity Geschwindigkeit des Roboters in pixel/s
     * @param radius Radius des Roboters in pixel
     * @param color Farbe des Roboters in Color
     * @param steuerung Steuerung des Roboters der Klasse Steuerung bzw. seiner Unterklasse AutonomeSteuerung
     */
    public Roboter(Sensor sensor1, Sensor sensor2, Sensor sensor3, Sensor sensor4, String name, int velocity, int radius, Color color, Steuerung steuerung) {
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.name = name;
        this.velocity = velocity;
        this.radius = radius;
        this.color = color;
        this.steuerung = steuerung;
        buildRegister();
    }

    /**
     * Gibt eine Liste aller Sensoren des Typs BaseSensor zurück
     * @return Liste der Sensoren
     */
    @Override
    public List<BaseSensor> getSensors() {
        List<BaseSensor> sensoren = new ArrayList<>();
        sensoren.add(sensor1);
        sensoren.add(sensor2);
        sensoren.add(sensor3);
        sensoren.add(sensor4);
        return sensoren;
    }

    /**
     * Erstellt ein default Register von Empfänger-Klassen für die Sensordaten, sobald die Autonome Steuerung aktiviert wurde und
     * gibt diese an die Sensoren weiter
     */
    public void buildRegister(){
    java.util.List<IObserver> register=new ArrayList<>();
        register.add(this.steuerung);
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
     * aktiviert die Autonome Steuerung des Roboters duch eine neue Instanz der Klasse AutoSteuerung und initiirt das Erstellen eines Registers von Empfänger-Klassen für die Sensordaten
     */
    @Override
    public void activateAutonomousStearing() {
        this.steuerung = new AutoSteuerung();
        addToRegister(this.steuerung);
    }

    /**
     * legt bei Aufruf einmalig die x und y-Positionen des Roboters mit gewünschten int- Werten sowie dessen Orientierung mit double-Werten in Radiant fest
     * @param posX int Position des Roboters in x-Richtung
     * @param posY int Position des Roboters in y-Richtung
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
        if (velocity <= MAX_VELOCITY && velocity>=-MAX_VELOCITY) {
            this.velocity = velocity;
        }
    }

    /**
     * beschleunigt den Roboter abhängig vom übergebenen Ziel-Wert
     * @param targetVelocity int zielwert für die Geschwindigkeit auf die beschleunigt werden soll
     */
    public void accelerate(int targetVelocity) {
       //while (velocity<targetVelocity){
       //     velocity+=MAX_ACCELERATE;
        //}
        int acceleration = targetVelocity - velocity;
        acceleration = Math.min(acceleration, MAX_ACCELERATE);

        Timer accelerationTimer = new Timer();
        int finalAcceleration = acceleration;
        accelerationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                velocity += finalAcceleration;
                velocity = Math.min(velocity, MAX_VELOCITY);

                if (velocity >= targetVelocity) {
                    accelerationTimer.cancel();
                }
            }
        }, 0, 100);
    }


    /**
     * bremst den Roboter ab, abhängig vom übergebenen Ziel-Wert und übergibt einen Wahrheitswert, um auf Laufzeit des Abbremsmanövers reagieren zu können
     * @param targetVelocity int Zielwert für die Geschwindigkeit auf die Abgebremst werden soll
     * @return boolean false, sobald Methode durchgeführt wurde
     */
    public boolean decelerate(int targetVelocity) {
        if (velocity>targetVelocity){
            velocity-=MAX_ACCELERATE*deltaTimeSec;
        }
        return false;

        /*int deceleration = velocity - targetVelocity;
        deceleration = Math.min(deceleration, MAX_ACCELERATE);

        Timer decelerationTimer = new Timer();
        int finalDeceleration = deceleration;
        decelerationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                velocity -= finalDeceleration;
                velocity = Math.max(velocity, 10);

                if (velocity <= targetVelocity) {
                    decelerationTimer.cancel();
                }
            }
        }, 0, 100);
        return false;*/
    }


    /**
     * legt den Radius des Roboters fest
     * @param newRadius  int Wert für Radius des Roboters
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
    public Steuerung getSteuerung() {
        return steuerung;
    }

    /**
     * gibt die Zeitdifferenz zurück mit welchem der Roboter in den Forbewegungs-Methoden rechnet
     * @return double Zeitdifferenz, für die die Bewegung berechnet werden soll
     */
    public double getDeltaTimeSec() {
        return deltaTimeSec;
    }

    /**
     * bei Aufruf bewegt sich der Roboter einmalig in Abhängigkeit seiner Orientierung und aktuellen Position, sowie Geschwindigkeit, und
     * der Zeitdifferenz und initiiert zudem dessen Steuerung, falls die Autonome Steuerung aktiviert ist
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
        if (steuerung instanceof AutoSteuerung) {
            steuerung.steuern(this);
        }
    }
}