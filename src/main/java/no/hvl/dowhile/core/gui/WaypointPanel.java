package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.*;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 * Created by JonKjetil on 08.05.2017.
 */
public class WaypointPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    private JLabel currentWaypointLabel;
    private JTextField waypointNameInput;
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
        // Label with the current waypoint file
        currentWaypointLabel = WINDOW.makeLabel("Prosesserer: ", WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 1);
        add(currentWaypointLabel, constraints);

        // Input field for new name for the waypoint file
        waypointNameInput = new JTextField();
        waypointNameInput.setToolTipText("Nytt navn");
        PromptSupport.setPrompt("Nytt navn", waypointNameInput);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 2);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(waypointNameInput, constraints);

        // Confirm button
        confirmNameButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.modifyConstraints(constraints, 2, 1, GridBagConstraints.WEST, 1);
        add(confirmNameButton, constraints);

        // Queue with remaining waypoint files
        queueLabel = WINDOW.makeLabel("", WINDOW.TEXT_FONT_SIZE);
        WINDOW.modifyConstraints(constraints, 0,2, GridBagConstraints.WEST, 1);
        add(queueLabel, constraints);
    }

    private void confirmButtonListener() {
        confirmNameButton.addActionListener(actionEvent -> {
            String name = waypointNameInput.getText();

            OPERATION_MANAGER.assignNameToWaypoint(name);
            waypointNameInput.setText("");

            String dialogText = Messages.SAVE_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
        });
    }
}
