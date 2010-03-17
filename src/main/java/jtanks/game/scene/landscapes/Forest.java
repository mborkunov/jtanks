/*
 * GNU General Public License v2
 *
 * @version $Id: Forest.java 295 2009-07-23 13:12:21Z ru.energy $
 */
package jtanks.game.scene.landscapes;

import java.awt.image.VolatileImage;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import jtanks.game.scene.controllers.AnimationController;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;

public class Forest extends Area {

    public Forest(int x, int y) {
        super(x, y);
        addController(new AnimationController());
    }

    @Override
    public List<VolatileImage> getAnimationImages() {
        String key = "animation-" + getClass().getName();
        return (List<VolatileImage>) Cache.GLOBAL.get(key, new Callable<List<VolatileImage>>() {
            public List<VolatileImage> call() throws Exception {
                List<URL> resources = ResourceManager.getAnimationResources("sprites/landscapes/forest");
                return ResourceManager.getImages(resources);
            }
        });
    }



}
