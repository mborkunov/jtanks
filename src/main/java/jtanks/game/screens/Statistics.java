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
import java.util.Map;
import jtanks.JTanks;
import jtanks.game.gameplay.StatisticsData;
import jtanks.system.Registry;
import jtanks.system.SoundManager;
import jtanks.system.SystemListener;

public class Statistics extends Screen {

    public Statistics() {
        keyboardListener = new KeyboardListener();
    }

    @Override
    public void draw(Graphics2D g) {
        Screen.lock.lock();
        try {
            if (cache.get("font") == null) {
                cache.put("font", getFont(getHeight() / 20));
            }
            g.setFont((Font) cache.get("font"));
        } finally {
            Screen.lock.unlock();
        }

        g.setColor(Color.WHITE);

        int lineHeight = (int) ((getHeight() / 20) * 1.1f);

        Map<String,Object> map = ((StatisticsData) Registry.get("statistics")).toMap();

        int line = 0;
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            g.drawString(entry.getKey() + ": " + entry.getValue(), getWidth() * .1f, lineHeight * line + getHeight() * .1f);
            line++;
        }
    }

    class KeyboardListener extends SystemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                JTanks.getInstance().getGameState().setScreen(getCaller());
                ((SoundManager) Registry.get(SoundManager.class)).play("menu");
            }
        }
    }
}


