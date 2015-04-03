import java.awt.Color;

/**
 * 
 */

/**
 * @author jpyla
 * 
 */
public class SeamCarver {

    private Picture picture;

    private PictureEnergy[][] energies;

    private int width;

    private int height;

    private Stack<Pixel> hortop;

    private Stack<Pixel> vertop;

    private static final double BORDER_ENERGY = 195075;

    /**
     * Constructor for the Seam carver
     * 
     * @param pic {@link Picture} Takes a picture and modifies the picture accordingly when a seam is removed
     */
    public SeamCarver(Picture pic) {

        modifyPic(pic);
        boolean[][] marked = new boolean[width][height];
        this.hortop = getTopologicalOrderHor(marked);

        marked = new boolean[width][height];
        this.vertop = getTopologicalOrderVer(marked);

    }

    /**
     * Returns the current picture
     * 
     * @return {@link Picture}
     */
    public Picture picture() {

        if (width == picture.width() && height == picture.height()) {
            return picture;
        }

        Picture pic = new Picture(width, height);

//        for (int i=0; i < energies.length; i++) {
//            for (int j =0; j < energies[i].length; j++) {
//                pic.set(i, j, energies[i][j].c);
//            }
//        }
        
        this.picture = pic;
        return pic;
        
    }

    /**
     * Returns the width of current picture
     * 
     * @return
     */
    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private void modifyPic(Picture pic) {

        this.picture = pic;
        this.width = picture.width();
        this.height = picture.height();

        this.energies = new PictureEnergy[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double energy = BORDER_ENERGY;
                Color piccolor = pic.get(i, j);
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
                    energy = BORDER_ENERGY;
                } else {
                    energy = findEnergy(i, j);
                }

                energies[i][j] = new PictureEnergy(piccolor, energy);
            }
        }
    }

    private double findEnergy(int x, int y) {
        if (x + 1 >= width || x - 1 < 0 || y + 1 >= height || y - 1 < 0) {
            return BORDER_ENERGY;
        }
        Color xm1 = picture.get(x - 1, y);
        Color xp1 = picture.get(x + 1, y);
        Color ym1 = picture.get(x, y - 1);
        Color yp1 = picture.get(x, y + 1);

        double xr = Math.abs(xp1.getRed() - xm1.getRed());
        double xg = Math.abs(xp1.getGreen() - xm1.getGreen());
        double xb = Math.abs(xp1.getBlue() - xm1.getBlue());

        double yr = Math.abs(yp1.getRed() - ym1.getRed());
        double yg = Math.abs(yp1.getGreen() - ym1.getGreen());
        double yb = Math.abs(yp1.getBlue() - ym1.getBlue());

        double energy = xr * xr + xg * xg + xb * xb + yr * yr + yg * yg + yb * yb;
        return energy;
    }

    public double energy(int x, int y) {
        return energies[x][y].energy;
    }

    public int[] findVerticalSeam() {

        Stopwatch w =  new Stopwatch();
        int[] finalarr = null;

        double[][] distTo = getDistArray();
        Pixel[][] edgeTo = new Pixel[width][height];

        for (int i = 0; i < width; i++) {
            distTo[i][0] = BORDER_ENERGY;
        }

        for (Pixel pl : vertop) {
            int y = pl.y + 1;
            relax(pl.x + 1, y, distTo, pl, edgeTo, false);
            relax(pl.x, y, distTo, pl, edgeTo, false);
            relax(pl.x - 1, y, distTo, pl, edgeTo, false);
        }

        double totalenergy = Double.POSITIVE_INFINITY;
        int len = distTo.length;
        int lasti = width - 1;
        for (int k = 0; k < len; k++) {
            double en = distTo[k][height - 1];
            if (en < totalenergy) {
                totalenergy = en;
                lasti = k;
            }
        }
        finalarr = prepareArrver(lasti, height - 1, edgeTo);

        System.out.println("vertical seam took : " + w.elapsedTime() + " seconds.");
        return finalarr;
    }

    public int[] findHorizontalSeam() {

        Stopwatch w = new Stopwatch();
        int[] finalarr = null;

        Pixel[][] edgeTo = new Pixel[width][height];

        double[][] distTo = getDistArray();

        for (int j = 0; j < height; j++) {
            distTo[0][j] = BORDER_ENERGY;
        }

        for (Pixel pl : hortop) {
            int x = pl.x + 1;

            relax(x, pl.y + 1, distTo, pl, edgeTo, true);
            relax(x, pl.y, distTo, pl, edgeTo, true);
            relax(x, pl.y - 1, distTo, pl, edgeTo, true);

        }
        double totalenergy = Double.POSITIVE_INFINITY;
        double[] last = distTo[width - 1];
        int lastj = height - 1;
        for (int k = 0; k < last.length; k++) {
            double en = last[k];
            if (en < totalenergy) {
                totalenergy = en;
                lastj = k;
            }
        }

        finalarr = prepareArr(width - 1, lastj, edgeTo);
        System.out.println("horizontal seam took : " + w.elapsedTime() + " seconds.");
        return finalarr;
    }

    //
    // private void setEdgeTo(double d1, double d2, double d3, int x, Pixel pl, int[] edgeTo) {
    // // TODO Auto-generated method stub
    // if (x >= width || x < 0 || pl.y >= height || pl.y - 1 < 0) {
    // return;
    // }
    // if (d1 < d2 && d1 < d3) {
    // edgeTo[x] = pl.y + 1;
    // } else if ( d2 < d1 && d2 < d3) {
    // edgeTo[x] = pl.y;
    // } else {
    // edgeTo[x] = pl.y - 1;
    // }
    // }

    private int[] prepareArr(int i, int j, Pixel[][] edgeTo) {
        // TODO Auto-generated method stub
        int[] arr = new int[width];
        arr[i] = j;
        Queue<Pixel> q = new Queue<Pixel>();
        q.enqueue(edgeTo[i][j]);

        while (!q.isEmpty()) {
            Pixel pl = q.dequeue();
            arr[pl.x] = pl.y;
            if (pl.x != 0) {
                q.enqueue(edgeTo[pl.x][pl.y]);
            }
        }

        return arr;
    }

    private double[][] getDistArray() {
        double[][] distTo = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        return distTo;
    }

    private void relax(int x, int y, double[][] distTo, Pixel pl, Pixel[][] edgeTo, boolean isHor) {
        // TODO Auto-generated method stub
        int plx = pl.x;
        int ply = pl.y;
        if (x >= width || x < 0 || y >= height || y < 0 || plx >= width || ply >= height) {
            return;
        }

        double compEner = energies[x][y].energy;

        if (distTo[x][y] > distTo[plx][ply] + compEner) {
            distTo[x][y] = distTo[plx][ply] + compEner;
            edgeTo[x][y] = pl;
        }

    }

    private Stack<Pixel> getTopologicalOrderHor(boolean[][] marked) {
        // TODO Auto-generated method stub

        Queue<Pixel> postOrder = new Queue<Pixel>();
        for (int j = 0; j < height; j++) {
            dfs(0, j, marked, postOrder);
        }

        Stack<Pixel> reverse = new Stack<Pixel>();
        for (Pixel v : postOrder)
            reverse.push(v);
        return reverse;

    }

    private Stack<Pixel> getTopologicalOrderVer(boolean[][] marked) {
        // TODO Auto-generated method stub

        Queue<Pixel> postOrder = new Queue<Pixel>();
        for (int i = 0; i < width; i++) {
            dfsver(i, 0, marked, postOrder);
        }

        Stack<Pixel> reverse = new Stack<Pixel>();
        for (Pixel v : postOrder)
            reverse.push(v);
        return reverse;

    }

    private void dfs(int i, int j, boolean[][] marked, Queue<Pixel> postOrder) {
        // TODO Auto-generated method stub
        marked[i][j] = true;
        if (i + 1 < width && j + 1 < height && !marked[i + 1][j + 1]) {
            dfs(i + 1, j + 1, marked, postOrder);
        }
        if (i + 1 < width && j < height && !marked[i + 1][j]) {
            dfs(i + 1, j, marked, postOrder);
        }
        if (i + 1 < width && j - 1 >= 0 && !marked[i + 1][j - 1]) {
            dfs(i + 1, j - 1, marked, postOrder);
        }

        postOrder.enqueue(new Pixel(i, j));

    }

    private void dfsver(int i, int j, boolean[][] marked, Queue<Pixel> postOrder) {
        // TODO Auto-generated method stub
        marked[i][j] = true;
        if (i + 1 < width && j + 1 < height && !marked[i + 1][j + 1]) {
            dfsver(i + 1, j + 1, marked, postOrder);
        }
        if (i < width && j + 1 < height && !marked[i][j + 1]) {
            dfsver(i, j + 1, marked, postOrder);
        }
        if (j + 1 < height && i - 1 >= 0 && !marked[i - 1][j + 1]) {
            dfsver(i - 1, j + 1, marked, postOrder);
        }

        postOrder.enqueue(new Pixel(i, j));

    }

    private int[] prepareArrver(int i, int j, Pixel[][] edgeTo) {
        // TODO Auto-generated method stub
        int[] arr = new int[height];
        arr[j] = i;
        Queue<Pixel> q = new Queue<Pixel>();
        q.enqueue(edgeTo[i][j]);

        while (!q.isEmpty()) {
            Pixel pl = q.dequeue();
            arr[pl.y] = pl.x;
            if (pl.y != 0) {
                q.enqueue(edgeTo[pl.x][pl.y]);
            }
        }

        return arr;
    }

    public void removeHorizontalSeam(int[] a) {

        
        System.out.println("Horizontal started....");
        long t = System.currentTimeMillis();
        for (int i = 0; i < a.length; i++) {

            PictureEnergy[] column = energies[i];
            PictureEnergy[] copy = new PictureEnergy[column.length - 1];

            int k = a[i];
            System.arraycopy(column, 0, copy, 0, k);
            System.arraycopy(column, k + 1, copy, k, height - k - 1);

            energies[i] = copy;

        }

        height = height - 1;
        System.out.println("took..: " + (double) (System.currentTimeMillis() - t)/1000);
    }

    public void removeVerticalSeam(int[] a) {

        System.out.println("Vertical started....");
        long t = System.currentTimeMillis();
        PictureEnergy[][] arr = new PictureEnergy[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arr[j][i] = energies[i][j];
            }
        }

        for (int i = 0; i < a.length; i++) {

            PictureEnergy[] column = arr[i];
            PictureEnergy[] copy = new PictureEnergy[column.length - 1];

            int k = a[i];
            System.arraycopy(column, 0, copy, 0, k);
            System.arraycopy(column, k + 1, copy, k, width - k - 1);

            arr[i] = copy;

        }

        PictureEnergy[][] copy = new PictureEnergy[arr[0].length][arr.length];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                copy[j][i] = arr[i][j];
            }
        }

        energies = copy;

        width = width - 1;

        System.out.println("Vertical took..: " + (double) (System.currentTimeMillis() - t)/1000);
    }

    private static final class Pixel {
        int x;

        int y;

        public Pixel(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return x + "," + y;
        }

    }

    private static final class PictureEnergy {

        Color c;

        double energy;

        public PictureEnergy(Color c, double energy) {
            super();
            this.c = c;
            this.energy = energy;
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
