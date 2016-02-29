package jtanks.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import jtanks.JTanks;
import jtanks.game.util.Cache;
import jtanks.system.Config;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;

public final class Graphics {

    private Logger logger = Logger.getLogger(Graphics.class.getName());

    public static Lock lock = new ReentrantLock();
    private GraphicsTask task;
    protected Thread thread;
    private boolean screenshot = false;

    public Graphics() {
        task = new GraphicsTask(this);
        thread = new Thread(task, "Game graphics");
    }

    public void start() {
        lock.lock();
        try {
            task = new GraphicsTask(this);
            thread = new Thread(task, "Game graphics");
            thread.start();
        } finally {
            lock.unlock();
        }
    }

    public void pause() {
        task.stop();
        while (!thread.getState().equals(State.TERMINATED)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resume() {
        if (thread.getState().equals(State.RUNNABLE)) {
            throw new IllegalStateException("Graphics thread is already running");
        }
        task.reset();
        thread = new Thread(task, "Game graphics");
        thread.start();
    }

    public void createScreenshot() {
        screenshot = true;
    }

    public boolean screenshot() {
        try {
            return screenshot;
        } finally {
            screenshot = false;
        }
    }

    class GraphicsTask implements Runnable {

        /**
         * Sleep after each cycle until this time elapse
         * in millisecond
         */
        public int FRAME_DELAY;

        private BufferStrategy strategy;
        private GameState gameState = JTanks.getInstance().getGameState();

        private ArrayBlockingQueue<Integer> workTimes = new ArrayBlockingQueue<Integer>(10);
        private boolean isRunning = true;
        private long cycleStartTime = 0;
        private Graphics graphics;

        public GraphicsTask(Graphics g) {
            int fps;
            try {
                fps = Config.getInstance().getInteger("maxfps");
                fps = Math.max(fps, 1);   // 1 fps minimum
                fps = Math.min(fps, 500); // 500 fps maximum
                FRAME_DELAY = 1000 / fps;
            } catch (NumberFormatException e) {
                fps = Integer.valueOf(Config.getInstance().getDefault("maxfps"));
                Config.getInstance().set("maxfps", fps);
                FRAME_DELAY = 1000 / fps; // default delay
            }
            graphics = g;
        }

        public void run() {
            if (strategy == null) {
                Canvas canvas = JTanks.getInstance().getCanvas();
                canvas.createBufferStrategy(2);
                strategy = canvas.getBufferStrategy();
            }

            while (isRunning) {
                render();
                syncFramerate();
            }
        }

        public void reset() {
            isRunning = true;
        }

        public void stop() {
            isRunning = false;
            while (!graphics.thread.getState().equals(Thread.State.TERMINATED)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        }

        private void render() {
            boolean screenshot = screenshot();
            Graphics2D g, g2 = null;
            Canvas canvas = JTanks.getInstance().getCanvas();
            g = (Graphics2D) strategy.getDrawGraphics();
            BufferedImage screenshotImage = null;

            if (screenshot) {
                int width  = canvas.getWidth();
                int height = canvas.getHeight();

                screenshotImage = ResourceManager.createImage(width, height, Transparency.OPAQUE);
                g2 = g;
                g = (Graphics2D) screenshotImage.getGraphics();
            }

            g.setColor(Color.BLACK);
            g.fillRect(0 , 0, canvas.getWidth(), canvas.getHeight());
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setClip(0, 0, canvas.getWidth(), canvas.getHeight());

            gameState.getScreen().draw(g);
            if (Config.getInstance().getBoolean("beta")) {
                showSign(g);
            }

            if (Registry.get("debug").equals(Boolean.TRUE)) {
                showFPS(g);
            }

            if (screenshot) {
                g2.drawImage(screenshotImage, null, null);
                ResourceManager.saveScreenshot(screenshotImage);
            }

            g.dispose();
            strategy.show();
            screenshot = false;
        }

        /**
         * @param g
         */
        private void showFPS(Graphics2D g) {
            int summary = 0;
            int count = workTimes.size();
            Integer[] array = workTimes.toArray(new Integer[count]);

            for (int i = 0; i < count; i++) {
                summary += array[i];
            }
            if (count > 0 ) {
                int workTime = summary / count;
                int workFps = 1000;
                if (workTime > 0) {
                    workFps = 1000 / workTime;
                }
                int fps = Math.min(workFps, 1000 / FRAME_DELAY);
                String fpsString = "FPS: " + fps;
                g.setColor(Color.WHITE);
                Canvas canvas = JTanks.getInstance().getCanvas();
                g.setFont(new Font("Tahoma", Font.BOLD, canvas.getHeight() / 50));
                Rectangle2D rect = g.getFontMetrics().getStringBounds(fpsString, g);
                int offsetX = (int) (canvas.getWidth() - rect.getWidth() - rect.getHeight() / 2);
                int offsetY = (int) (canvas.getHeight() - rect.getHeight() / 2);
                g.drawString(fpsString, offsetX, offsetY);
            }
        }

        private void showSign(Graphics2D g) {

            final VolatileImage alphaVersionImage = (VolatileImage) Cache.GLOBAL.get("alpha", new Callable<VolatileImage>() {
                public VolatileImage call() {
                    return ResourceManager.getVolatileImage("alpha");
                }
            });

            AffineTransform trans = (AffineTransform) Cache.GLOBAL.get("alphaTransformation", new Callable<AffineTransform>() {
                public AffineTransform call() {
                    float scale = JTanks.getInstance().getCanvas().getHeight() /
                                  (float) (alphaVersionImage.getHeight() * 4);
                    AffineTransform transform = new AffineTransform();
                    transform.scale(scale, scale);
                    return transform;
                }
            });
            g.drawImage(alphaVersionImage, trans, null);
        }

        private void syncFramerate() {
            long worktime = System.currentTimeMillis() - cycleStartTime;
            if (workTimes.remainingCapacity() == 0) {
                workTimes.poll();
            }
            workTimes.offer((int) worktime);

            long diff = FRAME_DELAY - worktime;
            try {
                Thread.sleep(Math.max(0, diff));
            } catch (InterruptedException ignored) {}
            cycleStartTime = System.currentTimeMillis();
        }
    }
}
