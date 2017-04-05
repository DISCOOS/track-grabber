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

public class TrackPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel statusLabel;
    private JLabel operationStartedLabel;
    private GridBagConstraints constraints;
    private List<JRadioButton> radioButtons;
    private ButtonGroup crewGroup;

    public TrackPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        WINDOW.getContentPane().add(this, BorderLayout.NORTH);
        constraints = new GridBagConstraints();
        setConstraintsInsets(5);

        // Header Label
        JLabel headerLabel = new JLabel();
        headerLabel.setText(Messages.PROJECT_NAME.get());
        headerLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 24));
        setConstraintsXY(0, 0);
        add(headerLabel, constraints);

        // Operation started label
        operationStartedLabel = new JLabel();
        operationStartedLabel.setText("<html><body>" + Messages.OPERATION_STARTED.get() + "<br>" + StringTools.formatDate(OPERATION_MANAGER.getOperationStartTime()) + "</body></html>");
        operationStartedLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(0, 1);
        constraints.gridwidth = 2;
        add(operationStartedLabel, constraints);

        // isConnected label
        statusLabel = new JLabel();
        statusLabel.setText(Messages.GPS_OFFLINE.get());
        statusLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 1);
        constraints.anchor = GridBagConstraints.NORTH;
        add(statusLabel, constraints);

        crewGroup = new ButtonGroup();
        radioButtons = generateButtons(generateNames());

        // adding them buttons
        setButtonsInWindow();

        // Label and input for team number
        JLabel crewNumberLabel = new JLabel();
        crewNumberLabel.setText(Messages.CREW_NUMBER.get());
        crewNumberLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 2);
        add(crewNumberLabel, constraints);

        SpinnerModel crewNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner groupNumberSpinner = new JSpinner(crewNumberInput);
        setConstraintsXY(2, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(groupNumberSpinner, constraints);

        // Label for crew count
        JLabel crewCountLabel = new JLabel();
        crewCountLabel.setText(Messages.CREW_COUNT.get());
        crewCountLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 4);
        add(crewCountLabel, constraints);

        SpinnerModel crewCountInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner crewCountSpinner = new JSpinner(crewCountInput);
        setConstraintsXY(2, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewCountSpinner, constraints);

        // Register button
        JButton registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        setConstraintsXY(2, 6);
        constraints.gridwidth = 2;
        add(registerButton, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String crew = getSelectedRadioButton();
                String crewCount = crewCountSpinner.getModel().getValue().toString();
                String crewNumber = groupNumberSpinner.getModel().getValue().toString();
                TrackInfo trackInfo = new TrackInfo();
                trackInfo.setTrackName(crew);
                trackInfo.setCrewCount(Integer.parseInt(crewCount));
                trackInfo.setCrewNumber(Integer.parseInt(crewNumber));
                OPERATION_MANAGER.initiateTrackCutter(trackInfo);
                String dialogText = Messages.SAVE_FILE.get() + crew + "_" + crewNumber + "_" + crewCount;

                JOptionPane.showMessageDialog(
                        JOptionPane.getRootFrame(),
                        dialogText);
            }
        });
    }

    /**
     * Open/show this panel.
     */
    public void open() {
        setVisible(true);
    }

    /**
     * Close/hide this panel.
     */
    public void close() {
        setVisible(false);
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
     * Setting the constraints for x and y coordinates
     *
     * @param x x coordinate for contstraints
     * @param y y coordinate for contstraints
     */

    private void setConstraintsXY(int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
    }

    /**
     * Setting the insets of the constraints
     *
     * @param borders all borders around grid cell
     */
    private void setConstraintsInsets(int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }

    // Creates a radio button and a label a places them in the given coordinates
    private void setButtonsInWindow() {
        int startY = 2;
        int x = 0;

        for (JRadioButton rb : radioButtons) {
            setConstraintsXY(x, startY);
            constraints.anchor = GridBagConstraints.WEST;
            add(rb, constraints);

            startY++;
        }

    }

    private List<JRadioButton> generateButtons(List<String> crewNames) {
        List<JRadioButton> rbs = new ArrayList<>();

        JRadioButton radioButton = null;
        for (String n : crewNames) {
            radioButton = new JRadioButton(n);
            radioButton.setText(n);
            rbs.add(radioButton);
            crewGroup.add(radioButton);
        }

        return rbs;
    }

    private List<String> generateNames() {
        List<String> crewNames = new ArrayList<>();
        crewNames.add("Mannskap");
        crewNames.add("Hund");
        crewNames.add("Bil");
        crewNames.add("Sjørøver");
        crewNames.add("Helikopter");
        crewNames.add("Etc.");
        return crewNames;
    }

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
