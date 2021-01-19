package javax.microedition.lcdui;

import javax.swing.JOptionPane;

public final class Alert extends Displayable {
    public static final int FOREVER = -1;

    public Alert(String title, String text, Image image, AlertType alertType) {
        super(new JOptionPane(text, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION));
    }

    public void setTimeout(int timeout) {
        getPeer().setVisible(true);
    }
}
