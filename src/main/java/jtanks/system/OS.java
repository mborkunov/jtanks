/*
 * GNU General Public License v2
 *
 * @version $Id: OS.java 167 2009-05-17 16:40:20Z ru.energy $
 */
package jtanks.system;

public class OS {

    private static String os = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (os.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }
}
