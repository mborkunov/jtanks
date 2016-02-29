package jtanks.game.geometry;

import java.awt.Point;

public interface Model extends Cloneable {

    Position getOffset();

    void setOffset(Position offset);

    double getWidth();

    double getHeight();

    void setWidth(double width);

    void setHeight(double height);

    //public void setBounds(int x, int y, int width, int height);
    boolean intersects(Model model);

    boolean isInside(Point point);

    Position getPosition();

    void setPosition(Position position);

    Object clone();
}
