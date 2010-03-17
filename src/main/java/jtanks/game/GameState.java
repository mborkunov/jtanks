/*
 * GNU General Public License v2
 *
 * @version $Id: GameState.java 279 2009-07-17 16:42:49Z ru.energy $
 */
package jtanks.game;

import java.awt.Component;
import java.lang.Thread.State;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.JTanks;
import jtanks.game.gameplay.StatisticsData;
import jtanks.game.screens.Screen;
import jtanks.game.screens.Splash;
import jtanks.system.Config;
import jtanks.system.Registry;

public final class GameState {

    private static int counter = 1;

    public enum Status {
        RUNNING, PAUSED;
    }

    protected Thread thread = new Thread();
    private GameTask task = new GameTask(this);
    protected Screen screen = new Splash();

    public GameState() {
        initialize();
    }

    /**
     * Get the value of screen
     *
     * @return the value of screen
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * Set the value of screen
     *
     * @param replaceScreen new value of screen
     */
    public synchronized void setScreen(Screen replaceScreen) {
        Component component = JTanks.getInstance().getCanvas();
         /**
         * Remove old event listener
         */
        if (screen != null) {
            component.removeKeyListener(screen.getKeyboardListener());

            if (screen.getMouseMotionListener() != null) {
                component.removeMouseMotionListener(screen.getMouseMotionListener());
            }
            if (screen.getMouseListener() != null) {
                component.removeMouseListener(screen.getMouseListener());
            }
        }

        /**
         * Replace screen object and assign new event listener
         */
        component.addKeyListener(replaceScreen.getKeyboardListener());
        
        if (replaceScreen.getMouseMotionListener() != null) {
            component.addMouseMotionListener(replaceScreen.getMouseMotionListener());
        }

        if (replaceScreen.getMouseListener() != null) {
            replaceScreen.getMouseListener().mouseClicked(null);
            component.addMouseListener(replaceScreen.getMouseListener());
        }

        replaceScreen.initialize();
        screen.terminate();
        screen = replaceScreen;
    }

    public void initialize() {
        Registry.set("statistics", StatisticsData.load());
        setScreen(screen);
    }

    public void start() {
        thread = new Thread(task, "Game state-" + counter++);
        thread.start();
    }

    public void pause() {
        if (thread.getState().equals(State.TERMINATED)) {
            return;
        }
        Logger.getLogger(GameState.class.getName()).info("Game pause");
        task.isRunning = false;
        while (thread.getState().equals(State.TERMINATED) == false) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resume() {
        if (thread.getState().equals(State.TERMINATED) == false) {
            return;
        }
        Logger.getLogger(GameState.class.getName()).info("Game resume");
        task.isRunning = true;
        start();
    }

    public Status getStatus() {
        return task.isRunning ? Status.RUNNING : Status.PAUSED;
    }

    public void quit() {
        screen.terminate();
        Config.getInstance().save();
        ((StatisticsData) Registry.get("statistics")).store();
        System.exit(0);
    }

    private class GameTask implements Runnable {

        private final int GAME_LOOP_DELAY = 20;
        private GameState game;
        protected boolean isRunning = true;
        private long time;

        public GameTask(GameState game) {
            this.game = game;
        }

        public void run() {
            while (isRunning) {
                updateState();
                synchronize();
            }
        }

        public void updateState() {
            game.getScreen().update();
        }

        private void synchronize() {
            long diff = GAME_LOOP_DELAY - (System.currentTimeMillis() - time);
            try {
                Thread.sleep(Math.max(0, diff));
            } catch (InterruptedException ignored) {}
            time = System.currentTimeMillis();
        }
    }
}
