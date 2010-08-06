/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.scene.gameplay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import jtanks.game.geometry.Position;
import jtanks.game.map.Map;
import jtanks.game.scene.Layer;
import jtanks.game.scene.Node;
import jtanks.game.scene.landscapes.Area;
import jtanks.game.scene.landscapes.Bricks;
import jtanks.game.scene.landscapes.Concrete;
import jtanks.game.scene.landscapes.Forest;
import jtanks.game.scene.landscapes.Ground;
import jtanks.game.scene.landscapes.MapNode;
import jtanks.game.scene.landscapes.Water;
import jtanks.game.scene.units.Base;
import jtanks.game.scene.units.Enemy;
import jtanks.game.scene.units.Player;
import jtanks.game.screens.Preloadable;
import jtanks.game.util.Cache;
import jtanks.system.Registry;

public final class BattleField extends Node implements Preloadable {

    public static BattleField instance;

    public Map map;
    private int areaSize;
    public int enemiesRemaining = 10;
    public int enemiesSpawned;
    private MapNode forestMap;
    private Layer mainLayer;
    private float progress = 0;

    public BattleField(Map map) {
        this.map = map;
        Class<Area>[] areas;

        Player player = new Player();
        player.getModel().setPosition(map.getPlayerPosition().clone());

        areas = new Class[]{Ground.class, Bricks.class, Concrete.class, Water.class};

        MapNode mapNode = new MapNode(map, Arrays.asList(areas));

        mainLayer = new Layer();
        mainLayer.addChild(mapNode);
        mainLayer.addChild(player);
        mainLayer.addChild(new Base(map.getBasePosition()));

        addChild(mainLayer);

        if (map.hasForest()) {
            areas = new Class[]{Forest.class};
            forestMap = new MapNode(map, Arrays.asList(areas));

            Layer forestLayer = new Layer();
            forestLayer.addChild(forestMap);

            addChild(forestLayer);
        }
        Cache.GLOBAL.remove("scale");

        instance = this;
    }

    @Override
    public void update() {
        if (Math.random() > .99) {
            spawnEnemy();
        }
        super.update();
    }

    @Override
    public void render(final Graphics2D g) {
        final int width = (int) g.getClipBounds().getWidth();
        final int height = (int) g.getClipBounds().getHeight();

        Cache.GLOBAL.putIfAbsent("scale", getScale(width, height));

        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, width, height);

        super.render(g);
        renderModel(g);
    }

    private float getScale(final int width, final int height) {
        return (Float) cache.get("scale", new Callable<Float>() {
            public Float call() {
                areaSize = 0;

                if (areaSize == 0) {
                    if (map.getHeight() >= map.getWidth()) {
                        areaSize = Math.round(height / (float) map.getHeight());
                    } else {
                        areaSize = Math.round(width / (float) map.getWidth());
                    }
                }

                if (areaSize % 2 != 0) {
                    areaSize--;
                }

                Cache.GLOBAL.put("areaSize", areaSize);

                int defaultImageSize = new Ground(0, 0).getImage().getWidth();

                return (float) (areaSize / (float) defaultImageSize);
            }
        });
    }

    public void preload() {
        try {
            forestMap.preload();
        } catch (NullPointerException ignored) {}
    }

    public int getProgress() {
        /**
         * @todo: make preloading
         */
        progress += 3;
        return (int) progress;
    }

    public boolean isPlayerAlive() {
        return Registry.get("playerTank") != null;
    }

    public boolean isBaseAlive() {
        return Registry.get("base") != null;
    }

    private void spawnEnemy() {
        if (enemiesRemaining == 0) {
            return;
        }

        if (enemiesSpawned > 3) {
            return;
        }

        ArrayList<Position> busy = new ArrayList<Position>();

        Enemy enemy = new Enemy();
        Position position = getRandomSpawnPosition(busy);
        enemy.getModel().setPosition(position.clone());

        /*boolean done = false;

        do {
            Position position = getRandomSpawnPosition(busy);
            try {
                enemy.getModel().setPosition(position.clone());
            } catch (NullPointerException e) {
                System.out.println("false");
                return;
            }


            boolean collision = false;
            if (hasCollision(enemy).size() > 0) {
                busy.add(position);
                System.out.println("busy");
                collision = true;
                break;
            }
            if (collision == false) {
                done = true;
            }
        } while (done == false || busy.size() < map.getEnemySpawnPositions().length);

        if (busy.size() == map.getEnemySpawnPositions().length) {
            return;
        }
         */

        mainLayer.addChild(enemy);
        enemiesSpawned++;
        enemiesRemaining--;

    }

    private Position getRandomSpawnPosition(List<Position> exclude) {
        if (exclude.size() == map.getEnemySpawnPositions().length) {
            return null;
        }

        Random random = new Random();
        List<Position> positions = Arrays.asList(map.getEnemySpawnPositions());


        Position position;

        position = positions.get(random.nextInt(positions.size()));
        return position;

        /*boolean done = false;
        do {
            if (positions.isEmpty()) {
                return null;
            }

            position = positions.get(random.nextInt(positions.size()));

            if (exclude.contains(position)) {
                positions.remove(position);
            } else {
                done = true;
            }
        } while (done == false);

        return position;*/
    }

    private void spawnPlayer() {
        //@todo:
    }

    public boolean hasEnemies() {
        return enemiesSpawned + enemiesRemaining > 0 ? true : false;
    }
}
