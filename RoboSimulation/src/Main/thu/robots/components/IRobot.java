package thu.robots.components;

import thu.robots.components.BaseSensor;

import java.util.List;

public interface IRobot {

    /**
     * Maximale Beschleunigung / Abbremsen in pixel/s^2
     */
    public static final int MAX_ACCELERATE = 10;

    /**
     * Maximale Geschwindigkeit in pixel/s
     */
    public static final int MAX_VELOCITY = 50;

    /**
     * Gibt die Liste aller Sensoren zurück
     * @return
     */
    public List<BaseSensor> getSensors();


    /**
     * Aktiviert das autonome Fahren. Roboter soll danach direkt selbständig losfahren.
     */
    public void activateAutonomousSteering();


    /**
     * Legt die initiale Position und Orientierung fest
     * @param posX
     * @param posY
     * @param orientation Orientierung in Radiant
     */
    public void setInitialPose(int posX, int posY, double orientation);

     /**
     * Gibt den Namen des Roboters zurück
     * @return
     */
    public String getName();

    /**
     * Liefert die X-Position zurück (wird weitergeleitet/delegiert an Stearing)
     * @return
     */
    public int getPosX();

    /**
     * Liefert die Y-Position zurück
     * @return
     */
    public int getPosY();

    /**
     * Liefert die Orientierung zurück
     * @return
     */
    public double getOrientation();

    /**
     * Liefert die Geschwindigkeit zurück
     * @return
     */
    public int getVelocity();

    /**
     * Liefert die Breite des Roboters zurück
     * @return
     */
    public int getRadius(

    );


    /**
     * Berechnet und aktualisiert die neue Position des Roboters
     * @param deltaTimeSec Zeitdifferenz, für die die Bewegung berechnet werden soll
     */
    public void move(double deltaTimeSec);


}
