/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import jtanks.JTanks;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;
import jtanks.system.SoundManager;
import jtanks.system.SystemListener;

public class Splash extends Screen {

    private int opacity = 0;
    private Image javaLogo;
    private String name;

    public Splash() {
        keyboardListener = new KeyboardListener();

        javaLogo = ResourceManager.getImage("java");
        name = "JTanks";
        ((SoundManager) Registry.get(SoundManager.class)).preload();
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform javaLogoTransform = null;
        Font font = null;
        int x = 0, y = 0;
        int offset = 4;

        Screen.lock.lock();
        try {
            if (cache.get("font") == null) {
                cache.put("font", getFont(getHeight() / 5));
            }

            if (cache.get("transform") == null) {
                float scale = getHeight() / (float) (javaLogo.getHeight(null) * 6);

                AffineTransform trans = new AffineTransform();
                trans.translate(getWidth() - getHeight() * .05f, getHeight() * .08f);
                trans.scale(scale, scale);
                trans.translate(- javaLogo.getHeight(null) / 2f, - javaLogo.getWidth(null) / 2f);
                cache.put("transform", trans);
            }

            if (cache.get("x") == null || cache.get("y") == null) {
                Rectangle2D rect = g.getFontMetrics((Font) cache.get("font")).getStringBounds(name, g);
                x = (int) (getWidth() / 2 - rect.getWidth() / 2);
                y = (int) (getHeight() / 2);

                cache.put("x", x);
                cache.put("y", y);
            }
            javaLogoTransform = (AffineTransform) cache.get("transform");
            font = (Font) cache.get("font");

            x = (Integer) cache.get("x");
            y = (Integer) cache.get("y");
        } finally {
            Screen.lock.unlock();
        }

        g.setFont(font);
        g.setColor(new Color(0x55, 0x55, 0x55, opacity));
        g.drawString(name, x + offset, y + offset);
        g.setColor(new Color(0xff, 0xff, 0xff, opacity));
        g.drawString(name, x, y);
        g.drawImage(javaLogo, javaLogoTransform, null);
    }

    @Override
    public void update() {
        if (opacity > 1 && System.currentTimeMillis() - startTime > 6000) {
            opacity-=2;
        } else if (opacity < 254 && System.currentTimeMillis() - startTime > 1500) {
            opacity+=2;
        }
        if (System.currentTimeMillis() - startTime > 10000 && opacity == 0) {
            JTanks.getInstance().getGameState().setScreen(new Start());
        }
    }

    class KeyboardListener extends SystemListener {

        private final Logger logger = Logger.getLogger(KeyboardListener.class.getName());

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_ESCAPE:
                    logger.info("Skipping intro");
                    JTanks.getInstance().getGameState().setScreen(new Start());
                    break;
                default:
                    break;
            }
        }
    }
}

