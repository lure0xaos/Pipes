package net.michaelkerley;

import org.jetbrains.annotations.NotNull;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

@SuppressWarnings("LawOfDemeter")
class PipesCanvas extends Canvas implements CommandListener {
    private final Logger logger = Logger.getLogger(PipesCanvas.class.getName());
    private final @NotNull PipesGame game;
    private final PipesMIDlet midlet;
    private final @NotNull Image offscreenGraphics;
    private final Timer timer = new Timer();

    public PipesCanvas(PipesMIDlet midlet, String version) {
        this.midlet = midlet;
        offscreenGraphics = Image.createImage(getWidth(), getHeight());
        game = new PipesGame(this, version);
        setCommandListener(this);
    }

    @Override
    protected void keyPressed(int keyCode) {
        game.onKeyPress(keyCode, getGameAction(keyCode));
    }

    @Override
    protected void keyRepeated(int keyCode) {
        game.onKeyPress(keyCode, getGameAction(keyCode));
    }

    @Override
    protected void paint(@NotNull Graphics graphics) {
        game.paint(offscreenGraphics.getGraphics(), getWidth(), getHeight());
        graphics.drawImage(offscreenGraphics, 0, 0, Graphics.LEFT | Graphics.TOP);
    }

    public void pause() {
        game.save();
    }

    public void destroy() {
        game.save();
        timer.cancel();
    }

    public void showAbout() {
        game.showAbout();
    }

    public void commandsRemove(@NotNull Command... commands) {
        for (Command command : commands) {
            addCommand(command);
        }
    }

    public void commandsAdd(@NotNull Command... commands) {
        for (Command command : commands) {
            addCommand(command);
        }
    }

    @Override
    public void commandAction(Command command, Displayable displayable) {
        game.onAction(command);
    }

    public void quit() {
        try {
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        } catch (MIDletStateChangeException e) {
            logger.severe(e.getLocalizedMessage());
        }
    }

    public void help(String title, String text) {
        midlet.help(title, text);
    }

    public void schedule(@NotNull Runnable runnable, long time) {
        timer.schedule(new RunnableTimerTask(runnable), time);
    }

    public void tryLoad() {
        if (!game.load()) {
            game.init();
        }
    }

    private static class RunnableTimerTask extends TimerTask {
        private final @NotNull Runnable runnable;

        public RunnableTimerTask(@NotNull Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }
}
