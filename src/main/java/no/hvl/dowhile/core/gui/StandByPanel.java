package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * This class has an interface for importing gpx-files from PC and accessing settings for the current Operation.
 */
public class StandByPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    // JComponents
    private JButton operationPanelButton;
    private JLabel infoLabel;
    private JButton importFileButton;

    StandByPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        infoLabel = WINDOW.makeLabel(Messages.AWAITING_GPS.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 3);
        add(infoLabel, constraints);

        // Import local GPX-file button
        importFileButton = WINDOW.makeButton(Messages.IMPORT_LOCAL_FILE.get(), 400, 60);
        importFileButton.setName("importFileButton");
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 2);
        add(importFileButton, constraints);

        makeSettingsButton();

        operationPanelButtonListener();
        importFileButtonListener();

        setBackground(new Color(255, 245, 252));
    }

    private void makeSettingsButton() {
        final int NEW_WIDTH = 60;
        final int NEW_HEIGHT = 60;
        operationPanelButton = WINDOW.makeButton("", NEW_WIDTH, NEW_HEIGHT);
        try {
            Image img = ImageIO.read(getClass().getResource("/images/settings-icon.png"));
            Image newimg = img.getScaledInstance((NEW_WIDTH - 10), (NEW_HEIGHT - 10), java.awt.Image.SCALE_SMOOTH);
            operationPanelButton.setIcon(new ImageIcon(newimg));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        operationPanelButton.setToolTipText("Operasjonsinnstillinger");
        WINDOW.modifyConstraints(constraints, 2, 1, GridBagConstraints.EAST, 1);
        add(operationPanelButton, constraints);
    }

    private void operationPanelButtonListener() {
        operationPanelButton.addActionListener(actionEvent -> {
            WINDOW.openOperationPanel(OPERATION_MANAGER.getOperation().pathsToString());
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
}
