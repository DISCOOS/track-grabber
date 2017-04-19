package no.hvl.dowhile.core.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationStartedLabel;
    private JLabel statusLabel;
    private JLabel operationNameLabel;
    private JTextField operationNameInput;
    private JLabel operationDateLabel;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private JLabel existingOperationLabel;
    private JComboBox<String> existingOperationInput;
    private JButton registerNewButton;
    private JButton registerExistingButton;

    private JButton newOperationButton;
    private JButton existingOperationButton;

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


        // New operation label and input
        operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 2);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(operationNameLabel, constraints);

        operationNameInput = new JTextField();
        WINDOW.setConstraintsXY(constraints, 0, 3);
        constraints.gridwidth = 2;
        constraints.weightx = 2;
        constraints.fill = GridBagConstraints.BOTH;
        add(operationNameInput, constraints);

        // Date for operation and input
        operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 4);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(operationDateLabel, constraints);

        datePicker = new DatePicker();
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        datePicker.setSettings(dateSettings);
        WINDOW.setConstraintsXY(constraints, 0, 5);
        add(datePicker, constraints);

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.initialTime = LocalTime.now();
        timePicker = new TimePicker(timeSettings);
        WINDOW.setConstraintsXY(constraints, 2, 5);
        add(timePicker, constraints);

        // Already existing operation label and input
        existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 2);
        add(existingOperationLabel, constraints);

        existingOperationInput = new JComboBox<String>();
        WINDOW.setConstraintsXY(constraints, 0, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(existingOperationInput, constraints);

        // Register new operation
        registerNewButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 3, 6);
        add(registerNewButton, constraints);

        // Register existing operation
        registerExistingButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 2, 3);
        add(registerExistingButton, constraints);

        // New operation button
        newOperationButton = new JButton(Messages.NEW_OPERATION_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 0, 2);
        add(newOperationButton, constraints);

        // Extisting operation button
        existingOperationButton = new JButton(Messages.EXISTING_OPERATION_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 2, 2);
        add(existingOperationButton, constraints);

        setVisibilityNewOperation(false);
        setVisibilityExistingOperation(false);

        newOperationButtonListener();
        existingOperationButtonListener();
        registerExistingOperationButtonListener();
        registerNewOperationButtonListener();

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

    private void setVisibilityNewOperation(boolean visible) {
        operationNameInput.setVisible(visible);
        operationDateLabel.setVisible(visible);
        operationNameLabel.setVisible(visible);
        datePicker.setVisible(visible);
        timePicker.setVisible(visible);
        registerNewButton.setVisible(visible);
    }

    private void setVisibilityExistingOperation(boolean visible) {
        existingOperationLabel.setVisible(visible);
        existingOperationInput.setVisible(visible);
        registerExistingButton.setVisible(visible);
    }

    private void setVisibilityOperationButtons(boolean visible) {
        newOperationButton.setVisible(visible);
        existingOperationButton.setVisible(visible);
    }

    private void newOperationButtonListener() {
        newOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOperationButtons(false);
                setVisibilityNewOperation(true);
            }
        });
    }

    private void existingOperationButtonListener() {
        existingOperationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOperationButtons(false);
                setVisibilityExistingOperation(true);
            }
        });
    }

    private void registerNewOperationButtonListener() {
        registerNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int day = datePicker.getDate().getDayOfMonth();
                int month = datePicker.getDate().getMonth().getValue();
                int year = datePicker.getDate().getYear();
                int hour = timePicker.getTime().getHour();
                int minute = timePicker.getTime().getMinute();
                Operation operation = new Operation(operationNameInput.getText(), day, month, year, hour, minute);
                setVisibilityNewOperation(false);
                setVisibilityOperationButtons(true);
                // TODO: Create new operation folder
            }
        });
    }

    public void registerExistingOperationButtonListener() {
        registerExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityExistingOperation(false);
                setVisibilityOperationButtons(true);
            }
        });
    }
}
