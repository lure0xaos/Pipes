package javax.microedition.lcdui;

import java.util.Objects;

public final class Command {
    public static final int BACK = 2;
    public static final int CANCEL = 3;
    public static final int EXIT = 7;
    public static final int HELP = 5;
    public static final int ITEM = 8;
    public static final int OK = 4;
    public static final int SCREEN = 1;
    public static final int STOP = 6;
    private final int commandType;
    private final String label;
    private final int position;

    public Command(String label, int commandType, int position) {
        this.label = label;
        this.commandType = commandType;
        this.position = position;
    }

    public String getLabel() {
        return label;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, commandType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Command)) {
            return false;
        }
        Command command = (Command) obj;
        return commandType == command.commandType &&
                Objects.equals(label, command.label);
    }

    @Override
    public String toString() {
        return String.format("Command{commandType=%d, label='%s', position=%d}", commandType, label, position);
    }
}
