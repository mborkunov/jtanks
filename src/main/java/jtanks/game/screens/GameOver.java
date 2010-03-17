/*
 * GNU General Public License v2
 * 
 * @version $Id: GameOver.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.screens;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.TimerTask;
import jtanks.JTanks;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;
import jtanks.system.SystemListener;
import jtanks.system.TimerManager;

/**
 * This screen appears when gamer loses
 */
public class GameOver extends Screen {

    private double angle = 0;
    private final String message = "Game Over";

    public GameOver() {
        keyboardListener = new KeyboardListener();
    }

    @Override
    public void initialize() {
        Registry.get(TimerManager.class).createTimer(GameOver.class, new TimerTask() {
            @Override
            public void run() {
                JTanks.getInstance().getGameState().setScreen(new Start());
                ((TimerManager) Registry.get(TimerManager.class)).stopTimer(GameOver.class);
            }
        }, 10000);
    }

    @Override
    public void draw(Graphics2D g) {
        if (cache.get("font") == null) {
            cache.put("font", getFont(getHeight() / 15));
        }

        g.setColor(color);
        g.setFont((Font) cache.get("font"));
        Rectangle2D rect = g.getFontMetrics().getStringBounds(message, g);

        g.drawString(message, (int) (getWidth() / 2 - rect.getWidth() / 2), (int) (getHeight() / 3 - rect.getHeight() / 2));

        /**
         * Draw animated skull
         */
        AffineTransform trans = new AffineTransform();
        Image image = ResourceManager.getImage("gameover");
        
        float scale = (getHeight() / ((float) image.getHeight(null) * 3));

        trans.translate((int) (getWidth() / 2), (int) (getHeight() / 2));
        trans.scale(scale, scale);
        trans.shear(Math.sin(angle) / 3, Math.cos(angle) / 3);
        trans.translate((int) (-image.getWidth(null) / 2), (int) (-image.getHeight(null) / 2));

        g.drawImage(image, trans, null);
    }

    @Override
    public void update() {
        angle += 0.1;
        if (angle >= 360) {
            angle = 0;
        }
    }

    class KeyboardListener extends SystemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_ESCAPE:
                    Registry.get(TimerManager.class).stopTimer(GameOver.class);
                    JTanks.getInstance().getGameState().setScreen(new Start());
                    break;
            }
        }
    }
}