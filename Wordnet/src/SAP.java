import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 */

/**
 * Computes the length and ancestor given a {@link Digraph} for vertices
 * 
 * @author jpyla
 * 
 */
public class SAP {

    private Digraph digraph;

    // private int ancestor = -1;
    //
    // private int ancestorMulti = -1;
    //
    // private int length = -1;
    //
    // private int lengthMulti = -1;

    // constructor takes a digraph (not necessarily a DAG)
    /**
     * constructor takes a digraph (not necessarily a DAG)
     * 
     * @param digraph {@link Digraph}
     */
    public SAP(Digraph digr) {
        this.digraph = new Digraph(digr);
    }

    private final class Ancestor {
        private int vertex;

        private int distance;

        public Ancestor(int vertex, int distance) {
            super();
            this.vertex = vertex;
            this.distance = distance;
        }

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    /**
     * length of shortest ancestral path between v and w; -1 if no such path
     * 
     * @param v {@link Integer} source vertex
     * @param w {@link Integer} source vertex 2
     * @return The shortest ancestral path distance
     */
    public int length(int v, int w) {
        // if (length != -1) {
        // int lengthTemp = length;
        // length = -1;
        // return lengthTemp;
        // }

        Ancestor ancest = getAncestor(v, w);
        // this.ancestor = ancest.vertex;
        return ancest.distance;
    }

    private Ancestor getAncestor(int v, int w) {
        // do BFS on v
        Map<Integer, Integer> distanceTov = distanceTo(v);
        Map<Integer, Integer> distanceTow = distanceTo(w);
        int minSum = -1;
        int ancestorVertex = -1;
        for (Entry<Integer, Integer> entry : distanceTov.entrySet()) {
            int key = entry.getKey();
            int distV = entry.getValue();
            Object distW = distanceTow.get(key);
            if (distW == null) {
                continue;
            }
            int dist = distV + ((Integer) distW).intValue();

            if (minSum == -1) {
                minSum = dist;
                ancestorVertex = key;
            } else if (minSum > dist) {
                minSum = dist;
                ancestorVertex = key;
            }
        }
        return new Ancestor(ancestorVertex, minSum);
    }

    private Map<Integer, Integer> distanceTo(int v) {
        int j = digraph.V();
        Map<Integer, Integer> distances = new HashMap<Integer, Integer>();
        boolean[] marked = new boolean[j];

        Queue<Integer> vertices = new Queue<Integer>();

        marked[v] = true;
        vertices.enqueue(v);
        distances.put(v, 0);

        processDistanceArray(distances, marked, vertices);
        return distances;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    /**
     * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     * 
     * @param v {@link Integer} Source vertex
     * @param w {@link Integer} Source vertex 2
     * @return Returns the common shortest path ancestor
     */
    public int ancestor(int v, int w) {
        // if (ancestor != -1) {
        // int ancestortemp = ancestor;
        // ancestor = -1;
        // return ancestortemp;
        // }
        Ancestor ancest = getAncestor(v, w);
        // this.length = ancest.distance;
        return ancest.vertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     * 
     * @param v List of source vertices
     * @param w List of source vertices 2
     * @return Shortest ancestral path between these set of vertices
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // if (lengthMulti != -1) {
        // int lentemp = lengthMulti;
        // lengthMulti = -1;
        // return lentemp;
        // }
        Ancestor ancest = getAncestors(v, w);
        // this.ancestorMulti = ancest.vertex;
        return ancest.distance;
    }

    private Ancestor getAncestors(Iterable<Integer> v, Iterable<Integer> w) {
        // do BFS on v

        Map<Integer, Integer> distanceTov = distanceTo(v);
        Map<Integer, Integer> distanceTow = distanceTo(w);
        int minSum = -1;
        int ancestorVertex = -1;
        for (Entry<Integer, Integer> entry : distanceTov.entrySet()) {
            int key = entry.getKey();
            int distV = entry.getValue();
            Object distW = distanceTow.get(key);
            if (distW == null) {
                continue;
            }
            int dist = distV + ((Integer) distW).intValue();

            if (minSum == -1) {
                minSum = dist;
                ancestorVertex = key;
            } else if (minSum > dist) {
                minSum = dist;
                ancestorVertex = key;
            }
        }
        return new Ancestor(ancestorVertex, minSum);
    }

    private Map<Integer, Integer> distanceTo(Iterable<Integer> v) {
        // TODO Auto-generated method stub
        int j = digraph.V();
        Map<Integer, Integer> distances = new HashMap<Integer, Integer>();
        boolean[] marked = new boolean[j];
        Queue<Integer> vertices = new Queue<Integer>();

        for (int k : v) {
            marked[k] = true;
            vertices.enqueue(k);
            distances.put(k, 0);
        }
        processDistanceArray(distances, marked, vertices);

        return distances;

    }

    private void processDistanceArray(Map<Integer, Integer> distances, boolean[] marked, Queue<Integer> vertices) {

        while (!vertices.isEmpty()) {
            int vertex = vertices.dequeue();
            int distance = distances.get(vertex);
            for (int neighbour : digraph.adj(vertex)) {
                if (!marked[neighbour]) {
                    distances.put(neighbour, distance + 1);
                    marked[neighbour] = true;
                    vertices.enqueue(neighbour);
                }
            }
        }

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no such path
     * 
     * @param v List of source vertices
     * @param w List of source vertices 2
     * @return Common ancestor of these list of vertices
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // if (ancestorMulti != -1) {
        // int ancestortemp = ancestorMulti;
        // ancestorMulti = -1;
        // return ancestortemp;
        // }
        Ancestor ancest = getAncestors(v, w);
        // this.lengthMulti = ancest.distance;
        return ancest.vertex;
    }

    /**
     * @param args {@link String} arguments
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }

}
