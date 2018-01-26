package net.michaelkerley;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

@SuppressWarnings("MethodMayBeStatic")
public class PipePainter {

    private static final int ARC = 360;
    private static final int CONNECTED_COLOR = 0x00ff00;
    private static final int DIM_CONNECTED_COLOR = 0x006600;
    private static final int DIM_DISCONNECTED_COLOR = 0x660000;
    private static final int DISCONNECTED_COLOR = 0xff0000;
    private static final int COLOR_BLACK = 0x000000;
    private static final Font FONT_GAME_OVER = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD | Font.STYLE_ITALIC, Font.SIZE_LARGE);
    private static final int COLOR_GAME_OVER_SHADOW = 0x666666;
    private static final int COLOR_GAME_OVER = 0xff0000;
    private static final int COLOR_WHITE = 0xffffff;
    private static final Font LARGE_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private static final Font MEDIUM_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private static final Font SMALL_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private static final String LOCATION_YOU_WIN_LARGE = "/you_win_large.png";
    private static final String LOCATION_YOU_WIN_MEDIUM = "/you_win_medium.png";
    private static final String LOCATION_YOU_WIN_SMALL = "/you_win_small.png";
    private static final int RESIZE_TEXT_COLOR = 0xffffff;
    private static final int RESIZE_GRIDLINE_COLOR = RESIZE_TEXT_COLOR;
    private static final int DIM_GRIDLINE_COLOR = 0x323232;
    private static final int GRIDLINE_COLOR = 0x646464;
    private static final int CURSOR_COLOR = 0xffff00;
    private final @Nullable Image imgYouWin;

    public PipePainter(int width, int height) {
        imgYouWin = PipeImages.loadImage(new String[]{LOCATION_YOU_WIN_LARGE, LOCATION_YOU_WIN_MEDIUM, LOCATION_YOU_WIN_SMALL}, width, height);
    }

    private void drawPipe(Pipe pipe, @NotNull Graphics g, boolean bright, int size) {
        int color = pipe.isInConnectedSet() ? (bright ? CONNECTED_COLOR : DIM_CONNECTED_COLOR) : (bright ? DISCONNECTED_COLOR : DIM_DISCONNECTED_COLOR);
        int pipeX = pipe.getX() * (size + 1) + 1;
        int pipeY = pipe.getY() * (size + 1) + 1;
        if (bright && PipeImages.imagesLoaded) {
            if (pipe.isInConnectedSet()) {
                g.drawImage(PipeImages.connectedImages[pipe.getConnections()], pipeX, pipeY, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawImage(PipeImages.disconnectedImages[pipe.getConnections()], pipeX, pipeY, Graphics.TOP | Graphics.LEFT);
            }
            return;
        }
        g.setColor(COLOR_BLACK);
        g.fillRect(pipeX, pipeY, size, size);
        g.setColor(color);
        int size_3 = size / 3;
        g.fillArc(pipeX + size_3, pipeY + size_3, size_3, size_3, 0, ARC); // center ball
        int size_2 = size / 2;
        if (pipe.isConnected(PipeConn.CONN_UP)) {
            g.fillRect(pipeX + size_3, pipeY, size_3, size_2);
        }
        if (pipe.isConnected(PipeConn.CONN_DOWN)) {
            g.fillRect(pipeX + size_3, pipeY + size_2, size_3, size_2 + 1);
        }
        if (pipe.isConnected(PipeConn.CONN_RIGHT)) {
            g.fillRect(pipeX + size_2, pipeY + size_3, size_2 + 1, size_3);
        }
        if (pipe.isConnected(PipeConn.CONN_LEFT)) {
            g.fillRect(pipeX, pipeY + size_3, size_2, size_3);
        }
    }


    private void paintYouWin(@NotNull Graphics offscreen, int width, int height) {
        if (imgYouWin != null) {
            offscreen.drawImage(imgYouWin, width / 2, height / 2, Graphics.HCENTER | Graphics.VCENTER);
        } else {
            int x = (width - FONT_GAME_OVER.stringWidth(PipeMessages.MESSAGE_GAME_OVER)) / 2;
            int y = (height - FONT_GAME_OVER.getHeight()) / 2;
            offscreen.setFont(FONT_GAME_OVER);
            offscreen.setColor(COLOR_GAME_OVER_SHADOW);
            offscreen.drawString(PipeMessages.MESSAGE_GAME_OVER, x + 1, y + 1, Graphics.TOP | Graphics.LEFT);
            offscreen.setColor(COLOR_GAME_OVER);
            offscreen.drawString(PipeMessages.MESSAGE_GAME_OVER, x - 1, y - 1, Graphics.TOP | Graphics.LEFT);
        }
    }

    private void paintAbout(Graphics g, int width, String version) {
        g.setColor(COLOR_WHITE);
        int y = 0;
        for (int i = 0; i < PipeMessages.ABOUT_TEXT.length; ++i) {
            String line = i == 0 ? String.format(PipeMessages.MESSAGE_VERSION, version) : PipeMessages.ABOUT_TEXT[i];
            if (LARGE_FONT.stringWidth(line) < width) {
                g.setFont(LARGE_FONT);
            } else if (MEDIUM_FONT.stringWidth(line) < width) {
                g.setFont(MEDIUM_FONT);
            } else {
                g.setFont(SMALL_FONT);
            }
            g.drawString(line, width / 2, y, Graphics.TOP | Graphics.HCENTER);
            y += g.getFont().getHeight();
        }
    }

    private void paintPipes(Graphics g, int size, int width, int height, int cursorX, int cursorY, PipeMode mode, PipesInfo info) {
        int cols = info.getCols();
        int rows = info.getRows();
        g.setColor(COLOR_BLACK);
        g.fillRect(0, 0, width, height);
        int gridWidth = cols * (size + 1) + 1;
        int gridHeight = rows * (size + 1) + 1;
        int xOffset;
        if (gridWidth <= width) {
            xOffset = (width - gridWidth) / 2;
        } else {
            int cursorLeft = cursorX * (size + 1);
            xOffset = -(cursorLeft + size / 2 - width / 2);
            xOffset = Math.min(xOffset, 0);
            xOffset = Math.max(xOffset, width - gridWidth);
        }
        int yOffset;
        if (gridHeight <= height) {
            yOffset = (height - gridHeight) / 2;
        } else {
            int cursorTop = cursorY * (size + 1);
            yOffset = -(cursorTop + size / 2 - height / 2);
            yOffset = Math.min(yOffset, 0);
            yOffset = Math.max(yOffset, height - gridHeight);
        }
        g.translate(xOffset, yOffset);
        if (mode == PipeMode.MODE_RESIZE) {
            g.setColor(RESIZE_GRIDLINE_COLOR);
        } else {
            for (int x = 0; x < cols; ++x) {
                for (int y = 0; y < rows; ++y) {
                    drawPipe(info.getPipe(x, y), g, mode != PipeMode.MODE_ABOUT, size);
                }
            }
            g.setColor(mode == PipeMode.MODE_ABOUT ? DIM_GRIDLINE_COLOR : GRIDLINE_COLOR);
        }
        for (int i = 0; i <= rows * (size + 1); i += size + 1) {
            g.drawLine(0, i, cols * (size + 1), i);
        }
        for (int i = 0; i <= cols * (size + 1); i += size + 1) {
            g.drawLine(i, 0, i, rows * (size + 1));
        }
        if (mode == PipeMode.MODE_GAME) {
            g.setColor(CURSOR_COLOR);
            g.drawRect(
                    cursorX * (size + 1),
                    cursorY * (size + 1),
                    size + 1,
                    size + 1);
        }
        g.translate(-xOffset, -yOffset);
        if (mode == PipeMode.MODE_RESIZE) {
            String msg = cols + " x " + rows;
            Font font = Font.getDefaultFont();
            g.setFont(font);
            int msgWidth = font.stringWidth(msg);
            int msgHeight = font.getHeight();
            int msgXOffset = (width - msgWidth) / 2;
            int msgYOffset = (height - msgHeight) / 2;
            g.setColor(COLOR_BLACK);
            g.fillRect(msgXOffset - 2, msgYOffset - 2, msgWidth + 4, msgHeight + 4);
            g.setColor(RESIZE_TEXT_COLOR);
            g.drawString(msg, msgXOffset, msgYOffset, Graphics.LEFT | Graphics.TOP);
        }
    }


    void paintOffScreen(@NotNull Graphics offscreen, PipesInfo info, int size, int width, int height, int cursorX, int cursorY, PipeMode mode, String version) {
        if (info != null) {
            paintPipes(offscreen, size, width, height, cursorX, cursorY, mode, info);
        }
        if (mode == PipeMode.MODE_ABOUT) {
            paintAbout(offscreen, width, version);
        } else if (mode == PipeMode.MODE_YOU_WIN) {
            paintYouWin(offscreen, width, height);
        }
    }

}
