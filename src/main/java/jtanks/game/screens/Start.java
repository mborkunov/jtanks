/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.JTanks;
import jtanks.game.screens.helpers.Menu;
import jtanks.game.screens.helpers.MenuItem;
import jtanks.game.screens.helpers.MenuItemHandler;
import jtanks.game.screens.helpers.MenuView;
import jtanks.mapeditor.MapEditor;
import jtanks.system.Registry;
import jtanks.system.SoundManager;
import jtanks.system.SystemListener;

public class Start extends Screen {

    private final Logger logger = Logger.getLogger(JTanks.class.getName());

    protected Menu menu = new Menu();
    private MenuView menuView;

    public Start() {
        menu.add(new MenuItem("Start", Maps.class, true));
        menu.add(new MenuItem("Options", Options.class));
        MenuItem menuItem = new MenuItem("Statistics", Statistics.class);
        menuItem.setDisabled(false);
        menu.add(menuItem);
        menu.add(new MenuItem("Editor", new MenuItemHandler() {
            @Override
            public void handle() {
                logger.log(Level.INFO, "Starting map editor");
                MapEditor.start(new String[0]);
            }
        }));
        menu.add(new MenuItem("Quit", Quit.class));
        menu.setCaller(this);

        menuView = new MenuView(menu);

        keyboardListener = new KeyboardListener(this);
    }

    @Override
    public void draw(Graphics2D g) {
        menuView.draw(g, this);
    }

    /**
     * Start screen event listener
     *
     */
    class KeyboardListener extends SystemListener {

        private Screen screen;

        public KeyboardListener(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    menu.selectPrevious();
                    break;
                case KeyEvent.VK_DOWN:
                    menu.selectNext();
                    break;
                case KeyEvent.VK_ESCAPE:
                    Screen quitScreen = new Quit();
                    quitScreen.setCaller(screen);
                    JTanks.getInstance().getGameState().setScreen(quitScreen);
                    Registry.get(SoundManager.class).play("menu");
                    break;
                case KeyEvent.VK_ENTER:
                    MenuItem item = menu.getSelected();
                    if (item.getHandler() == null) {
                        try {
                            JTanks.getInstance().getGameState().setScreen(item.getScreen());
                        } catch (InstantiationException ex) {
                            Logger.getLogger(KeyboardListener.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(KeyboardListener.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        item.getHandler().handle();
                    }
                    Registry.get(SoundManager.class).play("menu");
                    break;
                default:
                    break;
            }
        }
    }
}
