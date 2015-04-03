import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Fast implementation of finding the four or more collinear points in a 2-D plane. Takes time proportional to N^2LogN
 * and space proportional to N
 * 
 * @author jpyla
 * 
 */
public class Fast {

    /**
     * Main method which takes input file name as argument and finds the points and evaluates the collinearity
     * 
     * @param args input filename
     */
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
        if (points != null) {
            findfourCollinearPointSets(points); // Evaluate the collinearity of four points
        }

    }

    /**
     * This method takes the array of points and evaluates their four or more point collinearity Given a point p, the
     * following method determines whether p participates in a set of 4 or more collinear points.
     * 
     * Think of p as the origin.
     * 
     * For each other point q, determine the slope it makes with p.
     * 
     * Sort the points according to the slopes they makes with p.
     * 
     * Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these
     * points, together with p, are collinear.
     * 
     * Applying this method for each of the N points in turn yields an efficient algorithm to the problem. The algorithm
     * solves the problem because points that have equal slopes with respect to p are collinear, and sorting brings such
     * points together. The algorithm is fast because the bottleneck operation is sorting.
     * 
     * @param points array of points which need to be checked
     */
    private static void findfourCollinearPointSets(Point[] points) {
        Map<String, ArrayList<Point>> drawingCheck = new HashMap<String, ArrayList<Point>>();

        for (int i = 0; i < points.length; i++) { // Fix a point
            Point p = points[i];
            StdDraw.setXscale(0, 32768);
            StdDraw.setYscale(0, 32768);
            // StdDraw.setPenColor( 0, 0, 255 );
            p.draw();
            Arrays.sort(points, i, points.length, p.SLOPE_ORDER); // Sort with respect to this point
            int j = 1, k = 1;
            while (j < points.length - 1) {
                if (p.slopeTo(points[j]) != p.slopeTo(points[++j])) { // evaluate if the consecutive slopes are
                                                                      // same
                    checkAndProcessThePoints(k, p, points, drawingCheck, j); // if yes check whether more the four
                                                                             // points in the pass, if yes print
                    k = j;
                }
            }
            checkAndProcessThePoints(k, p, points, drawingCheck, j + 1); // check whether more the four points in the
                                                                         // pass, if yes print (for boundary case where
                                                                         // the last point is fourth or higher
                                                                         // collinear point)
        }

    }

    /**
     * Checks whether the number of points are greater than or equal to four and also checks if the line segment is not
     * already processed and then prints and draws the segments
     * 
     * @param startIndex The index from which the collinearity starts (slopes are compared to be equal)
     * @param basePoint The point against which the slopes are being compared
     * @param points The array of collinear points
     * @param drawingCheck {@link HashMap} which contains the state of whether a particular slope (line segment is
     *            already drawn or not)
     * @param endIndex The index(exclusive) where the collinearity ends
     */
    private static void checkAndProcessThePoints(int startIndex, Point basePoint, Point[] points,
        Map<String, ArrayList<Point>> drawingCheck, int endIndex) {
        if (endIndex - startIndex > 2 && !connected(basePoint, points, endIndex, startIndex, drawingCheck)) { // check
                                                                                                              // if
                                                                                                              // four
                                                                                                              // or
                                                                                                              // more
                                                                                                              // points
                                                                                                              // and
                                                                                                              // check
                                                                                                              // whether
                                                                                                              // not
                                                                                                              // already
                                                                                                              // processed
            Arrays.sort(points, startIndex, endIndex); // Sort the points
            outputOnStdOut(basePoint, points, endIndex, startIndex); // print to the StdOut
            drawPoints(basePoint, points, endIndex, startIndex); // Draw to the StdDraw
        }
    }

    /**
     * Checks whether a line segment is already processed (printed, drawn)
     * 
     * @param startIndex The index from which the collinearity starts (slopes are compared to be equal)
     * @param basePoint The point against which the slopes are being compared
     * @param points The array of collinear points
     * @param drawingCheck {@link HashMap} which contains the state of whether a particular slope (line segment is
     *            already drawn or not)
     * @param endIndex The index(exclusive) where the collinearity ends
     * @return true if the line segment is processed false if the line segment is not processed
     */
    private static boolean connected(Point basePoint, Point[] points, int endIndex, int startIndex,
        Map<String, ArrayList<Point>> drawnMap) {
        // TODO Auto-generated method stub
        double slope = basePoint.slopeTo(points[startIndex]);
        String key = String.valueOf(slope);
        

        if (drawnMap.containsKey(key)) {
            ArrayList<Point> pointsJoined = drawnMap.get(key);
            for (Point toPoint : pointsJoined) {
                if (basePoint.slopeTo(toPoint) == slope) {
                    return true;
                }
            }
            pointsJoined.add(basePoint);
            return false;
        } else {
            ArrayList<Point> pointsJoined = new ArrayList<Point>();
            pointsJoined.add(basePoint);
            drawnMap.put(key, pointsJoined);
        }
        return false;
    }

    /**
     * Draw the points to the StdDraw
     * 
     * @param startIndex The index from which the collinearity starts (slopes are compared to be equal)
     * @param basePoint The point against which the slopes are being compared
     * @param points The array of collinear points
     * @param endIndex The index(exclusive) where the collinearity ends
     */
    private static void drawPoints(Point p, Point[] points, int endIndex, int startIndex) {
        // TODO Auto-generated method stub
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenColor( 0, 0, 255 );

        if (p != null && p.compareTo(points[startIndex]) < 0) { // Draw a single line segment between the highest
            p.drawTo(points[endIndex - 1]);
        } else if (p != null && p.compareTo(points[endIndex - 1]) > 0) {
            points[startIndex].drawTo(p);
        } else {
            points[startIndex].drawTo(points[endIndex - 1]);
        }

    }

    /**
     * Prints the points to the StdOut
     * 
     * @param startIndex The index from which the collinearity starts (slopes are compared to be equal)
     * @param basePoint The point against which the slopes are being compared
     * @param points The array of collinear points
     * @param endIndex The index(exclusive) where the collinearity ends
     */
    private static void outputOnStdOut(Point p, Point[] points, int endIndex, int startIndex) {
        // TODO Auto-generated method stub
        StdOut.println();

        boolean pPrinted = false;
        for (int i = startIndex; i < endIndex; i++) {
            if (points[i] != null) {
                if (p.compareTo(points[i]) < 0 && !pPrinted) {
                    StdOut.print(p);
                    StdOut.print(" -> ");
                    pPrinted = true;
                }
                StdOut.print(points[i]);
            }
            if (i != endIndex - 1)
                StdOut.print(" -> ");
        }

        if (!pPrinted) {
            StdOut.print(" -> ");
            StdOut.print(p);
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

            int[] arr = in.readAllInts();
            if (arr.length / 2 < numberOfItems) {
                numberOfItems = arr.length / 2;
            }
            Point[] points = new Point[numberOfItems];
            for (int i = 0; i < 2 * numberOfItems; i = i + 2) {
                Point p = new Point(Integer.valueOf(arr[i]), Integer.valueOf(arr[i + 1]));
                points[i / 2] = p;
            }

            in.close();
            return points;
        }
        return null;

    }
}
