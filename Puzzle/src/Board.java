import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jpyla
 * 
 *         The board representation of a NxN grid which can be used for solving 8- and 15- puzzles. Also provides
 *         utility functions to calculate priority functions for the given board.
 * 
 */
public class Board {

    private char[][] boardContents;

    private int dimension;

    private int manhattan;

    private int emptyIndexI;

    private int emptyIndexJ;

    /**
     * construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j
     * 
     * @param blocks board array
     */
    public Board(int[][] blocks) { // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in
                                   // row i, column j)
        if (blocks == null || blocks[0].length != blocks.length) {
            throw new IllegalArgumentException("Invaild blocks");
        }
        this.dimension = blocks.length;
        boardContents = new char[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardContents[i][j] = (char) blocks[i][j]; // copy
            }
        }

        this.manhattan = manhattan();
    }

    /**
     * Returns the order or dimension of the grid to be solved
     * 
     * @return the dimesnion of the NxN puzzle (returns N)
     */
    public int dimension() { // board dimension N
        return dimension;
    }

    /**
     * Calculates the hamming priority value for this board
     * 
     * @return the hamming priority value (number of blocks out of place)
     */
    public int hamming() { // number of blocks out of place
        if (boardContents == null) {
            return 0;
        }
        int hamming = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int item = boardContents[i][j];
                if (item == 0) {
                    this.emptyIndexI = i;
                    this.emptyIndexJ = j;
                } else if (dimension * i + j + 1 != item) { // check the expected with the actual
                    hamming++;
                }
            }
        }
        return hamming;
    }

    /**
     * Calculates the manhattan priority value for this board
     * 
     * @return the manhattan priority value (sum of Manhattan distances between blocks and goal)
     */
    public int manhattan() { // sum of Manhattan distances between blocks and goal
        if (boardContents == null) {
            return 0;
        }
        if (manhattan != 0) {
            return manhattan;
        }
        int currentManhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int item = boardContents[i][j];
                if (item == 0) {
                    this.emptyIndexI = i;
                    this.emptyIndexJ = j;
                } else if (dimension * i + j + 1 != item) { // check the expected with the actual
                    int expectedI = (item - 1) / dimension; // from y = mx +b ( straight line with infinite
                                                            // solutions), the equation for the i and j is j=-Ni
                                                            // + (b - 1). Integral solution for this when i and j
                                                            // are bounded by N-1 is i=floor(b-1/N) and
                                                            // j=mod(b-1/N)
                    int expectedJ = (item - 1) % dimension;
                    currentManhattan = currentManhattan + Math.abs(i - expectedI) + Math.abs(j - expectedJ);
                }
            }
        }
        return currentManhattan;
    }

    /**
     * Returns true if the current board is goal board.
     * 
     * @return true if is goal board, else false
     */
    public boolean isGoal() { // is this board the goal board?
        if (this.manhattan == 0) {
            return true;
        }
        return false;
    }

    /**
     * Creates a twin of the current board my replacing the adjacent blocks of the puzzle which are not blank.
     * 
     * @return {@link Board} twin {@link Board}
     */
    public Board twin() { // a board obtained by exchanging two adjacent blocks in the same row
        int[][] twin = copyContents();

        int movableI = emptyIndexI - 1;
        if (movableI >= 0) {
            int movableJ = emptyIndexJ - 1;
            if (movableJ >= 0) {
                swap(twin, movableI, movableJ, movableI, emptyIndexJ);
            } else {
                movableJ = emptyIndexJ + 1;
                if (movableJ < dimension) {
                    swap(twin, movableI, movableJ, movableI, emptyIndexJ);
                }
            }
        } else {
            movableI = emptyIndexI + 1;
            if (movableI < dimension) {
                int movableJ = emptyIndexJ - 1;
                if (movableJ >= 0) {
                    swap(twin, movableI, movableJ, movableI, emptyIndexJ);
                } else {
                    movableJ = emptyIndexJ + 1;
                    if (movableJ < dimension) {
                        swap(twin, movableI, movableJ, movableI, emptyIndexJ);
                    }
                }
            }
        }
        return new Board(twin);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object y) { // does this board equal y?
        if (y == null) {
            return false;
        }
        if (this == y) {
            return true;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board compared = (Board) y;
        if (dimension != compared.dimension) {
            return false;
        }
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (compared.boardContents[i][j] != boardContents[i][j]) { // compare
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Iterates over the neighboring boards of the current board
     * 
     * @return {@link Iterable} of neighboring boards
     */
    public Iterable<Board> neighbors() { // all neighboring boards

        List<Board> boards = new ArrayList<Board>();

        int movable = emptyIndexI - 1; // top
        if (movable >= 0) {
            int[][] topBoardArray = copyContents();
            swap(topBoardArray, emptyIndexI, emptyIndexJ, movable, emptyIndexJ);
            Board topBoard = new Board(topBoardArray);
            boards.add(topBoard);
        }
        movable = emptyIndexI + 1;
        if (movable < dimension) { // bottom
            int[][] bottomBoardArray = copyContents();
            swap(bottomBoardArray, emptyIndexI, emptyIndexJ, movable, emptyIndexJ);
            Board bottomBoard = new Board(bottomBoardArray);
            boards.add(bottomBoard);
        }
        movable = emptyIndexJ - 1;
        if (movable >= 0) { // left
            int[][] leftBoardArray = copyContents();
            swap(leftBoardArray, emptyIndexI, emptyIndexJ, emptyIndexI, movable);
            Board leftBoard = new Board(leftBoardArray);
            boards.add(leftBoard);
        }
        movable = emptyIndexJ + 1;
        if (movable < dimension) { // right
            int[][] rightBoardArray = copyContents();
            swap(rightBoardArray, emptyIndexI, emptyIndexJ, emptyIndexI, movable);
            Board rightBoard = new Board(rightBoardArray);
            boards.add(rightBoard);
        }
        return boards;
    }

    /**
     * Copy the contests of the current Board into a new char[][]
     * 
     * @return int[][] copied
     */
    private int[][] copyContents() {
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = boardContents[i][j]; // copy
            }
        }
        return copy;
    }

    /**
     * Swap the given indexes in the given source board
     * 
     * @param sourceBoard Source board in which the items with indexes should be swapped
     * @param sourceIndexI Source index of i
     * @param sourceIndexJ Source index of j
     * @param destinationI Destination index of i
     * @param destinationJ Destination index of j
     */
    private void swap(int[][] sourceBoard, int sourceIndexI, int sourceIndexJ, int destinationI, int destinationJ) {
        // TODO Auto-generated method stub
        int sourceTemp = sourceBoard[sourceIndexI][sourceIndexJ];
        sourceBoard[sourceIndexI][sourceIndexJ] = sourceBoard[destinationI][destinationJ];
        sourceBoard[destinationI][destinationJ] = sourceTemp;

    }

    /**
     * {@inheritDoc}
     */
    public String toString() { // string representation of the board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", (int) boardContents[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
