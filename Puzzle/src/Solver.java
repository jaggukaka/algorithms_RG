import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Solves a NxN grid puzzle which are similar to 8- and 15- puzzles.
 * 
 * @author jpyla
 * 
 */
public class Solver {

    private boolean isSolvable;

    private SearchNode solution;

    private int finalMoves = -1;

    private volatile boolean terminate = false;

    private BlockingQueue<SolutionSet> blockingQueue = new ArrayBlockingQueue<SolutionSet>(2);

    /**
     * A Node holding the current board, it's moves and the previous Search node in the flow.
     * 
     * @author jpyla
     * 
     */
    private final class SearchNode {
        private Board board;

        private int moves;

        private SearchNode previousNode;

        /**
         * Constructor for the Search Node
         * 
         * @param board {@link Board} in this flow for this node
         * @param moves number of moves by which this board is arrived from initial board
         * @param previousNode previous {@link SearchNode}
         */
        public SearchNode(Board board, int moves, SearchNode previousNode) {
            super();
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
        }

    }

    /**
     * Comparator for comparing the {@link SearchNode}s
     * 
     * @author jpyla
     * 
     */
    private static final class SearchNodePriorityComparator implements Comparator<SearchNode> {

        private SearchNodePriorityComparator() {
            // TODO Auto-generated constructor stub
            // deliberate
        }

        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            int o1Priority = o1.board.manhattan() + o1.moves;
            int o2Priority = o2.board.manhattan() + o2.moves;
            if (o1Priority < o2Priority) {
                return -1;
            } else if (o1Priority == o2Priority) {
                return 0;
            } else {
                return +1;
            }
        }

        private static final class SearchNodePriorityComparatorHolder {
            private static final SearchNodePriorityComparator SEARCH_NODE_PRIORITY_COMPARATOR = new SearchNodePriorityComparator();
        }

        /**
         * Returns a singleton instance of the {@link SearchNodePriorityComparator} lazily
         * 
         * @return {@link SearchNodePriorityComparator}
         */
        public static SearchNodePriorityComparator getInstance() {
            return SearchNodePriorityComparatorHolder.SEARCH_NODE_PRIORITY_COMPARATOR;
        }

    }

    /**
     * Constructor for the solver which eventually solves the problem board
     * 
     * @param initial initial {@link Board}
     */
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new IllegalArgumentException("Invaild Board");
        }
        Runnable actual = new Solve(initial, false);
        Runnable twin = new Solve(initial.twin(), true);

        Thread actualThread = new Thread(actual);
        Thread twinThread = new Thread(twin);

        actualThread.start();
        twinThread.start();

        try {
            SolutionSet soSet = blockingQueue.take();
            terminate = true;
            if (soSet.isTwin) {
                this.isSolvable = false;
            } else {
                this.isSolvable = soSet.solvable;
                this.finalMoves = soSet.totalmoves;
                this.solution = soSet.finalsolution;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Concurrent solver which solves for twin and the actual parallely
     * 
     * @author jpyla
     * 
     */
    private final class Solve implements Runnable {

        private final Board initialBoard;

        private final boolean twin;

        /**
         * Constructor for the Solve
         * 
         * @param initial Initial or Twin {@link Board}
         * @param isTwin {@link Boolean} representing if it is twin
         */
        public Solve(Board initial, boolean isTwin) {
            // TODO Auto-generated constructor stub
            this.initialBoard = initial;
            this.twin = isTwin;
        }

        /**
         * executes the A* algorithm and puts the result if the solution is hit in a blocking queue
         */
        @Override
        public void run() {
            SearchNode nodeInitial = new SearchNode(initialBoard, 0, null);
            MinPQ<SearchNode> pq = new MinPQ<Solver.SearchNode>(SearchNodePriorityComparator.getInstance());
            pq.insert(nodeInitial);

            while (!pq.isEmpty() && !terminate) {
                SearchNode currentNode = pq.delMin();
                if (currentNode.board.manhattan() == 0) {
                    try {
                        blockingQueue.put(new SolutionSet(currentNode, currentNode.moves, true, twin));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                if (terminate) {
                    break;
                }
                prepareNieghbours(currentNode, pq, currentNode.moves + 1);
            }
        }

    }

    /**
     * Utility pojo for the solutiong returned by the A* algorithm
     * 
     * @author jpyla
     * 
     */
    private final class SolutionSet {
        private SearchNode finalsolution;

        private int totalmoves;

        private boolean solvable;

        private boolean isTwin;

        /**
         * Constructor for the {@link SolutionSet}
         * 
         * @param finalsolution Final solution {@link SearchNode}
         * @param totalmoves total number of moves from the root node for the solution
         * @param solvable if the solution is solvable
         * @param isTwin if the solved solution is twin
         */
        public SolutionSet(SearchNode finalsolution, int totalmoves, boolean solvable, boolean isTwin) {
            super();
            this.finalsolution = finalsolution;
            this.totalmoves = totalmoves;
            this.solvable = solvable;
            this.isTwin = isTwin;
        }

    }

    private void prepareNieghbours(SearchNode currentNode, MinPQ<SearchNode> pq, int moves) {
        // TODO Auto-generated method stub
        Board currentBoard = currentNode.board;
        for (Board neighbour : currentBoard.neighbors()) {
            if (terminate) {
                break;
            }
            if (currentNode.previousNode != null && currentNode.previousNode.board.equals(neighbour)) { // critical
                // optimization
                continue;
            }
            SearchNode newNeighbourNode = new SearchNode(neighbour, moves, currentNode);
            pq.insert(newNeighbourNode);
        }
    }

    /**
     * Returns if the puzzle with initial board is solvable
     * 
     * @return true if solvable
     */
    public boolean isSolvable() {
        // is the initial board solvable?
        return this.isSolvable;
    }

    /**
     * Returns total number of moves from the initial board to the solution board
     * 
     * @return moves
     */
    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        return this.finalMoves;
    }

    /**
     * Returns {@link Iterable} to iterate over the solution
     * 
     * @return {@link Iterable}
     */
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if no solution
        if (solution == null) {
            return null;
        }
        Stack<Board> solutionBoards = new Stack<Board>();
        SearchNode mySolNode = solution;
        while (mySolNode != null) {
            solutionBoards.push(mySolNode.board);
            mySolNode = mySolNode.previousNode;
        }
        return solutionBoards;
    }

    /**
     * Main function for testing
     * 
     * @param args filename
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }
}
