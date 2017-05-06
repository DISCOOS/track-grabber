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

/**
 * This class has an interface for creating a new operation or choosing an existing operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationNameLabel;
    private JTextField operationNameInput;
    private JLabel areasLabel;
    private JSpinner areasInput;
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
    private JLabel editAreasLabel;
    private JSpinner editAreasInput;
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
        existingOperationLabel = WINDOW.makeLabel(Messages.EXISTING_OPERATION.get(), WINDOW.TEXT_FONT_SIZE);
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
        operationNameLabel = WINDOW.makeLabel(Messages.OPERATION_NAME.get(), WINDOW.TEXT_FONT_SIZE);
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
        errorMessageLabel = WINDOW.makeLabel(" ", WINDOW.TEXT_FONT_SIZE);
        errorMessageLabel.setName("errorMessageLabel");
        errorMessageLabel.setForeground(Color.RED);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 4);
        add(errorMessageLabel, constraints);

        // Number of areas label and input
        areasLabel = WINDOW.makeLabel(Messages.NUMBER_OF_AREAS.get(), WINDOW.TEXT_FONT_SIZE);
        areasLabel.setName("areasLabel");
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.CENTER, 2);
        add(areasLabel, constraints);

        // Spinner for crew count input
        SpinnerModel numberOfAreasSpinner = new SpinnerNumberModel(0, 0, 999, 1);
        areasInput = new JSpinner(numberOfAreasSpinner);
        areasInput.setName("areasInput");
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(areasInput, constraints);

        // Date for operation and input
        operationDateLabel = WINDOW.makeLabel(Messages.OPERATION_START_DATE.get(), WINDOW.TEXT_FONT_SIZE);
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
    }

    /**
     * Creates the GUI for choices on an active operation
     */
    private void activeOperationGUI() {
        // Awaiting GPS label
        awaitingGPSLabel = WINDOW.makeLabel(Messages.AWAITING_GPS.get(), WINDOW.TEXT_FONT_SIZE);
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
    }

    /**
     * Creates the GUI for editing an active operation
     */
    private void editActiveOperationGUI() {
        // Edit operation date label
        editDateLabel = WINDOW.makeLabel(Messages.EDIT_OPERATION_TIME.get(), WINDOW.TEXT_FONT_SIZE);
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


        // Number of areas label and input
        editAreasLabel = WINDOW.makeLabel(Messages.NUMBER_OF_AREAS.get(), WINDOW.TEXT_FONT_SIZE);
        editAreasLabel.setName("editAreasLabel");
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 2);
        add(editAreasLabel, constraints);

        // Spinner for crew count input
        SpinnerModel editAreasSpinner = new SpinnerNumberModel(0, 0, 999, 1);
        editAreasInput = new JSpinner(editAreasSpinner);
        editAreasInput.setName("editAreasInput");
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(editAreasInput, constraints);

        // Save edited operation button
        saveOperationButton = new JButton(Messages.EDIT_OPERATION_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 7, GridBagConstraints.CENTER, 4);
        add(saveOperationButton, constraints);

        // Button for choosing path(s) to save operation to
        definePathButton = new JButton(Messages.DEFINE_OPERATION_PATH.get());
        definePathButton.setName("definePathButton");
        WINDOW.modifyConstraints(constraints, 3, 8, GridBagConstraints.CENTER, 1);
        add(definePathButton, constraints);

        allSavedPathsHeaderLabel = WINDOW.makeLabel(Messages.ALL_SAVED_PATHS.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 8, GridBagConstraints.CENTER, 4);
        add(allSavedPathsHeaderLabel, constraints);

        allSavedPathsLabel = WINDOW.makeLabel("", WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 9, GridBagConstraints.CENTER, 4);
        add(allSavedPathsLabel, constraints);
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
        editAreasInput.setValue(operation.getNumberOfAreas());
        editDatePicker.setDate(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        editTimePicker.setTime(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
    }

    /**
     * Adding the existing operations to selector.
     *
     * @param operations the operations to add.
     */
    public void showExistingOperations(List<Operation> operations) {
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
        areasLabel.setVisible(visibility);
        areasInput.setVisible(visibility);
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
        editAreasLabel.setVisible(visibility);
        editAreasInput.setVisible(visibility);
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
                allSavedPathsLabel.setText(OPERATION_MANAGER.getOperation().pathsToString());
                allSavedPathsHeaderLabel.setVisible(true);
                allSavedPathsLabel.setVisible(true);
                definePathButton.setVisible(true);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            } else if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_HIDE_BUTTON.get())) {
                setVisibilityEditInfo(false);
                definePathButton.setVisible(false);
                allSavedPathsHeaderLabel.setVisible(false);
                allSavedPathsLabel.setVisible(false);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            }
        });
    }

    /**
     * Setup the listener for the button to register a new operation.
     */
    private void registerNewOperationButtonListener() {
        registerNewButton.addActionListener(actionEvent -> {
            int numberOfAreas = Integer.parseInt(areasInput.getModel().getValue().toString());
            int day = datePicker.getDate().getDayOfMonth();
            int month = datePicker.getDate().getMonthValue();
            int year = datePicker.getDate().getYear();
            int hour = timePicker.getTime().getHour();
            int minute = timePicker.getTime().getMinute();
            String operationName = operationNameInput.getText();

            if (StringTools.isValidOperationName(operationName)) {
                if (OPERATION_MANAGER.operationNameAlreadyExists(operationName)) {
                    errorMessageLabel.setText(Messages.OPERATION_NAME_ALREADY_EXISTS.get());
                } else {
                    Operation operation = new Operation(operationName, numberOfAreas, day, month, year, hour, minute);
                    OPERATION_MANAGER.setupOperation(operation);
                    OPERATION_MANAGER.reloadExistingOperations();
                    setVisibilityNewOperation(false);
                    setVisibilityToggleEditInfo(true);
                    errorMessageLabel.setVisible(false);
                    awaitingGPSLabel.setVisible(true);
                }
            } else {
                errorMessageLabel.setText(Messages.INVALID_OPERATION_NAME.get());
            }
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
        });
    }

    private void definePathButtonListener() {
        definePathButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //fileChooser.showSaveDialog(null);
            int option = fileChooser.showSaveDialog(JOptionPane.getRootFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                OPERATION_MANAGER.getOperation().addPath(fileChooser.getSelectedFile().getAbsolutePath());
                OPERATION_MANAGER.updateOperationFile();
            }
        });
    }

    /**
     * Setup the listener for the button to save an edited operation.
     */
    private void saveOperationButtonListener() {
        saveOperationButton.addActionListener(actionEvent -> {
            int numberOfAreas = Integer.parseInt(editAreasInput.getModel().getValue().toString());
            int year = editDatePicker.getDate().getYear();
            int month = editDatePicker.getDate().getMonthValue();
            int day = editDatePicker.getDate().getDayOfMonth();
            int hour = editTimePicker.getTime().getHour();
            int minute = editTimePicker.getTime().getMinute();
            OPERATION_MANAGER.updateCurrentOperation(numberOfAreas, year, month, day, hour, minute);
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
