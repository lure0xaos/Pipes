package net.michaelkerley;

public final class PipesInfo {
    private int cols;
    private int rows;
    private Pipe[][] pipes;


    public PipesInfo(int cols, int rows) {
        init(cols, rows);
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
        init();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        init();
    }

    private Pipe[][] getPipes() {
        return pipes;
    }

    public void init() {
        init(cols, rows);
    }

    public void init(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        pipes = new Pipe[cols][rows];
        for (int x = 0; x < cols; ++x) {
            for (int y = 0; y < rows; ++y) {
                //noinspection ObjectAllocationInLoop
                pipes[x][y] = new Pipe(x, y);
            }
        }
    }


    public Pipe getPipe(int x, int y) {
        return pipes[x][y];
    }

    public boolean noPipes() {
        return pipes == null;
    }
}
