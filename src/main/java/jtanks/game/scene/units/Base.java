/*
 * GNU General Public License v2
 *
 * @version $Id: Base.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.game.scene.units;

import java.awt.Graphics2D;
import jtanks.game.geometry.Position;
import jtanks.game.geometry.Rectangle;
import jtanks.game.scene.Node;
import jtanks.game.scene.units.util.Motion.Direction;
import jtanks.system.Registry;

public class Base extends Unit {

    private Base() {
    }
    
    public Base(Position position) {
        super();
        Rectangle rectangle = new Rectangle();

        rectangle.setPosition(position);
        rectangle.setOffset(new Position(.125, .075));
        rectangle.setWidth(.75);
        rectangle.setHeight(.85);
        setModel(rectangle);
        Registry.set("base", this);
    }

    @Override
    public boolean removeNow() {
        Registry.remove("base");
        return super.removeNow();
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
    }

    @Override
    public void offscreenAction(Direction direction) {
        // do nothing
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }

    @Override
    public void affect(Unit unit) {
        if (unit.getClass().equals(Bullet.class) == false) {
            unit.getMotion().setLock(true);
        }
    }
}
