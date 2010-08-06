/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.game.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.JTanks;
import jtanks.game.gameplay.StatisticsData;
import jtanks.game.map.Manager;
import jtanks.game.map.Map;
import jtanks.game.screens.helpers.Menu;
import jtanks.game.screens.helpers.MenuItem;
import jtanks.system.SystemListener;
import jtanks.game.screens.helpers.MenuView;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;
import jtanks.system.SoundManager;


public strictfp class Maps extends Screen {

    public Menu<Map> menu = new Menu<Map>();
    private MenuView menuView;
    private String string;
    private HashMap<Integer,Future> futures = new HashMap<Integer,Future>();
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private double previewImageAngle = 0;
    private long time;
    private final static Logger logger = Logger.getLogger(Maps.class.getName());

    public Maps() {
        Manager mapManager = Manager.getInstance();
        List<Map> maps = mapManager.getMaps();

        boolean disabled = false;
        for (int i = 0; i < maps.size(); i++) {
            if (!disabled && i > Registry.get(StatisticsData.class).getLastMap()) {
                disabled = true;
            }
            Map map = maps.get(i);
            MenuItem<Map> item = new MenuItem<Map>(map.getName(), Loading.class, i == 0);
            item.set(map);
            item.setDisabled(disabled );
            menu.add(item);
        }
        keyboardListener = new Listener();
        menuView = new MenuView(menu);

        string = "Choose map";
    }

    @Override
    public void draw(Graphics2D g) {
        int width = 0, height = 0;
        Image preview = null;

        final int previewSize = getHeight() / 2;

        lock.lock();
        try {
            if (cache.get("font") == null) {
                cache.put("font", getFont(getHeight() / 20));
            }
            g.setFont((Font) cache.get("font"));

            if (cache.get("width") == null || cache.get("height") == null) {
                width = (int) (getWidth() / 2 - g.getFontMetrics().getStringBounds(string, g).getWidth() / 2);
                height = getHeight() / 10;
                cache.put("width", width);
                cache.put("height", height);
            }
            width = (Integer) cache.get("width");
            height = (Integer) cache.get("height");

            final int index = menu.selectedIndex();

            Image[] imgs = (Image[]) cache.get("previews");

            final Image[] images = (imgs != null) ? imgs : new Image[Manager.getInstance().getMaps().size()];
            if (imgs == null) {
                cache.put("previews", images);
            }
            
            if (futures.get(index) == null) {
                try {
                    futures.put(index, service.submit(new Runnable() {
                        public void run() {
                            images[index] = Manager.getInstance().getMaps().get(index).createPreviewImage(previewSize);
                        }
                    }));
                } catch (RejectedExecutionException e) {
                    logger.log(Level.WARNING, null, e);
                }
            }
            preview = images[index];
        } finally {
            lock.unlock();
        }

        VolatileImage menuImage = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                     .getDefaultScreenDevice()
                                                     .getDefaultConfiguration()
                                                     .createCompatibleVolatileImage(getWidth() - getWidth() / 15, getHeight(), Transparency.OPAQUE);
        Graphics2D g2 = menuImage.createGraphics();
        g2.setBackground(Color.BLACK);
        g2.clearRect(0, 0, getWidth(), getHeight());
        menuView.draw(menuImage.createGraphics(), this);

        AffineTransform transform = new AffineTransform();
        transform.translate(getWidth() / 15, 0);
        g.drawImage(menuImage, transform, null);

        g.setBackground(Color.GRAY);
        g.clearRect(getWidth() / 15 - 5, (getHeight() - previewSize) / 2 - 5, previewSize + 10, previewSize + 10);
        g.setBackground(Color.BLACK);
        g.clearRect(getWidth() / 15, (getHeight() - previewSize) / 2, previewSize, previewSize);


        transform = new AffineTransform();
        transform.translate(getWidth() / 15, (getHeight() - getHeight() / 2) / 2);

        if (preview != null) {
            g.drawImage(preview, transform, null);
        } else {
            VolatileImage loadingImage = ResourceManager.getImage("mapPreviewLoading");
            transform.translate(previewSize / 2, previewSize / 2);
            double scale = (previewSize / loadingImage.getWidth()) / 3;
            transform.scale(scale, scale);
            transform.rotate(previewImageAngle);
            transform.translate(- loadingImage.getWidth() / 2, - loadingImage.getHeight() / 2);

            if (System.currentTimeMillis() - time >= 50) {
                previewImageAngle += 15 * Math.PI / 180;

                if (previewImageAngle >= 360) {
                    previewImageAngle = 0;
                }
                time = System.currentTimeMillis();
            }

            g.drawImage(loadingImage, transform, null);
        }

        g.setColor(Color.WHITE);
        g.drawString(string, width, height);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void terminate() {
        service.shutdown();
        super.terminate();
    }

    private class Listener extends SystemListener {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    menu.selectPrevious();
                    break;
                case KeyEvent.VK_DOWN:
                    menu.selectNext();
                    break;
                case KeyEvent.VK_ESCAPE:
                    JTanks.getInstance().getGameState().setScreen(new Start());
                    Registry.get(SoundManager.class).play("menu");
                    break;
                case KeyEvent.VK_ENTER:
                    try {
                        MenuItem<Map> item = menu.getSelected();
                        Registry.set("map", item.get());
                        JTanks.getInstance().getGameState().setScreen(item.getScreen());
                    } catch (InstantiationException ex) {
                        Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Registry.get(SoundManager.class).play("menu");
                    break;
                default:
                    break;
            }
        }
    }
}
