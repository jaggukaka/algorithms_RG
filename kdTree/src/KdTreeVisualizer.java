/*************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: StdDraw.java Point2D.java KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 *************************************************************************/

public class KdTreeVisualizer {

    public static void main(String[] args) {
       // StdDraw.show(0);
        
        
        KdTree kdtree = new KdTree();

        String filename = args[0];
        In in = new In(filename);
//
//        // initialize the data structures with N points from standard input
//        //PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            //brute.insert(p);
        }
        kdtree.draw();
        System.out.println(kdtree.contains(new Point2D(0.024472, 0.654508)));
//        while (true) {
//            if (StdDraw.mousePressed()) {
//                double x = StdDraw.mouseX();
//                double y = StdDraw.mouseY();
//                System.out.printf("%8.6f %8.6f\n", x, y);
//                Point2D p = new Point2D(x, y);
//                kdtree.insert(p);
//                StdDraw.clear();
//                kdtree.draw();
//            }
//            StdDraw.show(50);
//        }

    }
}
