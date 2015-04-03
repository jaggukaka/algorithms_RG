import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */

/**
 * Represents a word net on which operations can be performed for semantics
 * 
 * @author jpyla
 * 
 */
public class WordNet {

    private static final String COMMA = ",";

    private static final String SPACE = " ";

    private Map<String, List<Integer>> wordMap = new HashMap<String, List<Integer>>();

    private Map<Integer, String> indexMap = new HashMap<Integer, String>();

    private SAP sap;

    // constructor takes the name of the two input files
    /**
     * constructor takes the name of the two input files
     * 
     * @param synsets synsets input file name
     * @param hypernyms hypernyms input file name
     */
    public WordNet(String synsets, String hypernyms) {

        int maxVertex = 0;
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String line = in.readLine();
            if (line != null) {
                String[] items = line.split(COMMA);
                int vertex = Integer.valueOf(items[0]);
                if (maxVertex < vertex) {
                    maxVertex = vertex;
                }
                processWords(vertex, items);
            }
        }

        Digraph digrh = new Digraph(maxVertex + 1);

        In in2 = new In(hypernyms);
        while (!in2.isEmpty()) {
            String line = in2.readLine();
            if (line != null) {
                String[] items = line.split(COMMA);
                int vertex = Integer.valueOf(items[0]);
                for (int i = 1; i < items.length; i++) {
                    digrh.addEdge(vertex, Integer.valueOf(items[i]));
                }
            }
        }

        if (!isDAG(digrh)) {
            throw new java.lang.IllegalArgumentException("The word net is not a DAG");
        }

        this.sap = new SAP(digrh);

    }

    private boolean isDAG(Digraph digr) {
        // TODO Auto-generated method stub
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

        int max = 0;
        for (int j : vertexReachability) {
            if (j == 1) {
                max++;
            }
        }

        if (max > 1) {
            return false;
        }

        return true;
    }

    private DFSResult dfs(Digraph digr, boolean[] marked, int vertex, boolean[] dfsm, int[] vertexReachability) {
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

        private boolean isDAG;

        private int vertexCount;

        public DFSResult(boolean isDAG, int vertexCount) {
            super();
            this.isDAG = isDAG;
            this.vertexCount = vertexCount;
        }

    }

    private void processWords(int vertex, String[] items) {
        // TODO Auto-generated method stub
        String words = items[1];
        String[] wordList = words.split(SPACE);
        for (String word : wordList) {
            if (!wordMap.containsKey(word)) {
                List<Integer> listOfVertices = new ArrayList<Integer>();
                listOfVertices.add(vertex);
                wordMap.put(word, listOfVertices);
            } else {
                List<Integer> listOfVertices = wordMap.get(word);
                listOfVertices.add(vertex);
                wordMap.put(word, listOfVertices);
            }
            indexMap.put(vertex, words);
        }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    /**
     * the set of nouns (no duplicates), returned as an Iterable
     * 
     * @return Iterable list of nouns
     */
    public Iterable<String> nouns() {
        return wordMap.keySet();
    }

    // is the word a WordNet noun?
    /**
     * is the word a WordNet noun?
     * 
     * @param word {@link String} word
     * @return true if yes else false
     */
    public boolean isNoun(String word) {
        return wordMap.containsKey(word);
    }

    // distance between nounA and nounB
    /**
     * distance between nounA and nounB
     * 
     * @param nounA {@link String} nounA
     * @param nounB {@link String} nounB
     * @return distance between the nouns
     */
    public int distance(String nounA, String nounB) {
        List<Integer> synsetA = wordMap.get(nounA);
        List<Integer> synsetB = wordMap.get(nounB);

        if (synsetA == null || synsetB == null) {
            throw new IllegalArgumentException("The nouns are not valid");
        }
        return sap.length(synsetA, synsetB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral
     * path
     * 
     * @param nounA {@link String} nounA
     * @param nounB {@link String} nounB
     * @return Common ancestor
     */
    public String sap(String nounA, String nounB) {
        List<Integer> synsetA = wordMap.get(nounA);
        List<Integer> synsetB = wordMap.get(nounB);

        if (synsetA == null || synsetB == null) {
            throw new IllegalArgumentException("The nouns are not valid");
        }

        int ancestor = sap.ancestor(synsetA, synsetB);
        return indexMap.get(ancestor);
    }

    /**
     * @param args {@link String} Args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WordNet wordnet = new WordNet(args[0], args[1]);
        System.out.println(wordnet.sap("a", "d"));

    }

}
