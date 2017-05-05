package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has an interface for configuring details related to one track.
 */
public class TrackPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    // Info about the current file and progress, if multiple files.
    private JLabel currentImportLabel;
    private JLabel remainingFilesLabel;
    // Check boxes for selecting type of team.
    private List<JRadioButton> radioButtons;
    private ButtonGroup radioButtonGroup;
    // Starts the processing of the file.
    private JButton registerButton;
    // Getting the number of the team.
    private JLabel crewNumberLabel;
    private JSpinner crewNumberSpinner;
    // Getting the amount of people on the team.
    private JLabel crewCountLabel;
    private JSpinner crewCountSpinner;
    // Getting the number of the track, if multiple tracks on a team etc.
    private JLabel trackNumberLabel;
    private JSpinner trackNumberSpinner;
    // Getting info about the area searched.
    private JLabel areaLabel;
    private JButton areaInputButton;
    private JLabel areaSearchedLabel;
    // Getting extra comments about the track.
    private JLabel trackCommentLabel;
    private JTextField trackCommentInput;
    // Controlling navigation flow.
    private JButton nextButton;
    private JButton backButton;
    private int viewCount;

    private List<JComponent> allInputComponents;

    private int numberOfAreas;
    private List<JCheckBox> areaCheckBoxes;
    private List<String> areaSearchedString;

    public TrackPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;
        numberOfAreas = 15;
        viewCount = 0;
        areaSearchedString = new ArrayList<>();
        allInputComponents = new ArrayList<>();

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        createButtonsAndInputFields();
        nextAndBackButtons();

        // adding buttons
        radioButtonGroup = new ButtonGroup();
        constraints.gridwidth = 1;
        radioButtons = generateButtons(getCrewNames());
        setButtonsInWindow();

        initialVisibility();

        registerButtonListener();
        areaInputButtonListener();
        nextButtonListener();
        backButtonListener();

        setBackground(new Color(255, 245, 252));
    }


    public int getNumberOfAreas() {
        return numberOfAreas;
    }

    public void setNumberOfAreas(int numberOfAreas) {
        this.numberOfAreas = numberOfAreas;
    }

    /**
     * Adds the buttons and input fields and sets them in the gridbaglayout
     */

    private void createButtonsAndInputFields() {
        // Current file imported from GPS
        String currentImportedFile = Messages.IMPORTED_FROM_GPS.get() + "Ingen fil.";
        currentImportLabel = WINDOW.makeLabel(currentImportedFile, WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 4);
        add(currentImportLabel, constraints);

        // Label and input for team number
        crewNumberLabel = WINDOW.makeLabel(Messages.CREW_NUMBER.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 1);
        add(crewNumberLabel, constraints);
        allInputComponents.add(crewNumberLabel);

        // Spinner for crew number input
        SpinnerModel crewNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        crewNumberSpinner = new JSpinner(crewNumberInput);
        WINDOW.modifyConstraints(constraints, 1, 4, GridBagConstraints.WEST, 1);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewNumberSpinner, constraints);
        allInputComponents.add(crewNumberSpinner);

        // Label and input for crew count
        crewCountLabel = WINDOW.makeLabel(Messages.CREW_COUNT.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 1, 5, GridBagConstraints.WEST, 1);
        add(crewCountLabel, constraints);
        allInputComponents.add(crewCountLabel);

        // Spinner for crew count input
        SpinnerModel crewCountInput = new SpinnerNumberModel(0, 0, 15, 1);
        crewCountSpinner = new JSpinner(crewCountInput);
        WINDOW.modifyConstraints(constraints, 1, 6, GridBagConstraints.WEST, 1);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewCountSpinner, constraints);
        allInputComponents.add(crewCountSpinner);

        // Label and input for area searched
        areaLabel = WINDOW.makeLabel(Messages.AREA_SEARCHED.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 3, 3, GridBagConstraints.WEST, 1);
        add(areaLabel, constraints);
        allInputComponents.add(areaLabel);

        // button for the area searched dialog
        areaInputButton = new JButton(Messages.CHOOSE_AREA.get());
        WINDOW.modifyConstraints(constraints, 3, 4, GridBagConstraints.WEST, 1);
        add(areaInputButton, constraints);
        allInputComponents.add(areaInputButton);

        // Label for showing areas chosen
        areaSearchedLabel = new JLabel();
        WINDOW.modifyConstraints(constraints, 3, 4, GridBagConstraints.WEST, 1);
        add(areaSearchedLabel, constraints);
        areaSearchedLabel.setVisible(false);

        // Label and input for track number
        trackNumberLabel = WINDOW.makeLabel(Messages.TRACK_NUMBER.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 3, 5, GridBagConstraints.WEST, 1);
        add(trackNumberLabel, constraints);
        allInputComponents.add(trackNumberLabel);

        // Spinner input for the track number
        SpinnerModel trackNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        trackNumberSpinner = new JSpinner(trackNumberInput);
        WINDOW.modifyConstraints(constraints, 3, 6, GridBagConstraints.WEST, 1);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(trackNumberSpinner, constraints);
        allInputComponents.add(trackNumberSpinner);

        // Label for comment to the track
        trackCommentLabel = WINDOW.makeLabel(Messages.TRACK_COMMENT.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 1, 7, GridBagConstraints.WEST, 3);
        add(trackCommentLabel, constraints);
        allInputComponents.add(trackCommentLabel);

        // TextField for adding a comment
        trackCommentInput = new JTextField();
        WINDOW.modifyConstraints(constraints, 1, 8, GridBagConstraints.WEST, 3);
        add(trackCommentInput, constraints);
        allInputComponents.add(trackCommentInput);

        // Register button
        registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 0, 9, GridBagConstraints.WEST, 4);
        add(registerButton, constraints);

        // Remaining files imported from GPS waiting to be processed
        String remainingFiles = Messages.IMPORTED_FILES_LEFT_TO_PROCESS.get("" + 0);
        remainingFilesLabel = WINDOW.makeLabel(remainingFiles, WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 10, GridBagConstraints.WEST, 4);
        add(remainingFilesLabel, constraints);
    }

    private void nextAndBackButtons() {
        nextButton = new JButton(Messages.NEXT.get());
        WINDOW.modifyConstraints(constraints, 3, 11, GridBagConstraints.CENTER, 1);
        add(nextButton, constraints);

        backButton = new JButton(Messages.BACK.get());
        WINDOW.modifyConstraints(constraints, 0, 11, GridBagConstraints.CENTER, 1);
        add(backButton, constraints);
    }

    /**
     * Creates a dialog with options for choosing area
     */
    private Object[] areaDialogBox() {
        areaCheckBoxes = new ArrayList<>();
        for (int i = 1; i <= numberOfAreas; i++ ) {
            JCheckBox checkBox = new JCheckBox(Integer.toString(i));
            areaCheckBoxes.add(checkBox);
        }
        return areaCheckBoxes.toArray(new Object[areaCheckBoxes.size()]);
    }

    /**
     * Updating info about the file currently being processed.
     *
     * @param filename  the new filename.
     * @param filesLeft amount of files left after the current file.
     */
    public void updateCurrentFile(String filename, int filesLeft) {
        String currentImportedFile = Messages.IMPORTED_FROM_GPS.get() + filename;
        String remainingFiles = Messages.IMPORTED_FILES_LEFT_TO_PROCESS.get("" + filesLeft);
        currentImportLabel.setText(currentImportedFile);
        remainingFilesLabel.setText(remainingFiles);
    }

    /**
     * Places the radio buttons in the given coordinates in the panel
     */
    private void setButtonsInWindow() {
        int y = 3;
        int x = 0;
        for (JRadioButton radioButton : radioButtons) {
            WINDOW.modifyConstraints(constraints, x, y, GridBagConstraints.WEST, 1);
            add(radioButton, constraints);
            allInputComponents.add(radioButton);
            y++;
        }
    }

    /**
     * Generates the radio buttons
     *
     * @param crewNames List of strings with the names used on the radio buttons
     * @return a List with the radio buttons
     */
    private List<JRadioButton> generateButtons(List<String> crewNames) {
        List<JRadioButton> radioButtons = new ArrayList<>();
        for (String crewName : crewNames) {
            JRadioButton radioButton = new JRadioButton(crewName);
            radioButtons.add(radioButton);
            radioButtonGroup.add(radioButton);
        }
        return radioButtons;
    }

    /**
     * Method for creating names that will be put in the radio buttons
     *
     * @return a List with the names
     */
    private List<String> getCrewNames() {
        return OPERATION_MANAGER.getConfig().getTeamNames();
    }

    /**
     * Finds the selected radio button and returns it
     *
     * @return a String with the selected radio button
     */
    private String getSelectedRadioButton() {
        String chosenRadioButton = "";
        for (JRadioButton radioButton : radioButtons) {
            if (radioButton.isSelected()) {
                chosenRadioButton = radioButton.getText();
            }
        }
        return chosenRadioButton;
    }

    /**
     * Initial setup for visibility for components
     */
    private void initialVisibility() {
        for( JComponent c : allInputComponents) {
            c.setVisible(false);
        }
        backButton.setVisible(false);
        registerButton.setVisible(false);
    }

    /**
     * Sets all JComponents visible/invisible other than the give components.
     * @param visibility true if visible, false if not.
     */
    private void setVisibilityComponents(boolean visibility) {
        for (JComponent c : allInputComponents) {
                c.setVisible(visibility);
        }
        registerButton.setVisible(visibility);
    }

    /**
     * Listener for the registerbutton
     */
    private void registerButtonListener() {
        registerButton.addActionListener(actionEvent -> {
            // Fetching the input data and sending it to the OperationManager
            String crew = getSelectedRadioButton();
            int crewCount = Integer.parseInt(crewCountSpinner.getModel().getValue().toString());
            int crewNumber = Integer.parseInt(crewNumberSpinner.getModel().getValue().toString());
            String areaSearched = areaSearchedString.toString();
            int trackNumber = Integer.parseInt(trackNumberSpinner.getModel().getValue().toString());
            String trackComment = trackCommentInput.getText();
            if (!trackComment.isEmpty()) {
                // TODO If comment: Add the comment to the track <desc> and add COMMENT to file name
            }
            TrackInfo trackInfo = new TrackInfo(crew, crewCount, crewNumber, areaSearched, trackNumber);
            OPERATION_MANAGER.initiateTrackCutter(trackInfo);

            // Resetting all input fields
            crewCountSpinner.setValue(0);
            crewNumberSpinner.setValue(0);
            areaInputButton.setVisible(true);
            areaSearchedLabel.setVisible(false);
            areaSearchedString.clear();
            trackNumberSpinner.setValue(0);
            trackCommentInput.setText("");

            // Message to user
            String dialogText = Messages.SAVE_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
        });
    }

    /**
     * Button for displaying the next "page".
     */
    private void nextButtonListener() {
        nextButton.addActionListener(actionEvent -> {
            viewCount++;
            switch (viewCount) {
                case 0:
                    setVisibilityComponents(false);
                    backButton.setVisible(false);
                    break;
                case 1:
                    setVisibilityComponents(false);
                    for(JRadioButton rb : radioButtons) {
                        rb.setVisible(true);
                    }
                    break;
                case 2:
                    setVisibilityComponents(false);
                    trackNumberLabel.setVisible(true);
                    trackNumberSpinner.setVisible(true);
                    backButton.setVisible(true);
                    break;
                case 3:
                    setVisibilityComponents(false);
                    crewNumberLabel.setVisible(true);
                    crewNumberSpinner.setVisible(true);
                    break;
                case 4:
                    setVisibilityComponents(false);
                    crewCountLabel.setVisible(true);
                    crewCountSpinner.setVisible(true);
                    break;
                case 5:
                    setVisibilityComponents(false);
                    areaLabel.setVisible(true);
                    areaInputButton.setVisible(true);
                    break;
                case 6:
                    setVisibilityComponents(false);
                    trackCommentLabel.setVisible(true);
                    trackCommentInput.setVisible(true);
                    break;
                case 7:
                    setVisibilityComponents(false);
                    nextButton.setVisible(false);
                    registerButton.setVisible(true);
                default:
                    break;
            }
        });
    }

    /**
     * Button for displaying the previous "page".
     */
    private void backButtonListener() {
        backButton.addActionListener(actionEvent -> {
            viewCount--;
            switch (viewCount) {
                case 1:
                    setVisibilityComponents(false);
                    for(JRadioButton rb : radioButtons) {
                        rb.setVisible(true);
                    }
                    backButton.setVisible(false);
                    break;
                case 2:
                    setVisibilityComponents(false);
                    trackNumberLabel.setVisible(true);
                    trackNumberSpinner.setVisible(true);
                    break;
                case 3:
                    setVisibilityComponents(false);
                    crewNumberLabel.setVisible(true);
                    crewNumberSpinner.setVisible(true);
                    break;
                case 4:
                    setVisibilityComponents(false);
                    crewCountLabel.setVisible(true);
                    crewCountSpinner.setVisible(true);
                    break;
                case 5:
                    setVisibilityComponents(false);
                    areaLabel.setVisible(true);
                    areaInputButton.setVisible(true);
                    nextButton.setVisible(true);
                    break;
                case 6:
                    setVisibilityComponents(false);
                    trackCommentLabel.setVisible(true);
                    trackCommentInput.setVisible(true);
                    nextButton.setVisible(true);
                    break;
                default:
                    break;
            }

        });
    }

    /**
     * Showing dialog to get info about the area searched.
     */
    private void areaInputButtonListener() {
        areaInputButton.addActionListener(actionEvent -> {
            int dialog = JOptionPane.showConfirmDialog(this, areaDialogBox(), Messages.CHOOSE_AREA_DIALOG.get() ,JOptionPane.OK_CANCEL_OPTION);
            if (dialog == JOptionPane.OK_OPTION) {
                for (JCheckBox cb : areaCheckBoxes) {
                    if (cb.isSelected()) {
                        areaSearchedString.add(cb.getText());
                    }
                }
                areaSearchedLabel.setText(areaSearchedString.toString());
                areaSearchedLabel.setVisible(true);
                areaInputButton.setVisible(false);
            }
        });
    }
}
