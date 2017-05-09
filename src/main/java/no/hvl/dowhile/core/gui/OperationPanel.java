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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationNameLabel;
    private JTextField operationNameInput;
    private JLabel operationDateLabel;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private JLabel awaitingGPSLabel;
    private JLabel errorMessageLabel;
    private JLabel allSavedPathsHeaderLabel;
    private JLabel allSavedPathsLabel;

    private JLabel existingOperationLabel;
    private JComboBox<String> existingOperationInput;
    private JButton registerNewButton;
    private JButton registerExistingButton;

    private JButton backButton;

    private JButton newOperationButton;
    private JButton existingOperationButton;

    private JButton importFileButton;
    private JButton switchOperationButton;
    private JButton toggleEditInfoButton;
    private JLabel editDateLabel;
    private DatePicker editDatePicker;
    private TimePicker editTimePicker;
    private JButton saveOperationButton;
    private JButton definePathButton;

    private GridBagConstraints constraints;

    public OperationPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        // creating buttons, labels, textfields
        universalButtonsGUI();
        chooseOperationGUI();
        existingOperationGUI();
        createNewOperationGUI();
        activeOperationGUI();
        editActiveOperationGUI();

        datePicker.setLocale(new Locale("no"));
        timePicker.setLocale(new Locale("no"));
        editDatePicker.setLocale(new Locale("no"));
        editTimePicker.setLocale(new Locale("no"));

        // Setting stuff invisible
        backButton.setVisible(false);
        errorMessageLabel.setVisible(false);
        allSavedPathsHeaderLabel.setVisible(false);
        allSavedPathsLabel.setVisible(false);
        setVisibilityNewOperation(false);
        setVisibilityExistingOperation(false);
        setVisibilityEditInfo(false);
        setVisibilityToggleEditInfo(false);

        // Starting button listeners
        newOperationButtonListener();
        existingOperationButtonListener();
        registerExistingOperationButtonListener();
        registerNewOperationButtonListener();
        toggleEditInfoButtonListener();
        saveOperationButtonListener();
        switchOperationListener();
        backButtonListener();
        importFileButtonListener();
        definePathButtonListener();

        setBackground(new Color(255, 245, 252));
    }

    /**
     * Creates the universal buttons in the GUI
     */

    private void universalButtonsGUI() {
        // Back button
        backButton = new JButton(Messages.GO_BACK.get());
        WINDOW.modifyConstraints(constraints, 0, 7, GridBagConstraints.WEST, 1);
        add(backButton, constraints);
        backButton.setName("backButton");
    }

    /**
     * Creates the start buttons for creating an new operation or choosing an existing operation in the GUI
     */
    private void chooseOperationGUI() {
        // New operation button
        newOperationButton = new JButton(Messages.NEW_OPERATION_BUTTON.get());
        newOperationButton.setName("newOperationButton");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.CENTER, 2);
        newOperationButton.setPreferredSize(new Dimension(200, 50));
        newOperationButton.setBackground(new Color(242, 94, 94));
        add(newOperationButton, constraints);

        // Existing operation button
        existingOperationButton = new JButton(Messages.EXISTING_OPERATION_BUTTON.get());
        existingOperationButton.setName("existingOperationButton");
        WINDOW.modifyConstraints(constraints, 2, 0, GridBagConstraints.CENTER, 2);
        existingOperationButton.setPreferredSize(new Dimension(200, 50));
        existingOperationButton.setBackground(new Color(242, 94, 94));
        add(existingOperationButton, constraints);
    }

    /**
     * Creates the GUI for choosing an existing operation
     */
    private void existingOperationGUI() {
        // Already existing operation label
        existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), Font.BOLD);
        existingOperationLabel.setName("existingOperationLabel");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 3);
        add(existingOperationLabel, constraints);

        // Already existing operation input
        existingOperationInput = new JComboBox<>();
        existingOperationInput.setName("existingOperationInput");
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(existingOperationInput, constraints);

        // Register existing operation
        registerExistingButton = new JButton(Messages.REGISTER_EXISTING_BUTTON.get());
        registerExistingButton.setName("registerExistingButton");
        WINDOW.modifyConstraints(constraints, 3, 1, GridBagConstraints.CENTER, 1);
        add(registerExistingButton, constraints);
    }

    /**
     * Creates the GUI for creating an new operation
     */
    private void createNewOperationGUI() {
        // New operation label
        operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), Font.BOLD);
        operationNameLabel.setName("operationNameLabel");
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 2);
        add(operationNameLabel, constraints);

        // New operation name input
        operationNameInput = new JTextField();
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
        operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), Font.BOLD);
        operationDateLabel.setName("operationDateLabel");
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 2);
        add(operationDateLabel, constraints);

        datePicker = new DatePicker(createDateSettings());
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 2);
        add(datePicker, constraints);

        timePicker = new TimePicker(createTimeSettings());
        WINDOW.modifyConstraints(constraints, 2, 6, GridBagConstraints.WEST, 2);
        add(timePicker, constraints);

        // Register new operation
        registerNewButton = new JButton(Messages.REGISTER_NEW_BUTTON.get());
        registerNewButton.setName("registerNewButton");
        WINDOW.modifyConstraints(constraints, 2, 8, GridBagConstraints.CENTER, 2);
        add(registerNewButton, constraints);

        // List of saved paths
        allSavedPathsLabel = WINDOW.makeLabel("", Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 9, GridBagConstraints.CENTER, 4);
        add(allSavedPathsLabel, constraints);
    }

    /**
     * Creates the GUI for choices on an active operation
     */
    private void activeOperationGUI() {
        // Awaiting GPS label
        awaitingGPSLabel = WINDOW.makeLabel(Messages.AWAITING_GPS.get(), Font.BOLD);
        awaitingGPSLabel.setName("awaitingGPSLabel");
        WINDOW.modifyConstraints(constraints, 1, 0, GridBagConstraints.CENTER, 2);
        add(awaitingGPSLabel, constraints);
        awaitingGPSLabel.setVisible(false);

        // Import local GPX-file button
        importFileButton = new JButton(Messages.IMPORT_LOCAL_FILE.get());
        importFileButton.setName("importFileButton");
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.CENTER, 4);
        add(importFileButton, constraints);

        // Edit info toggle button
        toggleEditInfoButton = new JButton(Messages.EDIT_INFO_SHOW_BUTTON.get());
        toggleEditInfoButton.setName("toggleEditInfoButton");
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 2);
        add(toggleEditInfoButton, constraints);

        // Switch operation
        switchOperationButton = new JButton(Messages.CHOOSE_OTHER_OPERATION.get());
        switchOperationButton.setName("switchOperationButton");
        WINDOW.modifyConstraints(constraints, 2, 2, GridBagConstraints.CENTER, 2);
        add(switchOperationButton, constraints);

        // Button for choosing path(s) to save operation to
        definePathButton = new JButton(Messages.DEFINE_OPERATION_PATH.get());
        definePathButton.setName("definePathButton");
        WINDOW.modifyConstraints(constraints, 3, 4, GridBagConstraints.CENTER, 1);
        add(definePathButton, constraints);

        // Saved paths header
        allSavedPathsHeaderLabel = WINDOW.makeLabel(Messages.ALL_SAVED_PATHS.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 2);
        add(allSavedPathsHeaderLabel, constraints);
    }

    /**
     * Creates the GUI for editing an active operation
     */
    private void editActiveOperationGUI() {
        // Edit operation date label
        editDateLabel = WINDOW.makeLabel(Messages.EDIT_OPERATION_TIME.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 4);
        add(editDateLabel, constraints);

        // Edit date of operation
        editDatePicker = new DatePicker(createDateSettings());
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.CENTER, 4);
        add(editDatePicker, constraints);

        // Edit time of operation
        editTimePicker = new TimePicker(createTimeSettings());
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 4);
        add(editTimePicker, constraints);

        // Save edited operation button
        saveOperationButton = new JButton(Messages.EDIT_OPERATION_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 7, GridBagConstraints.CENTER, 4);
        add(saveOperationButton, constraints);
    }

    /**
     * Creating a settings object to use when creating a date picker.
     *
     * @return settings for a date picker.
     */
    private DatePickerSettings createDateSettings() {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        dateSettings.setAllowEmptyDates(false);
        return dateSettings;
    }

    /**
     * Creating a settings object to use when creating a time picker.
     *
     * @return settings for a time picker.
     */
    private TimePickerSettings createTimeSettings() {
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.initialTime = LocalTime.now();
        return timeSettings;
    }

    /**
     * Updating the label with info about the operation.
     *
     * @param operation the current operation.
     */
    public void updateOperationInfo(Operation operation) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(operation.getStartTime());
        editDatePicker.setDate(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        editTimePicker.setTime(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    /**
     * Adding the existing operations to selector.
     *
     * @param operations the operations to add.
     */
    public void showExistingOperations(List<Operation> operations) {
        existingOperationInput.removeAll(); // Avoid duplicates.
        for (Operation operation : operations) {
            existingOperationInput.addItem(operation.getName());
        }
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
        backButton.setVisible(visibility);
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
        backButton.setVisible(visibility);
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
     * Set the visibility of the items required to edit data about an operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityEditInfo(boolean visibility) {
        editDateLabel.setVisible(visibility);
        editDatePicker.setVisible(visibility);
        editTimePicker.setVisible(visibility);
        definePathButton.setVisible(visibility);
        saveOperationButton.setVisible(visibility);
        if (visibility) {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            switchOperationButton.setVisible(false);
            importFileButton.setVisible(false);
        } else {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            switchOperationButton.setVisible(true);
            importFileButton.setVisible(true);
        }
    }

    /**
     * Set the visibility of the edit info toggle button.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityToggleEditInfo(boolean visibility) {
        importFileButton.setVisible(visibility);
        toggleEditInfoButton.setVisible(visibility);
        switchOperationButton.setVisible(visibility);
        definePathButton.setVisible(visibility);
        allSavedPathsHeaderLabel.setVisible(visibility);
        allSavedPathsLabel.setVisible(visibility);
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
     * Setup the listener for the edit info toggle button.
     */
    private void toggleEditInfoButtonListener() {
        toggleEditInfoButton.addActionListener(actionEvent -> {
            if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_SHOW_BUTTON.get())) {
                setVisibilityEditInfo(true);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            } else if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_HIDE_BUTTON.get())) {
                setVisibilityEditInfo(false);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            }
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
            setVisibilityNewOperation(false);
            setVisibilityToggleEditInfo(true);
            errorMessageLabel.setVisible(false);
            awaitingGPSLabel.setVisible(true);
            allSavedPathsLabel.setText(OPERATION_MANAGER.getOperation().pathsToString());
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
                    awaitingGPSLabel.setVisible(true);
                }
            }
            setVisibilityExistingOperation(false);
            setVisibilityToggleEditInfo(true);
            allSavedPathsLabel.setText(OPERATION_MANAGER.getOperation().pathsToString());
        });
    }

    private void definePathButtonListener() {
        definePathButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showSaveDialog(JOptionPane.getRootFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                OPERATION_MANAGER.getOperation().addPath(fileChooser.getSelectedFile().getAbsolutePath());
                OPERATION_MANAGER.updateOperationFile();
                OPERATION_MANAGER.updateOperationFolders();
                allSavedPathsLabel.setText(OPERATION_MANAGER.getOperation().pathsToString());
            }
        });
    }

    /**
     * Setup the listener for the button to save an edited operation.
     */
    private void saveOperationButtonListener() {
        saveOperationButton.addActionListener(actionEvent -> {
            int year = editDatePicker.getDate().getYear();
            int month = editDatePicker.getDate().getMonthValue();
            int day = editDatePicker.getDate().getDayOfMonth();
            int hour = editTimePicker.getTime().getHour();
            int minute = editTimePicker.getTime().getMinute();
            OPERATION_MANAGER.updateCurrentOperation(year, month, day, hour, minute);
            setVisibilityEditInfo(false);
        });
    }

    /**
     * Setup the listener for the import file button
     */
    private void importFileButtonListener() {
        importFileButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("GPX Files", "gpx");
            fileChooser.setFileFilter(filter);
            int option = fileChooser.showOpenDialog(JOptionPane.getRootFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                OPERATION_MANAGER.handleImportedFile(fileChooser.getSelectedFile());
            }
        });
    }

    /**
     * Setup the listener for the button to switch operation
     */
    private void switchOperationListener() {
        switchOperationButton.addActionListener(actionEvent -> {
            setVisibilityToggleEditInfo(false);
            setVisibilityOperationButtons(true);
            awaitingGPSLabel.setVisible(false);
        });
    }

    /**
     * Setup the listener for the button to go back in both new operation and existing operation
     */
    private void backButtonListener() {
        backButton.addActionListener(actionEvent -> {
            setVisibilityExistingOperation(false);
            setVisibilityNewOperation(false);
            setVisibilityOperationButtons(true);
            errorMessageLabel.setVisible(false);
        });
    }
}
