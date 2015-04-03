import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jpyla
 * 
 */
public class PointSET {

    private SET<Point2D> points;

    public PointSET() { // construct an empty set of points

        this.points = new SET<Point2D>();
    }

    /**
     * Constructor
     * 
     * @param p {@link Point2D} point to insert
     */
    public void insert(Point2D p) { // add the point p to the set (if it is not already in the set)
        // TODO Auto-generated method stub

        points.add(p);
    }

    /**
     * Checks if empty
     * 
     * @return {@link Boolean} boolean value of the emptiness
     */
    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    /**
     * Size of the point set
     * 
     * @return {@link Integer} size of the set
     */
    public int size() { // number of points in the set
        return points.size();
    }

    /**
     * Returns true or false based on the key is contained or not
     * 
     * @param p {@link Point2D}
     * @return {@link Boolean}
     */
    public boolean contains(Point2D p) { // does the set contain the point p?
        return points.contains(p);
    }

    /**
     * Draws the set of points
     */
    public void draw() { // draw all of the points to standard draw
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D point : points) {
            point.draw();
        }
    }

    /**
     * all points in the set that are inside the rectangle
     * 
     * @param rect {@link RectHV}
     * @return {@link Iterable}
     */
    public Iterable<Point2D> range(RectHV rect) { // all points in the set that are inside the rectangle
        if (rect == null) {
            return null;
        }
        List<Point2D> rangePoints = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                rangePoints.add(point);
            }
        }
        return rangePoints;
    }

    /**
     * a nearest neighbor in the set to p; null if set is empty
     * 
     * @param p {@link Point2D}
     * @return {@link Point2D}
     */
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to p; null if set is empty
        if (p == null) {
            return null;
        }
        double minDist = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D point : points) {
            double tempMin = p.distanceSquaredTo(point);
            if (tempMin < minDist) {
                minDist = tempMin;
                minPoint = point;
            }
        }
        return minPoint;
    }

}
