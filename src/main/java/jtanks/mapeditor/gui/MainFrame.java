package jtanks.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import jtanks.mapeditor.MapEditor;
import jtanks.mapeditor.Landscape;
import jtanks.mapeditor.utils.WindowStorage;

/**
 * @author Voroshin Alexey
 */
public class MainFrame extends JFrame {

    private static final long           serialVersionUID = 1L;

    private static final Logger         LOGGER           = Logger.getLogger(MainFrame.class.getName());
    private static ResourceBundle MESSAGES         = null;

    private JMenuBar                    mainMenuBar;
    private final WindowStorage         windowStorage    = new WindowStorage("win.txt", this);
    private final Landscape             landscape        = new Landscape("/resources/landscape.txt");
    private final MapDialog             mapDialog        = new MapDialog(this, landscape);
    private JFileChooser                fileChooser;
    private final MainFrame             mainFrame        = this;

    public MainFrame() {
        MESSAGES = ResourceBundle.getBundle("jtanks.mapeditor.gui.editor");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent arg0) {
                MapEditor.close();
            }
        });

        setTitle(MapEditor.getName() + " v" + MapEditor.getVersion());
        setMinimumSize(new Dimension(400, 400));

        initComponents();
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser(MapEditor.getMapsLocation());
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jtm - файлы карт для JTanks", "jtm"));
        }
        return fileChooser;
    }

    private JMenuBar getMainMenuBar() {
        if (mainMenuBar == null) {
            mainMenuBar = new JMenuBar();

            // FILE
            final JMenuItem fileMenuItemExit = new JMenuItem(MESSAGES.getString("MainFrame.Menu.File.Exit"));
            fileMenuItemExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    MapEditor.close();
                }
            });

            final JMenuItem fileMenuItemNew = new JMenuItem(MESSAGES.getString("MainFrame.Menu.File.New"));
            fileMenuItemNew.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    mapDialog.createNewMap();
                }
            });

            final JMenuItem fileMenuItemEdit = new JMenuItem(MESSAGES.getString("MainFrame.Menu.File.Edit"));
            fileMenuItemEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    mapDialog.editMapSettings();
                }
            });

            final JMenuItem fileMenuItemLoad = new JMenuItem(MESSAGES.getString("MainFrame.Menu.File.Load"));
            fileMenuItemLoad.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    final JFileChooser chooser = getFileChooser();
                    if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                        landscape.loadMap(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            });

            final JMenuItem fileMenuItemSave = new JMenuItem(MESSAGES.getString("MainFrame.Menu.File.Save"));
            fileMenuItemSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    final JFileChooser chooser = getFileChooser();
                    if (chooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                        landscape.saveMap(chooser.getSelectedFile().getAbsolutePath());
                    }
                }
            });

            final JMenu fileMenu = new JMenu(MESSAGES.getString("MainFrame.Menu.File"));
            fileMenu.add(fileMenuItemNew);
            fileMenu.add(fileMenuItemEdit);
            fileMenu.add(fileMenuItemLoad);
            fileMenu.add(fileMenuItemSave);
            fileMenu.add(fileMenuItemExit);

            // TOOLS
            final JMenuItem toolsMenuItemFill = new JMenuItem(MESSAGES.getString("MainFrame.Menu.Tools.Fill"));
            toolsMenuItemFill.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    landscape.fillMatrix();
                }
            });

            final JMenu toolsMenu = new JMenu(MESSAGES.getString("MainFrame.Menu.Tools"));
            toolsMenu.add(toolsMenuItemFill);

            // HELP
            final JMenuItem helpMenuItemAbout = new JMenuItem(MESSAGES.getString("MainFrame.Menu.Help.About"));

            final JMenu helpMenu = new JMenu(MESSAGES.getString("MainFrame.Menu.Help"));
            helpMenu.add(helpMenuItemAbout);

            // ADD COMPONENTS
            mainMenuBar.add(fileMenu);
            mainMenuBar.add(toolsMenu);
            mainMenuBar.add(helpMenu);
        }
        return mainMenuBar;
    }

    private void initComponents() {
        setJMenuBar(getMainMenuBar());

        add(landscape.getToolBar(), BorderLayout.WEST);
        add(landscape.getMatrixScrollPane());
    }

    public void open() {
        try {
            windowStorage.load();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            setSize(800, 600);
            setLocation(0, 0);
        }

        setVisible(true);
    }

    public void close() {
        try {
            windowStorage.save();
        } catch (final FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, null, e);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }
}