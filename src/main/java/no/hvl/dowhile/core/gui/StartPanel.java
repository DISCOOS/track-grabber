package no.hvl.dowhile.core.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by JonKjetil on 15.05.2017.
 */
public class StartPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    private JButton newOperationButton;
    private JButton existingOperationButton;

    private JLabel operationNameLabel;
    private JTextField operationNameInput;
    private JLabel operationDateLabel;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private JLabel errorMessageLabel;

    private JLabel existingOperationLabel;
    private JComboBox<String> existingOperationInput;
    private JButton registerNewButton;
    private JButton registerExistingButton;

    public StartPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        chooseOperationGUI();
        existingOperationGUI();
        createNewOperationGUI();

        datePicker.setLocale(new Locale("no"));
        timePicker.setLocale(new Locale("no"));

        setInitialVisibility();

        registerExistingOperationButtonListener();
        registerNewOperationButtonListener();
        newOperationButtonListener();
        existingOperationButtonListener();

        setBackground(new Color(255, 245, 252));
    }


    /**
     * Creates the start buttons for creating an new operation or choosing an existing operation in the GUI
     */
    private void chooseOperationGUI() {
        // New operation button
        newOperationButton = WINDOW.makeButton(Messages.NEW_OPERATION_BUTTON.get(), 300, 100);
        newOperationButton.setName("newOperationButton");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.CENTER, 2);

        add(newOperationButton, constraints);

        // Existing operation button
        existingOperationButton = WINDOW.makeButton(Messages.EXISTING_OPERATION_BUTTON.get(), 300, 100);
        existingOperationButton.setName("existingOperationButton");
        WINDOW.modifyConstraints(constraints, 2, 0, GridBagConstraints.CENTER, 2);
        add(existingOperationButton, constraints);
    }

    /**
     * Creates the GUI for choosing an existing operation
     */
    private void existingOperationGUI() {
        // Already existing operation label
        existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), Font.PLAIN);
        existingOperationLabel.setName("existingOperationLabel");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 3);
        add(existingOperationLabel, constraints);

        // Already existing operation input
        existingOperationInput = new JComboBox<>();
        existingOperationInput.setName("existingOperationInput");
        existingOperationInput.setPreferredSize(new Dimension(100, 40));
        existingOperationInput.setFont(WINDOW.TEXT_FONT);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(existingOperationInput, constraints);

        // Register existing operation
        registerExistingButton = WINDOW.makeButton(Messages.REGISTER_EXISTING_BUTTON.get(), 150, 50);
        registerExistingButton.setName("registerExistingButton");
        WINDOW.modifyConstraints(constraints, 3, 1, GridBagConstraints.CENTER, 1);
        add(registerExistingButton, constraints);
    }

    /**
     * Creates the GUI for creating an new operation
     */
    private void createNewOperationGUI() {
        // New operation label
        operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), Font.PLAIN);
        operationNameLabel.setName("operationNameLabel");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 2);
        add(operationNameLabel, constraints);

        // New operation name input
        operationNameInput = WINDOW.makeTextField(100, 30);
        operationNameInput.setName("operationNameInput");
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.CENTER, 4);
        constraints.fill = GridBagConstraints.BOTH;
        add(operationNameInput, constraints);

        // Error message label
        errorMessageLabel = WINDOW.makeLabel(" ", Font.BOLD);
        errorMessageLabel.setName("errorMessageLabel");
        errorMessageLabel.setForeground(Color.RED);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 4);
        add(errorMessageLabel, constraints);

        // Date for operation and input
        operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), Font.PLAIN);
        operationDateLabel.setName("operationDateLabel");
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 2);
        add(operationDateLabel, constraints);

        datePicker = WINDOW.makeDatePicker(50, 30);
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 2);
        add(datePicker, constraints);

        timePicker = WINDOW.makeTimePicker(50,30);
        WINDOW.modifyConstraints(constraints, 2, 6, GridBagConstraints.WEST, 2);
        add(timePicker, constraints);

        // Register new operation
        registerNewButton = WINDOW.makeButton(Messages.REGISTER_NEW_BUTTON.get(), 200, 50);
        registerNewButton.setName("registerNewButton");
        WINDOW.modifyConstraints(constraints, 2, 7, GridBagConstraints.CENTER, 2);
        add(registerNewButton, constraints);
    }

    /**
     * set the initial visibility for StartPanel
     *
     */
    private void setInitialVisibility() {
        setVisibilityOperationButtons(true);
        setVisibilityExistingOperation(false);
        setVisibilityNewOperation(false);
    }

    /**
     * Adding the existing operations to selector.
     *
     * @param operations the operations to add.
     */
    public void showExistingOperations(java.util.List<Operation> operations) {
        existingOperationInput.removeAll(); // Avoid duplicates.
        for (Operation operation : operations) {
            existingOperationInput.addItem(operation.getName());
        }
    }

    /**
     * Set the visibility of the main operation selector button.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityOperationButtons(boolean visibility) {
        newOperationButton.setVisible(visibility);
        existingOperationButton.setVisible(visibility);
    }

    /**
     * Set the visibility of the items required to make a new operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityNewOperation(boolean visibility) {
        operationNameInput.setVisible(visibility);
        operationDateLabel.setVisible(visibility);
        operationNameLabel.setVisible(visibility);
        datePicker.setVisible(visibility);
        timePicker.setVisible(visibility);
        registerNewButton.setVisible(visibility);
        errorMessageLabel.setText(" ");
        errorMessageLabel.setVisible(visibility);
    }

    /**
     * Set the visibility of the items required to find an existing operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityExistingOperation(boolean visibility) {
        existingOperationLabel.setVisible(visibility);
        existingOperationInput.setVisible(visibility);
        registerExistingButton.setVisible(visibility);
    }

    /**
     * Setup the listener for the new operation button.
     */
    private void newOperationButtonListener() {
        newOperationButton.addActionListener(actionEvent -> {
            setVisibilityOperationButtons(false);
            setVisibilityNewOperation(true);
        });
    }

    /**
     * Setup the listener for the existing operation button.
     */
    private void existingOperationButtonListener() {
        existingOperationButton.addActionListener(actionEvent -> {
            setVisibilityOperationButtons(false);
            setVisibilityExistingOperation(true);
        });
    }

    /**
     * Setup the listener for the button to register a new operation.
     */
    private void registerNewOperationButtonListener() {
        registerNewButton.addActionListener(actionEvent -> {
            int day = datePicker.getDate().getDayOfMonth();
            int month = datePicker.getDate().getMonthValue();
            int year = datePicker.getDate().getYear();
            int hour = timePicker.getTime().getHour();
            int minute = timePicker.getTime().getMinute();
            String operationName = operationNameInput.getText();

            if (OPERATION_MANAGER.operationNameAlreadyExists(operationName)) {
                errorMessageLabel.setText(Messages.OPERATION_NAME_ALREADY_EXISTS.get());
                return;
            }
            if (!StringTools.isValidOperationName(operationName)) {
                errorMessageLabel.setText(Messages.INVALID_OPERATION_NAME.get());
                return;
            }
            if (!StringTools.operationNameLengthIsValid(operationName)) {
                errorMessageLabel.setText(Messages.OPERATION_NAME_IS_TOO_LONG_OR_SHORT.get());
                return;
            }
            Operation operation = new Operation(operationName, day, month, year, hour, minute);
            OPERATION_MANAGER.setupOperation(operation);
            OPERATION_MANAGER.reloadExistingOperations();
            setInitialVisibility();
            errorMessageLabel.setVisible(false);
            WINDOW.openStandByPanel();
        });
    }

    /**
     * Setup the listener for the button to load an existing operation.
     */
    private void registerExistingOperationButtonListener() {
        registerExistingButton.addActionListener(actionEvent -> {
            String selectedOperationName = (String) existingOperationInput.getSelectedItem();
            for (Operation operation : OPERATION_MANAGER.getExistingOperations()) {
                if (operation.getName().endsWith(selectedOperationName)) {
                    OPERATION_MANAGER.setupOperation(operation);
                }
            }
            setInitialVisibility();
            WINDOW.openStandByPanel();
        });
    }
}
