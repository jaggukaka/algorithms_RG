/**
 * 
 */

/**
 * Computes the outcast from a given list of strings and {@link WordNet}
 * 
 * @author jpyla
 * 
 */
public class Outcast {

    private WordNet wordNet;

    // constructor takes a WordNet object
    /**
     * Constructor takes a WordNet object
     * 
     * @param wordnet - {@link WordNet}
     */
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    /**
     * given an array of WordNet nouns, return an outcast
     * 
     * @param nouns {@link String}[] List of nouns
     * @return {@link String} the outcast noun
     */
    public String outcast(String[] nouns) {
        int outcastDist = 0;
        String outcast = null;
        int len = nouns.length;
        int[][] computed = new int[len][len];
        for (int i = 0; i < nouns.length; i++) {
            String noun = nouns[i];
            int totaldist = 0;
            for (int j = 0; j < nouns.length; j++) {
                String noun2 = nouns[j];
                if (noun.equalsIgnoreCase(noun2)) {
                    continue;
                }
                if (wordNet.isNoun(noun) && wordNet.isNoun(noun2)) {
                    int computedDist = computed[i][j];
                    if (computedDist == 0) {
                        int dist = wordNet.distance(noun, noun2);
                        computed[i][j] = dist;
                        computed[j][i] = dist;
                        totaldist = totaldist + dist;
                    } else {
                        totaldist = totaldist + computedDist;
                    }
                }
            }

            if (totaldist > outcastDist) {
                outcastDist = totaldist;
                outcast = noun;
            }
        }
        return outcast;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = (new In(args[t])).readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
