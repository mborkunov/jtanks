/*
 * GNU General Public License v2
 *
 * @version $Id: BattleFieldInfo.java 308 2009-07-23 13:43:01Z ru.energy $
 */
package jtanks.game.scene.gameplay;

import java.awt.Color;
import java.awt.Graphics2D;
import jtanks.game.scene.Node;

public class BattleFieldInfo extends Node {

    private BattleField field;

    protected BattleFieldInfo(BattleField field) {
        this.field = field;
    }

    @Override
    public void render(Graphics2D g) {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());

        g.setColor(Color.WHITE);

        g.drawString("spawned: " + String.valueOf(field.enemiesSpawned), 30, 50);
        g.drawString("remaining: " + String.valueOf(field.enemiesRemaining), 30, 100);

        super.render(g);
    }

    @Override
    public void update() {
        super.update();
    }

}
