/*
 * GNU General Public License v2
 *
 * @version $Id: Unit.java 301 2009-07-23 13:29:23Z ru.energy $
 */
package jtanks.game.scene.units;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.Arrays;
import java.util.concurrent.Callable;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.geometry.Model;
import jtanks.game.geometry.Position;
import jtanks.game.scene.Node;
import jtanks.game.scene.controllers.UnitController;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;

public abstract class Unit extends Node {

    protected boolean locked = false;
    protected Motion motion;

    public Unit() {
        motion = new Motion();
        addController(new UnitController());
    }

    /**
     * Get the value of motion
     *
     * @return the value of motion
     */
    public Motion getMotion() {
        return motion;
    }

    public abstract void offscreenAction(Motion.Direction direction);

    /**
     * Set the value of motion
     *
     * @param motion new value of motion
     */
    public void setMotion(Motion motion) {
        this.motion = motion;
    }

    @Override
    public void render(Graphics2D g) {
        lock.lock();

        try {
            if (hidden == false)  {
                final String cls = getClass().getSimpleName().toLowerCase();
                Image image = (Image) cache.get("sprite", new Callable<Image>() {
                    public Image call() {
                        return ResourceManager.getImage("sprites/units/" + cls);
                    }
                });
                
                float scale = (Float) Cache.GLOBAL.get("scale");
                int width = (int) (image.getWidth(null) * scale);
                int height = (int) (image.getHeight(null) * scale);

                image = image.getScaledInstance(width, height, 0);

                AffineTransform transform = new AffineTransform();
                int size = (Integer) Cache.GLOBAL.get("areaSize");
                int x = (int) (getModel().getPosition().getX() * size - image.getWidth(null) / 2f);
                int y = (int) (getModel().getPosition().getY() * size - image.getHeight(null) / 2f);

                transform.translate(image.getWidth(null), image.getHeight(null));
                transform.translate(x, y);

                if (this instanceof Tank) {
                    transform.rotate((((Tank) this).angle - 90) * Math.PI / 180);
                } else {
                    int index = Arrays.asList(Motion.Direction.values()).indexOf(getMotion().getDirection());
                    transform.rotate(Math.PI * index / 2 - Math.PI / 2);
                }

                transform.translate(- image.getWidth(null) / 2f, - image.getHeight(null) / 2f);

                g.drawImage(image, transform, null);
            }
        } finally {
            lock.unlock();
        }

        super.render(g);
    }

    /**
     * Get model
     * @return
     */
    @Override
    public Model getModel() {
        return super.getModel();
    }

    /**
     * Get rotated model
     * 
     * @param direction
     * @return
     */
    public Model getModel(Motion.Direction direction) {
        double height = model.getHeight(), width = model.getWidth();
        Model m = (Model) model.clone();
        Position position = m.getPosition();
        Position offset = m.getOffset();
        double offsetX = offset.getX(), offsetY = offset.getY();

        switch (getMotion().getDirection()) {
            case SOUTH:
                break;
            case WEST:
                offsetX = offset.getY();
                offsetY = offset.getX();
                height = model.getWidth();
                width  = model.getHeight();
                break;
            case EAST:
                offsetX = offset.getY();
                offsetY = offset.getX();
                height = model.getWidth();
                width  = model.getHeight();
                break;
        }
        offset.setX(offsetX);
        offset.setY(offsetY);
        m.setPosition(position);
        m.setWidth(width);
        m.setHeight(height);

        return m;
    }

    @Override
    public String toString() {
        return super.toString() + " " + motion.toString();
    }

    public void lock() {
        lock.lock();
        try {
            locked = true;
        } finally {
            lock.unlock();
        }
    }

    public void unlock() {
        lock.lock();
        try {
            locked = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean locked() {
        lock.lock();
        try {
            return locked;
        } finally {
            lock.unlock();
        }
    }
}
