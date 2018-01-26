package javax.microedition.lcdui;

@FunctionalInterface
public interface CommandListener {
    void commandAction(Command command, Displayable displayable);
}
