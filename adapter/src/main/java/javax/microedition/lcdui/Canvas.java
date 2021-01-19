package javax.microedition.lcdui;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;

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
        ((PeerPanel) getPeer()).setDisplayable(this);
    }

    protected int getGameAction(int code) {
        switch (code) {
            case KeyEvent.VK_UP:
                return UP;
            case KeyEvent.VK_LEFT:
                return LEFT;
            case KeyEvent.VK_RIGHT:
                return RIGHT;
            case KeyEvent.VK_DOWN:
                return DOWN;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                return FIRE;
        }
        return 0;
    }

    protected void keyPressed(int keyCode) {
    }

    protected void keyRepeated(int keyCode) {
    }

    public final void repaint() {
        getPeer().repaint();
    }

    private static final class PeerPanel extends JPanel {
        private static final long serialVersionUID = 3277821937516211666L;
        private Displayable displayable;

        Displayable getDisplayable() {
            return displayable;
        }

        @Override
        public void paint(java.awt.Graphics g) {
            displayable.paint(Graphics.forGraphics(g));
        }

        void setDisplayable(Displayable displayable) {
            this.displayable = displayable;
        }
    }
}
