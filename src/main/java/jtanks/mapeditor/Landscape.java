package jtanks.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import jtanks.mapeditor.utils.RCSVStorage;

public class Landscape {

    private static final Logger           LOGGER     = Logger.getLogger(Landscape.class.getName());

    private final RCSVStorage             storage;

    private final HashMap<String, String> symbolsKey = new HashMap<String, String>();
    private final HashMap<String, String> keySymbols = new HashMap<String, String>();
    private final HashMap<String, Color>  keyColors  = new HashMap<String, Color>();

    private String                        currentAreaLeft;
    private String                        currentAreaRight;
    private JPanel[][]                    matrix;
    private JPanel                        matrixPanel;
    private JScrollPane                   matrixScrollPane;
    private JToolBar                      toolBar;

    private final JTanksMap               map        = new JTanksMap();

    private Color getCurrentColorLeft() {
        return keyColors.get(currentAreaLeft);
    }

    private Color getCurrentColorRight() {
        return keyColors.get(currentAreaRight);
    }

    public Landscape(final String path) {
        storage = new RCSVStorage(path);
        try {
            storage.load();
            parse();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            System.exit(0);
        }
    }

    private void parse() {
        for (final String key : storage.getKeys()) {
            final List<String> list = storage.getValue(key);

            final int r = Integer.valueOf(list.get(1));
            final int g = Integer.valueOf(list.get(2));
            final int b = Integer.valueOf(list.get(3));

            keySymbols.put(key, list.get(0));
            symbolsKey.put(list.get(0), key);
            keyColors.put(key, new Color(r, g, b, 255));
            currentAreaLeft = key;
            currentAreaRight = key;
        }
    }

    public void fillMatrix() {
        if (matrix != null) {
            for (int ay = 0; ay < matrix.length; ay++) {
                for (int ax = 0; ax < matrix[ay].length; ax++) {
                    matrix[ay][ax].setBackground(getCurrentColorLeft());
                }
            }
        }
    }

    public void editMapSettings(final String name, final String about) {
        map.setName(name);
        map.setAbout(about);
    }

    public void createNewMap(final int x, final int y, final String name, final String about) {
        matrixPanel.removeAll();
        matrixPanel.add(newMatrixPanel(x, y, false));
        matrixScrollPane.paintComponents(matrixScrollPane.getGraphics());

        map.createMatrix(x, y);
        map.setName(name);
        map.setAbout(about);
    }

    public void loadMap(final String fileName) {
        try {
            map.load(fileName);

            matrixPanel.removeAll();
            matrixPanel.add(newMatrixPanel(map.getWidth(), map.getHeight(), true));
            matrixScrollPane.paintComponents(matrixScrollPane.getGraphics());
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void saveMap(final String fileName) {
        try {
            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    final String key = matrix[y][x].getName();
                    map.setCell(x, y, keySymbols.get(key));
                }
            }

            map.save(fileName);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    private void updateCellFromMap(final JPanel cell, final int x, final int y) {
        final String symbol = String.valueOf(map.getCell(x, y));
        cell.setBackground(keyColors.get(symbolsKey.get(symbol)));
        cell.setName(symbolsKey.get(symbol));
    }

    private void updateCellLeft(final JPanel cell) {
        cell.setBackground(getCurrentColorLeft());
        cell.setName(currentAreaLeft);
    }

    private void updateCellRight(final JPanel cell) {
        cell.setBackground(getCurrentColorRight());
        cell.setName(currentAreaRight);
    }

    public JTanksMap getMap() {
        return map;
    }

    private String getKey(final JPanel cell) {
        return cell.getName();
    }

    private JPanel newMatrixPanel(final int x, final int y, final boolean createFromMap) {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(y, x));
        panel.setMaximumSize(new Dimension(30 * x, 30 * y));
        panel.setPreferredSize(new Dimension(30 * x, 30 * y));
        panel.setMinimumSize(new Dimension(30 * x, 30 * y));

        matrix = new JPanel[y][x];

        for (int ay = 0; ay < y; ay++) {
            for (int ax = 0; ax < x; ax++) {
                final JPanel cell = new JPanel();
                if (createFromMap)
                    updateCellFromMap(cell, ax, ay);
                else
                    updateCellLeft(cell);
                cell.setToolTipText((ax + 1) + " : " + (ay + 1));
                cell.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent arg0) {
                        if ((arg0.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                            updateCellLeft(cell);
                        } else if ((arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                            updateCellRight(cell);
                        }
                    }

                    @Override
                    public void mouseExited(final MouseEvent arg0) {
                        cell.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                    }

                    @Override
                    public void mouseEntered(final MouseEvent arg0) {
                        if ((arg0.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                            updateCellLeft(cell);
                        } else if ((arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                            updateCellRight(cell);
                        }
                        cell.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    }
                });

                matrix[ay][ax] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    public JScrollPane getMatrixScrollPane() {
        if (matrixScrollPane == null) {
            matrixScrollPane = new JScrollPane();
            matrixScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            matrixScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            matrixScrollPane.setViewportView(getMatrixPanel());
        }
        return matrixScrollPane;
    }

    public JPanel getMatrixPanel() {
        if (matrixPanel == null) {
            matrixPanel = new JPanel();
            matrixPanel.setBorder(new EtchedBorder());
        }
        return matrixPanel;
    }

    public JToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();

            toolBar.setRollover(false);
            toolBar.setFloatable(false);
            toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));

            for (final String key : storage.getKeys()) {
                final JPanel panel = new JPanel();
                panel.setBackground(keyColors.get(key));
                panel.setMaximumSize(new Dimension(30, 30));
                panel.setPreferredSize(new Dimension(30, 30));
                panel.setMinimumSize(new Dimension(30, 30));
                panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent arg0) {
                        if ((arg0.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                            currentAreaLeft = key;
                        } else if ((arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                            currentAreaRight = key;
                        }
                    }

                    @Override
                    public void mouseExited(final MouseEvent arg0) {
                        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                    }

                    @Override
                    public void mouseEntered(final MouseEvent arg0) {
                        panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    }
                });

                panel.setToolTipText(key);

                toolBar.add(panel);
            }
        }

        return toolBar;
    }
}