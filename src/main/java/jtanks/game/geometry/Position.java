/*
 * GNU General Public License v2
 * 
 * @version $Id: Position.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.geometry;

public class Position {

    /**
     * X axis value
     */
    protected double x;
    /**
     * Y axis value
     */
    protected double y;

    /**
     * Protects from creating object with empty coordinates
     */
    private Position() {
    }

    /**
     * Creates a Position object with given coordinates
     * @param x X axis value
     * @param y Y axis value
     */
    public Position(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public double getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(double x) {
//        if (x < 0) {
//            throw new IllegalArgumentException("value cannot less than zero");
//        }
        this.x = x;
    }

    /**
     * Get the value of y
     *
     * @return the value of y
     */
    public double getY() {
        return y;
    }

    /**
     * Set the value of y
     *
     * @param y new value of y
     */
    public void setY(double y) {
//        if (y < 0) {
//            throw new IllegalArgumentException("value cannot less that zero");
//        }
        this.y = y;
    }

    /**
     * Creates a copy of current object with the same coordinates
     *
     * @return A copy
     */
    @Override
    public Position clone() {
        return new Position(getX(), getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Position.class) == false) {
            return false;
        }
        Position object = (Position) obj;
        if (object.getX() == getX() && object.getY() == getY()) {
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        long hash = 7;
        hash = 97 * hash + Double.doubleToLongBits(x);
        hash = 97 * hash + Double.doubleToLongBits(y);
        return (int) hash;
    }

    @Override
    public String toString() {
        return "x: " + this.x + ", y: " + this.y;
    }
}
