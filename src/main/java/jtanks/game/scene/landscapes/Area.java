package jtanks.game.scene.landscapes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.concurrent.Callable;
import jtanks.game.geometry.Model;
import jtanks.game.geometry.Position;
import jtanks.game.geometry.Rectangle;
import jtanks.game.scene.Node;
import jtanks.game.util.Cache;
import jtanks.system.ResourceManager;

public class Area extends Node {

    protected int x;
    protected int y;
    protected Model defaultModel;

    public Area(int x, int y) {
        this.x = x;
        this.y = y;

        Rectangle rectangle = new Rectangle();

        Position position = new Position(x, y);
        rectangle.setPosition(position);
        rectangle.setOffset(new Position(0, 0));
        rectangle.setWidth(1);
        rectangle.setHeight(1);

        defaultModel = rectangle;
        model = (Model) defaultModel.clone();
    }

    @Override
    public void render(Graphics2D g) {
        Image image = getImage();
        AffineTransform transform = new AffineTransform();
        float scale = (Float) Cache.GLOBAL.get("scale");
        transform.scale(scale, scale);
        transform.translate(image.getWidth(null) * x, image.getHeight(null) * y);
        g.drawImage(image, transform, null);
        super.render(g);
    }

    public VolatileImage getImage() {
        final String areaName = getClass().getSimpleName().toLowerCase();
        return (VolatileImage) cache.get("sprite", new Callable<VolatileImage>() {
            public VolatileImage call() {
                return ResourceManager.getImage("sprites/landscapes/" + areaName);
            }
        });
    }
}