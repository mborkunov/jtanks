package jtanks.game.gameplay;

public class Carrier {

    private static Carrier instance;

    private int level;

    private Carrier() {
        level = 1;
    }

    public static Carrier getInstance() {
        if (instance == null) {
            instance = new Carrier();
        }
        return instance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
