/*
 * GNU General Public License v2
 * 
 * @version $Id: Item.java 294 2009-07-23 13:11:08Z ru.energy $
 */
package jtanks.game.scene.items;

import jtanks.game.scene.Node;
import jtanks.game.scene.units.Tank;

public abstract class Item extends Node {

    protected Item() {
    }

    abstract public void influence(Tank tank);
}
