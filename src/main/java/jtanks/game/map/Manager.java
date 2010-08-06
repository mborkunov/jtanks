/*
 * GNU General Public License v2
 * 
 * @version $Id$
 */
package jtanks.game.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Manager {

    private final String EXTENSION = "jtm";
    private List<Map> maps = new ArrayList<Map>();
    private String mapFolder;
    private static Manager instance;
    private Logger logger = Logger.getLogger(Manager.class.getName());

    private Manager() {
        this.mapFolder = "/resources/maps/";
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
            Map map = new Map(mapFolder + i + '.' + EXTENSION);
            map.setId(i);
            maps.add(map);
            i++;
        }
        logger.info("Found " + (i - 1) + " map(s)");
    }
}
