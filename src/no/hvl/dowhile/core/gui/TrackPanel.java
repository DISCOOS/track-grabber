package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has an interface for configuring details related to one track.
 */
public class TrackPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel statusLabel;
    private JLabel operationStartedLabel;
    private GridBagConstraints constraints;
    private List<JRadioButton> radioButtons;
    private ButtonGroup radioButtonGroup;

    public TrackPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
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
        constraints.gridwidth = 2;
        add(operationStartedLabel, constraints);

        // isConnected label
        statusLabel = WINDOW.makeLabel(Messages.GPS_OFFLINE.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 2, 1);
        constraints.anchor = GridBagConstraints.NORTH;
        add(statusLabel, constraints);

        radioButtonGroup = new ButtonGroup();
        radioButtons = generateButtons(generateNames());

        // adding them buttons
        setButtonsInWindow();

        // Label and input for team number
        JLabel crewNumberLabel = WINDOW.makeLabel(Messages.CREW_NUMBER.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 2, 2);
        add(crewNumberLabel, constraints);

        SpinnerModel crewNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner groupNumberSpinner = new JSpinner(crewNumberInput);
        WINDOW.setConstraintsXY(constraints, 2, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(groupNumberSpinner, constraints);

        // Label and input for crew count
        JLabel crewCountLabel = WINDOW.makeLabel(Messages.CREW_COUNT.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 2, 4);
        add(crewCountLabel, constraints);

        SpinnerModel crewCountInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner crewCountSpinner = new JSpinner(crewCountInput);
        WINDOW.setConstraintsXY(constraints, 2, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewCountSpinner, constraints);

        // Label and input for area searched
        JLabel areaLabel = WINDOW.makeLabel(Messages.AREA_SEARCHED.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 2, 6);
        add(areaLabel, constraints);

        JTextField areaInput = new JTextField();
        WINDOW.setConstraintsXY(constraints, 2, 7);
        add(areaInput, constraints);

        // Register button
        JButton registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 2, 8);
        constraints.gridwidth = 2;
        add(registerButton, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetching the input data and sending it to the OperationManager
                String crew = getSelectedRadioButton();
                int crewCount = Integer.parseInt(crewCountSpinner.getModel().getValue().toString());
                int crewNumber = Integer.parseInt(groupNumberSpinner.getModel().getValue().toString());
                String areaSearched = areaInput.getText();
                TrackInfo trackInfo = new TrackInfo(crew, crewCount, crewNumber, areaSearched);
                OPERATION_MANAGER.initiateTrackCutter(trackInfo);

                // Message to user
                String dialogText = Messages.SAVE_FILE.get() + crew + "_" + crewNumber + "_" + crewCount;
                JOptionPane.showMessageDialog(
                        JOptionPane.getRootFrame(),
                        dialogText);
            }
        });
    }

    /**
     * Set the status of whether a gps is connected or not.
     *
     * @param status the new status.
     */
    public void setStatus(String status) {
        statusLabel.setText("GPS: " + status);
    }

    /**
     * places the radio buttons in the given coordinates in the panel
     */
    private void setButtonsInWindow() {
        int startY = 2;
        int x = 0;

        for (JRadioButton rb : radioButtons) {
            WINDOW.setConstraintsXY(constraints, x, startY);
            constraints.anchor = GridBagConstraints.WEST;
            add(rb, constraints);

            startY++;
        }

    }

    /**
     * Generates the radio buttons
     *
     * @param crewNames List of strings with the names used on the radio buttons
     * @return a List with the radio buttons
     */
    private List<JRadioButton> generateButtons(List<String> crewNames) {
        List<JRadioButton> rbs = new ArrayList<>();

        JRadioButton radioButton = null;
        for (String n : crewNames) {
            radioButton = new JRadioButton(n);
            radioButton.setText(n);
            rbs.add(radioButton);
            radioButtonGroup.add(radioButton);
        }

        return rbs;
    }

    /**
     * Method for creating names that will be put in the radio buttons
     *
     * @return a List with the names
     */
    private List<String> generateNames() {
        List<String> crewNames = new ArrayList<>();
        crewNames.add("Lag");
        crewNames.add("Hund");
        crewNames.add("Bil");
        crewNames.add("ATV");
        crewNames.add("Helikopter");
        crewNames.add("Båt");
        return crewNames;
    }

    /**
     * Finds the selected radio button and returns it
     *
     * @return a String with the selected radio button
     */
    private String getSelectedRadioButton() {
        String chosenRadioButton = "";
        for (JRadioButton rb : radioButtons) {
            if (rb.isSelected()) {
                chosenRadioButton = rb.getText();
            }
        }
        return chosenRadioButton;
    }
}
