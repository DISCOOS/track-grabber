package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationStartedLabel;
    private JLabel statusLabel;
    private GridBagConstraints constraints;

    public OperationPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        WINDOW.getContentPane().add(this, BorderLayout.NORTH);
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        // Header Label
        JLabel headerLabel = WINDOW.makeLabel(Messages.PROJECT_NAME.get(), WINDOW.HEADER_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 0);
        add(headerLabel, constraints);

        // Operation started label
        operationStartedLabel = WINDOW.makeLabel("<html><body>"
                        + Messages.OPERATION_STARTED.get()
                        + "<br>"
                        + StringTools.formatDate(OPERATION_MANAGER.getOperationStartTime())
                        + "</body></html>",
                WINDOW.TEXT_FONT_SIZE
        );
        WINDOW.setConstraintsXY(constraints, 0, 1);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 2;
        add(operationStartedLabel, constraints);

        // isConnected label
        statusLabel = WINDOW.makeLabel(Messages.GPS_OFFLINE.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 3, 1);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(statusLabel, constraints);

        // New operation label and input
        JLabel operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 2);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(operationNameLabel, constraints);

        JTextField operationNameInput = new JTextField();
        WINDOW.setConstraintsXY(constraints, 0, 3);
        constraints.gridwidth = 2;
        constraints.weightx = 2;
        constraints.fill = GridBagConstraints.BOTH;
        add(operationNameInput, constraints);

        // Date for operation and input
        JLabel operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 2, 2);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(operationDateLabel, constraints);

        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setDate(Calendar.getInstance().getTime());
        datePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        WINDOW.setConstraintsXY(constraints, 2, 3);
        add(datePicker, constraints);

        // Already existing operation label and input
        JLabel existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 4);
        add(existingOperationLabel, constraints);

        JComboBox<String> existingOperationInput = new JComboBox<String>();
        WINDOW.setConstraintsXY(constraints, 0, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(existingOperationInput, constraints);

        JButton registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 3, 5);
        add(registerButton, constraints);
    }

    private void testJComboBox(JComboBox<String> comboBox) {
        //comboBox.add("Hund");
    }

    /**
     * Set the status of whether a gps is connected or not.
     *
     * @param status the new status.
     */
    public void setStatus(String status) {
        statusLabel.setText("GPS: " + status);
    }
}
