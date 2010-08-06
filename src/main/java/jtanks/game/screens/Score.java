/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import jtanks.JTanks;
import jtanks.game.gameplay.StatisticsData;
import jtanks.game.map.Map;
import jtanks.system.Registry;
import jtanks.system.SystemListener;
import jtanks.system.TimerManager;

public class Score extends Screen {

    private Map map;
    private Map nextMap;

    public Score() {
        keyboardListener = new KeyboardListener();

        Registry.get(TimerManager.class).createTimer(Score.class, new TimerTask() {
            @Override
            public void run() {
                Map map = (Map) data.get("nextMap");
                Screen screen = new Loading();
                Registry.set("map", map);
                JTanks.getInstance().getGameState().setScreen(screen);
                Registry.get(TimerManager.class).stopTimer(Score.class);
            }
        }, 5000);
    }

    @Override
    public void draw(Graphics2D g) {
        if (map == null || nextMap == null) {
            map = (Map) data.get("map");
            if (Registry.get(StatisticsData.class).getLastMap() < map.getId()) {
                Registry.get(StatisticsData.class).setLastMap(map.getId());
            }
            nextMap = (Map) data.get("nextMap");
        }

        Screen.lock.lock();
        try {
            if (cache.get("font") == null) {
                cache.put("font", getFont(getHeight() / 30));
            }
            g.setFont((Font) cache.get("font"));
        } finally {
            Screen.lock.unlock();
        }

        g.setColor(Color.WHITE);
        g.drawString("Map completed", (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds("Map completed", g).getWidth() / 2), 150);
        g.drawString(map.getName(), (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(map.getName(), g).getWidth() / 2), 200);

        g.drawString("Next map", (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds("Next map", g).getWidth() / 2), 350);
        g.drawString(nextMap.getName(), (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(nextMap.getName(), g).getWidth() / 2), 400);
    }

    class KeyboardListener extends SystemListener {

        KeyboardListener() {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            /**
             * Return to Start screen if ESCAPE key is pressed
             */
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Registry.get(TimerManager.class).stopTimer(Score.class);
                JTanks.getInstance().getGameState().setScreen(new Start());
            }
        }
    }
}