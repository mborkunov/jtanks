/*
 * GNU General Public License v2
 * 
 * @version $Id$
 */
package jtanks.system;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class DisplayManager {

    private DisplayMode system;
    private GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private GraphicsDevice gd = env.getDefaultScreenDevice();

    public DisplayManager() {
        system = gd.getDisplayMode();
    }

    /**
     * Return display mode based on config properties and environment
     * 
     * @return display mode
     */
    public DisplayMode getDisplayMode() {
        Config config = Config.getInstance();
        int width = Integer.parseInt(config.get("window.width"));
        int height = Integer.parseInt(config.get("window.height"));

        DisplayMode mode = find(width, height);
        if (Config.getInstance().getBoolean("fullscreen")) {
            return mode;
        } else {
            return new DisplayMode(width, height, mode.getBitDepth(), mode.getRefreshRate());
        }
    }

    public DisplayMode find(int width, int height) {
        DisplayMode[] modes = getDisplayModes();
        DisplayMode result = modes[0];
        for (DisplayMode mode : modes) {
            if (mode.getWidth() == width && mode.getHeight() == height) {
                result = mode;
            }

            if (result.equals(mode) == false && result.getBitDepth() < mode.getBitDepth()) {
                result = mode;
            }
        }
        return result;
    }

    /**
     * Change display mode
     * @param mode
     */
    public void setDisplayMode(DisplayMode mode) {
        if (isDisplayChangeSupported()) {
            gd.setDisplayMode(mode);
        }
    }

    /**
     * Restore system mode
     */
    public void restoreDisplay() {
        if (gd.getDisplayMode().equals(system) == false) {
            setDisplayMode(system);
        }
    }

    /**
     * Return system mode
     * @return System mode
     */
    public DisplayMode getSystemDisplayMode() {
        return system;
    }

    public boolean isDisplayChangeSupported() {
        return gd.isDisplayChangeSupported();
    }

    public DisplayMode[] getDisplayModes() {
        return gd.getDisplayModes();
    }

    public DisplayMode getCurrentDisplayMode() {
        return gd.getDisplayMode();
    }

    public GraphicsDevice getGraphicsDevice() {
        return gd;
    }

    public boolean isFullScreen() {
        return gd.getFullScreenWindow() == null ? false : true;
    }
}
