package javax.microedition.lcdui;

import javax.swing.*;

import static java.awt.event.KeyEvent.*;

public abstract class Canvas extends Displayable {
    public static final int DOWN = 6;
    public static final int FIRE = 8;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM3 = 50;
    public static final int KEY_POUND = 35;
    public static final int KEY_STAR = 42;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int UP = 1;

    protected Canvas() {
        super(new PeerPanel());
        ((PeerPanel) peer).displayable = this;
    }

    public int getGameAction(int code) {
        switch (code) {
            case VK_UP:
                return UP;
            case VK_LEFT:
                return LEFT;
            case VK_RIGHT:
                return RIGHT;
            case VK_DOWN:
                return DOWN;
            case VK_ENTER:
            case VK_SPACE:
                return FIRE;
        }
        return 0;
    }

    protected void keyPressed(int keyCode) {
    }

    protected void keyRepeated(int keyCode) {
    }

    public void repaint() {
        peer.repaint();
    }

    @SuppressWarnings("NonSerializableFieldInSerializableClass")
    private static class PeerPanel extends JPanel {
        private static final long serialVersionUID = 3277821937516211666L;
        Displayable displayable;

        @Override
        public void paint(java.awt.Graphics g) {
            displayable.paint(Graphics.forGraphics(g));
        }
    }
}
