package jtanks.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jtanks.mapeditor.JTanksMap;
import jtanks.mapeditor.Landscape;

/**
 * @author Voroshin Alexey
 */
public class MapDialog extends JDialog {

    private static final long           serialVersionUID  = 1L;
    private static ResourceBundle MESSAGES          = null;//ResourceBundle.getBundle("jtanks.mapeditor.gui.messages");

    private static final int            TEXT_FIELD_HEIGHT = 30;
    private static final int            LABEL_HEIGHT      = 20;
    private static final int            LABEL_WIDTH       = 100;

    private final Window                mainWindow;
    private final Landscape             landscape;

    private JPanel                      namePanel;
    private JLabel                      nameLabel;
    private JTextField                  nameTextField;
    private JPanel                      aboutPanel;
    private JLabel                      aboutLabel;
    private JTextArea                   aboutTextArea;
    private JPanel                      sizePanel;
    private JLabel                      sizeLabel;
    private JTextField                  widthTextField;
    private JLabel                      xLabel;
    private JTextField                  heightTextField;
    private JPanel                      buttonsPanel;
    private JButton                     okButton;
    private JButton                     cancelButton;
    private JScrollPane                 aboutScrollPane;

    private boolean                     createNew         = true;

    public MapDialog(final Window mainWindowA, final Landscape landscapeA) {
        MESSAGES = ResourceBundle.getBundle("jtanks.mapeditor.gui.messages");
        setModal(true);
        setResizable(false);
        setTitle(MESSAGES.getString("MapDialog.Settings"));
        setSize(400, 250);

        mainWindow = mainWindowA;
        landscape = landscapeA;

        initComponents();
    }

    public void createNewMap() {
        createNew = true;

        getOKButton().setText(MESSAGES.getString("MapDialog.Create"));

        getWidthTextField().setEnabled(true);
        getHeightTextField().setEnabled(true);

        GuiManager.moveToCenterWindow(mainWindow, this);
        setVisible(true);
    }

    public void editMapSettings() {
        final JTanksMap map = landscape.getMap();
        if (map.getName() != null) {
            createNew = false;

            getOKButton().setText(MESSAGES.getString("MapDialog.Update"));

            getNameTextField().setText(map.getName());
            getWidthTextField().setText(Integer.toString(map.getWidth()));
            getWidthTextField().setEnabled(false);
            getHeightTextField().setText(Integer.toString(map.getHeight()));
            getHeightTextField().setEnabled(false);

            GuiManager.moveToCenterWindow(mainWindow, this);
            setVisible(true);
        }
    }

    public void initComponents() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        centerPanel.add(getNamePanel());
        centerPanel.add(getSizePanel());
        centerPanel.add(getAboutPanel());
        centerPanel.add(getButtonsPanel());

        add(centerPanel);
    }

    private JPanel getNamePanel() {
        if (namePanel == null) {
            namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
            namePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
            namePanel.add(getNameLabel());
            namePanel.add(getNameTextField());
        }
        return namePanel;
    }

    private JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new JLabel(MESSAGES.getString("MapDialog.Name"));
            nameLabel.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            nameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            nameLabel.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        }
        return nameLabel;
    }

    private JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, TEXT_FIELD_HEIGHT));
        }
        return nameTextField;
    }

    private JPanel getAboutPanel() {
        if (aboutPanel == null) {
            aboutPanel = new JPanel();
            aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.X_AXIS));
            aboutPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
            aboutPanel.add(getAboutLabel());
            aboutPanel.add(getAboutScrollPane());
        }
        return aboutPanel;
    }

    private JLabel getAboutLabel() {
        if (aboutLabel == null) {
            aboutLabel = new JLabel(MESSAGES.getString("MapDialog.Description"));
            aboutLabel.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            aboutLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            aboutLabel.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        }
        return aboutLabel;
    }

    private JScrollPane getAboutScrollPane() {
        if (aboutScrollPane == null) {
            aboutScrollPane = new JScrollPane(getAboutTextArea());
            aboutScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            aboutScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            aboutScrollPane.setPreferredSize(new Dimension(500, 300));
        }
        return aboutScrollPane;
    }

    private JTextArea getAboutTextArea() {
        if (aboutTextArea == null) {
            aboutTextArea = new JTextArea();
        }
        return aboutTextArea;
    }

    private JPanel getSizePanel() {
        if (sizePanel == null) {
            sizePanel = new JPanel();
            sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.X_AXIS));
            sizePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
            sizePanel.add(getSizeLabel());
            sizePanel.add(getWidthTextField());
            sizePanel.add(getXLabel());
            sizePanel.add(getHeightTextField());
        }
        return sizePanel;
    }

    private JLabel getSizeLabel() {
        if (sizeLabel == null) {
            sizeLabel = new JLabel(MESSAGES.getString("MapDialog.Size"));
            sizeLabel.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            sizeLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            sizeLabel.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        }
        return sizeLabel;
    }

    private JTextField getWidthTextField() {
        if (widthTextField == null) {
            widthTextField = new JTextField();
            widthTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, TEXT_FIELD_HEIGHT));
        }
        return widthTextField;
    }

    private JLabel getXLabel() {
        if (xLabel == null) {
            xLabel = new JLabel("x");
        }
        return xLabel;
    }

    private JTextField getHeightTextField() {
        if (heightTextField == null) {
            heightTextField = new JTextField();
            heightTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, TEXT_FIELD_HEIGHT));
        }
        return heightTextField;
    }

    private JPanel getButtonsPanel() {
        if (buttonsPanel == null) {
            buttonsPanel = new JPanel();
            buttonsPanel.add(getOKButton(), BorderLayout.WEST);
            buttonsPanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
            buttonsPanel.add(getCancelButton(), BorderLayout.EAST);
            buttonsPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        }
        return buttonsPanel;
    }

    private JButton getOKButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setMinimumSize(new Dimension(120, 30));
            okButton.setPreferredSize(new Dimension(120, 30));
            okButton.setMaximumSize(new Dimension(120, 30));
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    if (getNameTextField().getText().isEmpty() == false
                            && getAboutTextArea().getText().isEmpty() == false
                            && getWidthTextField().getText().isEmpty() == false
                            && getHeightTextField().getText().isEmpty() == false) {
                        if (createNew) {
                            byte x = 10;
                            try {
                                x = Byte.valueOf(getWidthTextField().getText());
                            } catch (final Exception ex) {
                            }

                            byte y = 10;
                            try {
                                y = Byte.valueOf(getHeightTextField().getText());
                            } catch (final Exception ex) {
                            }

                            x = (byte) Math.min(Math.max(x, 10), 100);
                            y = (byte) Math.min(Math.max(y, 10), 100);

                            landscape.createNewMap(x, y, getNameTextField().getText());

                        } else {
                            landscape.editMapSettings(getNameTextField().getText());
                        }

                        setVisible(false);
                    }
                }
            });
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton(MESSAGES.getString("MapDialog.Cancel"));
            cancelButton.setMinimumSize(new Dimension(120, 30));
            cancelButton.setPreferredSize(new Dimension(120, 30));
            cancelButton.setMaximumSize(new Dimension(120, 30));
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return cancelButton;
    }

}