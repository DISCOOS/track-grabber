package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;

import javax.swing.*;
import java.awt.*;

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    public OperationPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        WINDOW.getContentPane().add(this, BorderLayout.NORTH);
        constraints = new GridBagConstraints();
        setConstraintsInsets(5);
    }

    /**
     * Open/show this panel.
     */
    public void open() {
        setVisible(true);
    }

    /**
     * Close/hide this panel.
     */
    public void close() {
        setVisible(false);
    }

    /**
     * Setting the constraints for x and y coordinates
     *
     * @param x x coordinate for contstraints
     * @param y y coordinate for contstraints
     */

    private void setConstraintsXY(int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
    }

    /**
     * Setting the insets of the constraints
     *
     * @param borders all borders around grid cell
     */
    private void setConstraintsInsets(int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }
}
