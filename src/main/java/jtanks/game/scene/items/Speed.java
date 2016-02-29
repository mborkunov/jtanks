package jtanks.game.scene.items;

import jtanks.game.scene.units.Tank;

public class Speed extends Item {

    /**
     * Temporary increase unit speed on this value
     */
    private float step = 3f;

    /**
     * Exposure time
     */
    private int time = 5000;

    @Override
    public void influence(final Tank tank) {
    }

    
}
