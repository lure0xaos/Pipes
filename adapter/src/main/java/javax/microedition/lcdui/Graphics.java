package javax.microedition.lcdui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.HashMap;
import java.util.Map;

public final class Graphics {
    public static final int BASELINE = 64;
    public static final int BOTTOM = 32;
    public static final int HCENTER = 1;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int VCENTER = 2;
    private static final Map<java.awt.Graphics, Graphics> peers = new HashMap<>();
    private final java.awt.Graphics peer;
    private Font font = Font.getDefaultFont();

    private Graphics(java.awt.Graphics graphics) {
        peer = graphics;
    }

    static Graphics forGraphics(java.awt.Graphics graphics) {
        if (peers.containsKey(graphics)) {
            return peers.get(graphics);
        }
        Graphics peerGraphics = new Graphics(graphics);
        peers.put(graphics, peerGraphics);
        return peerGraphics;
    }

    public void drawImage(Image image, int x, int y, int anchor) {
        int xx = x;
        if ((anchor & LEFT) != 0) {
            xx = x;
        }
        if ((anchor & HCENTER) != 0) {
            xx = x - image.getWidth() / 2;
        }
        if ((anchor & RIGHT) != 0) {
            xx = x - image.getWidth() / 2;
        }
        int yy = y;
        if ((anchor & TOP) != 0) {
            yy = y;
        }
        if ((anchor & VCENTER) != 0) {
            yy = y - image.getHeight() / 2;
        }
        if ((anchor & BOTTOM) != 0) {
            yy = y - image.getHeight();
        }
        peer.drawImage(image.getPeer(), xx, yy, null);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        peer.drawLine(x1, y1, x2, y2);
    }

    public void drawRect(int x, int y, int width, int height) {
        peer.drawRect(x, y, width, height);
    }

    public void drawString(String str, int x, int y, int anchor) {
        FontMetrics fm = peer.getFontMetrics();
        int xx = x;
        if ((anchor & LEFT) != 0) {
            xx = x;
        }
        if ((anchor & HCENTER) != 0) {
            xx = x - font.stringWidth(fm, str) / 2;
        }
        if ((anchor & RIGHT) != 0) {
            xx = x - font.stringWidth(fm, str);
        }
        int yy = y;
        if ((anchor & TOP) != 0) {
            yy = y;
        }
        if ((anchor & BASELINE) != 0) {
            yy = y - font.getBaselinePosition(fm);
        }
        if ((anchor & BOTTOM) != 0) {
            yy = y - font.getHeight(fm);
        }
        if ((anchor & VCENTER) != 0) {
            yy = y - font.getHeight(fm) / 2;
        }
        peer.drawString(str, xx, yy);
    }

    public void fillArc(int x, int y, int width, int height, int arcX, int arcY) {
        peer.fillArc(x, y, width, height, arcX, arcY);
    }

    public void fillRect(int x, int y, int width, int height) {
        peer.fillRect(x, y, width, height);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(int color) {
        peer.setColor(new Color(color));
    }

    public void translate(int dX, int dY) {
        peer.translate(dX, dY);
    }
}
