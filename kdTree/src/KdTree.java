import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jpyla
 * 
 */
public class KdTree {

    private Node root;

    private int size = 0;

    private final class Node {
        private Node left;

        private Node right;

        private Orientation orientation;

        private RectHV segment;

        private Point2D point; // key

        private Node(Node left, Node right, Orientation orientation, Point2D point, RectHV segment) {
            super();
            this.left = left;
            this.right = right;
            this.orientation = orientation;
            this.point = point;
            this.segment = segment;
        }

        private Node(Node node) {
            copy(node);
        }

        private void copy(Node node) {
            if (node == null) {
                return;
            }
            this.left = node.left;
            this.right = node.right;
            this.orientation = node.orientation;
            this.segment = node.segment;
            this.point = node.point;
        }

        public String toString() {
            return point.toString();
        }

    }

    private enum Orientation {
        HORIZONTAL,

        VERTICAL

    }

    /**
     * inserts a {@link Point2D} into the kd tree
     * 
     * @param p {@link Point2D}
     */
    public void insert(Point2D p) { // add the point p to the set (if it is not already in the set)
        // TODO Auto-generated method stub
        if (root == null) {
            root = new Node(null, null, Orientation.VERTICAL, p, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        insert(root, root.orientation, p);

    }

    private void insert(Node node, Orientation orientation, Point2D p) {

        if (node.point.equals(p)) {
            return;
        }

        if (Orientation.VERTICAL == orientation) {
            if (Point2D.X_ORDER.compare(p, node.point) >= 0) {
                if (node.right != null) {
                    insert(node.right, Orientation.HORIZONTAL, p);
                } else {
                    node.right = new Node(null, null, Orientation.HORIZONTAL, p, new RectHV(node.point.x(),
                        node.segment.ymin(), node.segment.xmax(), node.segment.ymax()));
                    size++;
                }
            } else {
                if (node.left != null) {
                    insert(node.left, Orientation.HORIZONTAL, p);
                } else {
                    node.left = new Node(null, null, Orientation.HORIZONTAL, p, new RectHV(node.segment.xmin(),
                        node.segment.ymin(), node.point.x(), node.segment.ymax()));
                    size++;
                }
            }
        } else if (Orientation.HORIZONTAL == orientation) {
            if (Point2D.Y_ORDER.compare(p, node.point) >= 0) {
                if (node.right != null) {
                    insert(node.right, Orientation.VERTICAL, p);
                } else {
                    node.right = new Node(null, null, Orientation.VERTICAL, p, new RectHV(node.segment.xmin(),
                        node.point.y(), node.segment.xmax(), node.segment.ymax()));
                    size++;
                }
            } else {
                if (node.left != null) {
                    insert(node.left, Orientation.VERTICAL, p);
                } else {
                    node.left = new Node(null, null, Orientation.VERTICAL, p, new RectHV(node.segment.xmin(),
                        node.segment.ymin(), node.segment.xmax(), node.point.y()));
                    size++;
                }
            }
        }

    }

    /**
     * Returns true or false based on the emptiness of the kdtree
     * 
     * @return
     */
    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    /**
     * Returns the size of the kdtree
     * 
     * @return
     */
    public int size() { // number of points in the set
        return size;
    }

    /**
     * checks if the point is present in the kd tree
     * 
     * @param p {@link Point2D}
     * @return {@link Boolean}
     */
    public boolean contains(Point2D p) { // does the set contain the point p?
        if (p == null || root == null) {
            return false;
        }

        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (Orientation.VERTICAL == node.orientation) {
            if (Point2D.X_ORDER.compare(p, node.point) == 0) {
                if (Point2D.Y_ORDER.compare(p, node.point) == 0) {
                    return true;
                } else {
                    return contains(node.right, p);
                }
            } else if (Point2D.X_ORDER.compare(p, node.point) > 0) {
                return contains(node.right, p);
            } else {
                return contains(node.left, p);
            }
        } else if (Orientation.HORIZONTAL == node.orientation) {
            if (Point2D.Y_ORDER.compare(p, node.point) == 0) {
                if (Point2D.X_ORDER.compare(p, node.point) == 0) {
                    return true;
                } else {
                    return contains(node.right, p);
                }
            } else if (Point2D.Y_ORDER.compare(p, node.point) > 0) {
                return contains(node.right, p);
            } else {
                return contains(node.left, p);
            }
        }
        return false;
    }

    /**
     * Draws the kd tree to std out
     */
    public void draw() { // draw all of the points to standard draw
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        draw(root);

    }

    /**
     * 
     * @param node
     */
    private void draw(Node node) {
        // TODO Auto-generated method stub
        if (node == null) {
            return;
        }
        if (Orientation.VERTICAL == node.orientation) {
            double x = node.point.x();
            double minY = node.segment.ymin();
            double maxY = node.segment.ymax();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.point.draw();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x, minY, x, maxY);
        } else if (Orientation.HORIZONTAL == node.orientation) {
            double y = node.point.y();
            double minX = node.segment.xmin();
            double maxX = node.segment.xmax();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.point.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(minX, y, maxX, y);
        }
        draw(node.left);
        draw(node.right);
    }

    /**
     * Returns the range of values in the given rectangle
     * 
     * @param rect {@link RectHV}
     * @return {@link Iterable} points
     */
    public Iterable<Point2D> range(RectHV rect) { // all points in the set that are inside the rectangle
        List<Point2D> pointsInRect = new ArrayList<Point2D>();
        range(root, rect, pointsInRect);
        return pointsInRect;
    }

    /**
     * 
     * @param node
     * @param rect
     * @param pointsInRect
     */
    private void range(Node node, RectHV rect, List<Point2D> pointsInRect) {
        // TODO Auto-generated method stub
        if (node == null) {
            return;
        }

        if (node.segment.intersects(rect)) {
            if (rect.contains(node.point)) {
                pointsInRect.add(node.point);
            }

            range(node.left, rect, pointsInRect);
            range(node.right, rect, pointsInRect);
        }

    }

    /**
     * Searches for the nearest neighbor recursively
     * 
     * @param p {@link Point2D}
     * @return {@link Point2D} nearest neighbor
     */
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to p; null if set is empty
        if (p == null) {
            return null;
        }
        Node nearest = new Node(root);
        nearestNew(root, p, nearest);
        return nearest.point;
    }

    private void nearestNew(Node node, Point2D p, Node nearest) {
        // TODO Auto-generated method stub
        if (node == null) {
            return;
        }
        if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.point)) {
            nearest.copy(node);
        }
        double pxMNodex = p.x() - node.point.x();
        double pyMNodeY = p.y() - node.point.y();
        double pxMNodexSqr = pxMNodex * pxMNodex;
        double pyMNodeySqr = pyMNodeY * pyMNodeY;

        if (Orientation.VERTICAL == node.orientation) {
            if (Point2D.X_ORDER.compare(p, node.point) >= 0) {
                nearestNew(node.right, p, nearest);
                if (p.distanceSquaredTo(nearest.point) > pxMNodexSqr) {
                    nearestNew(node.left, p, nearest);
                }
            } else {
                nearestNew(node.left, p, nearest);
                if (p.distanceSquaredTo(nearest.point) > pxMNodexSqr) {
                    nearestNew(node.right, p, nearest);
                }
            }
        } else if (Orientation.HORIZONTAL == node.orientation) {
            if (Point2D.Y_ORDER.compare(p, node.point) >= 0) {
                nearestNew(node.right, p, nearest);
                if (p.distanceSquaredTo(nearest.point) > pyMNodeySqr) {
                    nearestNew(node.left, p, nearest);
                }
            } else {
                nearestNew(node.left, p, nearest);
                if (p.distanceSquaredTo(nearest.point) > pyMNodeySqr) {
                    nearestNew(node.right, p, nearest);
                }
            }
        }
    }

    // /**
    // *
    // * @param node
    // * @param p
    // * @param nearest
    // * @return
    // */
    // private void nearest(Node node, Point2D p, Node nearest) {
    // // TODO Auto-generated method stub
    // if (node == null) {
    // return;
    // }
    // if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.point)) {
    // nearest.copy(node);
    // }
    // if (Orientation.VERTICAL == node.orientation) {
    // if (Point2D.X_ORDER.compare(p, node.point) >= 0) {
    // if (checkNearestRight(p, node, nearest)) {
    // nearest(node.right, p, nearest);
    // } else {
    // traverseBothSidesRightFirst(node, p, nearest);
    // }
    // } else {
    // if (checkNearestLeft(p, node, nearest)) {
    // nearest(node.left, p, nearest);
    // } else {
    // traverseBothSidesLeftFirst(node, p, nearest);
    // }
    // }
    // } else if (Orientation.HORIZONTAL == node.orientation) {
    // if (Point2D.Y_ORDER.compare(p, node.point) >= 0) {
    // if (checkNearestTop(p, node, nearest)) {
    // nearest(node.right, p, nearest);
    // } else {
    // traverseBothSidesRightFirst(node, p, nearest);
    // }
    // } else {
    // if (checkNearestBottom(p, node, nearest)) {
    // nearest(node.left, p, nearest);
    // } else {
    // traverseBothSidesLeftFirst(node, p, nearest);
    // }
    // }
    // }
    // }
    //
    // private boolean checkNearestBottom(Point2D p, Node node, Node nearest) {
    // // TODO Auto-generated method stub
    // if (node.left == null) {
    // if ((node.point.y() - p.y()) * (node.point.y() - p.y()) > p.distanceSquaredTo(nearest.point)) {
    // return true;
    // }
    // return false;
    // }
    // if ((node.point.y() - p.y()) * (node.point.y() - p.y()) >= p.distanceSquaredTo(node.left.point)) {
    // if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.left.point)) {
    // nearest.copy(node.left);
    // }
    // return true;
    // }
    //
    // return false;
    // }
    //
    // private boolean checkNearestTop(Point2D p, Node node, Node nearest) {
    // // TODO Auto-generated method stub
    // if (node.right == null) {
    // if ((p.y() - node.point.y()) * (p.y() - node.point.y()) > p.distanceSquaredTo(nearest.point)) {
    // return true;
    // }
    // return false;
    // }
    // if ((p.y() - node.point.y()) * (p.y() - node.point.y()) >= p.distanceSquaredTo(node.right.point)) {
    // if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.right.point)) {
    // nearest.copy(node.right);
    // }
    //
    // return true;
    // }
    //
    // return false;
    // }
    //
    // private void traverseBothSidesRightFirst(Node node, Point2D p, Node nearest) {
    // nearest(node.right, p, nearest);
    // nearest(node.left, p, nearest);
    // }
    //
    // private void traverseBothSidesLeftFirst(Node node, Point2D p, Node nearest) {
    // nearest(node.left, p, nearest);
    // nearest(node.right, p, nearest);
    // }
    //
    // private boolean checkNearestRight(Point2D p, Node node, Node nearest) {
    // // TODO Auto-generated method stub
    // if (node.right == null) {
    // if ((p.x() - node.point.x()) * (p.x() - node.point.x()) > p.distanceSquaredTo(nearest.point)) {
    // return true;
    // }
    // return false;
    // }
    // if ((p.x() - node.point.x()) * (p.x() - node.point.x()) >= p.distanceSquaredTo(node.right.point)) {
    // if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.right.point)) {
    // nearest.copy(node.right);
    // }
    // return true;
    // }
    //
    // return false;
    // }
    //
    // private boolean checkNearestLeft(Point2D p, Node node, Node nearest) {
    // // TODO Auto-generated method stub
    // if (node.left == null) {
    // if ((node.point.x() - p.x()) * (node.point.x() - p.x()) > p.distanceSquaredTo(nearest.point)) {
    // return true;
    // }
    // return false;
    // }
    // if ((node.point.x() - p.x()) * (node.point.x() - p.x()) >= p.distanceSquaredTo(node.left.point)) {
    // if (p.distanceSquaredTo(nearest.point) > p.distanceSquaredTo(node.left.point)) {
    // nearest.copy(node.left);
    // }
    // return true;
    // }
    //
    // return false;
    // }

}
