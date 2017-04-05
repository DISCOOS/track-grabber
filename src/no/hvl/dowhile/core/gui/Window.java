package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class represents the Window exposed to the user for configuring the application.
 * Responsible of laying out elements and handling user interaction.
 */
public class Window extends JFrame {
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
}
