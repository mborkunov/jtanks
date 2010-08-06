/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens.helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import jtanks.system.ResourceManager;

public class Overlay implements Renderable {

    protected int width;

    protected int height;

    /**
     * Start left top point
     */
    protected Point point;

    public Overlay() {
    }

    public Overlay(int width, int height, Point point) {
        this.width = width;
        this.height = height;
        this.point = point;
    }
    /**
     * Get the value of point
     *
     * @return the value of point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Set the value of point
     *
     * @param point new value of point
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Get the value of height
     *
     * @return the value of height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the value of height
     *
     * @param height new value of height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the value of width
     *
     * @return the value of width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the value of width
     *
     * @param width new value of width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    public void render(Graphics2D g) {
        Image overlayImage = ResourceManager.getImage("overlay");

        AffineTransform trans = new AffineTransform();
        float scaleX = width / (float) overlayImage.getWidth(null);
        float scaleY = height / (float) overlayImage.getHeight(null);
        trans.scale(scaleX, scaleY);

        g.drawImage(overlayImage, trans, null);
    }

}
