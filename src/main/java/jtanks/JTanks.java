package jtanks;

import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import jtanks.game.GameState;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.IllegalComponentStateException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import jtanks.game.Graphics;
import jtanks.game.map.Manager;
import jtanks.system.Config;
import jtanks.system.DisplayManager;
import jtanks.system.OS;
import jtanks.system.Registry;
import jtanks.system.ResourceManager;
import jtanks.system.SoundManager;
import jtanks.system.TimerManager;

/**
 * JTanks application entry point
 */
public final class JTanks {

    private Logger logger = Logger.getLogger(JTanks.class.getName());
    /**
     * Instance of main class
     * It's implementation of a Singleton pattern
     */
    private static JTanks instance;
    /**
     * DisplayManager 
     */
    private DisplayManager display;
    /**
     * Our gui reference
     */
    private Canvas canvas;
    /**
     * Window reference
     */
    private final JFrame frame = new JFrame();
    /**
     * Graphics thread target reference
     */
    private Config config;
    private GameState gameState;
    private Graphics graphics;

    /**
     * Constructor
     */
    private JTanks() {
        initialize();
    }

    private void start() {
        gameState = new GameState();
        graphics = new Graphics();
        graphics.start();
        gameState.start();
    }

    /**
     * Change fullscreen mode
     */
    public void flipFullScreenMode() {
        setFullScreenMode(!display.isFullScreen());
    }

    public GameState getGameState() {
        return gameState;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void quit() {
        turnOnRepeating();
        Registry.get(DisplayManager.class).restoreDisplay();
        gameState.quit();
    }

    public void setFullScreenMode(final boolean fullscreen) {
        EventQueue.invokeLater(new FullScreenQueueTask(fullscreen));
    }

    /**
     * @return Game window
     */
    public JFrame getWindow() {
        return frame;
    }

    /**
     * Return canvas
     * 
     * @return Game window canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JTanks.getInstance().start();
    }

    /**
     * Singleton pattern
     * 
     * @return Instance of JTanks class
     */
    public static JTanks getInstance() {
        if (JTanks.instance == null) {
            JTanks.instance = new JTanks();
        }
        return JTanks.instance;
    }

    /**
     * Initialize game state
     */
    private void initialize() {
        initializeLogger();

        Manager.getInstance();
        ResourceManager.preload();
        display = new DisplayManager();
        config = Config.getInstance();

        Registry.set(SoundManager.class, new SoundManager());
        Registry.set(TimerManager.class, new TimerManager());
        Registry.set(DisplayManager.class, display);
        Registry.set("debug", Boolean.FALSE);
        initializeScreen();
        turnOffRepeating();
    }

    private void initializeLogger() {
        BufferedInputStream is = null;
        try {
            File file = new File(Config.getInstance().applicationDirectory + File.separator + "logging.properties");
            if (file.exists()) {
                is = new BufferedInputStream(new FileInputStream(file));
                LogManager.getLogManager().readConfiguration(is);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void turnOffRepeating() {
        if (OS.isUnix() || OS.isMac()) {
            try {
                Runtime.getRuntime().exec("xset -r");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void turnOnRepeating() {
        if (OS.isUnix() || OS.isMac()) {
            try {
                Runtime.getRuntime().exec("xset r");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createWindow(DisplayMode mode) {
        GraphicsDevice gd = display.getGraphicsDevice();
        
        frame.setBackground(Color.BLACK);
        frame.setSize(mode.getWidth(), mode.getHeight());
        frame.setTitle("JTanks");
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/favicon.png"));
        frame.setIconImage(icon.getImage());

        frame.addWindowListener(new GameWindowListener());
        frame.addFocusListener(new GameWindowFocusListener());
        int xPos = gd.getDisplayMode().getWidth() / 2 - mode.getWidth() / 2;
        int yPos = gd.getDisplayMode().getHeight() / 2 - mode.getHeight() / 2;
        frame.setLocation(xPos, yPos);

        canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        canvas.setFocusable(true);
        canvas.enableInputMethods(false);

        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        canvas.requestFocus();
        canvas.setIgnoreRepaint(true);

        logger.info("Window initialized");
    }

    /**
     * Initialize screen
     */
    private void initializeScreen() {
        createWindow(display.getDisplayMode());
        boolean fullscreen = Boolean.parseBoolean(config.get("fullscreen"));
        setFullScreenMode(fullscreen);
    }

    class GameWindowListener implements WindowListener {
        public void windowClosing(WindowEvent e) {
            Logger.getLogger(getClass().getName()).info("Exiting the game");
            JTanks.getInstance().quit();
        }
        public void windowOpened(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}

        public void windowIconified(WindowEvent e) {
            gameState.pause();
            graphics.pause();
        }

        public void windowDeiconified(WindowEvent e) {
            graphics.resume();
            gameState.resume();
        }

        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
    }

    class GameWindowFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            turnOffRepeating();
            getWindow().setTitle("JTanks");
            try {
                getGameState().resume();
            } catch (NullPointerException ignored) {}
        }

        public void focusLost(FocusEvent e) {
            turnOnRepeating();
            getWindow().setTitle("JTanks (Not focused)");
            try {
                GameState gameState = getGameState();
                gameState.pause();
            } catch (NullPointerException ignored) {}
        }
    }

    class FullScreenQueueTask implements Runnable {

        private boolean fullscreen;

        FullScreenQueueTask(boolean fullscreen) {
            this.fullscreen = fullscreen;
        }

        public void run() {
            try {
                graphics.pause();
            } catch (NullPointerException ignored) {}
            GraphicsDevice gd = display.getGraphicsDevice();
            synchronized (frame) {
                if (fullscreen) {
                    logger.info("Setting fullscreen mode");
                    frame.setVisible(false);
                    frame.dispose();
                    frame.setUndecorated(true);
                    frame.setVisible(true);
                    frame.setAlwaysOnTop(true);
                    gd.setFullScreenWindow(frame);
                    try {
                        display.setDisplayMode(display.getDisplayMode());
                    } catch (IllegalArgumentException e) {
                        logger.log(Level.SEVERE, "cannot change display mode", e);
                    }
                    frame.setVisible(true);
                } else {
                    logger.info("Setting windowed mode");
                    display.restoreDisplay();
                    frame.dispose();
                    try {
                        frame.setAlwaysOnTop(false);
                        frame.setVisible(false);
                        frame.setUndecorated(false);
                        gd.setFullScreenWindow(null);
                    } catch (IllegalComponentStateException e) {
                        logger.log(Level.SEVERE, "Cannot set windowed mode", e);
                    } finally {
                        frame.setVisible(true);
                    }

                    Config config = Config.getInstance();

                    int width  = config.getInteger("window.width");
                    int height = config.getInteger("window.height");

                    frame.setSize(width, height);
                }
            }
            turnOffRepeating();
            Config.getInstance().set("fullscreen", fullscreen);
            canvas.requestFocus();

            try {
                graphics.resume();
            } catch (NullPointerException ignored) {}
        }
    }
}