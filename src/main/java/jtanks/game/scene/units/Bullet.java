/*
 * GNU General Public License v2
 *
 * @version $Id: Bullet.java 301 2009-07-23 13:29:23Z ru.energy $
 */
package jtanks.game.scene.units;

import java.awt.Graphics2D;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.geometry.Position;
import jtanks.game.geometry.Rectangle;
import jtanks.game.scene.Node;

public class Bullet extends Unit {

    public Bullet() {
        super();
        motion.setSpeed(6);
        model = new Rectangle();
        model.setWidth(.25);
        model.setHeight(.5);
        model.setOffset(new Position(0.375, .25));
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

    public Tank getOwner() {
        return (Tank) parent;
    }

    @Override
    public void offscreenAction(Motion.Direction direction) {
        remove();
    }

    @Override
    public void affect(Unit unit) {

        if (unit.getClass().equals(getClass())) {
            this.removeNow();
            unit.remove();
        } else if (getOwner().getClass().equals(Player.class) && unit.getClass().equals(Enemy.class)) {
            unit.lock();
            unit.remove();
            removeNow();
        } else if (getOwner().getClass().equals(Enemy.class) && unit.getClass().equals(Player.class)) {
            unit.lock();
            unit.remove();
            removeNow();
        } else if (getOwner().getClass().equals(Enemy.class) && unit.getClass().equals(Enemy.class)) {
            removeNow();
        } else {
            unit.lock();
            unit.remove();
            removeNow();
        }
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }
}
