/*
 * GNU General Public License v2
 *
 * @version $Id$
 */
package jtanks.system;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {

    private HashMap<String,Timer> timers = new HashMap<String,Timer>();

    /**
     * Create timer
     * 
     * @param caller The created timer will be associated with this class
     * @param task TimerTask object
     * @param delay delay before executing
     * @return Created timer
     */
    public boolean createTimer(Class caller, TimerTask task, long delay) {
        if (getTimer(caller) != null) {
            return false;
        }
        
        Timer timer = new Timer(new StringBuffer(caller.getName()).append(" timer").toString());
        timer.schedule(task, delay);

        timers.put(caller.getName(), timer);
        return true;
    }

    /**
     * Return timer that associated with a given name
     * @param caller 
     * @return timer for the given class             
     */
    public Timer getTimer(Class caller) {
        return timers.get(caller.getName());
    }

    /**
     * Remove timer from timer pool
     * @param caller Timer pool key
     * @return
     */
    public Timer removeTimer(Class caller) {
        return timers.remove(caller.getName());
    }

    /**
     * Stop scheduled timer and remove it from storage
     * @param caller Timer pool key
     */
    public void stopTimer(Class caller) {
        removeTimer(caller).cancel();
    }
}
