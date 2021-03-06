package javax.microedition.midlet;

import javax.microedition.lcdui.Display;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@SuppressWarnings({"EmptyMethod", "RedundantThrows"})
public abstract class MIDlet {
    public Display display;

    public static void main(String[] args) throws ReflectiveOperationException {
        //noinspection unchecked
        ((Class<? extends MIDlet>) Class.forName(args[0])).getConstructor().newInstance().showMe();
    }

    @SuppressWarnings("SameParameterValue")
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    public String getAppProperty(String name) {
        try {
            Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream(JarFile.MANIFEST_NAME));
            Attributes mainAttributes = manifest.getMainAttributes();
            if (mainAttributes.containsKey(name)) {
                return mainAttributes.getValue(name);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public final void notifyDestroyed() {
        try {
            destroyApp(true);
        } catch (MIDletStateChangeException e) {
//            throw new RuntimeException(e.getMessage(), e);
        }
        display.peer.dispose();
        try {
            Thread.sleep(1000);
            System.exit(-1);
        } catch (InterruptedException ignored) {
            //
        }
    }

    public final void notifyPaused() {
        pauseApp();
    }

    protected void pauseApp() {
    }

    protected void resumeApp() {
    }

    public final void resumeRequest() {
        resumeApp();
    }

    void showMe() {
        Display.getDisplay(this).peer.setVisible(true);
        try {
            startApp();
        } catch (MIDletStateChangeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void startApp() throws MIDletStateChangeException {
    }
}
