package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import jtanks.JTanks;
import jtanks.system.Registry;
import jtanks.system.SystemListener;
import jtanks.system.TimerManager;

/**
 * This screen appears when a gamer wins
 */
public class Final extends Screen {

    private String finalString = "Job well done";
    private String programmed = "Programmed by Maxim Borkunov, Alexey Voroshin";

    public Final() {
        keyboardListener = new KeyboardListener();
    }

    @Override
    public void draw(Graphics2D g) {
        Font font = null, bottomFont = null;
        Screen.lock.lock();
        try {
            if (cache.get("font") == null || cache.get("bottomFont") == null) {
                cache.put("font", getFont(getHeight() / 25));
                cache.put("bottomFont", getFont(getHeight() / 45));
            }
            font = (Font) cache.get("font");
            bottomFont = (Font) cache.get("bottomFont");
        } finally {
            Screen.lock.unlock();
        }
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(finalString, (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(finalString, g).getWidth() / 2), getHeight() / 2);
        g.setFont(bottomFont);
        g.drawString(programmed, (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(programmed, g).getWidth() / 2), getHeight() - getHeight() / 10);
    }

    @Override
    public void initialize() {
        Registry.get(TimerManager.class).createTimer(Final.class, new TimerTask() {
            @Override
            public void run() {
                JTanks.getInstance().getGameState().setScreen(new Start());
                Registry.get(TimerManager.class).stopTimer(GameOver.class);
            }
        }, 10000);
    }

    class KeyboardListener extends SystemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            /**
             * Return to Start screen if ESCAPE key is pressed
             */
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                JTanks.getInstance().getGameState().setScreen(new Start());
                Registry.get(TimerManager.class).stopTimer(Final.class);
            }
        }
    }
}

