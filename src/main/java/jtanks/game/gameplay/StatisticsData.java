package jtanks.game.gameplay;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import jtanks.system.ResourceManager;

public class StatisticsData implements Serializable, Iterable {

    private static transient File FILE = new File(ResourceManager.JTANKS_USER_DIR, "statistics");
    private transient Logger logger = Logger.getLogger(StatisticsData.class.getName());
    private static final long serialVersionUID = 2L;

    protected long time = 0;
    protected int bullets = 0;
    protected int tanks = 0;
    protected int deaths = 0;
    protected int lastMap = 0;

    private StatisticsData() {
    }

    /**
     * Load statistics data from file
     * @return Loaded statistics object
     */
    public static StatisticsData load() {
        Logger log = Logger.getLogger(StatisticsData.class.getName());
        StatisticsData stats = null;
        if (FILE.exists() && FILE.canRead()) {
            try {
                ObjectInputStream is = new ObjectInputStream(
                                          new BufferedInputStream(
                                             new InflaterInputStream(
                                                new FileInputStream(FILE))));
                try {
                    stats = (StatisticsData) is.readObject();
                } catch (Exception ex) {
                    log.severe("Cannot restore statistics from file: " + ex.getMessage());
                } finally {
                    is.close();
                }
            } catch (IOException ex) {
                log.severe("Cannot open statistics file: " + ex.getMessage());
            }
        }
        if (stats == null) {
            stats = new StatisticsData();
        } else {
            log.info("Statistics data successfully loaded");
        }
        return stats;
    }

    /**
     * Save statistics data to file
     */
    public void store() {
        try {
            if (FILE.exists() || FILE.createNewFile()) {
                ObjectOutputStream os = new ObjectOutputStream(
                                            new BufferedOutputStream(
                                                new DeflaterOutputStream(
                                                    new FileOutputStream(FILE))));
                try {
                    os.writeObject(this);
                } finally {
                    os.close();
                }
            } else {
                logger.severe("Cannot create statistics file");
            }
        } catch (IOException ex) {
            logger.severe("Cannot save statistics to file: " + ex.getMessage());
        }
    }

    /**
     * Get the value of bullets
     *
     * @return the value of bullets
     */
    public int getBullets() {
        return bullets;
    }

    /**
     * Set the value of bullets
     *
     * @param bullets new value of bullets
     */
    public void setBullets(int bullets) {
        this.bullets = bullets;
    }


    /**
     * Get the value of time
     *
     * @return the value of time
     */
    public long getTime() {
        return time;
    }

    /**
     * Set the value of time
     *
     * @param time new value of time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Get the value of tanks
     *
     * @return the value of tanks
     */
    public int getTanks() {
        return tanks;
    }

    /**
     * Set the value of tanks
     *
     * @param tanks new value of tanks
     */
    public void setTanks(int tanks) {
        this.tanks = tanks;
    }

    /**
     * Get the value of deaths
     *
     * @return the value of deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Set the value of deaths
     *
     * @param deaths new value of deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getLastMap() {
        return lastMap;
    }

    public void setLastMap(int lastMap) {
        this.lastMap = lastMap;
    }

    /**
     * Return current statistics data as map
     *
     * @return Current statistics as a simple key-value map
     */
    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("time", TimeUnit.MILLISECONDS.toMinutes(time) + " minute(s)");
        map.put("tanks", tanks);
        map.put("bullets", bullets);
        map.put("deaths", deaths);
        map.put("lastMap", lastMap);
        return map;
    }

    /**
     * Iterate through a map
     * 
     * @return
     */
    public Iterator<Map.Entry<String, Object>> iterator() {
        return toMap().entrySet().iterator();
    }
}
