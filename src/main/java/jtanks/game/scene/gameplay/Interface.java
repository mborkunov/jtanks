/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.scene.gameplay;

import java.awt.Color;
import java.awt.Graphics2D;
import jtanks.game.map.Map;
import jtanks.game.scene.Node;
import jtanks.game.screens.Preloadable;
import jtanks.system.Registry;

public class Interface extends Node implements Preloadable {

    public Interface() {
        BattleField field = new BattleField((Map) Registry.get("map"));
        Registry.remove("map");
        addChild(field);
        field.setParent(null);
        addChild(new BattleFieldInfo(field));
    }

    @Override
    public void render(Graphics2D g) {
        int width  = (int) g.getClip().getBounds().getWidth();
        int height = (int) g.getClip().getBounds().getHeight();

        Color borderColor = Color.DARK_GRAY;
        int borderSize = 3;
        int offset = 10;

        int battleFieldWidth = (int) (width * .8f - offset * 2 - borderSize * 2);
        int battleFieldHeight = height - offset * 2 - borderSize * 2;

        int battleInfoWidth =  width - battleFieldWidth - offset * 3 - borderSize * 4;
        int battleInfoHeight = battleFieldHeight;

        g.setBackground(borderColor);
        g.clearRect(offset, offset, (int) (width * .8f) - offset * 2, height - offset * 2);

        g.setBackground(borderColor);
        g.clearRect(battleFieldWidth + offset * 2 + borderSize * 2, offset, width - battleFieldWidth - offset * 3 - borderSize * 2, battleFieldHeight  + borderSize * 2);

        Graphics2D g2 = (Graphics2D) g.create(offset + borderSize, offset + borderSize, battleFieldWidth, battleFieldHeight);
        Graphics2D g3 = (Graphics2D) g.create(battleFieldWidth + offset * 2 + borderSize * 3, offset + borderSize, battleInfoWidth, battleInfoHeight);

        children.get(0).render(g2);
        children.get(1).render(g3);

        g2.dispose();
        g3.dispose();
    }

    public void preload() {
        ((Preloadable) children.get(0)).preload();
    }

    public int getProgress() {
        return ((Preloadable) children.get(0)).getProgress();
    }

}
