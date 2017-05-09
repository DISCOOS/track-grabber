package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents the Window exposed to the user for configuring the application.
 * The class has different panels for displaying information depending on certain events in the application.
 */
public class Window extends JFrame {
    final int HEADER_FONT_SIZE = 32;
    final int TEXT_FONT_SIZE = 24;
    private final OperationManager OPERATION_MANAGER;
    private JPanel cardPanel;
    private HeaderPanel headerPanel;
    private OperationPanel operationPanel;
    private TrackPanel trackPanel;
    private WaypointPanel waypointPanel;

    /**
     * Constructor setting up the Window, logo, listener for closing and creating the different panels to display.
     *
     * @param OPERATION_MANAGER the current instance of the OperationManager.
     * @see OperationManager
     */
    public Window(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;

        setTitle(Messages.PROJECT_NAME.get());
        setSize(800, 400);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Image logo = getLogo();
        if (logo != null) {
            setIconImage(logo);
        }

        headerPanel = new HeaderPanel(this);
        operationPanel = new OperationPanel(OPERATION_MANAGER, this);
        trackPanel = new TrackPanel(OPERATION_MANAGER, this);
        waypointPanel = new WaypointPanel(OPERATION_MANAGER, this);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(operationPanel, "Operation");
        cardPanel.add(trackPanel, "Track");
        cardPanel.add(waypointPanel, "Waypoint");
        add(cardPanel, BorderLayout.NORTH);

        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        open();
        openOperationPanel();

        // Listener for when the window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), Messages.CONFIRM_EXIT.get(), Messages.PROJECT_NAME.get(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    OPERATION_MANAGER.stop();
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Read in the logo to use as the window icon.
     *
     * @return the logo as an Image.
     */
    private Image getLogo() {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("src/main/resources/red_cross_icon.jpg"));
        } catch (IOException ex) {
            return null;
        }
        return bufferedImage;
    }

    /**
     * Open/show the window.
     */
    public void open() {
        setVisible(true);
    }

    /**
     * Close/hide the window.
     */
    public void close() {
        setVisible(false);
    }

    /**
     * Open the panel allowing the administrator to change operation or edit the current operation.
     */
    public void openOperationPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Operation");
    }

    /**
     * Open the panel allowing to user to give info about a track.
     */
    public void openTrackPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Track");
    }

    public void openWaypointPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Waypoint");
    }

    /**
     * Shows a dialog with the given text.
     *
     * @param text the text to show in the dialog.
     */
    public void showDialog(String title, String text) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updating the labels with info about the operation.
     *
     * @param operation the current operation.
     */
    public void updateOperationInfo(Operation operation) {
        operationPanel.updateOperationInfo(operation);
        headerPanel.updateOperationInfo(operation);
    }

    /**
     * Telling the track panel to update the info about which file is currently processed.
     *
     * @param filename  the name of the file.
     * @param filesLeft the amount of files left after the one currently processing.
     */
    public void updateCurrentFile(String filename, int filesLeft) {
        trackPanel.updateCurrentFile(filename, filesLeft);
    }

    /**
     * Tell the operation panel to add existing operations to the selector in the user interface.
     *
     * @param operations the operations to add.
     */
    public void showExistingOperations(List<Operation> operations) {
        operationPanel.showExistingOperations(operations);
    }

    /**
     * Makes a JLabel with given text and font size
     *
     * @param text     text that will be inserted into the JLabel
     * @param fontSize font size that will be used on the Jlabel
     * @return a JLabel with given text and font size
     */
    public JLabel makeLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, fontSize));
        return label;
    }

    public JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, TEXT_FONT_SIZE));

        return button;
    }

    /**
     * Utility method to set the data for the constraints.
     *
     * @param constraints the constraints to modify.
     * @param x           X-coordinate.
     * @param y           Y-coordinate.
     * @param anchor      Anchor to define how the object will "float" in the window.
     * @param gridWidth   The amount of grids the element should cover.
     */
    public void modifyConstraints(GridBagConstraints constraints, int x, int y, int anchor, int gridWidth) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.anchor = anchor;
        constraints.gridwidth = gridWidth;
    }

    /**
     * Setting the insets of the constraints
     *
     * @param constraints the GridBagConstraints for which we will set the insets
     * @param borders     all borders around grid cell
     */
    public void setConstraintsInsets(GridBagConstraints constraints, int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }
}
