package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class represents the Window exposed to the user for configuring the application.
 * The class has different panels for displaying information depending on certain events in the application.
 */
public class Window extends JFrame {
    protected final int HEADER_FONT_SIZE = 24;
    protected final int TEXT_FONT_SIZE = 16;
    private final OperationManager OPERATION_MANAGER;
    private OperationPanel operationPanel;
    private TrackPanel trackPanel;

    public Window(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;

        setTitle(Messages.PROJECT_NAME.get());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        operationPanel = new OperationPanel(OPERATION_MANAGER, this);
        trackPanel = new TrackPanel(OPERATION_MANAGER, this);

        open();
        trackPanel.close();
        operationPanel.open();

        // Listener for when the window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), Messages.CONFIRM_EXIT.get(), Messages.PROJECT_NAME.get(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
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
     * Set the status of whether a gps is connected or not.
     *
     * @param status the new status.
     */
    public void setStatus(String status) {
        trackPanel.setStatus(status);
    }

    /**
     * Makes a JLabel with given text and font size
     *
     * @param text text that will be inserted into the JLabel
     * @param fontSize font size that will be used on the Jlabel
     * @return a JLabel with given text and font size
     */
    public JLabel makeLabel(String text, int fontSize) {
        JLabel theLabel = new JLabel(text);
        theLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, fontSize));

        return theLabel;
    }
    /**
     * Setting the constraints for x and y coordinates
     *
     * @param constraints the GridBagConstraints for which we will set the x and y coordinate
     * @param x x coordinate for contstraints
     * @param y y coordinate for contstraints
     */
    public void setConstraintsXY(GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
    }

    /**
     * Setting the insets of the constraints
     *
     * @param constraints the GridBagConstraints for which we will set the insets
     * @param borders all borders around grid cell
     */
    public void setConstraintsInsets(GridBagConstraints constraints, int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }
}
