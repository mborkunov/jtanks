package jtanks.game.screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import jtanks.system.SystemListener;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import jtanks.JTanks;
import jtanks.game.screens.helpers.MultilineText;
import jtanks.system.Quote;
import jtanks.system.ResourceManager;

public class Loading extends Screen {

    private Quote quote = ResourceManager.getRandomQuote();
    private int percentage = 0;
    protected Screen loadableScreen;
    private String message = "Press any key to continue";

    public Loading() {
        keyboardListener = new LoadingListener(this);
    }

    @Override
    public void draw(Graphics2D g) {
        ArrayList<String> lines = null;
        Font authorFont = null, quoteFont = null;

        Screen.lock.lock();
        try {
            if (cache.get("quoteFont") == null) {
                cache.put("quoteFont", getFont(getHeight() / 30));
            }
            if (cache.get("authorFont") == null) {
                cache.put("authorFont", getFont(getHeight() / 25));
            }
            if (cache.get("lines") == null) {
                int wrapWidth = (int) (getWidth() * 0.8f);

                MultilineText mt = new MultilineText();
                mt.setText(quote.getContent());
                mt.setFont(getFont(getHeight() / 30));
                mt.setWrapWidth(wrapWidth);
                mt.breakLines();

                cache.put("lines", mt.getLines());
            }
            quoteFont = (Font) cache.get("quoteFont");
            authorFont = (Font) cache.get("authorFont");
            lines = ((ArrayList<String>) cache.get("lines"));
        } finally {
            Screen.lock.unlock();
        }

        g.setColor(color);
        g.setFont(quoteFont);

        int i = 0;

        float lineHeight = getHeight() / 30 * 1.1f;
        for (i = 0; i < lines.size(); i++) {
            g.drawString(lines.get(i), (int) ((getWidth() - (getWidth() * 0.8f)) / 2), getHeight() - getHeight() * 0.8f + i * lineHeight);
        }

        g.setFont(authorFont);
        FontMetrics metrics = g.getFontMetrics(authorFont);
        int x = (int) (getWidth() - getWidth() * 0.1f - metrics.getStringBounds(quote.getAuthor(), g).getWidth());
        int y = (int) (getHeight() - getHeight() * 0.8f + (i + 2) * lineHeight);
        g.drawString(quote.getAuthor(), x, y);

        if (percentage < 100) {
            drawProgressBar(g);
        } else {
            Rectangle2D rect = g.getFontMetrics().getStringBounds(message, g);
            x = (int) (getWidth() / 2 - rect.getWidth() / 2);
            y = (int) (getHeight() - getHeight() / 10);
            g.drawString(message, x, y);
        }
    }

    private void drawProgressBar(Graphics2D g) {
        int width = (int) (getWidth() * 0.8f);
        int height = getHeight() / 20;
        int border = 2;

        int x = (int) (getWidth() - width) / 2;
        int y = (int) (getHeight() * 0.9f) - height;

        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.fillRect(x + border, y + border, width - border * 2, height - border * 2);
        g.setColor(Color.WHITE);
        g.fillRect(x + border * 2, y + border * 2, (int) ((width - border * 4) * (percentage / 100f)), height - border * 4);
    }

    @Override
    public void update() {
        if (loadableScreen == null) {
            loadableScreen = new Battle();
            ((Preloadable) loadableScreen).preload();
        }
        percentage = ((Preloadable) loadableScreen).getProgress();
    }

    public boolean finished() {
        return percentage >= 100;
    }
}

class LoadingListener extends SystemListener {

    private Loading screen;
    private boolean over = false;

    LoadingListener(Loading screen) {
        this.screen = screen;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (screen.finished() && screen.loadableScreen != null && !over) {
            over = true;
            JTanks.getInstance().getGameState().setScreen(screen.loadableScreen);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    JTanks.getInstance().getGameState().setScreen(new Start());
                    break;
                default:
                    break;
            }
        }
    }
}
