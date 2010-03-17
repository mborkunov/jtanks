/*
 * GNU General Public License v2
 * 
 * @version $Id: Options.java 271 2009-07-17 16:31:12Z ru.energy $
 */
package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import jtanks.JTanks;
import jtanks.game.screens.controls.Binding;
import jtanks.game.screens.controls.Checkbox;
import jtanks.game.screens.controls.Layout;
import jtanks.system.Config;
import jtanks.system.ResourceManager;
import jtanks.system.SystemListener;

public class Options extends Screen {

    public Options() {

        cursor = ResourceManager.getCursor();
        keyboardListener    = new OptionsKeyboardListener();
        mouseMotionListener = new OptionsMouseMotionListener();
        mouseListener       = new OptionsMouseListener();

        Layout layout = new Layout();
        Checkbox checkbox = new Checkbox("Fullscreen");
        checkbox.setBinding(new Binding() {
            @Override
            public void process() {
                boolean fullscreen = ((Checkbox) control).checked();
                Config.getInstance().set("fullscreen", fullscreen);
                JTanks.getInstance().setFullScreenMode(fullscreen);
            }
        });
        layout.addChild(checkbox);

        Checkbox soundCheckbox = new Checkbox("Sound (Not supported yet)");
        soundCheckbox.setBinding(new Binding(){
            @Override
            public void process() {
                boolean sound = ((Checkbox) control).checked();
                Config.getInstance().set("sound", sound);
            }
        });
        layout.addChild(soundCheckbox);
        rootNode = layout;
    }

    /**
     * Render single screen frame
     *
     * @param g Graphics
     */
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

        String options = "Options";
        g.drawString(options, (int) (getWidth() * .1f), (int) (getHeight() * .1f));

        rootNode.render(g);
    }

    /**
     * Options screen key event listener
     */
    class OptionsKeyboardListener extends SystemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            ((Layout) rootNode).dispatchEvent(e);
            /**
             * Return to Start screen if ESCAPE key is pressed
             */
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                JTanks.getInstance().getGameState().setScreen(getCaller());
            }
        }
    }

    class OptionsMouseMotionListener implements MouseMotionListener {

        public void mouseDragged(MouseEvent e) {
            ((Layout) rootNode).dispatchEvent(e);
        }

        public void mouseMoved(MouseEvent e) {
            ((Layout) rootNode).dispatchEvent(e);
        }
    }

    class OptionsMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (e != null) {
                ((Layout) rootNode).dispatchEvent(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }
    }
}
