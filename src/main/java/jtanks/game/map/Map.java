/*
 * GNU General Public License v2
 * 
 * @version $Id: Map.java 293 2009-07-23 12:53:09Z ru.energy $
 */
package jtanks.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.game.geometry.Position;
import jtanks.game.scene.landscapes.Area;
import jtanks.game.scene.landscapes.Ground;

public class Map {

    public enum Type {
        GROUND, WATER, FOREST, BRICKS, CONCRETE, OFFSCREEN;
    }

    /**
     * Map data storage
     */
    final int[] mapKeys = {"[NAME]".hashCode(),
                           "[ABOUT]".hashCode(),
                           "[WIDTH]".hashCode(),
                           "[HEIGHT]".hashCode(),
                           "[LAND]".hashCode()};
    private Type[][] map;
    private String path;
    private String name;
    private String about;
    private int    width;
    private int    height;
    private boolean hasForest;
    private boolean hasWater;
    private Position[] spawns;
    private Position playerPosition;

    public Map(String path) {
        this.path = path;

        try {
            getData();
        } catch(IOException e) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, "Error has occurred while reading map file", e);
        }

        spawns = new Position[3];

        byte y = 0;
        spawns[0] = new Position(getWidth() - 1, y);
        spawns[1] = new Position(getWidth() / 2, y);
        spawns[2] = new Position(0, y);

        playerPosition = getBasePosition().clone();
        playerPosition.setX(playerPosition.getX() - 1);
    }

    /**
     * Fill map with random areas
     */
    private void getData() throws IOException {
        int key;
        String line;
        BufferedReader mapStream = new BufferedReader(
                                       new InputStreamReader(getClass().getResourceAsStream(this.path)));
        for (int i = 0; i < mapKeys.length; i++) {
             key = mapStream.readLine().hashCode();
             if (key == mapKeys[0]) {//get map name
                 this.name = mapStream.readLine();
             } else if (key == mapKeys[1]) {//get map about
                 this.about = mapStream.readLine();
             } else if (key == mapKeys[2]) {//get map width
                 this.width = Integer.parseInt(mapStream.readLine());
             } else if (key == mapKeys[3]) {//get map height
                 this.height = Integer.parseInt(mapStream.readLine());
             } else if (key == mapKeys[4]) {//get map land
                 this.map = new Type[this.width][this.height];
                 for (int y = 0; y < this.height; y++) {
                     line = mapStream.readLine();
                     for (int x = 0; x < this.width; x++) {
                         if (line.length() <= x) {
                             map[x][y] = Type.GROUND;
                             continue;
                         }
                         switch (line.charAt(x)) {
                             case ' ': map[x][y] = Type.GROUND; break;
                             case 'w': map[x][y] = Type.WATER; hasWater = true; break;
                             case 'b': map[x][y] = Type.BRICKS; break;
                             case 'f': map[x][y] = Type.FOREST; hasForest = true; break;
                             case 'c': map[x][y] = Type.CONCRETE; break;
                         }
                     }
                 }
             }
        }
    }

    /**
     * Return map width
     * 
     * @return Width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Return map height
     * 
     * @return Height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Return map name
     * 
     * @return Current map name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return area type on the given position
     * 
     * @param x Offset x
     * @param y Offset y
     * @return
     */
    public Map.Type getArea(int x, int y) {
        if (x >= 0  && x < getWidth() && y >= 0 && y < getHeight()) {
            return map[x][y];
        }
        return Type.OFFSCREEN;
    }

    /**
     * Check availability forest on the current map
     * 
     * @return 
     */
    public boolean hasForest() {
        return hasForest;
    }

    public boolean hasWater() {
        return hasWater;
    }

    /**
     * Create current map preview image
     * @param sideSize preview box side size
     * @return
     */
    public VolatileImage createPreviewImage(int sideSize) {
        VolatileImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                 .getDefaultScreenDevice()
                                                 .getDefaultConfiguration()
                                                 .createCompatibleVolatileImage(sideSize, sideSize, Transparency.OPAQUE);

        Graphics2D g = image.createGraphics();
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, sideSize, sideSize);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform transform;
        Double length = (1.0 * sideSize) / (getWidth() > getHeight() ? getWidth() : getHeight());

        Double scale = length / new Ground(0, 0).getImage().getWidth();

        Double dx = 0.0, dy = 0.0;

        if (getWidth() > getHeight()) {
            dy = (sideSize - (getHeight() * length)) / 2.0;
        } else if (getWidth() < getHeight()) {
            dx = (sideSize - (getWidth() * length)) / 2.0;
        }

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                String className = getArea(x, y).name().toLowerCase();
                className = className.substring(0, 1).toUpperCase() + className.substring(1);
                className = Ground.class.getPackage().getName() + '.' + className;
                Area area = null;
                try {
                    Constructor<Area>[] constructors;
                    constructors = (Constructor<Area>[]) Class.forName(className).getConstructors();
                    area = constructors[0].newInstance(x, y);
                } catch (Exception ex) {
                    Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
                }

                transform = new AffineTransform();
                transform.translate(dx, dy);
                transform.translate(length * x, length * y);
                transform.scale(scale, scale);
                g.drawImage(area.getImage(), transform, null);
            }
        }

        return image;
    }

    public Position getBasePosition() {
        return new Position(getWidth() / 2, getHeight() - 1);
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public Position[] getEnemySpawnPositions() {
        return spawns;
    }

    @Override
    public String toString() {
        return getName() + " : " + getWidth() + "x" + getHeight();
    }
}
