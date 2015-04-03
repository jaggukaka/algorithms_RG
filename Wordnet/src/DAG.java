
/**
 * 
 */

/**
 * @author jpyla
 * 
 */
public class DAG {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        DAG dag = new DAG();
        System.out.println(dag.isDAG(G));

    }

    private boolean isDAG(Digraph digr) {
        // TODO Auto-generated method stub
        digr = digr.reverse();
        int vertices = digr.V();
        boolean[] marked = new boolean[vertices];
        boolean[] dfsm = new boolean[vertices];
        int[] vertexReachability = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            if (!marked[i]) {
                DFSResult result = dfs(digr, marked, i, dfsm, vertexReachability);
                if (!result.isDAG) {
                    return false;
                }
                vertexReachability[i] = result.vertexCount;
            }
        }

        return true;
    }

    private DFSResult dfs(Digraph digr, boolean[] marked, int vertex, boolean[] dfsm,
        int[] vertexReachability) {
        // TODO Auto-generated method stub
        boolean isDAG = true;
        marked[vertex] = true;
        dfsm[vertex] = true;
        int vertexCount = 1;
        for (int nextVertex : digr.adj(vertex)) {
            if (!dfsm[nextVertex]) {
                if (!marked[nextVertex]) {
                    DFSResult result = dfs(digr, marked, nextVertex, dfsm, vertexReachability);
                    vertexCount = vertexCount + result.vertexCount;
                    if (!result.isDAG) {
                        return result;
                    }
                } else {
                        vertexCount = vertexCount + vertexReachability[nextVertex];
                }
            } else {
                return new DFSResult(false, vertexCount);
            }
        }
        vertexReachability[vertex] = vertexCount;
        dfsm[vertex] = false;
        return new DFSResult(isDAG, vertexCount);
    }

    private final class DFSResult {

        boolean isDAG;

        int vertexCount;

        public DFSResult(boolean isDAG, int vertexCount) {
            super();
            this.isDAG = isDAG;
            this.vertexCount = vertexCount;
        }

    }
    
}
