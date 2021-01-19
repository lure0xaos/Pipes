package net.michaelkerley;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

@SuppressWarnings({"RedundantThrows", "LawOfDemeter"})
public class PipesMIDlet extends MIDlet {
    private static final String MIDLET_VERSION = "MIDlet-Version";
    private final PipesCanvas canvas;
    private boolean firstStart = true;

    public PipesMIDlet() {
        canvas = new PipesCanvas(this, getAppProperty(MIDLET_VERSION));
    }

    @Override
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        canvas.destroy();
    }

    @Override
    protected void pauseApp() {
        canvas.pause();
    }

    @Override
    protected void startApp() throws MIDletStateChangeException {
        canvas.tryLoad();
        if (firstStart) {
            first();
            firstStart = false;
        }
    }

    private void first() {
        canvas.showAbout();
        Display.getDisplay(this).setCurrent(canvas);
    }

    public void help(String title, String text) {
        Alert alert = new Alert(title, text, null, null);
        alert.setTimeout(Alert.FOREVER);
        Display.getDisplay(this).setCurrent(alert);
    }

}
