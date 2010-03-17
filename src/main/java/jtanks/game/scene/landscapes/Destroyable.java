/*
 * GNU General Public License v2
 *
 * @version $Id: Destroyable.java 297 2009-07-23 13:19:13Z ru.energy $
 */
package jtanks.game.scene.landscapes;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.concurrent.Callable;
import jtanks.game.scene.Node;
import jtanks.game.scene.units.Bullet;
import jtanks.game.scene.units.Unit;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;


public strictfp class Destroyable extends Area {

    protected boolean modified = false;

    public Destroyable(int x, int y) {
        super(x, y);
    }

    @Override
    public void affect(Unit unit) {
        if (unit.getClass().equals(Bullet.class) == false) {
            unit.getMotion().setLock(true);
        } else {
            final int pieces = 4;

            switch (unit.getMotion().getDirection()) {
                case NORTH:
                    model.setHeight(model.getHeight() - defaultModel.getHeight() / pieces);
                    break;
                case SOUTH:
                    model.setHeight(model.getHeight() - defaultModel.getHeight() / pieces);
                    model.getOffset().setY(model.getOffset().getY() + defaultModel.getHeight() / pieces);
                    break;
                case WEST:
                    model.setWidth(model.getWidth() - defaultModel.getWidth() / pieces);
                    break;
                case EAST:
                    model.setWidth(model.getWidth() - defaultModel.getWidth() / pieces);
                    model.getOffset().setX(model.getOffset().getX() + defaultModel.getWidth() / pieces);
                    break;
            }
            modified = true;

            if (model.getWidth() * model.getHeight() <= 0) {
                parent.replaceChild(this, new Ground(x, y));
            }
            unit.lock();
            unit.remove();
        }
    }

    /**
     * Return modification flag
     * 
     * @return
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Render destroyable object
     * 
     * @param g
     */
    @Override
    public void render(final Graphics2D g) {
        final VolatileImage groundImage = ResourceManager.getImage("sprites/landscapes/ground");

        final AffineTransform transform = new AffineTransform();
        float scale = (Float) Cache.GLOBAL.get("scale");
        transform.scale(scale, scale);
        transform.translate(groundImage.getWidth() * x, groundImage.getHeight() * y);

        BufferedImage img = (BufferedImage) cache.get("image", new Callable<BufferedImage>() {
            public BufferedImage call() throws Exception {
                final VolatileImage areaImage = getImage();
                BufferedImage img = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                               .getDefaultScreenDevice()
                                               .getDefaultConfiguration()
                                               .createCompatibleImage(areaImage.getWidth(), areaImage.getHeight(), Transparency.BITMASK);

                img.createGraphics().drawImage(areaImage, null, null);
                return img;
            }
        });

        if (modified) {
            int x1, y1, width, height;
            x1 = (int) (model.getOffset().getX() * groundImage.getWidth());
            y1 = (int) (model.getOffset().getY() * groundImage.getHeight());
            width = (int) (model.getWidth() * img.getWidth());
            height = (int) (model.getHeight() * img.getHeight());

            g.drawImage(groundImage, transform, null);

            if (x1 >= 0 && y1 >= 0 && width > 0 && height > 0) {
                transform.translate(x1, y1);
                g.drawImage(img.getSubimage(x1, y1, width, height), transform, null);
                transform.translate(-x1, -y1);
            }
        } else {
            g.drawImage(img, transform, null);
        }

        lock.lock();
        try {
            if (children != null) {
                for (Node child : children) {
                    child.render(g);
                }
            }
        } finally {
            lock.unlock();
        }
    }

}
