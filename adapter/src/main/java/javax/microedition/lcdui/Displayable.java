package javax.microedition.lcdui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Displayable {
    private final Map<Command, JMenuItem> commands = Collections.synchronizedMap(new LinkedHashMap<>());
    private final List<Command> orphans = Collections.synchronizedList(new LinkedList<>());
    private final JComponent peer;
    private CommandListener commandListener;
    private final ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (commandListener != null) {
                commands.forEach((key, value) -> {
                    if (Objects.equals(e.getSource(), value)) {
                        commandListener.commandAction(key, Displayable.this);
                        getPeer().getRootPane().repaint();
                    }
                });
            }
        }
    };

    Displayable(JComponent peer) {
        this.peer = peer;
        Dimension size = new Dimension(Display.WIDTH, Display.HEIGHT);
        peer.setSize(size);
        peer.setPreferredSize(size);
    }

    public final synchronized void addCommand(Command command) {
        JMenuBar menuBar = getMenuBar();
        if (menuBar != null) {
            JMenuItem item = createMenuItem(command);
            invokeAndWait(() -> menuBar.add(item));
            commands.put(command, item);
        } else {
            orphans.add(command);
        }
    }

    private JMenuItem createMenuItem(Command command) {
        JMenuItem item = new JMenuItem(command.getLabel());
        item.addActionListener(listener);
        return item;
    }

    public final int getHeight() {
        return peer.getHeight();
    }

    private JMenuBar getMenuBar() {
        JFrame ancestor = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, peer);
        if (ancestor == null) {
            return null;
        }
        JMenuBar menuBar = ancestor.getJMenuBar();
        if (menuBar == null) {
            menuBar = new JMenuBar();
            ancestor.setJMenuBar(menuBar);
        }
        return menuBar;
    }

    public final JComponent getPeer() {
        return peer;
    }

    public final int getWidth() {
        return getPeer().getWidth();
    }

    private synchronized void invokeAndWait(Runnable runnable) {
        runnable.run();
        getPeer().getRootPane().validate();
        getPeer().getRootPane().repaint();
    }


    final synchronized void onAdd() {
        JMenuBar menuBar = getMenuBar();
        if (menuBar != null) {
            for (Iterator<Command> iterator = orphans.iterator(); iterator.hasNext(); ) {
                Command command = iterator.next();
                JMenuItem menuItem = createMenuItem(command);
                invokeAndWait(() -> menuBar.add(menuItem));
                commands.put(command, menuItem);
                iterator.remove();
            }
        }
    }

    final synchronized void onRemove() {
        JMenuBar menuBar = getMenuBar();
        if (menuBar != null) {
            for (Iterator<Map.Entry<Command, JMenuItem>> iterator = commands.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<Command, JMenuItem> entry = iterator.next();
                JMenuItem item = entry.getValue();
                item.removeActionListener(listener);
                invokeAndWait(() -> menuBar.remove(item));
                orphans.add(entry.getKey());
                iterator.remove();
            }
        }
    }

    protected void paint(Graphics graphics) {
    }

    public synchronized void removeCommand(Command command) {
        JMenuBar menuBar = getMenuBar();
        if (menuBar != null) {
            JMenuItem removed = commands.remove(command);
            if (removed != null) {
                invokeAndWait(() -> menuBar.remove(removed));
            }
        } else {
            orphans.remove(command);
        }
    }

    public final void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }
}
