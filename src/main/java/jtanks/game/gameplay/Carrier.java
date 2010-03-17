/*
 * GNU General Public License v2
 * 
 * @version $Id: Carrier.java 99 2008-06-23 00:40:15Z ru.energy $
 */

package jtanks.game.gameplay;

/**
 */
public class Carrier {

    private static Carrier instance;

    private int level;

    private Carrier() {
        level = 1;
    }

    public static Carrier getInstance() {
        if (instance == null) {
            instance = new Carrier();
        }
        return instance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
