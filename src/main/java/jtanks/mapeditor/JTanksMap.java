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

        NAME("[NAME]"), ABOUT("[ABOUT]"), WIDTH("[WIDTH]"), HEIGHT("[HEIGHT]"), LAND("[LAND]");

        public final String id;

        MapToken(final String id) {
            this.id = id;
        }

    }

    private String   name;
    private String   about;
    private char[][] map;
    private int      width;
    private int      height;

    public JTanksMap() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(final String about) {
        this.about = about;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void createMatrix(final int x, final int y) {
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
                if (line.equals(MapToken.NAME.id)) {
                    name = in.readLine();
                } else if (line.equals(MapToken.ABOUT.id)) {
                    about = in.readLine();
                } else if (line.equals(MapToken.WIDTH.id)) {
                    width = Integer.valueOf(in.readLine());
                } else if (line.equals(MapToken.HEIGHT.id)) {
                    height = Integer.valueOf(in.readLine());
                } else if (line.equals(MapToken.LAND.id)) {
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

            out.println(MapToken.NAME.id);
            out.println(name);

            out.println(MapToken.ABOUT.id);
            out.println(about);

            out.println(MapToken.WIDTH.id);
            out.println(width);

            out.println(MapToken.HEIGHT.id);
            out.println(height);

            out.println(MapToken.LAND.id);
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