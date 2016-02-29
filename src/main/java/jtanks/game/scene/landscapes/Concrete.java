package jtanks.game.scene.landscapes;

import jtanks.game.scene.Node;
import jtanks.game.scene.units.Unit;

public class Concrete extends Destroyable {

    public Concrete(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }

}
