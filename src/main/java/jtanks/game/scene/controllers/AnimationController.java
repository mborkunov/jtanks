/*
 * GNU General Public License v2
 *
 * @version $Id: AnimationController.java 309 2009-07-23 13:43:20Z ru.energy $
 */
package jtanks.game.scene.controllers;

import jtanks.game.scene.units.Unit;

public class AnimationController extends Controller {

    private int counter = 0;
    private long lastUpdate = System.currentTimeMillis();

    @Override
    public void update(long time) {

        if (System.currentTimeMillis() - lastUpdate > node.getAnimationTime()) {
            lastUpdate = System.currentTimeMillis();

            if (counter == node.getAnimationImages().size()) {
                if (node instanceof Unit) {
                    node.hide();
                    node.removeNow();
                    return;
                } else {
                    counter = 0;
                }
            }
            node.setImage(node.getAnimationImages().get(counter++));
        }
    }
}