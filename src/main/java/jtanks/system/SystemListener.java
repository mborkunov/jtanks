/*
 * GNU General Public License v2
 * 
 * @version $Id$
 */
package jtanks.system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import jtanks.JTanks;
import jtanks.game.screens.Screen;

/**
 * Default game keyboard listener
 * Every screen keylistener should extends this one
 */
public class SystemListener implements KeyListener {

    protected HashMap<Integer,Boolean> keys = new HashMap<Integer,Boolean>();

    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (e.isAltDown()) {
                    JTanks.getInstance().flipFullScreenMode();
                    Screen screen = JTanks.getInstance().getGameState().getScreen();
                    screen.purge();
                    try {
                        screen.getCaller().purge();
                    } catch (NullPointerException ignored) {}
                    e.setKeyCode(0);
                }
                break;
            case KeyEvent.VK_PRINTSCREEN:
            case KeyEvent.VK_F12:
                JTanks.getInstance().getGraphics().createScreenshot();
                break;
            case KeyEvent.VK_D:
                Registry.set("debug", ((Boolean) Registry.get("debug")).equals(Boolean.FALSE));
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {
    }

}
