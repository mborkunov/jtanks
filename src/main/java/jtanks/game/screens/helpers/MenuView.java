/*
 * GNU General Public License v2
 * 
 * @version $Id: MenuView.java 166 2009-05-14 20:04:22Z ru.energy $
 */
package jtanks.game.screens.helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import jtanks.game.screens.Screen;
import jtanks.system.ResourceManager;

public class MenuView {

    private jtanks.game.screens.helpers.Menu menu;
    private int lineHeight;
    private int fontSize;
    private VolatileImage cursor;

    private final Color ACTIVE_ITEM_COLOR = Color.WHITE;
    private final Color INACTIVE_ITEM_COLOR = Color.LIGHT_GRAY;

    public MenuView(jtanks.game.screens.helpers.Menu menu) {
        this.menu = menu;
        cursor = ResourceManager.getVolatileImage("cursor");
    }
    
    public void draw(Graphics2D g, Screen screen) {
        fontSize = screen.getHeight() / 25;
        lineHeight = screen.getHeight() / 15;

        Screen.lock.lock();
        try {
            if (screen.cache.get("menuFont") == null) {
                screen.cache.put("menuFont", screen.getFont(fontSize));
            }
            g.setFont((Font) screen.cache.get("menuFont"));
        } finally {
            Screen.lock.unlock();
        }

        float scale = ((float) fontSize) / cursor.getHeight();

        int offsetLeft = (int) (screen.getWidth() / 2 - cursor.getWidth() * scale);
        int offsetTop  = screen.getHeight() / 2 - (menu.size() * lineHeight) / 2;

        int i = 0;

        for (MenuItem item : menu.toArray()) {
            g.setColor(item.isSelected() ? ACTIVE_ITEM_COLOR : INACTIVE_ITEM_COLOR);
            g.drawString(item.toString(), offsetLeft, offsetTop + i++ * lineHeight);
        }

        /**
         * Draw pointer
         */
        AffineTransform transform = new AffineTransform();

        int left = (int) (offsetLeft - cursor.getWidth() * scale);
        float delta = (lineHeight - cursor.getHeight() * scale) / 4;
        int top  = (int) (offsetTop + menu.selectedIndex() * lineHeight - cursor.getHeight() * scale + delta);
        transform.translate(left, top);
        transform.scale(scale, scale);
        transform.rotate(Math.PI / 2);
        g.drawImage(cursor, transform, null);
    }

}
