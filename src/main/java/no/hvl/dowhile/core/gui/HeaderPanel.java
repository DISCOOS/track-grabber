package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;

/**
 * This class has labels for displaying application name and info about the current operation.
 */
class HeaderPanel extends JPanel {
    private final Window WINDOW;
    private GridBagConstraints constraints;
    private JLabel appName;
    private JLabel spacer;
    private JLabel operationInfoHeader;
    private JLabel operationInfoName;
    private JLabel operationInfoStart;

    /**
     * Constructor setting up the labels.
     *
     * @param WINDOW the current instance of the Window.
     * @see Window
     */
    HeaderPanel(final Window WINDOW) {
        this.WINDOW = WINDOW;
        this.constraints = new GridBagConstraints();
        this.appName = WINDOW.makeHeaderLabel(Messages.PROJECT_NAME.get());
        this.spacer = WINDOW.makeHeaderLabel(Messages.SPACER.get());
        this.operationInfoHeader = WINDOW.makeLabel(Messages.OPERATION_INFO.get(), Font.BOLD);
        this.operationInfoName = WINDOW.makeLabel(Messages.OPERATION_INFO_NAME.get("Ingen operasjon valgt."), Font.BOLD);
        this.operationInfoStart = WINDOW.makeLabel(Messages.OPERATION_INFO_START.get("Ingen operasjon valgt."), Font.BOLD);

        setLayout(new GridBagLayout());

        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 1);
        constraints.gridheight = 3;
        add(appName, constraints);
        WINDOW.modifyConstraints(constraints, 1, 0, GridBagConstraints.NORTH, 4);
        constraints.gridheight = 1; // Reset the above height.
        add(spacer, constraints);
        WINDOW.modifyConstraints(constraints, 5, 0, GridBagConstraints.WEST, 1);
        add(operationInfoHeader, constraints);
        WINDOW.modifyConstraints(constraints, 5, 1, GridBagConstraints.WEST, 1);
        add(operationInfoName, constraints);
        WINDOW.modifyConstraints(constraints, 5, 2, GridBagConstraints.WEST, 1);
        add(operationInfoStart, constraints);

        operationInfoHeader.setName("operationInfoHeader");
        operationInfoName.setName("operationInfoName");
        operationInfoStart.setName("operationInfoStart");

        setBackground(new Color(242, 94, 94));
    }

    /**
     * Update the info about the current operation.
     *
     * @param operation the current operation.
     */
    void updateOperationInfo(Operation operation) {
        operationInfoName.setText(Messages.OPERATION_INFO_NAME.get(operation.getName()));
        operationInfoStart.setText(Messages.OPERATION_INFO_START.get(StringTools.formatDate(operation.getStartTime())));
    }
}
