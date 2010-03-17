/*
 * GNU General Public License v2
 * 
 * @version $Id: DataTransfer.java 144 2009-05-13 03:13:26Z ru.energy $
 */
package jtanks.game.screens.helpers;

import java.util.HashMap;

public class DataTransfer {

    private HashMap<String,Object> data = new HashMap<String,Object>();

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

}
