/**
 * 
 */

/**
 * @author jpyla
 * 
 */
public class SAPArr {

    private Digraph digraph;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAPArr sap = new SAPArr(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAPArr(Digraph digraph) {
        this.digraph = digraph;
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
    public int length(int v, int w) {
        Ancestor ancestor = getAncestor(v, w);
        return ancestor.distance;
    }

    private Ancestor getAncestor(int v, int w) {
        int j = digraph.V();
        // do BFS on v
        int[] distanceTov = distanceTo(v);
        int[] distanceTow = distanceTo(w);
        int minSum = -1;
        int ancestorVertex = -1;
        for (int i = 0; i < j; i++) {
            int distV = distanceTov[i];
            int distW = distanceTow[i];
            if (distV == -1 || distW == -1) {
                continue;
            }
            int dist = distV + distW;

            if (minSum == -1) {
                minSum = dist;
                ancestorVertex = i;
            } else if (minSum > dist) {
                minSum = dist;
                ancestorVertex = i;
            }
        }
        return new Ancestor(ancestorVertex, minSum);
    }

    private int[] distanceTo(int v) {
        int j = digraph.V();
        int[] distances = new int[j];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = -1;
        }

        boolean[] marked = new boolean[j];
        processDistanceArray(distances, marked, v);
        return distances;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Ancestor ancestor = getAncestor(v, w);
        return ancestor.vertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Ancestor ancestor = getAncestors(v, w);
        return ancestor.distance;
    }

    private Ancestor getAncestors(Iterable<Integer> v, Iterable<Integer> w) {
        int j = digraph.V();
        // do BFS on v
        int[] distanceTov = distanceTo(v);
        int[] distanceTow = distanceTo(w);
        int minSum = -1;
        int ancestorVertex = -1;
        for (int i = 0; i < j; i++) {
            int distV = distanceTov[i];
            int distW = distanceTow[i];
            if (distV == -1 || distW == -1) {
                continue;
            }
            int dist = distV + distW;

            if (minSum == -1) {
                minSum = dist;
                ancestorVertex = i;
            } else if (minSum > dist) {
                minSum = dist;
                ancestorVertex = i;
            }
        }
        return new Ancestor(ancestorVertex, minSum);
    }

    private int[] distanceTo(Iterable<Integer> v) {
        // TODO Auto-generated method stub
        int j = digraph.V();
        int[] distances = new int[j];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = -1;
        }
        boolean[] marked = new boolean[j];
        for (int k : v) {
            if (marked[k]) {
                continue;
            }
            processDistanceArray(distances, marked, k);
        }

        return distances;

    }

    private void processDistanceArray(int[] distances, boolean[] marked, int k) {
        Queue<Integer> vertices = new Queue<Integer>();
        vertices.enqueue(k);
        int distance = 0;
        while (!vertices.isEmpty()) {
            int vertex = vertices.dequeue();
            if (!marked[vertex]) {
                distances[vertex] = distance++;
                marked[vertex] = true;
                for (int neighbour : digraph.adj(vertex)) {
                    vertices.enqueue(neighbour);
                }
            }
        }

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return -1;
    }

}
