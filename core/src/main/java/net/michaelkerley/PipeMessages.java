package net.michaelkerley;

public final class PipeMessages {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String[] ABOUT_TEXT = {
            "", // Version placeholder - this line will be replaced at runtime
            "",
            "Written by Kornhornio",
            "http://www.kornhornio.net",
            "",
            "Concept by Ernest Pazera",
            "http://www.playdeez.com"
    };
    public static final String HELP_ALERT_TEXT =
            "Rules:\n"
                    + " * To win the game, you must connect all the pipe sections. Connected pipes will turn green.\n"
                    + " * There are no impossible puzzles; every game can be solved.\n"
                    + " * When the puzzle is solved, there will be no dangling sections of pipe. For example, if there is a straight piece along the bottom edge of the board, it MUST be turned so that it runs left-to-right.\n"
                    + '\n'
                    + "Controls:\n"
                    + " * Joystick - Move cursor\n"
                    + " * OK/center button - Rotate clockwise\n"
                    + " * #/* buttons - Zoom in/out\n"
                    + '\n'
                    + "Alternate controls:\n"
                    + " * 2/4/6/8 - Move cursor\n"
                    + " * 3/5 - Rotate clockwise\n"
                    + " * 1 - Rotate counter-clockwise";
    public static final String HELP_ALERT_TITLE = "Pipes Help...";
    public static final String MESSAGE_GAME_OVER = "You Win!";
    public static final String MESSAGE_VERSION = "Pipes %s";
    public static final String MESSAGE_ROTATE = "Rotate";
    public static final String MESSAGE_RESET = "Reset";
    public static final String MESSAGE_RESIZE = "Resize";
    public static final String MESSAGE_HELP = "Help...";
    public static final String MESSAGE_ABOUT = "About...";
    public static final String MESSAGE_QUIT = "Quit";
    public static final String MESSAGE_OK = "OK";

    private PipeMessages() {
    }
}
