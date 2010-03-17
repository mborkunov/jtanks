/*
 * GNU General Public License v2
 *
 * @version $Id: Player.java 298 2009-07-23 13:20:43Z ru.energy $
 */
package jtanks.game.scene.units;

import jtanks.game.gameplay.State;
import jtanks.game.gameplay.StatisticsData;
import jtanks.system.Registry;
import jtanks.system.SoundManager;

public class Player extends Tank {

    public State state = Registry.get(State.class);

    public Player() {
        super();
        Registry.set("playerTank", this);
    }

    @Override
    public boolean removeNow() {
        Registry.remove("playerTank");
        StatisticsData stats = ((StatisticsData) Registry.get("statistics"));
        stats.setDeaths(stats.getDeaths() + 1);
        return super.removeNow();
    }

    @Override
    public boolean shoot() {
        boolean result;
        if ((result = super.shoot())) {
            StatisticsData stats = ((StatisticsData) Registry.get("statistics"));
            stats.setBullets(stats.getBullets() + 1);
            ((SoundManager) Registry.get(SoundManager.class)).play("shoot");
        }
        return result;
    }
}
