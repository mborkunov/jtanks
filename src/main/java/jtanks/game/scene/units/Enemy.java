/*
 * GNU General Public License v2
 *
 * @version $Id: Enemy.java 301 2009-07-23 13:29:23Z ru.energy $
 */
package jtanks.game.scene.units;

import jtanks.game.scene.controllers.AIController;
import jtanks.game.scene.gameplay.BattleField;
import jtanks.game.scene.units.util.Motion;

public class Enemy extends Tank {

    protected String registryKey = "enemies";

    public Enemy() {
        super();
        maxBullets = 4;
        fireDelay = 500;
        angle = 270;
        motion.setDirection(Motion.Direction.SOUTH);
        addController(new AIController());
    }

    @Override
    public boolean removeNow() {
        if (super.removeNow()) {
            BattleField.instance.enemiesSpawned--;
            return true;
        }
        return false;
    }

    /**
     * Apply damage to enemy tank. Also update statistics
     * @todo Tank can be destroed or not. Update statistics only in first case.
     *
     * @param element
     */
    /*@Override
    public void damage(Element element) {
        StatisticsData stats = (StatisticsData) (Registry.get("statistics"));
        stats.setTanks(stats.getTanks() + 1);
        super.damage(element);
        ((SoundManager) Registry.get(SoundManager.class)).play("enemy_destroyed");
    }*/
}