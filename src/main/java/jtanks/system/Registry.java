/*
 * GNU General Public License v2
 *
 * @version $Id: Registry.java 261 2009-07-05 04:13:37Z ru.energy $
 */
package jtanks.system;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Registry {

    /**
     * Objects storage
     */
    private static HashMap<Object, Object> map = new HashMap<Object, Object>();

    /**
     * Store object in the registry
     * 
     * @param key Cannot be null
     * @param value Cannot be null
     */
    public static void set(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key value cannot be null");
        }

        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        map.put(key, value);
    }

    /**
     * Return object that stored in the registry
     * @param key Key
     * @return
     */
    public static Object get(Object key) {
        return map.get(key);
    }

    public static <T> T get(Class<T> key) {
        Object result = map.get(key);

        if (result != null) {
            if (result.getClass().equals(key) == false) {
                throw new RuntimeException();
            }
        } else {
            try {
                result = key.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(Registry.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Registry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (T) result;
    }

    /**
     * Remove value for the specified key if it present
     * @param key
     */
    public static void remove(Object key) {
        map.remove(key);
    }

}
