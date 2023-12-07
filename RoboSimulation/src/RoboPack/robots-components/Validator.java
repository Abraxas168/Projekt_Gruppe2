package thu.robots.components;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Validator {
    private Environment environment;

    public Validator(Environment env){
        this.environment = env;
    }


    public EnvironmentObject checkCollosion(IRobot robot) {
        int radius = robot.getRadius();

        int x = robot.getPosX();
        int y = robot.getPosY();

        // create rectangle with transformed center of robot (reference of rectangle is top-left corner)
        //Rectangle rBot = new Rectangle(x-radius,y-radius,radius*2, radius*2);
        Ellipse2D rBot = new Ellipse2D.Double(x-radius,y-radius,radius*2, radius*2);


        for(EnvironmentObject obj : environment.getObjects()) {
            // Transform robot rectangle into coordinate system of object
            AffineTransform rt = new AffineTransform();
            // rotate robot rectangle around center of object opposite to object orientation
            rt.rotate(-obj.getOrientation(), obj.getX(), obj.getY());
            // rotate around robot center
            rt.rotate(robot.getOrientation(), x,y);
            Area aBotTemp = new Area(rBot);
            // create transformed area of robot rectangle
            Area aBot = aBotTemp.createTransformedArea(rt);
            // check intersection
            Rectangle2D objRect = obj.getRectangle();
            if(aBot.intersects(objRect)){
                return obj;
            }
        }
        return null;
    }

    public boolean checkTargetZone(IRobot robot) {
        return(robot.getPosX() >= this.environment.getWidth());
    }

}
