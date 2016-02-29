package jtanks.system;

public class OS {

    private static String os = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (os.contains("win"));
    }

    public static boolean isMac() {
        return (os.contains("mac"));
    }

    public static boolean isUnix() {
        return (os.contains("nix") || os.contains("nux"));
    }
}
