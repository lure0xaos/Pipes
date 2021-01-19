package javax.microedition.lcdui;


import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

public final class Font {
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int FACE_SYSTEM = 0;
    public static final int SIZE_LARGE = 16;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_SMALL = 8;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_UNDERLINED = 4;
    public static final int FONT_SIZE_LARGE = 32;
    public static final int FONT_SIZE_MEDIUM = 16;
    public static final int FONT_SIZE_SMALL = 8;
    private final java.awt.Font peer;
    private final FontMetrics fontMetrics;

    private Font(java.awt.Font peer) {
        this.peer = peer;
        fontMetrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics().getFontMetrics(peer);
    }

    private static String face(int face) {
        switch (face) {
            case FACE_MONOSPACE:
                return java.awt.Font.MONOSPACED;
            case FACE_PROPORTIONAL:
                return java.awt.Font.DIALOG;
            case FACE_SYSTEM:
                return java.awt.Font.DIALOG_INPUT;
            default:
                throw new IllegalArgumentException(String.format("invalid constant for FACE: %d", face));
        }
    }

    public static Font getDefaultFont() {
        return getFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
    }

    public static Font getFont(int family, int style, int size) {
        return new Font(new java.awt.Font(face(family), style(style), size(size)));
    }

    private static int size(int size) {
        switch (size) {
            case SIZE_LARGE:
                return FONT_SIZE_LARGE;
            case SIZE_MEDIUM:
                return FONT_SIZE_MEDIUM;
            case SIZE_SMALL:
                return FONT_SIZE_SMALL;
        }
        throw new IllegalArgumentException(String.format("invalid constant for SIZE: %d", size));
    }

    private static int style(int style) {
        switch (style) {
            case STYLE_BOLD:
                return java.awt.Font.BOLD;
            case STYLE_ITALIC:
                return java.awt.Font.ITALIC;
            case STYLE_PLAIN:
                return java.awt.Font.PLAIN;
            case STYLE_BOLD | STYLE_ITALIC:
                return java.awt.Font.BOLD | java.awt.Font.ITALIC;
        }
        throw new IllegalArgumentException(String.format("invalid constant for STYLE: %d", style));
    }

    public int getBaselinePosition() {
        return fontMetrics.getAscent();
    }

    int getBaselinePosition(FontMetrics fm) {
        return fm.getAscent();
    }

    public int getHeight() {
        return fontMetrics.getHeight();
    }

    int getHeight(FontMetrics fm) {
        return fm.getHeight();
    }

    public java.awt.Font getPeer() {
        return peer;
    }

    public int stringWidth(String s) {
        return fontMetrics.stringWidth(s);
    }

    int stringWidth(FontMetrics fm, String s) {
        return fm.stringWidth(s);
    }
}
