package jtanks.mapeditor;

import jtanks.mapeditor.gui.MainFrame;

/**
 * @author Voroshin Alexey
 */
public class MapEditor {

    static {
        // Locale.setDefault(Locale.ENGLISH);
        // GuiManager.setNimbusLookAndFeel();
        // GuiManager.setSystemLookAndFeel();
    }

    private static final String    MAPS_DEFAULT_LOCATION = "maps/";
    private static String          MAPS_NEW_LOCATION     = MAPS_DEFAULT_LOCATION;

    private static final String    VERSION               = "1.0";
    private static final String    NAME                  = "JTanks.MapEditor";

    private static final MainFrame MAIN_FRAME            = new MainFrame();

    public static void main(final String[] args) {
        MapEditor.start(args);
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getName() {
        return NAME;
    }

    public static String getMapsLocation() {
        return MAPS_NEW_LOCATION;
    }

    public static void start(final String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-m") || args[i].equals("-maps")) {
                    MAPS_NEW_LOCATION = args[++i];
                } else if (args[i].equals("-h") || args[i].equals("-help")) {
                    usage();
                    System.exit(0);
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            usage();
            System.exit(0);
        }

        MAIN_FRAME.open();
    }

    public static void close() {
        MAIN_FRAME.close();

        System.exit(0);
    }

    public static void usage() {
        System.out.println("-m -maps            new maps location");
        System.out.println("-h -help            help message");
    }

}