/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.scene.units;

import java.awt.*;

import jtanks.game.gameplay.StatisticsData;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.geometry.Position;
import jtanks.game.geometry.Rectangle;
import jtanks.game.scene.Node;
import jtanks.system.Registry;
import jtanks.system.SoundManager;

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
            Registry.get(SoundManager.class).play("bullets_impact");
        } else if (getOwner().getClass().equals(Player.class) && unit.getClass().equals(Enemy.class)) {
            unit.lock();
            unit.remove();
            removeNow();
            StatisticsData stats = Registry.get(StatisticsData.class);
            stats.setTanks(stats.getTanks() + 1);
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
