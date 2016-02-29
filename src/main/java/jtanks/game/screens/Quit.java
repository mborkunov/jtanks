package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import jtanks.JTanks;
import jtanks.game.screens.helpers.Menu;
import jtanks.game.screens.helpers.MenuItem;
import jtanks.game.screens.helpers.MenuView;
import jtanks.system.Registry;
import jtanks.system.SoundManager;
import jtanks.system.SystemListener;

/**
 * Quit screen
 */
public class Quit extends Screen {
    /**
     * Menu helper
     */
    public Menu menu = new Menu();

    /**
     * Menu UI helper
     */
    private MenuView menuView;

    /**
     * Quit question text
     */
    private String str;

    /**
     * Initialize quit screen
     */
    public Quit() {
        menu.add(new MenuItem("No", Start.class, true));
        menu.add(new MenuItem("Yes", Screen.class));
        menu.setCaller(this.getCaller());

        keyboardListener = new QuitListener(this);
        menuView = new MenuView(menu);
        str = "Do you really want to quit?";
    }

    /**
     * Draw quit screen
     * 
     * @param g
     */
    @Override
    public void draw(Graphics2D g) {
        Font font = null;
        Screen.lock.lock();
        try {
            if (cache.get("font") == null) {
                cache.put("font", getFont(getHeight() / 20));
            }
            font = (Font) cache.get("font");
        } finally {
            Screen.lock.unlock();
        }
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(str, (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(str, g).getWidth() / 2), getHeight() / 6);
        menuView.draw(g, this);
    }
}

class QuitListener extends SystemListener {

    private Menu menu;
    private Screen screen;

    QuitListener(Quit screen) {
        this.screen = screen;
        menu = screen.menu;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_N:
                menu.selectPrevious();
            break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_Y:
                menu.selectNext();
            break;
            case KeyEvent.VK_ESCAPE:
                JTanks.getInstance().getGameState().setScreen(screen.getCaller());
                ((SoundManager) Registry.get(SoundManager.class)).play("menu");
            break;
            case KeyEvent.VK_ENTER:
                if (menu.selectedIndex() == 1) {
                    JTanks.getInstance().quit();
                } else {
                    JTanks.getInstance().getGameState().setScreen(screen.getCaller());
                    ((SoundManager) Registry.get(SoundManager.class)).play("menu");
                }
            break;
            default:
            break;
        }
    }
}