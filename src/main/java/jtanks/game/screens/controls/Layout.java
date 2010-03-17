/*
 * GNU General Public License v2
 *
 * @version $Id: Layout.java 262 2009-07-12 11:01:27Z ru.energy $
 */
package jtanks.game.screens.controls;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import jtanks.game.geometry.Position;
import jtanks.game.scene.Node;

public class Layout extends Node {

    private int offsetX;
    private int offsetY;

    @Override
    public void addChild(Node child) {
        int size = 1;
        lock.lock();
        try {
            size += children.size();
        } catch (NullPointerException e) {
            ((Control) child).requestFocus();
        } finally {
            lock.unlock();
        }
        /*
        int width = (int) g.getClipBounds().getWidth();
        int height = (int) g.getClipBounds().getHeight();
        int offsetX = width / 10;
        int offsetY = height / 8;
        */

        ((Control) child).setPosition(new Position(50, 50 * size));

        super.addChild(child);
    }

    public void dispatchEvent(KeyEvent e) {
        lock.lock();
        try {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    for (int i = 0; i < children.size(); i++) {
                        Control child = (Control) children.get(i);
                        if (child.hasFocus()) {
                            Control focusChild = null;
                            if (e.isShiftDown()) {
                                if (i == 0) {
                                    focusChild = ((Control) children.get(children.size() - 1));
                                } else {
                                    focusChild = ((Control) children.get(i - 1));
                                }
                                focusChild.requestFocus();
                            } else {
                                if (i + 1 == children.size()) {
                                    focusChild = ((Control) children.get(0));
                                } else {
                                    focusChild = ((Control) children.get(i + 1));
                                }
                            }
                            focusChild.requestFocus();
                            break;
                        }
                    }
                    return;
                }
            }

            for (Node child : getChildren()) {
                if (((Control) child).focused) {
                    ((Control) child).processEvent(e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void render(Graphics2D g) {
        int width = (int) g.getClipBounds().getWidth();
        int height = (int) g.getClipBounds().getHeight();
        offsetX = width / 10;
        offsetY = height / 8;

        Graphics2D g2 = (Graphics2D) g.create(offsetX, offsetY, width - offsetX * 2, height - offsetY * 2);
        g2.drawRect(0, 0, width - offsetX * 2 - 1, height - offsetY * 2 - 1);
        super.render(g2);
        g2.dispose();
    }

    public void dispatchEvent(MouseEvent e) {
        e.translatePoint(- offsetX, - offsetY);
        lock.lock();
        try {
            for (Node child : getChildren()) {
                ((Control) child).processEvent(e);
            }
        } finally {
            lock.unlock();
        }
    }
}

