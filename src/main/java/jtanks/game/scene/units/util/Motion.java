/*
 * GNU General Public License v2
 * 
 * @version $Id: Motion.java 303 2009-07-23 13:38:31Z ru.energy $
 */
package jtanks.game.scene.units.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Motion implements Cloneable {

    public enum Direction {
        WEST, NORTH, EAST, SOUTH;
    }

    private Direction direction = Direction.NORTH;
    private Direction course = null;
    private double speed;
    private boolean locked = false;

    public Direction getDirection() {
        return direction;
    }

    public Direction getCourse() {
        if (course == null) {
            return direction;
        }
        return course;
    }

    /**
     * Set motion speed
     *
     * @param speed cells per second
     * @return
     */
    public Motion setSpeed(double speed) {
        this.speed = speed;
        return this;
    }
    
    public double getSpeed() {
        return getSpeed(false);
    }
    
    public double getRealSpeed() {
        return speed;
    }

    public double getSpeed(boolean ignore) {
        if (locked && ignore == false) {
            return 0;
        }
        return speed;
    }

    public void eraseCourse() {
        course = null;
    }
    
    public void setCourse(Direction course) {
        if (isValidDirection(course) == false) {
            throw new IllegalArgumentException("wrong course: "  + course);
        }
        this.course = course;
    }
    
    private boolean isValidDirection(Direction dir) {
        return Arrays.asList(Direction.values()).contains(dir);
    }

    public void setDirection(Direction dir) throws IllegalArgumentException {
        if (isValidDirection(dir) == false) {
            throw new IllegalArgumentException("wrong direction: "  + dir);
        }
        if (direction.equals(dir) == false) {
            setLock(false);
        }
        this.direction = dir;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLock(boolean lock) {
        if (lock) {
            eraseCourse();
        }
        locked = lock;
    }

    @Override
    public Motion clone() {
        Motion motion = new Motion();
        try {
            motion.setDirection(getDirection());
        } catch (Exception ex) {
            Logger.getLogger(Motion.class.getName()).log(Level.SEVERE, "Exception has occurred", ex);
        }
        motion.setSpeed(speed);
        return motion;
    }
    
    @Override
    public String toString() {
        return "speed: " + this.speed + ", direction " + this.getDirection() + ", locked: " + this.locked;
    }

}
