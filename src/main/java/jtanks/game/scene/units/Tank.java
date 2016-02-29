package jtanks.game.scene.units;

import java.awt.image.VolatileImage;
import java.util.Arrays;
import java.util.List;
import jtanks.game.scene.units.util.Motion;
import jtanks.game.geometry.Position;
import jtanks.game.geometry.Rectangle;
import jtanks.game.scene.Node;
import jtanks.game.scene.controllers.AnimationController;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;

public class Tank extends Unit {

    protected int maxBullets = 3;
    protected int fireDelay = 1000;
    protected long lastShootTime;
    protected int angle = 90;
    private boolean destroyed = false;

    /**
     * Get the value of angle
     *
     * @return the value of angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Set the value of angle
     *
     * @param angle new value of angle
     */
    public void setAngle(int angle) {
        if (angle > 360) {
            throw new IllegalArgumentException("Angle value is too high " + angle);
        }
        this.angle = angle;
    }

    public Tank() {
        super();
        Rectangle rectangle = new Rectangle();

        rectangle.setPosition(new Position(1, 1));
        rectangle.setOffset(new Position(.125, .075));
        rectangle.setWidth(.75);
        rectangle.setHeight(.85);
        setModel(rectangle);
    }

    public boolean shoot() {
        if (canShoot() == false) {
            return false;
        }
        Bullet bullet = new Bullet();
        bullet.getMotion().setDirection(getMotion().getDirection());
        bullet.getModel().setPosition(getModel().getPosition().clone());

        double x = bullet.getModel().getPosition().getX(), y = bullet.getModel().getPosition().getY();

        double delta = getModel().getHeight() / 2 - getModel().getOffset().getY();
        switch (getMotion().getDirection()) {
            case NORTH:
                y -= delta;
                break;
            case SOUTH:
                y += delta;
                break;
            case EAST:
                x += delta;
                break;
            case WEST:
                x -= delta;
                break;
        }
        bullet.getModel().getPosition().setX(Math.max(0, x));
        bullet.getModel().getPosition().setY(Math.max(0, y));
        lastShootTime = System.currentTimeMillis();
        addChild(bullet);
        return true;
    }

    @Override
    public void remove() {
        destroyed = true;
        String key = "animation-" + getClass().getName();
        
        List<VolatileImage> images = ResourceManager.getImages(ResourceManager.getAnimationResources("sprites/units/explosion"));

        Cache.GLOBAL.putIfAbsent(key, images);

        lock.lock();
        try {
            addController(new AnimationController());
        } finally {
            lock.unlock();
        }
    }

    public boolean rotating() {
        if (angle % 90 != 0) {
            return true;
        } else {
            int index = Arrays.asList(Motion.Direction.values()).indexOf(getMotion().getDirection());
            return angle != index * 90;
        }
    }

    @Override
    public void affect(Node node) {
        if (node instanceof Unit) {
            affect((Unit) node);
        }
    }

    @Override
    public void affect(Unit unit) {
        if (unit.getClass().equals(Bullet.class)) {
            affect((Bullet) unit);
        } else if (unit instanceof Tank) {
            affect((Tank) unit);
        }
    }

    public void affect(Bullet bullet) {
        if (!bullet.getOwner().equals(this)) {
        }
    }

    public void affect(Tank tank) {
        if (!remove && !destroyed) {
            this.getMotion().setLock(true);
            tank.getMotion().setLock(true);
        }
    }

    protected boolean canShoot() {
        if (locked || rotating()) {
            return false;
        }
        lock.lock();
        try {
            if (children != null) {
                if (children.size() >= maxBullets) {
                    return false;
                }
            }
        } finally {
            lock.unlock();
        }
        if (System.currentTimeMillis() - lastShootTime < fireDelay) {
            return false;
        }
        return true;
    }

    @Override
    public void offscreenAction(Motion.Direction direction) {
        if (motion.getDirection().equals(direction)) {
            getMotion().setLock(true);
        } else {
            getMotion().setLock(false);
        }
    }

    @Override
    public int getAnimationTime() {
        return 10;
    }
}
