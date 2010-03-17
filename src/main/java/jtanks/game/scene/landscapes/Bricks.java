/*
 * GNU General Public License v2
 *
 * @version $Id: Bricks.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.scene.landscapes;

import jtanks.game.scene.Node;
import jtanks.game.scene.units.Unit;

public class Bricks extends Destroyable {

    public Bricks(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }
}