package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Display {
    public static final int HEIGHT = 400;
    public static final int WIDTH = 300;
    public final JFrame peer;
    private Displayable displayable;

    private Display(MIDlet miDlet) {
        peer = new JFrame();
        peer.addWindowListener(new DisplayWindowAdapter(miDlet));
        peer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (displayable instanceof Canvas) {
                    ((Canvas) displayable).keyPressed(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //            displayable.keyPressed(e.getKeyCode());
            }
        });
        peer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        peer.getContentPane().setLayout(new BorderLayout());
        peer.setSize(WIDTH, HEIGHT);
    }

    public static Display getDisplay(MIDlet miDlet) {
        if (miDlet.display == null) {
            miDlet.display = new Display(miDlet);
        }
        return miDlet.display;
    }

    public void setCurrent(Displayable displayable) {
        Container pane = peer.getContentPane();
        if (this.displayable != null) {
            this.displayable.onRemove();
            pane.remove(this.displayable.peer);
        }
        this.displayable = displayable;
        pane.add(displayable.peer, BorderLayout.CENTER);
        displayable.onAdd();
        peer.validate();
        peer.repaint();
    }

    private static class DisplayWindowAdapter extends WindowAdapter {
        private final MIDlet miDlet;

        public DisplayWindowAdapter(MIDlet miDlet) {
            this.miDlet = miDlet;
        }

        @Override
        public void windowClosed(WindowEvent e) {
            miDlet.notifyDestroyed();
        }

        @Override
        public void windowActivated(WindowEvent e) {
            miDlet.resumeRequest();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            miDlet.notifyPaused();
        }
    }
}
