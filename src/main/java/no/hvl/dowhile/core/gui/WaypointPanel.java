package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JonKjetil on 08.05.2017.
 */
public class WaypointPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    private JLabel waypointHeaderLabel;
    private JLabel currentWaypointLabel;
    private JTextField waypointNameInput;
    private JTextField waypointDescriptionInput;
    private JButton confirmNameButton;

    private JLabel queueLabel;

    public WaypointPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {

        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        wayPointGUI();

        confirmButtonListener();

        setBackground(new Color(255, 245, 252));
    }

    private void wayPointGUI() {
        // Header label for waypoint processing
        waypointHeaderLabel = WINDOW.makeHeaderLabel(Messages.WAYPOINT_HEADER.get());
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 1);
        add(waypointHeaderLabel, constraints);

        // Label with the current waypoint file
        currentWaypointLabel = WINDOW.makeLabel(Messages.IMPORTED_FROM_WAYPOINT_GPS.get() + "Ingen fil.", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 1);
        add(currentWaypointLabel, constraints);

        // Input field for new name for the waypoint file
        waypointNameInput = WINDOW.makeTextField(100, 60);
        PromptSupport.setPrompt(Messages.NEW_NAME.get(), waypointNameInput);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(waypointNameInput, constraints);

        // Input for description for waypoint file
        waypointDescriptionInput = WINDOW.makeTextField(100, 60);
        PromptSupport.setPrompt(Messages.NEW_DESCRIPTION.get(), waypointDescriptionInput);
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(waypointDescriptionInput, constraints);

        // Confirm button
        confirmNameButton = WINDOW.makeButton(Messages.REGISTER_BUTTON.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.WEST, 1);
        add(confirmNameButton, constraints);

        // Queue with remaining waypoint files
        queueLabel = WINDOW.makeLabel(Messages.IMPORTED_FILES_LEFT_TO_PROCESS.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.WEST, 1);
        add(queueLabel, constraints);
    }

    private void confirmButtonListener() {
        confirmNameButton.addActionListener(actionEvent -> {
            String name = waypointNameInput.getText();
            String description = waypointDescriptionInput.getText();

            OPERATION_MANAGER.saveWaypoint(name, description);
            waypointNameInput.setText("");

            String dialogText = Messages.SAVE_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
        });
    }

    /**
     * Updating info about the file currently being processed.
     *
     * @param filename  the new filename.
     * @param queueSize Total files in queue
     * @param queuePosition current postion in queue
     */
    public void updateCurrentFile(String filename, int queueSize, int queuePosition) {
        String currentImportedFile = Messages.IMPORTED_FROM_WAYPOINT_GPS.get() + filename;
        String remainingFiles = "Prosesserer fil " + queuePosition + " av " + queueSize;
        currentWaypointLabel.setText(currentImportedFile);
        queueLabel.setText(remainingFiles);
    }
}
