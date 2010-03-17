/*
 * GNU General Public License v2
 *
 * @version $Id: Battle.java 296 2009-07-23 13:13:00Z ru.energy $
 */
package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jtanks.JTanks;
import jtanks.game.GameState;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.scene.units.util.Motion.Direction;
import jtanks.game.gameplay.StatisticsData;
import jtanks.game.map.Manager;
import jtanks.game.map.Map;
import jtanks.game.scene.gameplay.BattleField;
import jtanks.game.scene.gameplay.Interface;
import jtanks.game.scene.units.Tank;
import jtanks.game.screens.helpers.DataTransfer;
import jtanks.game.util.Cache;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;
import jtanks.system.SystemListener;
import jtanks.system.TimerManager;

public class Battle extends Screen implements Preloadable {

    private Map map;
    private Status status = Status.INCOMPLETE;
    private boolean showQuitDialog = false;

    private enum Status {
        COMPLETE, GAMEOVER, INCOMPLETE, PAUSED;
    }

    public Battle() {
        map = (Map) Registry.get("map");
        cursor = ResourceManager.createInvisibleCursor();
        rootNode = new Interface();
        keyboardListener = new KeyboardListener();
    }

    public void preload() {
        ((Preloadable) rootNode).preload();
    }

    public int getProgress() {
        return ((Preloadable) rootNode).getProgress();
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        if (showQuitDialog) {
            Font font = null;
            Screen.lock.lock();
            try {
                if (cache.get("font") == null) {
                    cache.put("font", getFont(getHeight() / 15));
                }
                font = (Font) cache.get("font");
            } finally {
                Screen.lock.unlock();
            }
            int width  = (int) g.getClipBounds().getWidth();
            int height = (int) g.getClipBounds().getHeight();

            g.setColor(Color.BLACK);
            g.setFont(font);
            
            String quitString = "Quit?";
            String keyString = "y / n";
            Rectangle2D rect = g.getFontMetrics().getStringBounds(quitString, g);
            int fontWidth  = (int) rect.getWidth();
            int fontHeight = (int) rect.getHeight();

            g.setBackground(Color.WHITE);
            g.clearRect(width / 2 - fontWidth / 2 - fontWidth / 4, height / 3 - 50 - fontHeight / 4, (int) (fontWidth * 1.5), (int) (fontHeight * 4));

            g.drawString(quitString, width / 2 - fontWidth / 2, height / 3);

            fontWidth = (int) g.getFontMetrics().getStringBounds(keyString, g).getWidth();
            g.drawString(keyString, width / 2 - fontWidth / 2, height / 3 + (int) (fontHeight * 1.5f));
        }
    }

    @Override
    public void update() {
        super.update();

        if (JTanks.getInstance().getGameState().getScreen().equals(this) == false) {
            return;
        }

        if (status.equals(Status.GAMEOVER) == false) {
            if (BattleField.instance.isPlayerAlive() == false || BattleField.instance.isBaseAlive() == false) {
                status = Status.GAMEOVER;
            }
        }

        if (status.equals(Status.COMPLETE) == false && status.equals(Status.GAMEOVER) == false) {
            if (BattleField.instance.hasEnemies() == false) {
                status = Status.COMPLETE;
            }
        }

        if (status == Status.INCOMPLETE || status == Status.PAUSED) {
            return;
        }

        if (Registry.get(TimerManager.class).getTimer(Battle.class) == null) {
            Registry.get(TimerManager.class).createTimer(Battle.class, new TimerTask() {
                @Override
                public void run() {
                    Screen screen = null;
                    switch (status) {
                        case COMPLETE:
                            int index = Manager.getInstance().indexOf(map);
                            List<Map> maps = Manager.getInstance().getMaps();
                            if (index + 1 >= maps.size()) {
                                screen = new Final();
                            } else {
                                screen = new Score();
                                DataTransfer data = new DataTransfer();
                                data.set("map", maps.get(index));
                                data.set("nextMap", maps.get(index + 1));
                                screen.setData(data);
                            }
                            break;
                        case GAMEOVER:
                            StatisticsData stats = ((StatisticsData) Registry.get("statistics"));
                            stats.setDeaths(stats.getDeaths() + 1);
                            screen = new GameOver();
                            break;
                    }
                    JTanks.getInstance().getGameState().setScreen(screen);
                    Registry.get(TimerManager.class).stopTimer(Battle.class);
                }
            }, 2000);
        }
    }

    @Override
    public void terminate() {
        jtanks.game.gameplay.StatisticsData stats = (jtanks.game.gameplay.StatisticsData) Registry.get("statistics");
        long time = (System.currentTimeMillis() - startTime);
        long pauseTime = 0;
        if (Registry.get("pauseTime") != null) {
            pauseTime = (Long) Registry.get("pauseTime");
            Registry.remove("pauseTime");
        }

        Logger.getLogger(this.getClass().getName()).info("Play time " + TimeUnit.MILLISECONDS.toSeconds((time - pauseTime)) + " seconds");
        stats.setTime(stats.getTime() + time - pauseTime);
    }

    class KeyboardListener extends SystemListener {

        private long startPauseTime = 0;

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    showQuitDialog = status.equals(Battle.Status.PAUSED) ? false : true;

                    if (status.equals(Battle.Status.PAUSED)) {
                        status = Status.INCOMPLETE;
                        JTanks.getInstance().getGameState().resume();
                        updatePauseTime();
                    } else if (status.equals(Status.INCOMPLETE)){
                        status = Status.PAUSED;
                        JTanks.getInstance().getGameState().pause();
                        startPauseTime = System.currentTimeMillis();
                    }
                    break;
                case KeyEvent.VK_P:
                case KeyEvent.VK_PAUSE:
                    if (showQuitDialog == false) {
                        if (status.equals(Battle.Status.PAUSED)) {
                            status = Status.INCOMPLETE;
                            JTanks.getInstance().getGameState().resume();
                            updatePauseTime();
                        } else if (status.equals(Status.INCOMPLETE)){
                            status = Status.PAUSED;
                            JTanks.getInstance().getGameState().pause();
                            startPauseTime = System.currentTimeMillis();
                        }
                        return;
                    }
                case KeyEvent.VK_N:
                    if (showQuitDialog) {
                        status = Status.INCOMPLETE;
                        JTanks.getInstance().getGameState().resume();
                        showQuitDialog = false;
                        updatePauseTime();
                    }
                break;
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_Y:
                    if (showQuitDialog) {
                        GameState state = JTanks.getInstance().getGameState();
                        state.setScreen(new Start());
                        state.resume();
                        updatePauseTime();
                    }
                    break;
            }

            if (status.equals(Status.PAUSED)) {
                return;
            }

            if (Registry.get("playerTank") != null) {
                Tank tank = (Tank) Registry.get("playerTank");
                Motion motion = tank.getMotion();
                double speed = 0;
                double defaultSpeed = 1.5;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        speed = defaultSpeed;
                        motion.setDirection(Motion.Direction.NORTH);
                        break;
                    case KeyEvent.VK_LEFT:
                        speed = defaultSpeed;
                        motion.setDirection(Motion.Direction.WEST);
                        break;
                    case KeyEvent.VK_RIGHT:
                        speed = defaultSpeed;
                        motion.setDirection(Motion.Direction.EAST);
                        break;
                    case KeyEvent.VK_DOWN:
                        speed = defaultSpeed;
                        motion.setDirection(Motion.Direction.SOUTH);
                        break;
                    case KeyEvent.VK_SPACE:
                        tank.lock.lock();
                        try {
                            tank.shoot();
                        } finally {
                            tank.lock.unlock();
                            return;
                        }
                }
                if (speed != 0) {
                    tank.lock.lock();
                    try {
                        tank.getMotion().setSpeed(speed);
                        tank.getMotion().setLock(false);
                    } finally {
                        tank.lock.unlock();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);

            if (status.equals(Status.PAUSED)) {
                return;
            }

            if (Registry.get("playerTank") != null) {
                Tank tank = (Tank) Registry.get("playerTank");
                Motion motion = tank.getMotion();
                Direction direction = motion.getDirection();
                double speed = motion.getSpeed();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        if (direction.equals(Motion.Direction.EAST)) {
                            speed = 0;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction.equals(Motion.Direction.WEST)) {
                            speed = 0;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction.equals(Motion.Direction.NORTH)) {
                            speed = 0;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction.equals(Motion.Direction.SOUTH)) {
                            speed = 0;
                        }
                        break;
                }
                motion.setSpeed(speed);
            }
        }

        private void updatePauseTime() {
            long pauseTime = 0;
            if (Registry.get("pauseTime") != null) {
                pauseTime = (Long) Registry.get("pauseTime");
            }
            Registry.set("pauseTime", pauseTime + (System.currentTimeMillis() - startPauseTime));
            startPauseTime = 0;
        }
    }

}

