package jtanks.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GuiManager {

    public static int STRAT_LENGTH = 5;

    public static void setPrefferedMaxAndMinSize(final Component c, final int width, final int height) {
        final Dimension dim = new Dimension(width, height);
        c.setPreferredSize(dim);
        c.setMinimumSize(dim);
        c.setMaximumSize(dim);
    }

    public static void setPreferredMinDim(final Component c, final int width, final int height) {
        final Dimension min = new Dimension(width, height);
        final Dimension max = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        c.setPreferredSize(min);
        c.setMinimumSize(min);
        c.setMaximumSize(max);
    }

    public static void setPreferredMinMaxHeight(final Component c, final int width, final int height) {
        final Dimension min = new Dimension(width, height);
        final Dimension max = new Dimension(Integer.MAX_VALUE, height);
        c.setPreferredSize(min);
        c.setMinimumSize(min);
        c.setMaximumSize(max);
    }

    public static JPanel insertIntoJPanel(final Component insert, final boolean north, final boolean south,
            final boolean west, final boolean east) {
        return (JPanel) insertIntoContainer(new JPanel(), insert, north, south, west, east);
    }

    public static Container insertIntoContainer(final Container container, final Component insert, final boolean north,
            final boolean south, final boolean west, final boolean east) {
        container.setLayout(new BorderLayout());
        if (north)
            container.add(Box.createVerticalStrut(STRAT_LENGTH), BorderLayout.NORTH);
        if (south)
            container.add(Box.createVerticalStrut(STRAT_LENGTH), BorderLayout.SOUTH);
        if (east)
            container.add(Box.createHorizontalStrut(STRAT_LENGTH), BorderLayout.EAST);
        if (west)
            container.add(Box.createHorizontalStrut(STRAT_LENGTH), BorderLayout.WEST);
        container.add(insert, BorderLayout.CENTER);
        return container;
    }

    public static DisplayMode getDisplayMode(final Window window) {
        return window.getGraphicsConfiguration().getDevice().getDisplayMode();
    }

    public static void moveToCenterScreen(final Window window) {
        final DisplayMode mode = getDisplayMode(window);
        window.setLocation((mode.getWidth() - window.getWidth()) / 2, (mode.getHeight() - window.getHeight()) / 2);
    }

    public static void moveToCenterWindow(final Window out, final Window in) {
        in.setLocation(out.getX() + (out.getWidth() - in.getWidth()) / 2,
                out.getY() + (out.getHeight() - in.getHeight()) / 2);
    }

    public static void setNimbusLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (final Exception e) {
            Logger.getLogger(GuiManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            Logger.getLogger(GuiManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Component setSize(final Component component, final int width, final int height) {
        setPrefferedMaxAndMinSize(component, width, height);
        return component;
    }

}