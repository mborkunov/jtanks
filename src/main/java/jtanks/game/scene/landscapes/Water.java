package jtanks.game.scene.landscapes;

import java.awt.image.VolatileImage;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import jtanks.game.scene.Node;
import jtanks.game.scene.controllers.AnimationController;
import jtanks.game.scene.units.Bullet;
import jtanks.game.scene.units.Unit;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;

public class Water extends Area {

    /**
     * Create water node with given coordinates
     *
     * @param x X axis
     * @param y Y axis
     */
    public Water(int x, int y) {
        super(x, y);
        addController(new AnimationController());
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }

    @Override
    public void affect(Unit unit) {
        if (unit.getClass().equals(Bullet.class) == false) {
            unit.getMotion().setLock(true);
        }
    }

    /**
     * Return animation delay between image changing
     *
     * @return delay in milliseconds
     */
    @Override
    public int getAnimationTime() {
        return 200;
    }

    @Override
    public List<VolatileImage> getAnimationImages() {
        String key = "animation-" + getClass().getName();
        return (List<VolatileImage>) Cache.GLOBAL.get(key, new Callable<List<VolatileImage>>() {
            public List<VolatileImage> call() throws Exception {
                List<URL> resources = ResourceManager.getAnimationResources("sprites/landscapes/water");
                return ResourceManager.getImages(resources);
            }
        });
    }
}
