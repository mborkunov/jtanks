package jtanks.game.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Manager {

    private static final String EXTENSION = "jtm";
    private List<Map> maps = new ArrayList<Map>();
    private String mapFolder;
    private static Manager instance;
    private Logger logger = Logger.getLogger(Manager.class.getName());

    private Manager() {
        this("/resources/maps/");
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
        while (getClass().getResourceAsStream(mapFolder + i + "." + EXTENSION) != null) {
            Map map = new Map(mapFolder + i + '.' + EXTENSION);
            map.setId(i);
            maps.add(map);
            i++;
        }
        logger.info("Found " + (i - 1) + " map(s)");

        /*String userMapsFolder = Config.getInstance().applicationDirectory.getAbsolutePath() + "/maps/";
        int m = 1;
        while (new File(userMapsFolder + String.valueOf(m) + "." + EXTENSION).exists()) {
            Map map = new Map(userMapsFolder + String.valueOf(m++) + "." + EXTENSION);
            map.setId(i++);
            maps.add(map);
        }
        logger.info("Found " + (m - 1) + " user's map(s)");*/
    }
}
