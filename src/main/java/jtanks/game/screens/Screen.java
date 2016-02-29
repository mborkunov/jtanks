package jtanks.game.screens;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jtanks.JTanks;
import jtanks.game.screens.helpers.DataTransfer;
import jtanks.system.Config;
import jtanks.system.SystemListener;
import jtanks.game.GameState;
import jtanks.game.scene.Node;
import jtanks.system.ResourceManager;

public abstract class Screen {

    protected Cursor cursor = ResourceManager.getCursor();
    protected Node rootNode;
    private Canvas canvas;
    protected long startTime;
    protected Screen caller;
    public static Lock lock = new ReentrantLock();
    protected GameState state = JTanks.getInstance().getGameState();

    /**
     * A listener instance
     */
    public KeyListener keyboardListener;
    public MouseListener mouseListener;
    public MouseMotionListener mouseMotionListener;

    protected DataTransfer data;
    protected Color color = Color.WHITE;

    public HashMap<String,Object> cache = new HashMap<String,Object>();

    /**
     * This is an empty listener
     */
    public class KeyboardListener extends SystemListener {
    }

    protected Screen() {
        cursor = ResourceManager.createInvisibleCursor();
        startTime = System.currentTimeMillis();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getWidth() {
        return JTanks.getInstance().getCanvas().getWidth();
    }

    public int getHeight() {
        return JTanks.getInstance().getCanvas().getHeight();
    }

    public void draw(Graphics2D g) {
        if (rootNode != null) {
            rootNode.render(g);
        } else {
            Font font = null;
            int x = 0, y = 0;
            String s = "This screen is not implemented";

            Screen.lock.lock();
            try {
                if (cache.get("font") == null) {
                    cache.put("font", getFont(getHeight() / 15));
                }
                font = (Font) cache.get("font");

                if (cache.get("x") == null || cache.get("y") == null) {
                    Rectangle2D rect = g.getFontMetrics(font).getStringBounds(s, g);
                    cache.put("x", (int) (getWidth() / 2 - rect.getWidth() / 2));
                    cache.put("y", (int) (getHeight() / 2));
                }
                x = (Integer) cache.get("x");
                y = (Integer) cache.get("y");
            } finally {
                Screen.lock.unlock();
            }

            g.setColor(color);
            g.setFont(font);
            g.drawString(s, x, y);
        }
    }

    public void setData(DataTransfer data) {
        this.data = data;
    }

    public KeyListener getKeyboardListener() {
        if (keyboardListener == null) {
            keyboardListener = new KeyboardListener();
        }
        return keyboardListener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }

    public void purge() {
        Screen.lock.lock();
        try {
            cache = new HashMap<String,Object>();
        } finally {
            Screen.lock.unlock();
        }
    }

    public Font getFont(int size) {
            String fontString = new StringBuffer(Config.getInstance().get("font.family"))
                              .append(" ").append(Config.getInstance().get("font.style"))
                              .append(" ").append(size).toString();
        return Font.decode(fontString.toString());
    }

    public void initialize() {
        JTanks.getInstance().getCanvas().setCursor(cursor);
    }

    public void terminate() {
        purge();
    }

    public void update() {
        if (rootNode != null) {
            rootNode.update();
        }
    }

    public boolean isRunning() {
        return state.getStatus().equals(GameState.Status.RUNNING);
    }

    public void setCaller(Screen caller) {
        this.caller = caller;
    }

    public Screen getCaller() {
        return caller;
    }
}