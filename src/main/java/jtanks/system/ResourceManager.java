/*
 * GNU General Public License v2
 *
 * @version $Id: ResourceManager.java 304 2009-07-23 13:39:01Z ru.energy $
 */
package jtanks.system;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jtanks.game.util.Cache;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResourceManager {

    private static Cache cache = new Cache<Object,Object>();

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static String JTANKS_USER_DIR = System.getProperty("user.home")
                                         + File.separator +".jtanks";

    public static void preload() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("sprites/units/explosion");
        list.add("sprites/landscapes/forest");
        list.add("sprites/landscapes/water");

        for (String item : list) {
            ResourceManager.getImages(ResourceManager.getAnimationResources(item));
        }
    }

    static public VolatileImage getVolatileImage(String name) {
        Image image = getImage(name);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration conf = device.getDefaultConfiguration();

        VolatileImage volatileImage = conf.createCompatibleVolatileImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT);

        Graphics2D g = volatileImage.createGraphics();
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());

        g.drawImage(image, null, null);
        return volatileImage;
    }

    /**
     * Returns Image by path/name
     * Search pattern "resources/images/${name}.png"
     *
     * @param name
     * @return
     */
    static public VolatileImage getImage(final String name) {
        return (VolatileImage) cache.get(name, new Callable<VolatileImage>() {
            public VolatileImage call() {
                URL url = ResourceManager.getImageResource(name);
                if (url == null) {
                    throw new NullPointerException("Image resource is not exist: " + name);
                }
                return ResourceManager.getImage(url);
            }
        });
    }

    static public BufferedImage createImage(int width, int height, int transparency) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration configuration = device.getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height, transparency);
    }

    static public VolatileImage getImage(final URL resource) {

        return (VolatileImage) cache.get(resource, new Callable<VolatileImage>() {

            public VolatileImage call() {
                BufferedImage image = null;
                try {
                    image = ImageIO.read(resource);
                } catch (IOException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }

                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                GraphicsConfiguration configuration = device.getDefaultConfiguration();

                VolatileImage volatileImage = configuration.createCompatibleVolatileImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
                Graphics2D g = volatileImage.createGraphics();
                g.setBackground(new Color(0, 0, 0, 0));
                g.clearRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());

                g.drawImage(image, null, null);

                return volatileImage;
            }
        });
    }


    public static Quote getRandomQuote() {

        ArrayList<Quote> quotes = quotes = (ArrayList<Quote>) cache.get("quotes", new Callable<ArrayList<Quote>>() {

            public ArrayList<Quote> call() {
                String path = "resources/quotes.xml";
                InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                ArrayList<Quote> quotes = new ArrayList<Quote>();
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = builder.parse(stream);

                    NodeList list = doc.getElementsByTagName("quote");

                    int i = 0;
                    while (i < list.getLength()) {
                    NodeList quoteNodes = list.item(i).getChildNodes();
                    Quote quote = new Quote();

                    for (int j = 0; j < quoteNodes.getLength(); j++) {
                        Node node = quoteNodes.item(j);
                        if (node.getNodeName().equals("content")) {
                            quote.setContent(node.getFirstChild().getNodeValue());
                        } else if (node.getNodeName().equals("author")) {
                            quote.setAuthor(node.getFirstChild().getNodeValue());
                        }
                    }
                    quotes.add(quote);
                    i++;
                    }
                    return quotes;
                } catch (SAXException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });

        if (quotes != null) {
            return quotes.get(new Random().nextInt(quotes.size()));
        }
        return null;
    }

    public static boolean fileExists(String filename) {
        if (ResourceManager.getImageResource(filename) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static URL getImageResource(String name) {
        String path = new StringBuffer("resources/images/").append(name).append(".png").toString();
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
    
    public static InputStream getStream(String name) {
        String path = new StringBuffer("resources/").append(name).toString();
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Return available animation resources by path string
     * @param source
     * @return
     */
    public static List<URL> getAnimationResources(String source) {
        List<URL> resources = new ArrayList<URL>();

        int i = 0;
        while (true) {
            String file = new StringBuffer(source).append("-").append(i).toString();
            if (fileExists(file)) {
                resources.add(getImageResource(file));
            } else {
                break;
            }
            i++;
        }
        return resources;
    }

    /**
     * Creates and returns invisible cursor
     *
     * @return Invisible cursor
     */
    public static Cursor createInvisibleCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage(new MemoryImageSource(16, 16, new int[16*16], 0, 16));
        return toolkit.createCustomCursor(image, new Point(0,0), "invisible");
    }

   /**
     * Creates and returns invisible cursor
     *
     * @return Invisible cursor
     */
    public static Cursor getCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.createCustomCursor(getImage("mouseCursor"), new Point(0,0), "jtanks cursor");
        //return toolkit.createCustomCursor(image, new Point(0,0), "invisible");
    }

    /**
     * Get images collection by collection of resources
     * @param resources
     * @return
     */
    public static List<VolatileImage> getImages(List<URL> resources) {
        List<VolatileImage> images = new ArrayList<VolatileImage>();
        for (int i = 0; i < resources.size(); i++) {
            images.add(ResourceManager.getImage(resources.get(i)));
        }
        return images;
    }

    public static void saveScreenshot(final BufferedImage screenshotImage) {
            executor.submit(new Runnable() {
                public void run() {
                    Logger logger = Logger.getLogger(ResourceManager.class.getName());
                    try {
                        File dir = new File(JTANKS_USER_DIR + File.separator + "screenshots");

                        if (dir.exists() == false) {
                            if (dir.mkdir() == false) {
                                logger.severe("Cannot create jtanks screenshot directory. (" + dir.getAbsolutePath() + ")");
                                return;
                            }
                        }
                        Calendar calendar = new GregorianCalendar();
                        StringBuffer filename = new StringBuffer();


                        int year, month, date, hour, minute, second;
                        year   = calendar.get(Calendar.YEAR);
                        month  = calendar.get(Calendar.MONTH);
                        date   = calendar.get(Calendar.DATE);
                        hour   = calendar.get(Calendar.HOUR);
                        minute = calendar.get(Calendar.MINUTE);
                        second = calendar.get(Calendar.SECOND);

                        String dateString = new Formatter().format("%04d-%02d-%02d_%02d:%02d:%02d",
                                                year, month, date, hour, minute, second).toString();

                        filename.append("screenshot-")
                                .append(dateString)
                                .append(".png");
                        File file = new File(dir, filename.toString());
                        ImageIO.write(screenshotImage, "png", file);
                        logger.info("Screenshot was successfully saved. Filename : " + file.getAbsolutePath());
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Error has occurred while saving screenshot", ex);
                    }
                }
            });
    }
}
