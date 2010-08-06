/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens.controls;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Input extends Control {

    @Override
    public void processEvent(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processEvent(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void render(Graphics2D g) {
        int x = (int) model.getPosition().getX();
        int y = (int) model.getPosition().getY();
        g.drawRect(x, y, width, height);
        super.render(g);
    }
}
