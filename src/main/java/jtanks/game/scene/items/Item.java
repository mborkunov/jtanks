package jtanks.game.scene.items;

import jtanks.game.scene.Node;
import jtanks.game.scene.units.Tank;

public abstract class Item extends Node {

    protected Item() {
    }

    abstract public void influence(Tank tank);
}
