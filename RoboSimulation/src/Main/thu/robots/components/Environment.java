package thu.robots.components;

import georegression.metric.ClosestPoint2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;

import java.util.LinkedList;
import java.util.List;

public class Environment {

    private final int width;
    private final int height;
    private String name;
    private List<EnvironmentObject> objects = new LinkedList<>();
    private List<LineSegment2D_F64> objectSegments = new LinkedList<>();


    public Environment(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        objects = new LinkedList<>();
    }



    protected List<SensorData> simulateSensorData(IRobot r, BaseSensor s){
        List<SensorData> sensData = new LinkedList<>();
        int xr = r.getPosX();
        int yr = r.getPosY();
        double robotOrientation = r.getOrientation();
        double globalOrientationOfSensor =robotOrientation+s.getOrientationToRobot();

        double beamWidth = s.getBeamWidth();
        int maxRange = s.getMaxRange();

        Point2D_F64 robotCenter = new Point2D_F64(xr,yr);

        double minDist = 99999;
        Point2D_F64 measPoint = null;

        double minAngle = globalOrientationOfSensor-beamWidth/2.;
        double maxAngle = globalOrientationOfSensor+beamWidth/2.;

        for(LineSegment2D_F64 objL : objectSegments) {
            // first check if objL is a valid candidate (for speedup)
            Point2D_F64 cpCheck = ClosestPoint2D_F64.closestPoint(objL, robotCenter, null);
            Point2D_F64 cpToRobot = new Point2D_F64(cpCheck.getX() - xr, cpCheck.getY() - yr);
            double distCheck = cpToRobot.norm();
            // if closest point of object segment is beyond range: we cannot see this segment
            if (distCheck > maxRange) {
                continue;
            }


            for (double a = minAngle; a <= maxAngle; a = a + 0.01) {
                LineSegment2D_F64 ray = new LineSegment2D_F64(xr, yr, xr + maxRange * Math.cos(a), yr + maxRange * Math.sin(a));
                // intersect ray with object line
                Point2D_F64 ip = Intersection2D_F64.intersection(ray, objL, null);
                if(ip==null){
                    continue;
                }

                Point2D_F64 vecToRobot = new Point2D_F64(ip.getX() - xr, ip.getY() - yr);
                double distToSensor = vecToRobot.norm();

                // only use the minimum distance in all rays
                if(distToSensor < minDist){
                    minDist = distToSensor;
                    measPoint= ip;
                }
            }
        }

        if(measPoint != null){
            Point2D_F64 vecToRobot = new Point2D_F64(measPoint.getX() - xr, measPoint.getY() - yr);
            double angleToGlobal= Math.atan2(vecToRobot.getY(), vecToRobot.getX());
            double angleToSensor = angleToGlobal - globalOrientationOfSensor;
            double distToSensor = vecToRobot.norm();
            SensorData sd = new SensorData(angleToSensor, (int)distToSensor, s);
            sensData.add(sd);
        }
        return sensData;
    }



    public void simulateSensorData(Robot r){
        List<BaseSensor> sensors = r.getSensors();
        for(BaseSensor s : sensors){
            LinkedList<SensorData> data = new LinkedList<SensorData>();
            List<SensorData> sd = simulateSensorData(r, s);
            data.addAll(sd);
            s.measurementFromEnvironment(sd);
        }
    }


    /**
     * Calculates object cache
     */
    public void prepare() {
        for(EnvironmentObject obj : objects){
            // env objects are given with center reference point coordinate (x,y)
            double xRef,x2,x3,x4;
            double yRef,y2,y3,y4;
            // top left, rop right, bottom right, bottom left
            double length = obj.getLength();
            double width = obj.getWidth();
            double alpha = obj.getOrientation();



            xRef = obj.getX();
            yRef = obj.getY();

            // top left, top right
            objectSegments.add(createLineSegmentRotated(xRef,yRef,-length/2.,-width/2.,length/2.,-width/2.,alpha));
            // top right, bottom right
            objectSegments.add(createLineSegmentRotated(xRef,yRef,length/2.,-width/2.,length/2.,width/2.,alpha));
            // bottom right, bottom left
            objectSegments.add(createLineSegmentRotated(xRef,yRef,length/2.,width/2.,-length/2., width/2.,alpha));
            // bottom left, top left
            objectSegments.add(createLineSegmentRotated(xRef,yRef,-length/2.,width/2.,-length/2., -width/2.,alpha));

        }
    }

    private LineSegment2D_F64 createLineSegmentRotated(double xRef, double yRef, double x1, double y1, double x2, double y2,double alpha) {
        double sinA = Math.sin(alpha);
        double cosA = Math.cos(alpha);

        double px1 = x1*cosA -y1*sinA + xRef;
        double py1 = x1*sinA + y1*cosA + yRef;

        double px2 = x2*cosA -y2*sinA + xRef;
        double py2 = x2*sinA + y2*cosA + yRef;

        LineSegment2D_F64 res = new LineSegment2D_F64(px1, py1, px2, py2);
        return res;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addObject(EnvironmentObject obj) {
        this.objects.add(obj);
    }

    public String getName() {
        return name;
    }

    public List<EnvironmentObject> getObjects() {
        return objects;
    }


}
