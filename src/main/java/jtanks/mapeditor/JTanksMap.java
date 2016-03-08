package jtanks.mapeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * @author Voroshin Alexey
 */
public class JTanksMap {

    private enum MapToken {

        NAME, SIZE, CONTENT;

        public String getIdentifier() {
            return "[" + name() + "]";
        }

    }

    private String   name;
    private char[][] map;
    private byte      width;
    private byte      height;

    public JTanksMap() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final byte width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final byte height) {
        this.height = height;
    }

    public void createMatrix(byte x, byte y) {
        map = new char[y][x];

        for (int ay = 0; ay < y; ay++)
            Arrays.fill(map[ay], ' ');

        setWidth(x);
        setHeight(y);
    }

    public void setCell(final int x, final int y, final String symbol) {
        map[y][x] = symbol.charAt(0);
    }

    public char getCell(final int x, final int y) {
        return map[y][x];
    }

    public void load(final String fileName) throws Exception {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(fileName)));

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals(MapToken.NAME.getIdentifier())) {
                    name = in.readLine();
                } else if (line.equals(MapToken.SIZE.getIdentifier())) {
                    String[] size = in.readLine().split("x");
                    width  = Byte.parseByte(size[0]);
                    height = Byte.parseByte(size[1]);
                } else if (line.equals(MapToken.CONTENT.getIdentifier())) {
                    createMatrix(width, height);
                    for (int y = 0; y < height; y++) {
                        line = in.readLine();
                        for (int x = 0; x < line.length() && x < width; x++) {
                            map[y][x] = line.charAt(x);
                        }
                    }
                }
            }
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (final Exception ex) {
            }
        }
    }

    public void save(final String fileName) throws Exception {
        PrintStream out = null;
        try {
            out = new PrintStream(fileName);

            out.println(MapToken.NAME.getIdentifier());
            out.println(name);

            out.println(MapToken.SIZE.getIdentifier());
            out.println(width + "x" + height);

            out.println(MapToken.CONTENT.getIdentifier());
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    out.print(map[y][x]);
                }
                out.println();
            }
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (final Exception ex) {
            }
        }
    }
}