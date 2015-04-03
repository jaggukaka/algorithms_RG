/*************************************************************************
 * Name: Email:
 * 
 * Compilation: javac Point.java Execution: Dependencies: StdDraw.java
 * 
 * Description: An immutable data type for points in the plane.
 * 
 *************************************************************************/

import java.awt.Font;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {

        @Override
        public int compare(Point o1, Point o2) {
            double slopeo1 = slopeTo(o1);
            double slopeo2 = slopeTo(o2);
            if (slopeo1 == slopeo2) {
                return 0;
            } else if (slopeo1 < slopeo2) {
                return -1;
            } else {
                return +1;
            }
        }

    }; // YOUR DEFINITION HERE

    private final double x; // x coordinate

    private final double y; // y coordinate

    // create the point (x, y)
    public Point(double x, double y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }
    
    

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */

        double y1 = that.y, x1 = that.x;
        boolean isHorizontal = y1 - this.y == 0;
        boolean isVertical = x1 - this.x == 0;
        if (isHorizontal && isVertical) {
            return Double.NEGATIVE_INFINITY;
        }
        if (isHorizontal) {
            return 0;
        }
        if (isVertical) {
            return Double.POSITIVE_INFINITY;
        }

        return (double) (y1 - this.y) / (x1 - this.x);

    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (that.y > this.y) {
            return -1;
        } else {
            if (that.y == this.y) {
                if (that.x > this.x) {
                    return -1;
                } else if (that.x == this.x) {
                    return 0;
                }
            }
        }

        return +1;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    // public static void main(String[] args) {
    // /* YOUR CODE HERE */
    // // Point p = new Point(10000, 0);
    // // Point q = new Point(0, 10000);
    // // Point r = new Point(3000, 7000);
    // // Point s = new Point(7000, 3000);
    // //
    // // p.draw();q.draw();r.draw();s.draw();
    // // p.drawTo(s);p.drawTo(q);
    // //
    // // if (p.slopeTo(q) == r.slopeTo(s)) {
    // // StdOut.println("Slope for " + p + q + r + s + " evaluated properly");
    // //
    // // } else {
    // // StdOut.println(" Some bug in point class, slope for " + p + q + r + s + " not evaluated properly!!");
    // // }
    // //
    // // s = new Point(7000, 13000);
    // //
    // // if (p.slopeTo(q) != r.slopeTo(s)) {
    // // StdOut.println("Slope for " + p + q + r + s + " evaluated properly");
    // // } else {
    // // StdOut.println(" Some bug in point class, slope for " + p + q + r + s + " not evaluated properly!!");
    // // }
    //
    // }

    public static void main(String[] args) {
        if (args == null || args.length <= 0) { // check the input argument
            throw new IllegalArgumentException("Argument to the main method should not be null or empty!!"); // throw
                                                                                                             // exception
                                                                                                             // if the
                                                                                                             // argument
                                                                                                             // is
                                                                                                             // illegal
        }
        In in = new In(args[0]); // Read the input using the StdIn

        Point[] points = populatePoints(in); // Populate points in Point[]
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 10));
//        for (int i =0; i < points.length -1; i = i +2) {
//            points[i].draw();points[i+1].draw();
//            double k, j=0;
//            if (points[i].slopeTo(points[i+1]) == 0) {
//                j = 0.3; k= 0;
//            } else {
//                j = 0; k= 0.3;
//            }
//            StdDraw.textLeft(points[i].x + k, points[i].y + j, points[i].toString());
//            StdDraw.textLeft(points[i+1].x + k, points[i+1].y - j, points[i+1].toString());
//            points[i].drawTo(points[i+1]);
//        }
        
        for (int i =0; i < points.length; i++) {
            points[i].draw();
            
            StdDraw.textLeft(points[i].x , points[i].y , points[i].toString());
            
        }
    }

    /**
     * Populates the points in a Point array
     * 
     * @param in {@link In} The reader of the input file
     * @return Point array
     */
    private static Point[] populatePoints(In in) {
        if (in != null) {
            String line = in.readLine();
            if (line != null) {
                line = line.trim();
            } else {
                line = "0";
            }
            int numberOfItems = Integer.valueOf(line);

            double[] arr = in.readAllDoubles();
            if (arr.length / 2 < numberOfItems) {
                numberOfItems = arr.length / 2;
            }
            Point[] points = new Point[numberOfItems];
            for (int i = 0; i < 2 * numberOfItems; i = i + 2) {
                Point p = new Point(Double.valueOf(arr[i]), Double.valueOf(arr[i + 1]));
                points[i / 2] = p;
            }

            in.close();
            return points;
        }
        return null;

    }
}
