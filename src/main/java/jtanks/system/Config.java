/*
 * GNU General Public License v2
 * 
 * @version $Id$
 */
package jtanks.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game persistent properties storage class
 */
public class Config implements Iterable {

    private Logger logger = Logger.getLogger(Config.class.getName());
    private HashMap<String,String> defaults = new HashMap<String,String>();
    public File applicationDirectory;
    private File configFile;
    private Properties properties = new Properties();
    private static final String JTANKS_DIR = ".jtanks";
    private static Config instance;

    private Config() {
        defaults.put("maxfps", String.valueOf(50));
        defaults.put("fullscreen", Boolean.FALSE.toString());
        defaults.put("sound", Boolean.TRUE.toString());
        defaults.put("beta", Boolean.TRUE.toString());
        defaults.put("window.width", String.valueOf(800));
        defaults.put("window.height", String.valueOf(600));
        defaults.put("font.family", String.valueOf("Georgia"));
        defaults.put("font.style", String.valueOf("bold"));

        String userHome = System.getProperty("user.home");

        StringBuffer dirName = new StringBuffer(userHome).append(File.separator).append(JTANKS_DIR);

        applicationDirectory = new File(dirName.toString());
        configFile = new File(applicationDirectory, "config");
        if (applicationDirectory.exists() == false) {
            if (applicationDirectory.mkdir() == false) {
                logger.log(Level.WARNING, "Cannot create application directory " + applicationDirectory);
            }
        }
        if (configFile.exists() && configFile.canRead()) {
            BufferedInputStream is = null;
            boolean exception = false;
            try {
                is = new BufferedInputStream(new FileInputStream(configFile));
                properties.loadFromXML(is);
            } catch (IOException ex) {
                exception = true;
                logger.log(Level.SEVERE, "An error has occurred while reading config file", ex);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "An error has occurred while reading config file", ex);
                }
            }
            if (exception == false) {
                logger.info("Config file " + configFile.getAbsolutePath() + " was successfully loaded");
            }
        }
        loadDefaultConfigValues();
    }

    /**
     * Return config property as String
     *
     * @param key Property key
     * @return String that represents property value
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    public String getDefault(String key) {
        return defaults.get(key);
    }

    /**
     * Return config property as boolean
     *
     * @param key Property key
     * @return boolean that represents property value
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * Return config property as integer
     *
     * @throws NumberFormatException  if the string does not contain a parsable integer.
     * @param key Property key
     * @return integer that represents property value
     */
    public int getInteger(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public void set(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void set(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Save properties to config file
     */
    public void save() {
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(
                     new FileOutputStream(configFile));
            properties.storeToXML(os, null);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error has occurred while saving config file", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error has occurred while saving config file", ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "An error has occurred while closing config file output stream", ex);
            }
        }
    }

    /**
     * Singleton pattern
     *
     * @return Single application Config instance
     */
    static public Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * Set default properties if loaded value is empty
     */
    private void loadDefaultConfigValues() {
        for (Map.Entry<String,String> entry: defaults.entrySet()) {
            if (properties.getProperty((String) entry.getKey()) == null) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    public Iterator iterator() {
        return new Iterator() {

            private int position = 0;
            private Map.Entry<Object,Object>[] cache = null;

            public boolean hasNext() {
                return (position < properties.size());
            }

            public Map.Entry next() {
                if (cache == null) {
                    Set<Map.Entry<Object,Object>> set = properties.entrySet();
                    cache = set.toArray(new Map.Entry[set.size()]);
                }
                return cache[position++];
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator cannot delete config entry");
            }
        };
    }
}
