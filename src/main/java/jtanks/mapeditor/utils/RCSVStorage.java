package jtanks.mapeditor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * хранилище в формате csv
 * хранит в map
 * ключи - string (первые элементы строк) 
 * значения - set of string
 * только для чтения
 */

public class RCSVStorage {

    private static final Logger             LOGGER = Logger.getLogger(RCSVStorage.class.getName());

    private final String                    delimiter;
    private final String                    fileName;
    private final Map<String, List<String>> array  = new HashMap<String, List<String>>();
    private Map<String, List<String>>       unmodifiableArray;

    public RCSVStorage(final String fileNameA, final String delimiterA) {
        fileName = fileNameA;
        delimiter = delimiterA;
    }

    public RCSVStorage(final String fileNameA) {
        fileName = fileNameA;
        delimiter = ";";
    }

    public RCSVStorage load() throws Exception {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));

            array.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] values = line.split(delimiter);
                final List<String> list = new ArrayList<String>();

                for (int i = 1; i < values.length; i++)
                    list.add(values[i]);

                array.put(values[0], list);
            }

            unmodifiableArray = Collections.unmodifiableMap(array);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (final IOException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }

        return this;
    }

    public Map<String, List<String>> export() {
        return unmodifiableArray;
    }

    public Set<String> getKeys() {
        return unmodifiableArray.keySet();
    }

    public List<String> getValue(final String keyA) {
        return unmodifiableArray.get(keyA);
    }

}