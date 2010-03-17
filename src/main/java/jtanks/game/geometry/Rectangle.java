/*
 * GNU General Public License v2
 *
 * @version $Id: Rectangle.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.geometry;

import java.awt.Point;

public class Rectangle implements Model, Cloneable {

    public double width = 0;
    public double height = 0;

    protected Position position = new Position(0, 0);

    protected Position offset = new Position(0, 0);

    public Position getPosition() {
        return position;
    }

    public void setOffset(Position offset) {
        this.offset = offset;
    }

    public Position getOffset() {
        return offset;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCenterX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getCenterY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean intersects(Model model) {
        if (model.getClass().equals(getClass())) {
            double tw = width;
            double th = height;
            double rw = model.getWidth();
            double rh = model.getHeight();
            if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
                return false;
            }
            double tx = this.getPosition().getX() + this.getOffset().getX();
            double ty = this.getPosition().getY() + this.getOffset().getY();
            double rx = model.getPosition().getX() + model.getOffset().getX();
            double ry = model.getPosition().getY() + model.getOffset().getY();
            rw += rx;
            rh += ry;
            tw += tx;
            th += ty;
            //      overflow || intersect
            return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry));
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "Rectangle: position=" + position + " width=" + width + " height = " + height;
    }

    @Override
    public Object clone() {
        Model model = new Rectangle();
        model.setPosition(getPosition().clone());
        model.setOffset(getOffset().clone());
        model.setHeight(getHeight());
        model.setWidth(getWidth());
        return model;
    }

    public boolean isInside(Point point) {
        if (point.getX() < (getPosition().getX() + getOffset().getX())) {
            return false;
        }
        if (point.getY() < (getPosition().getY() + getOffset().getY())) {
            return false;
        }
        if (point.getX() > (getPosition().getX() + getOffset().getX()) + width) {
            return false;
        }
        if (point.getY() > (getPosition().getY() + getOffset().getY()) + height) {
            return false;
        }
        return true;
    }
}
