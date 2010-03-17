/*
 * GNU General Public License v2
 *
 * @version $Id: Model.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.geometry;

import java.awt.Point;

public interface Model extends Cloneable {

    public Position getOffset();
    public void setOffset(Position offset);

    public double getWidth();
    public double getHeight();
    public void setWidth(double width);
    public void setHeight(double height);

    //public void setBounds(int x, int y, int width, int height);
    public boolean intersects(Model model);
    public boolean isInside(Point point);

    public Position getPosition();
    public void setPosition(Position position);

    public Object clone();
}
