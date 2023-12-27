package RoboPack;

public class ManuelleSteuerung {
   /* private int velocity=velocity;
    private double orientation=orientation;
    private int posX=posX;
    private int posY=posY;
    private int radius=radius;
    private RoboGUI robokontroll;

    ManuelleSteuerung(int velocity, double orientation, int posX, int posY, int radius){
        this.orientation=orientation;
        this.velocity=velocity;
        this.posX=posX;
        this.posY=posY;
        this.radius=radius;
        this.robokontroll=robokontroll;
    }

    public void steuern(double deltaTimeSec) {
            double deltaX = deltaTimeSec * velocity * Math.cos(orientation * Math.PI / 180.0);
            double deltaY = deltaTimeSec * velocity * Math.sin(orientation * Math.PI / 180.0);
            double futurX=deltaTimeSec * 10 * Math.cos(orientation * Math.PI / 180.0);
            double futurY=deltaTimeSec * 10 * Math.sin(orientation * Math.PI / 180.0);
            if (posX > 600 - deltaX | posX < radius + deltaX | posY > 400 - deltaY | posY < radius + deltaY) {
               if(velocity != 0){
                   this.velocity=0;
               }if(velocity==0 && posX+futurX+radius > 0 && posX+futurX-radius<600 && posY+futurY+radius>0 && posY+futurY-radius<400){
                   roboter.move(deltaTimeSec);
                }
            }
            else {
                roboter.move(deltaTimeSec);
            }
        }*/
    }
