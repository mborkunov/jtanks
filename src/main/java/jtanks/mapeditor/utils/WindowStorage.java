package jtanks.mapeditor.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Voroshin Alexey
 */
public class WindowStorage {

    private static final Logger LOGGER     = Logger.getLogger(WindowStorage.class.getName());

    public static final String  WIDTH      = "Width";
    public static final String  HEIGHT     = "Height";
    public static final String  LEFT       = "Left";
    public static final String  TOP        = "Top";

    private final Properties    properties = new Properties();
    private final Window        window;
    private final String        fileName;

    public WindowStorage(final String fileNameA, final Window windowA) {
        fileName = fileNameA;
        window = windowA;
    }

    public WindowStorage save() throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(fileName));

            final Point point = window.getLocation();
            final Rectangle rectangle = window.getBounds();

            properties.put(LEFT, Integer.toString(point.x));
            properties.put(TOP, Integer.toString(point.y));
            properties.put(WIDTH, Integer.toString(rectangle.width));
            properties.put(HEIGHT, Integer.toString(rectangle.height));

            properties.store(out, null);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }

        return this;
    }

    public WindowStorage load() throws FileNotFoundException, IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(fileName));
            properties.load(in);

            window.setSize(Integer.valueOf((String) properties.get(WIDTH)),
                    Integer.valueOf((String) properties.get(HEIGHT)));
            window.setLocation(Integer.valueOf((String) properties.get(LEFT)),
                    Integer.valueOf((String) properties.get(TOP)));
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
        return this;
    }

}