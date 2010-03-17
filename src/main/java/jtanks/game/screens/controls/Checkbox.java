/*
 * GNU General Public License v2
 *
 * @version $Id: Checkbox.java 262 2009-07-12 11:01:27Z ru.energy $
 */
package jtanks.game.screens.controls;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import jtanks.game.geometry.Rectangle;
import jtanks.system.Config;

public class Checkbox extends Control {

    private boolean checked = Config.getInstance().getBoolean("fullscreen");

    {
        width = 15;
        height = 15;

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        model = rectangle;
    }

    public Checkbox() {
        super();
    }

    public Checkbox(String name) {
        super(name);
    }

    public boolean checked() {
        return checked;
    }

    public void setChecked(boolean value) {
        checked = value;
        try {
            getBinding().process();
        } catch (NullPointerException e) {}
    }

    @Override
    public void processEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            setChecked(checked() == false);
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            setChecked(checked() == false);
        }
    }

    @Override
    public void processEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_MOVED:
                if (model.isInside(e.getPoint())) {
                    requestFocus();
                }
                break;
            case MouseEvent.MOUSE_CLICKED:
                if (model.isInside(e.getPoint())) {
                    setChecked(checked() == false);
                }
                break;
        }
    }

    @Override
    public void render(Graphics2D g) {

        int x = (int) model.getPosition().getX();
        int y = (int) model.getPosition().getY();
        if (focused) {
            g.setBackground(background);
            g.clearRect(x, y, width, height);
        }
        g.setColor(color);
        g.drawRect(x, y, width, height);
        int offsetX = (int) model.getWidth() / 5;
        int offsetY = (int) model.getHeight() / 5;

        if (checked()) {
            g.drawLine(x + offsetX, y + offsetY, x + width - offsetX, y + height - offsetY);
            g.drawLine(x + offsetX, y + height - offsetY, x + width - offsetX, y + offsetY);
        }

        if (label != null) {
            g.setFont(font.deriveFont(height * 1.5f));
            g.drawString(label, x + width * 2, y + height);
        }
    }
}
