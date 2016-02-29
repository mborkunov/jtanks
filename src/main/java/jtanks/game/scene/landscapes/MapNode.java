package jtanks.game.scene.landscapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.game.map.Map;
import jtanks.game.scene.Node;
import jtanks.game.screens.Preloadable;

public class MapNode extends Node implements Preloadable {

    private VolatileImage image;
    private Map map;
    private List<Class<Area>> areas;

    public MapNode(Map map, List<Class<Area>> areas) {
        this.map = map;
        this.areas = areas;
        fillChildren();
    }

    private void fillChildren() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                String className = map.getArea(x, y).name().toLowerCase();
                className = className.substring(0, 1).toUpperCase() + className.substring(1);
                className = getClass().getPackage().getName() + '.' + className;
                try {
                    Class areaClass = Class.forName(className);
                    if (areas.contains(areaClass)) {
                        Constructor<Area>[] constructors = (Constructor<Area>[]) areaClass.getConstructors();
                        Area area = constructors[0].newInstance(x, y);
                        addChild(area);
                    } else if (areaClass.equals(Forest.class)) {
                        addChild(new Ground(x, y));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MapNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void render(final Graphics2D g) {
        if (renderer != null) {
            renderer.render(g);
        } else {
            if (true || image == null) {
                int width = (int) g.getClipBounds().getWidth();
                int height = (int) g.getClipBounds().getHeight();

                int transparency = (areas.size() == 1) ? Transparency.BITMASK : Transparency.OPAQUE;
                image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                           .getDefaultScreenDevice()
                                           .getDefaultConfiguration()
                                           .createCompatibleVolatileImage(width, height, transparency);

                Graphics2D g2 = image.createGraphics();
                if (areas.size() > 1) {
                    g2.setBackground(Color.BLACK);
                } else {
                    g2.setBackground(new Color(0, 0, 0, 0));
                }
                g2.clearRect(0, 0, width, height);
                super.render(g2);
            }
            g.drawImage(image, null, null);
        }
    }

    public void preload() {
    }

    public int getProgress() {
        return -1;
    }
}
