package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JonKjetil on 12.05.2017.
 */
public class StandByPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    // JComponents

    public StandByPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        setBackground(new Color(255, 245, 252));
    }
}
