package no.hvl.dowhile.core.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class has an interface for editing the current operation or choose another operation.
 */
public class OperationPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel allSavedPathsHeaderLabel;
    private JLabel allSavedPathsLabel;

    private JButton backButton;

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
        activeOperationGUI();
        editActiveOperationGUI();

        editDatePicker.setLocale(new Locale("no"));
        editTimePicker.setLocale(new Locale("no"));

        // Setting stuff invisible
        setVisibilityToggleEditInfo(true);
        setVisibilityEditInfo(false);

        // Starting button listeners
        toggleEditInfoButtonListener();
        saveOperationButtonListener();
        switchOperationListener();
        backButtonListener();
        definePathButtonListener();

        setBackground(new Color(255, 245, 252));
    }

    /**
     * Creates the universal buttons in the GUI
     */

    private void universalButtonsGUI() {
        // Back button
        backButton = WINDOW.makeButton(Messages.GO_BACK.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 3, 6, GridBagConstraints.SOUTHEAST, 1);
        add(backButton, constraints);
        backButton.setName("backButton");
    }

    /**
     * Creates the GUI for choices on an active operation
     */
    private void activeOperationGUI() {
        // Edit info toggle button
        toggleEditInfoButton = WINDOW.makeButton(Messages.EDIT_INFO_SHOW_BUTTON.get(), 300, 50);
        toggleEditInfoButton.setName("toggleEditInfoButton");
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.CENTER, 2);
        add(toggleEditInfoButton, constraints);

        // Switch operation
        switchOperationButton = WINDOW.makeButton(Messages.CHOOSE_OTHER_OPERATION.get(), 300, 50);
        switchOperationButton.setName("switchOperationButton");
        WINDOW.modifyConstraints(constraints, 2, 2, GridBagConstraints.CENTER, 2);
        add(switchOperationButton, constraints);

        // Button for choosing path(s) to save operation to
        definePathButton = WINDOW.makeButton(Messages.DEFINE_OPERATION_PATH.get(), 300, 50);
        definePathButton.setName("definePathButton");
        WINDOW.modifyConstraints(constraints, 3, 4, GridBagConstraints.CENTER, 1);
        add(definePathButton, constraints);

        // Saved paths header
        allSavedPathsHeaderLabel = WINDOW.makeLabel(Messages.ALL_SAVED_PATHS.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.SOUTHWEST, 2);
        add(allSavedPathsHeaderLabel, constraints);

        // List of saved paths
        allSavedPathsLabel = WINDOW.makeLabel("", Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.WEST, 4);
        add(allSavedPathsLabel, constraints);
    }

    /**
     * Creates the GUI for editing an active operation
     */
    private void editActiveOperationGUI() {
        // Edit operation date label
        editDateLabel = WINDOW.makeLabel(Messages.EDIT_OPERATION_TIME.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.CENTER, 4);
        add(editDateLabel, constraints);

        // Edit date of operation
        editDatePicker = WINDOW.makeDatePicker(300, 50);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.CENTER, 4);
        add(editDatePicker, constraints);

        // Edit time of operation
        editTimePicker = WINDOW.makeTimePicker(300, 50);
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 4);
        add(editTimePicker, constraints);

        // Save edited operation button
        saveOperationButton = WINDOW.makeButton(Messages.EDIT_OPERATION_BUTTON.get(), 300, 50);
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 4);
        add(saveOperationButton, constraints);
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
     * Set the visibility of the items required to edit data about an operation.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityEditInfo(boolean visibility) {
        editDateLabel.setVisible(visibility);
        editDatePicker.setVisible(visibility);
        editTimePicker.setVisible(visibility);
        saveOperationButton.setVisible(visibility);
        if (visibility) {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            switchOperationButton.setVisible(false);
        } else {
            toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            switchOperationButton.setVisible(true);
        }
    }

    /**
     * Set the visibility of the edit info toggle button.
     *
     * @param visibility the new visibility.
     */
    private void setVisibilityToggleEditInfo(boolean visibility) {
        switchOperationButton.setVisible(visibility);
        definePathButton.setVisible(visibility);
        allSavedPathsHeaderLabel.setVisible(visibility);
        allSavedPathsLabel.setVisible(visibility);
        backButton.setVisible(visibility);
    }

    /**
     * Setup the listener for the edit info toggle button.
     */
    private void toggleEditInfoButtonListener() {
        toggleEditInfoButton.addActionListener(actionEvent -> {
            if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_SHOW_BUTTON.get())) {
                setVisibilityEditInfo(true);
                setVisibilityToggleEditInfo(false);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_HIDE_BUTTON.get());
            } else if (toggleEditInfoButton.getText().equals(Messages.EDIT_INFO_HIDE_BUTTON.get())) {
                setVisibilityEditInfo(false);
                setVisibilityToggleEditInfo(true);
                toggleEditInfoButton.setText(Messages.EDIT_INFO_SHOW_BUTTON.get());
            }
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
            setVisibilityToggleEditInfo(true);
        });
    }

    /**
     * Setup the listener for the button to switch operation
     */
    private void switchOperationListener() {
        switchOperationButton.addActionListener(actionEvent -> {
            WINDOW.openStartPanel();
        });
    }

    /**
     * Setup the listener for the button to go back in both new operation and existing operation
     */
    private void backButtonListener() {
        backButton.addActionListener(actionEvent -> {
            WINDOW.openStandByPanel();
        });
    }

    public void setAllSavedPathsLabel(String paths) {
        allSavedPathsLabel.setText(paths);
    }
}
