package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.Messages;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has an interface for configuring details related to one track.
 */
class TrackPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    // Info about the current file and progress, if multiple files.
    private JLabel trackHeaderLabel;
    private JLabel currentImportLabel;
    private JLabel remainingFilesLabel;
    private JLabel trackLengthLabel;
    private double trackDistance;

    // Info for user at start of processing file
    private JLabel startInfoLabel;

    // Check boxes for selecting type of team and a label.
    private JLabel crewTypeLabel;
    private List<JRadioButton> radioButtons;
    private ButtonGroup radioButtonGroup;

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
    private JLabel areaInfoLabel;
    private JButton areaInputButton;
    private JSpinner areaSearchedSpinner;
    private JLabel areaSearchedLabel;

    // Getting extra comments about the track.
    private JLabel trackCommentLabel;
    private JTextArea trackCommentInput;

    // Summary labels before registering
    private JLabel crewTypeSummaryLabel;
    private JLabel crewNumberSummaryLabel;
    private JLabel crewCountSummaryLabel;
    private JLabel trackNumberSummaryLabel;
    private JLabel areaSearchedSummaryLabel;
    private JLabel trackCommentSummaryLabel;

    // Data for summary befor registering
    private JLabel crewTypeSummaryData;
    private JLabel crewNumberSummaryData;
    private JLabel crewCountSummaryData;
    private JLabel trackNumberSummaryData;
    private JLabel areaSearchedSummaryData;
    private JLabel trackCommentSummaryData;

    // Controlling navigation flow.
    private JButton nextButton;
    private JButton backButton;
    private JButton registerButton;
    private JButton skipButton;
    private int viewCount;

    private List<JComponent> allInputComponents; // All JComponents connected to input from user is added to this List
    private List<String> areaSearchedStrings; // List with all areas searched

    /**
     * Constructor setting the OPERATION_MANAGER and WINDOW.
     * Also sets the layout and the constraints and the background color
     *
     * @param OPERATION_MANAGER the current instance of the OperationManager
     * @param WINDOW            the current instance of the Window
     */
    TrackPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;
        viewCount = 0;
        trackDistance = 0.0;
        areaSearchedStrings = new ArrayList<>();
        allInputComponents = new ArrayList<>();

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        infoGUI();
        crewTypeButtonsGUI();
        crewNumberGUI();
        crewCountGUI();
        areaSearchedGUI();
        trackNumberGUI();
        trackCommentGUI();
        buttonsGUI();
        summaryGUI();

        initialVisibility();
        radioButtons.get(0).setSelected(true);

        registerButtonListener();
        areaInputButtonListener();
        nextButtonListener();
        backButtonListener();

        setBackground(new Color(255, 245, 252));
    }

    /**
     * Adds basic info about the track file and sets them in the GridBagLayout
     */
    private void infoGUI() {
        // Header label for track processing
        trackHeaderLabel = WINDOW.makeHeaderLabel(Messages.TRACK_HEADER.get());
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 4);
        add(trackHeaderLabel, constraints);

        // Start info before importing file
        startInfoLabel = WINDOW.makeLabel(Messages.TRACK_START_INFO.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.CENTER, 4);
        add(startInfoLabel, constraints);
        allInputComponents.add(startInfoLabel);

        // Current file imported from GPS
        String currentImportedFile = "Ingen fil.";
        currentImportLabel = WINDOW.makeLabel(currentImportedFile, Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 4);
        add(currentImportLabel, constraints);

        trackLengthLabel = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.WEST, 4);
        add(trackLengthLabel, constraints);
        allInputComponents.add(trackLengthLabel);

        // Remaining files imported from GPS waiting to be processed
        remainingFilesLabel = WINDOW.makeLabel(Messages.PROCESSING_FILES.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 10, GridBagConstraints.WEST, 4);
        add(remainingFilesLabel, constraints);
    }

    /**
     * Adds navigation buttons and sets them in the GridBagLayout
     */
    private void buttonsGUI() {
        // Next button
        nextButton = WINDOW.makeButton(Messages.NEXT.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 3, 9, GridBagConstraints.EAST, 1);
        add(nextButton, constraints);

        // Back button
        backButton = WINDOW.makeButton(Messages.BACK.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 0, 9, GridBagConstraints.WEST, 2);
        add(backButton, constraints);

        // Skip button
        skipButton = WINDOW.makeButton(Messages.SKIP_BUTTON.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 0, 9, GridBagConstraints.WEST, 1);
        add(skipButton, constraints);

        // Register button
        registerButton = WINDOW.makeButton(Messages.REGISTER_BUTTON.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 3, 9, GridBagConstraints.WEST, 1);
        add(registerButton, constraints);
    }

    /**
     * Adds input for crew type and sets them in the GridBagLayout
     */
    private void crewTypeButtonsGUI() {
        // adding radio buttons for type of crew
        crewTypeLabel = WINDOW.makeLabel(Messages.CREW_TYPE_MESSAGE.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(crewTypeLabel, constraints);
        allInputComponents.add(crewTypeLabel);

        radioButtonGroup = new ButtonGroup();
        constraints.gridwidth = 2;
        radioButtons = generateButtons(OPERATION_MANAGER.getConfig().getTeamNames());
        setButtonsInWindow();
    }

    /**
     * Adds input for crew number and sets them in the GridBagLayout
     */
    private void crewNumberGUI() {
        // Label and input for crew number
        crewNumberLabel = WINDOW.makeLabel(Messages.CREW_NUMBER.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(crewNumberLabel, constraints);
        allInputComponents.add(crewNumberLabel);

        // Spinner for crew number input
        SpinnerModel crewNumberInput = new SpinnerNumberModel(21, 21, 999, 1);
        crewNumberSpinner = WINDOW.makeSpinner(crewNumberInput);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewNumberSpinner, constraints);
        allInputComponents.add(crewNumberSpinner);
    }

    /**
     * Adds input for how many there are in the crew and sets them in the GridBagLayout
     */
    private void crewCountGUI() {
        // Label and input for crew count
        crewCountLabel = WINDOW.makeLabel(Messages.CREW_COUNT.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(crewCountLabel, constraints);
        allInputComponents.add(crewCountLabel);

        // Spinner for crew count input
        SpinnerModel crewCountInput = new SpinnerNumberModel(1, 1, 15, 1);
        crewCountSpinner = WINDOW.makeSpinner(crewCountInput);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewCountSpinner, constraints);
        allInputComponents.add(crewCountSpinner);
    }

    /**
     * Adds input for which areas is searched and sets them in the GridBagLayout
     */
    private void areaSearchedGUI() {
        // Label for area searched
        areaLabel = WINDOW.makeLabel(Messages.AREA_SEARCHED.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(areaLabel, constraints);
        allInputComponents.add(areaLabel);

        // Label with info on area searched
        areaInfoLabel = WINDOW.makeLabel(Messages.AREA_SEARCHED_INFO.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.WEST, 4);
        add(areaInfoLabel, constraints);
        allInputComponents.add(areaInfoLabel);

        // Spinner for input of area
        SpinnerModel areaSearchedModel = new SpinnerNumberModel(1, 1, 1000, 1);
        areaSearchedSpinner = WINDOW.makeSpinner(areaSearchedModel);
        WINDOW.modifyConstraints(constraints, 1, 4, GridBagConstraints.WEST, 1);
        add(areaSearchedSpinner, constraints);
        allInputComponents.add(areaSearchedSpinner);

        // button for the area searched dialog
        areaInputButton = WINDOW.makeButton(Messages.CHOOSE_AREA.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 2, 4, GridBagConstraints.WEST, 1);
        add(areaInputButton, constraints);
        allInputComponents.add(areaInputButton);

        // Label for showing areas chosen
        areaSearchedLabel = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 1, 5, GridBagConstraints.WEST, 2);
        add(areaSearchedLabel, constraints);
        allInputComponents.add(areaSearchedLabel);
    }

    /**
     * Adds input for the track number and sets them in the GridBagLayout
     */
    private void trackNumberGUI() {
        // Label and input for track number
        trackNumberLabel = WINDOW.makeLabel(Messages.TRACK_NUMBER.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(trackNumberLabel, constraints);
        allInputComponents.add(trackNumberLabel);

        // Spinner input for the track number
        SpinnerModel trackNumberInput = new SpinnerNumberModel(1, 1, 15, 1);
        trackNumberSpinner = WINDOW.makeSpinner(trackNumberInput);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(trackNumberSpinner, constraints);
        allInputComponents.add(trackNumberSpinner);
    }

    /**
     * Adds input for a comment to the track and sets them in the GridBagLayout
     */
    private void trackCommentGUI() {
        // Label for comment to the track
        trackCommentLabel = WINDOW.makeLabel(Messages.TRACK_COMMENT.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        add(trackCommentLabel, constraints);
        allInputComponents.add(trackCommentLabel);

        // TextField for adding a comment
        trackCommentInput = new JTextArea();
        trackCommentInput.setFont(WINDOW.TEXT_FONT);
        trackCommentInput.setPreferredSize(new Dimension(300, 150));
        trackCommentInput.setBackground(new Color(228, 228, 230));
        trackCommentInput.setLineWrap(true);
        trackCommentInput.setWrapStyleWord(true);
        PromptSupport.setPrompt(Messages.TRACK_COMMENT_PLACEHOLDER.get(), trackCommentInput);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 2);
        add(trackCommentInput, constraints);
        allInputComponents.add(trackCommentInput);
    }

    /**
     * Adds JLabels for a summary of all the inputs and sets them in the GridBagLayout
     */
    private void summaryGUI() {
        // Type of crew
        crewTypeSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_CREW_TYPE.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 7, GridBagConstraints.WEST, 1);
        add(crewTypeSummaryLabel, constraints);

        crewTypeSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 7, GridBagConstraints.WEST, 1);
        add(crewTypeSummaryData, constraints);

        // CrewNumber
        crewNumberSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_CREW_NUMBER.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 6, GridBagConstraints.WEST, 1);
        add(crewNumberSummaryLabel, constraints);

        crewNumberSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 6, GridBagConstraints.WEST, 1);
        add(crewNumberSummaryData, constraints);

        // TrackNumber
        trackNumberSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_TRACK_NUMBER.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 5, GridBagConstraints.WEST, 1);
        add(trackNumberSummaryLabel, constraints);

        trackNumberSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 5, GridBagConstraints.WEST, 1);
        add(trackNumberSummaryData, constraints);

        // CrewCount
        crewCountSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_CREW_COUNT.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 4, GridBagConstraints.WEST, 1);
        add(crewCountSummaryLabel, constraints);

        crewCountSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 4, GridBagConstraints.WEST, 1);
        add(crewCountSummaryData, constraints);

        // AreaSearched
        areaSearchedSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_AREA_SEARCHED.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 3, GridBagConstraints.WEST, 1);
        add(areaSearchedSummaryLabel, constraints);

        areaSearchedSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 3, GridBagConstraints.WEST, 1);
        add(areaSearchedSummaryData, constraints);

        // TrackComment
        trackCommentSummaryLabel = WINDOW.makeLabel(Messages.SUMMARY_TRACK_COMMENT.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 1, 2, GridBagConstraints.WEST, 1);
        add(trackCommentSummaryLabel, constraints);

        trackCommentSummaryData = WINDOW.makeLabel("", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 2, 2, GridBagConstraints.WEST, 1);
        add(trackCommentSummaryData, constraints);
    }

    /**
     * Updating info about the file currently being processed.
     *
     * @param filename      the new filename.
     * @param queueSize     Total files in queue
     * @param queuePosition current postion in queue
     */
    void updateCurrentFile(String filename, int queueSize, int queuePosition) {
        String remainingFiles = Messages.PROCESSING_FILES.get(queuePosition + "", queueSize + "");
        currentImportLabel.setText(filename);
        remainingFilesLabel.setText(remainingFiles);
    }

    /**
     * Updating the info about the position and amount of files in the queue.
     *
     * @param queueSize     the size of the queue.
     * @param queuePosition the current position in the queue.
     */
    void updateQueueInfo(int queueSize, int queuePosition) {
        remainingFilesLabel.setText(Messages.PROCESSING_FILES.get(queuePosition + "", queueSize + ""));
    }

    /**
     * Updating the distance covered in the current track
     *
     * @param trackDistance the distance covered in the track
     */
    void updateCurrentFileDistance(double trackDistance) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.trackDistance = trackDistance;

        if (trackDistance > 1000.0) {
            double trackDistanceInKm = trackDistance / 1000;
            trackLengthLabel.setText(Messages.TRACK_LENGTH.get() + df.format(trackDistanceInKm) + " km");
        } else {
            trackLengthLabel.setText(Messages.TRACK_LENGTH.get() + df.format(trackDistance) + " m");
        }

    }

    /**
     * Places the radio buttons in the given coordinates in the panel
     */
    private void setButtonsInWindow() {
        int y = 3;
        int x = 1;
        for (JRadioButton radioButton : radioButtons) {
            WINDOW.modifyConstraints(constraints, x, y, GridBagConstraints.WEST, 1);
            radioButton.setPreferredSize(new Dimension(200, 50));
            add(radioButton, constraints);
            allInputComponents.add(radioButton);
            y++;
            if (y == 7) {
                y = 3;
                x++;
            }
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
            radioButton.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, WINDOW.TEXT_FONT_SIZE));
            radioButtons.add(radioButton);
            radioButtonGroup.add(radioButton);
        }
        return radioButtons;
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
        hideAllComponents();
        startInfoLabel.setVisible(true);
        backButton.setVisible(false);
        nextButton.setVisible(true);
        currentImportLabel.setVisible(true);
        trackLengthLabel.setVisible(true);
        registerButton.setVisible(false);
        viewCount = 0;
    }

    /**
     * Sets all JComponents in allInputComponents visible/invisible
     */
    private void hideAllComponents() {
        for (JComponent c : allInputComponents) {
            c.setVisible(false);
        }
        registerButton.setVisible(false);
        currentImportLabel.setVisible(false);
        setSummaryVisibility(false);
    }

    /**
     * Sets the summary labels visible/invisible
     *
     * @param visibility true if visible, false if not
     */
    private void setSummaryVisibility(boolean visibility) {
        areaSearchedSummaryLabel.setVisible(visibility);
        crewCountSummaryLabel.setVisible(visibility);
        crewNumberSummaryLabel.setVisible(visibility);
        crewTypeSummaryLabel.setVisible(visibility);
        trackCommentSummaryLabel.setVisible(visibility);
        trackNumberSummaryLabel.setVisible(visibility);

        areaSearchedSummaryData.setVisible(visibility);
        crewCountSummaryData.setVisible(visibility);
        crewNumberSummaryData.setVisible(visibility);
        crewTypeSummaryData.setVisible(visibility);
        trackCommentSummaryData.setVisible(visibility);
        trackNumberSummaryData.setVisible(visibility);
    }

    /**
     * Sets det data in the summaryData labels fetched from the input fields
     */

    private void setSummaryData() {
        String crew = getSelectedRadioButton();
        String crewCount = crewCountSpinner.getModel().getValue().toString();
        String crewNumber = crewNumberSpinner.getModel().getValue().toString();
        String areaSearched = areaSearchedStrings.toString();
        String trackNumber = trackNumberSpinner.getModel().getValue().toString();
        String trackComment = trackCommentInput.getText();

        areaSearchedSummaryData.setText(areaSearched);
        crewCountSummaryData.setText(crewCount);
        crewNumberSummaryData.setText(crewNumber);
        crewTypeSummaryData.setText(crew);
        trackCommentSummaryData.setText(trackComment);
        trackNumberSummaryData.setText(trackNumber);
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
            String areaSearched = areaSearchedStrings.toString();
            int trackNumber = Integer.parseInt(trackNumberSpinner.getModel().getValue().toString());
            String trackComment = trackCommentInput.getText();
            TrackInfo trackInfo = new TrackInfo(crew, crewCount, crewNumber, areaSearched, trackDistance, trackNumber, trackComment);
            OPERATION_MANAGER.processFile(trackInfo);

            // Resetting all input fields
            radioButtons.get(0).setSelected(true);
            crewCountSpinner.setValue(1);
            crewNumberSpinner.setValue(21);
            areaSearchedLabel.setText("");
            areaSearchedStrings.clear();
            areaSearchedSpinner.setValue(1);
            trackNumberSpinner.setValue(1);
            trackCommentInput.setText("");

            initialVisibility();

            // Message to user
            String dialogText = Messages.SAVE_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
        });
    }

    /**
     * Button listener for displaying the next "page".
     */
    private void nextButtonListener() {
        nextButton.addActionListener(actionEvent -> {
            viewCount++;
            switch (viewCount) {
                case 0:
                    initialVisibility();
                    break;
                case 1:
                    hideAllComponents();
                    crewTypeLabel.setVisible(true);
                    for (JRadioButton rb : radioButtons) {
                        rb.setVisible(true);
                    }
                    backButton.setVisible(true);
                    break;
                case 2:
                    hideAllComponents();
                    trackNumberLabel.setVisible(true);
                    trackNumberSpinner.setVisible(true);
                    backButton.setVisible(true);
                    break;
                case 3:
                    hideAllComponents();
                    crewNumberLabel.setVisible(true);
                    crewNumberSpinner.setVisible(true);
                    break;
                case 4:
                    hideAllComponents();
                    crewCountLabel.setVisible(true);
                    crewCountSpinner.setVisible(true);
                    break;
                case 5:
                    hideAllComponents();
                    areaLabel.setVisible(true);
                    areaInfoLabel.setVisible(true);
                    areaSearchedSpinner.setVisible(true);
                    areaInputButton.setVisible(true);
                    areaSearchedLabel.setVisible(true);
                    break;
                case 6:
                    hideAllComponents();
                    trackCommentLabel.setVisible(true);
                    trackCommentInput.setVisible(true);
                    break;
                case 7:
                    hideAllComponents();
                    nextButton.setVisible(false);
                    registerButton.setVisible(true);
                    setSummaryVisibility(true);
                    setSummaryData();
                default:
                    break;
            }
        });
    }

    /**
     * Button listener for displaying the previous "page".
     */
    private void backButtonListener() {
        backButton.addActionListener(actionEvent -> {
            viewCount--;
            switch (viewCount) {
                case 0:
                    initialVisibility();
                    break;
                case 1:
                    hideAllComponents();
                    crewTypeLabel.setVisible(true);
                    for (JRadioButton rb : radioButtons) {
                        rb.setVisible(true);
                    }
                    break;
                case 2:
                    hideAllComponents();
                    trackNumberLabel.setVisible(true);
                    trackNumberSpinner.setVisible(true);
                    break;
                case 3:
                    hideAllComponents();
                    crewNumberLabel.setVisible(true);
                    crewNumberSpinner.setVisible(true);
                    break;
                case 4:
                    hideAllComponents();
                    crewCountLabel.setVisible(true);
                    crewCountSpinner.setVisible(true);
                    break;
                case 5:
                    hideAllComponents();
                    areaLabel.setVisible(true);
                    areaInfoLabel.setVisible(true);
                    areaSearchedSpinner.setVisible(true);
                    areaInputButton.setVisible(true);
                    areaSearchedLabel.setVisible(true);
                    nextButton.setVisible(true);
                    break;
                case 6:
                    hideAllComponents();
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
     * A method for the skipButton's listener
     */
    private void skipButtonListener() {
        skipButton.addActionListener(actionEvent -> {
            OPERATION_MANAGER.prepareNextFile();

            String dialogText = Messages.SKIP_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
        });
    }

    /**
     * Showing dialog to get info about the area searched.
     */
    private void areaInputButtonListener() {
        areaInputButton.addActionListener(actionEvent -> {
            String areaSearchedString = areaSearchedSpinner.getValue().toString();
            if (!areaSearchedStrings.contains(areaSearchedString)) {
                areaSearchedStrings.add(areaSearchedString);
                areaSearchedLabel.setText(areaSearchedStrings.toString());
            }
        });
    }
}
