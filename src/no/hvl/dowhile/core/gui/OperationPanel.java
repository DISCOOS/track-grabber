package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;

import javax.swing.*;

/**
 * Created by JonKjetil on 05.04.2017.
 */
public class OperationPanel extends JPanel {

    public OperationPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {

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

}
