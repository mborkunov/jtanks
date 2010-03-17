/*
 * GNU General Public License v2
 * 
 * @version $Id: Manager.java 152 2009-05-13 03:34:04Z ru.energy $
 */
package jtanks.game.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Manager {

    private final String DEFAULT_MAP_FOLDER = "/resources/maps/";
    private final String EXTENSION = "jtm";
    private List<Map> maps = new ArrayList<Map>();
    private String mapFolder;
    private static Manager instance;
    private Logger logger = Logger.getLogger(Manager.class.getName());

    private Manager() {
        this.mapFolder = DEFAULT_MAP_FOLDER;
        init();
    }

    private Manager(String mapFolder) {
        this.mapFolder = mapFolder;
        init();
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    public List<Map> getMaps() {
        return maps;
    }

    public int indexOf(Map map) {
        return maps.indexOf(map);
    }

    private void init() {
        int i = 1;
        while (getClass().getResourceAsStream(mapFolder + i + '.' + EXTENSION) != null) {
            maps.add(new Map(mapFolder + i + '.' + EXTENSION));
            i++;
        }
        logger.info("Found " + (i - 1) + " map(s)");
    }
}
