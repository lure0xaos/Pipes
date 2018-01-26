package javax.microedition.lcdui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public abstract class Displayable {
    private final Map<Command, JMenuItem> commands = Collections.synchronizedMap(new LinkedHashMap<>());
    private final List<Command> orphans = Collections.synchronizedList(new LinkedList<>());
    final JComponent peer;
    private CommandListener commandListener;
    private final ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (commandListener != null) {
                commands.forEach((key, value) -> {
                    if (Objects.equals(e.getSource(), value)) {
                        commandListener.commandAction(key, Displayable.this);
                        peer.getRootPane().repaint();
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

    public synchronized void addCommand(Command command) {
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

    public int getHeight() {
        return peer.getHeight();
    }

    private JMenuBar getMenuBar() {
        JFrame ancestor = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, peer);
        if (ancestor == null) {
            return null;
        } else {
            JMenuBar menuBar = ancestor.getJMenuBar();
            if (menuBar == null) {
                menuBar = new JMenuBar();
                ancestor.setJMenuBar(menuBar);
            }
            return menuBar;
        }
    }

    public int getWidth() {
        return peer.getWidth();
    }

    private synchronized void invokeAndWait(Runnable runnable) {
        (runnable).run();
        peer.getRootPane().validate();
        peer.getRootPane().repaint();
    }


    synchronized void onAdd() {
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

    synchronized void onRemove() {
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

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }
}
