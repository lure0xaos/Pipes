package net.michaelkerley;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

final class PipesGame {
    private static final int COLS = 8;
    private static final int ROWS = 8;
    private static final int AUTO_CLOSE = 2000;
    private static final int ROTATIONS = 20;
    private final Command commandAbout;
    private final Command commandHelp;
    private final Command commandOk;
    private final Command commandQuit;
    private final Command commandReset;
    private final Command commandResize;
    private final Command commandRotate;
    private final String version;
    private final Stack<Pipe> connectedPipes = new Stack<>();
    private final Stack<Pipe> toBeChecked = new Stack<>();
    private final PipePainter painter;
    private final PipeStore store;
    private final PipesInfo info = new PipesInfo(COLS, ROWS);
    private final PipesCanvas canvas;
    private int cursorX;
    private int cursorY;
    private PipeMode mode = PipeMode.MODE_GAME;

    public PipesGame(PipesCanvas canvas, String version) {
        this.canvas = canvas;
        this.version = version;
        commandRotate = new Command(PipeMessages.MESSAGE_ROTATE, Command.SCREEN, 1);
        commandReset = new Command(PipeMessages.MESSAGE_RESET, Command.SCREEN, 2);
        commandResize = new Command(PipeMessages.MESSAGE_RESIZE, Command.SCREEN, 3);
        commandHelp = new Command(PipeMessages.MESSAGE_HELP, Command.SCREEN, 4);
        commandAbout = new Command(PipeMessages.MESSAGE_ABOUT, Command.SCREEN, 5);
        commandQuit = new Command(PipeMessages.MESSAGE_QUIT, Command.SCREEN, 6);
        commandOk = new Command(PipeMessages.MESSAGE_OK, Command.OK, 1);
        painter = new PipePainter(canvas.getWidth(), canvas.getHeight());
        store = new PipeStore(this);
    }

    private void assertValidCursor() {
        if (cursorX < 0) {
            cursorX = 0;
        }
        int cols = info.getCols();
        if (cursorX >= cols) {
            cursorX = cols - 1;
        }
        if (cursorY < 0) {
            cursorY = 0;
        }
        int rows = info.getRows();
        if (cursorY >= rows) {
            cursorY = rows - 1;
        }
    }

    private void buildPipes() {
        initPipes();
        int cols = info.getCols();
        int rows = info.getRows();
        List<Pipe> connected = new ArrayList<>(rows * cols);
        connected.add(info.getPipe(Rnd.rnd(cols), Rnd.rnd(rows)));
        while (connected.size() < rows * cols) {
            Pipe pipe = connected.get(Rnd.rnd(connected.size()));
            int direction = 1 << Rnd.rnd(4);
            switch (direction) {
                case PipeConn.CONN_UP:
                    setConnectedIf(connected, pipe, getPipe(pipe.getX(), pipe.getY() - 1), PipeConn.CONN_DOWN, direction);
                    break;
                case PipeConn.CONN_DOWN:
                    setConnectedIf(connected, pipe, getPipe(pipe.getX(), pipe.getY() + 1), PipeConn.CONN_UP, direction);
                    break;
                case PipeConn.CONN_LEFT:
                    setConnectedIf(connected, pipe, getPipe(pipe.getX() - 1, pipe.getY()), PipeConn.CONN_RIGHT, direction);
                    break;
                case PipeConn.CONN_RIGHT:
                    setConnectedIf(connected, pipe, getPipe(pipe.getX() + 1, pipe.getY()), PipeConn.CONN_LEFT, direction);
                    break;
            }
        }
    }

    private void setConnectedIf(Collection<Pipe> connected, Pipe pipe, Pipe pipe2, int reverseDirection, int direction) {
        if (pipe2 != null && pipe2.getConnections() == 0) {
            pipe.setConnected(direction, true);
            pipe2.setConnected(reverseDirection, true);
            connected.add(pipe2);
        }
    }

    public void checkConnections() {
        while (!connectedPipes.isEmpty()) {
            connectedPipes.pop().setInConnectedSet(false);
        }
        int cols = info.getCols();
        int rows = info.getRows();
        Pipe pipe = info.getPipe(cols / 2, rows / 2);
        setConnection(pipe);
        while (!toBeChecked.isEmpty()) {
            pipe = toBeChecked.pop();
            int x = pipe.getX();
            int y = pipe.getY();
            if (pipe.isConnected(PipeConn.CONN_UP)) {
                checkConnection(getPipe(x, y - 1), PipeConn.CONN_DOWN);
            }
            if (pipe.isConnected(PipeConn.CONN_DOWN)) {
                checkConnection(getPipe(x, y + 1), PipeConn.CONN_UP);
            }
            if (pipe.isConnected(PipeConn.CONN_LEFT)) {
                checkConnection(getPipe(x - 1, y), PipeConn.CONN_RIGHT);
            }
            if (pipe.isConnected(PipeConn.CONN_RIGHT)) {
                checkConnection(getPipe(x + 1, y), PipeConn.CONN_LEFT);
            }
        }
        if (connectedPipes.size() == (rows * cols)) {
            setMode(PipeMode.MODE_YOU_WIN, true);
        }
    }

    private void setConnection(Pipe pipe) {
        connectedPipes.add(pipe);
        toBeChecked.add(pipe);
        pipe.setInConnectedSet(true);
    }

    private void checkConnection(Pipe pipe2, int conn) {
        if (pipe2 != null && pipe2.isConnected(conn) && pipe2.isInUnconnectedSet()) {
            setConnection(pipe2);
        }
    }

    void onAction(Command command) {
        if (command == commandRotate) {
            rotate(true);
        } else if (command == commandReset) {
            init();
        } else if (command == commandQuit) {
            canvas.quit();
        } else if (command == commandResize) {
            setMode(PipeMode.MODE_RESIZE, true);
        } else if (command == commandOk) {
            setMode(PipeMode.MODE_GAME, true);
        } else if (command == commandAbout) {
            setMode(PipeMode.MODE_ABOUT, true);
        } else if (command == commandHelp) {
            canvas.help(PipeMessages.HELP_ALERT_TITLE, PipeMessages.HELP_ALERT_TEXT);
        }
    }

    private void rotate(boolean clockwise) {
        info.getPipe(cursorX, cursorY).rotate(clockwise);
        checkConnections();
    }

    private void setMode(PipeMode mode, boolean repaint) {
        PipeMode oldMode = this.mode;
        this.mode = mode;
        canvas.commandsRemove(commandOk, commandRotate, commandReset, commandResize, commandQuit, commandAbout, commandHelp);
        switch (mode) {
            case MODE_GAME:
                canvas.commandsAdd(commandRotate, commandReset, commandResize, commandHelp, commandAbout, commandQuit);
                startGame(oldMode);
                break;
            case MODE_YOU_WIN:
                youWin();
                break;
            case MODE_GAME_OVER:
                break;
            case MODE_RESIZE:
                initPipeSize();
                canvas.commandsAdd(commandOk);
                break;
            case MODE_ABOUT:
                canvas.commandsAdd(commandOk);
                break;
        }
        if (repaint) {
            repaint();
        }
    }

    private void startGame(PipeMode oldMode) {
        assertValidCursor();
        PipeImages.loadBitmaps();
        if (oldMode != PipeMode.MODE_ABOUT && oldMode != PipeMode.MODE_GAME) {
            init();
        }
        checkConnections();
    }

    private void youWin() {
        initPipeSize();
        PipeImages.loadBitmaps();
        canvas.schedule(this::closeYouWin, AUTO_CLOSE);
    }

    public void initPipes() {
        info.init();
        connectedPipes.clear();
        toBeChecked.clear();
    }

    private Pipe getPipe(int x, int y) {
        return x >= 0 && x < info.getCols() && y >= 0 && y < info.getRows() ? info.getPipe(x, y) : null;
    }

    public void init() {
        initPipeSize();
        buildPipes();
        repaint();
        scramblePipes();
        checkConnections();
        if (mode != PipeMode.MODE_GAME) {
            setMode(PipeMode.MODE_GAME, false);
        }
    }

    void repaint() {
        canvas.repaint();
    }

    public void initPipeSize() {
        int width = getWidth();
        int height = getHeight();
        int sizeX = (width - 1) / info.getCols();
        int sizeY = (height - 1) / info.getRows();
        while (sizeX % 3 != 1) {
            --sizeX;
        }
        while (sizeY % 3 != 1) {
            --sizeY;
        }
        int size = Math.min(sizeX, sizeY);
        PipeImages.setSize(size - 1);
    }

    private int getWidth() {
        return canvas.getWidth();
    }

    int getHeight() {
        return canvas.getHeight();
    }

    private void keyPressedGameOver() {
        onAction(commandReset);
    }

    public void onKeyPress(int keyCode, int gameAction) {
        switch (mode) {
            case MODE_GAME:
                keyPressedGame(keyCode, gameAction);
                break;
            case MODE_YOU_WIN:
                keyPressedGameOver();
                break;
            case MODE_GAME_OVER:
                keyPressedGameOver();
                break;
            case MODE_RESIZE:
                keyPressedResize(gameAction, PipeImages.getSize());
                break;
            case MODE_ABOUT:
                setMode(PipeMode.MODE_GAME, true);
                break;
        }
    }

    private void keyPressedGame(int keyCode, int gameAction) {
        int cols = info.getCols();
        int rows = info.getRows();
        switch (gameAction) {
            case Canvas.UP:
                --cursorY;
                if (cursorY < 0) {
                    cursorY = rows - 1;
                }
                repaint();
                break;
            case Canvas.LEFT:
                --cursorX;
                if (cursorX < 0) {
                    cursorX = cols - 1;
                }
                repaint();
                break;
            case Canvas.DOWN:
                ++cursorY;
                if (cursorY >= rows) {
                    cursorY = 0;
                }
                repaint();
                break;
            case Canvas.RIGHT:
                ++cursorX;
                if (cursorX >= cols) {
                    cursorX = 0;
                }
                repaint();
                break;
            case Canvas.FIRE:
                rotate(true);
                repaint();
                break;
            default:
                //noinspection NestedSwitchStatement
                switch (keyCode) {
                    case Canvas.KEY_NUM1:
                        rotate(false);
                        repaint();
                        break;
                    case Canvas.KEY_NUM3:
                        rotate(true);
                        repaint();
                        break;
                    case Canvas.KEY_STAR:
                        PipeImages.zoom(false);
                        repaint();
                        break;
                    case Canvas.KEY_POUND:
                        PipeImages.zoom(true);
                        repaint();
                        break;
                }
        }
    }

    private void keyPressedResize(int gameAction, int size) {
        boolean smallSize = size < 3;
        int cols = info.getCols();
        int rows = info.getRows();
        switch (gameAction) {
            case Canvas.UP:
                if (rows > 2) {
                    info.setRows(rows - 1);
                    initPipeSize();
                }
                repaint();
                break;
            case Canvas.LEFT:
                if (cols > 2) {
                    info.setCols(cols - 1);
                    initPipeSize();
                }
                repaint();
                break;
            case Canvas.DOWN:
                info.setRows(rows + 1);
                initPipeSize();
                if (smallSize) {
                    info.setRows(rows - 1);
                    initPipeSize();
                }
                repaint();
                break;
            case Canvas.RIGHT:
                info.setCols(cols + 1);
                initPipeSize();
                if (smallSize) {
                    info.setCols(cols - 1);
                    initPipeSize();
                }
                repaint();
                break;
            case Canvas.FIRE:
                onAction(commandOk);
                break;
        }
    }

    public boolean load() {
        return store.load(info, COLS, ROWS);
    }

    protected void paint(Graphics offscreen, int width, int height) {
        painter.paintOffScreen(offscreen, info, PipeImages.getSize(), width, height, cursorX, cursorY, mode, version);
    }

    public boolean setConnections(byte[] pipesByte, int cols, int rows) {
        boolean success = true;
        int i = 0;
        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < cols; ++x) {
                info.getPipe(x, y).setConnections(pipesByte[i]);
                i++;
                if (info.getPipe(x, y).getConnections() == 0) {
                    success = false;
                }
            }
        }
        return success;
    }

    public void save() {
        store.save(info);
    }

    private void scramblePipes() {
        for (int x = 0; x < info.getCols(); ++x) {
            for (int y = 0; y < info.getRows(); ++y) {
                Pipe pipe = info.getPipe(x, y);
                int rotations = Rnd.rnd(ROTATIONS);
                for (int i = 0; i < rotations; ++i) {
                    pipe.rotate(true);
                }
            }
        }
    }

    public void showAbout() {
        setMode(PipeMode.MODE_ABOUT, true);
        canvas.schedule(this::closeAbout, AUTO_CLOSE);
    }

    private void closeAbout() {
        if (isMode(PipeMode.MODE_ABOUT)) {
            setMode(PipeMode.MODE_GAME, true);
        }
    }

    private boolean isMode(PipeMode mode) {
        return this.mode == mode;
    }

    private void closeYouWin() {
        if (isMode(PipeMode.MODE_YOU_WIN)) {
            setMode(PipeMode.MODE_GAME_OVER, true);
        }
    }

}
