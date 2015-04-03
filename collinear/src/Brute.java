import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Brute force implementation of finding the four collinear points in a 2-D plane Takes time proportional to N^4 and
 * space proportional to N
 * 
 * @author jpyla
 * 
 */
public class Brute {

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
     * This method takes the array of points and evaluates their four point collinearity
     * 
     * @param points array of points which need to be checked
     */
    private static void findfourCollinearPointSets(Point[] points) {

        Map<String, String> drawingCheck = new HashMap<String, String>(); // for avoiding multiple permutations of the
                                                                          // found line segment with different order

        for (int i = 0; i < points.length; i++) { // Fix the first point
            Point first = points[i];

            StdDraw.setXscale(0, 32768);
            StdDraw.setYscale(0, 32768);
            // StdDraw.setPenColor( 0, 0, 255 );
            first.draw();

            for (int j = i; j < points.length; j++) { // Fix the second point, other than first point
                if (i == j)
                    continue;
                Point second = points[j];
                for (int k = j; k < points.length; k++) { // Fix the third point, other than first and second
                    if (k == j || k == i)
                        continue;
                    Point third = points[k];
                    if (first.SLOPE_ORDER.compare(second, third) != 0)
                        continue;
                    for (int l = k; l < points.length; l++) { // Fix the fourth point, other than first, second and
                                                              // third
                        if (l == i || l == j || l == k)
                            continue;
                        Point fourth = points[l];
                        if (first.SLOPE_ORDER.compare(second, third) == 0
                            && first.SLOPE_ORDER.compare(third, fourth) == 0) { // Evaluate the relative slopes of
                                                                                // the four points
                            // points are collinear
                            // connect using UF
                            if (!connected(drawingCheck, i, j, k, l)) { // check if the similar line segments are
                                                                        // connected already
                                outputOnStdOut(first, second, third, fourth); // Output the result to StdOut
                                drawPoints(first, second, third, fourth); // Draw the resulting points
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * This method evaluates whether the the line segment has been processed in a different permutation. e.g. if the
     * points are p, q, r, s and if the p->q->r->s is already processed (printed, drawn) then it's permutations such as
     * q->r->s->p, r->p->s->q, s->p->q->r need not be printed.
     * 
     * This is done evaluating the index anagram (string representation of the indexes in the given order) and sorting
     * the indexes will always result in same value. Put the string value of this in a HashMap. For subsequent calls
     * check if the sorted index string key value already exists in the map, then this iteration for these
     * indexes(points) is a permutation of already processed indexes(points), so need to process (print, draw) again
     * 
     * @param drawnMap {@link HashMap} containing the key of the sorted indexes (anagrams)
     * @param points int[] of the indexes which need to be processed.
     * @return true if the indexes are already processed false if the indexes are not processed before
     */
    private static boolean connected(Map<String, String> drawnMap, int... points) {

        StringBuffer buffer = new StringBuffer();
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            buffer.append(String.valueOf(points[i]));
        }
        String key = buffer.toString();
        if (drawnMap.containsKey(key)) {
            return true;
        } else {
            drawnMap.put(key, "true");
        }
        return false;
    }

    /**
     * Draws the points to the StdDraw. Lines are drawn only once between collinear point (by taking the advantage of
     * sort of points on y co-ordinate.) Line is drawn only between first and the last point in the line segment
     * 
     * @param points Points to be drwan
     */
    private static void drawPoints(Point... points) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenColor( 0, 0, 255 );
        Arrays.sort(points);
        points[0].drawTo(points[points.length - 1]);

    }

    /**
     * Prints the points in a specific format such as p->q->r->s to the StdOut.
     * 
     * @param points Points to be printed
     */
    private static void outputOnStdOut(Point... points) {
        StdOut.println();
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null)
                StdOut.print(points[i]);
            if (i != points.length - 1)
                StdOut.print(" -> ");
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
