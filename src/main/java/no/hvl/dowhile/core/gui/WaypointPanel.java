package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has an interface for configuring details related to one waypoints.
 */
public class WaypointPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private GridBagConstraints constraints;

    // JComponents
    private JLabel waypointHeaderLabel;
    private JLabel currentWaypointLabel;
    private JTextField waypointNameInput;
    private JTextArea waypointDescriptionInput;
    private JButton confirmButton;
    private JButton skipButton;
    private JLabel queueLabel;

    // Radio buttons
    private JRadioButton redButton;
    private JRadioButton greenButton;
    private List<JRadioButton> coloredButtons;
    private ButtonGroup colorGroup;

    /**
     * Constructor setting the OPERATION_MANAGER and WINDOW.
     * Also sets the layout and the constraints and the background color
     *
     * @param OPERATION_MANAGER the current instance of the OperationManager
     * @param WINDOW            the current instance of the Window
     */
    public WaypointPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {

        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        coloredButtons = new ArrayList<>();
        colorGroup = new ButtonGroup();

        waypointGUI();
        colorRadioButtons();

        confirmButtonListener();
        skipButtonListener();
        radioButtonListeners();

        setBackground(new Color(255, 245, 252));
    }

    /**
     * Creates all the JComponents and adds them to the JPanel
     */
    private void waypointGUI() {
        // Header label for waypoint processing
        waypointHeaderLabel = WINDOW.makeHeaderLabel(Messages.WAYPOINT_HEADER.get());
        WINDOW.modifyConstraints(constraints, 0, 0, GridBagConstraints.WEST, 4);
        add(waypointHeaderLabel, constraints);

        // Label with the current waypoint file
        currentWaypointLabel = WINDOW.makeLabel(Messages.IMPORTED_FROM_WAYPOINT_GPS.get() + "Ingen fil.", Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 1, GridBagConstraints.WEST, 4);
        add(currentWaypointLabel, constraints);

        // Input field for new name for the waypoint file
        waypointNameInput = WINDOW.makeTextField(100, 60);
        PromptSupport.setPrompt(Messages.NEW_NAME.get(), waypointNameInput);
        WINDOW.modifyConstraints(constraints, 0, 2, GridBagConstraints.WEST, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(waypointNameInput, constraints);

        // Input for description for waypoint file
        waypointDescriptionInput = new JTextArea();
        waypointDescriptionInput.setFont(WINDOW.TEXT_FONT);
        waypointDescriptionInput.setPreferredSize(new Dimension(100, 150));
        waypointDescriptionInput.setLineWrap(true);
        waypointDescriptionInput.setWrapStyleWord(true);
        PromptSupport.setPrompt(Messages.NEW_DESCRIPTION.get(), waypointDescriptionInput);
        WINDOW.modifyConstraints(constraints, 0, 3, GridBagConstraints.WEST, 4);
        add(waypointDescriptionInput, constraints);

        // Flag label
        JLabel flagLabel = WINDOW.makeLabel(Messages.FLAG_COLOR.get(), Font.PLAIN);
        WINDOW.modifyConstraints(constraints, 0, 4, GridBagConstraints.WEST, 4);
        add(flagLabel, constraints);

        // Confirm button
        confirmButton = WINDOW.makeButton(Messages.REGISTER_BUTTON.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 2, 7, GridBagConstraints.EAST, 2);
        add(confirmButton, constraints);

        // Skip button
        skipButton = WINDOW.makeButton(Messages.SKIP_BUTTON.get(), 150, 50);
        WINDOW.modifyConstraints(constraints, 0, 7, GridBagConstraints.WEST, 2);
        add(skipButton, constraints);

        // Queue with remaining waypoint files
        queueLabel = WINDOW.makeLabel(Messages.PROCESSING_FILES.get(), Font.BOLD);
        WINDOW.modifyConstraints(constraints, 0, 10, GridBagConstraints.WEST, 4);
        add(queueLabel, constraints);
    }

    /**
     * Creates flag color radio buttons adds them to the JPanel
     */
    private void colorRadioButtons() {
        redButton = new JRadioButton();
        redButton.setName("Red");
        redButton.setText(OPERATION_MANAGER.getConfig().getRedLabel());
        ImageIcon red = new ImageIcon(getColoredImage(Color.RED, 50));
        redButton.setIcon(red);
        redButton.setBorder(new LineBorder(Color.BLACK, 4));
        colorGroup.add(redButton);
        coloredButtons.add(redButton);
        WINDOW.modifyConstraints(constraints, 0, 5, GridBagConstraints.CENTER, 4);
        add(redButton, constraints);

        greenButton = new JRadioButton();
        greenButton.setName("Green");
        greenButton.setText(OPERATION_MANAGER.getConfig().getGreenLabel());
        ImageIcon green = new ImageIcon(getColoredImage(Color.green, 50));
        greenButton.setIcon(green);
        greenButton.setBorder(new LineBorder(Color.BLACK, 4));
        colorGroup.add(greenButton);
        coloredButtons.add(greenButton);
        WINDOW.modifyConstraints(constraints, 0, 6, GridBagConstraints.CENTER, 4);
        add(greenButton, constraints);
    }

    /**
     * A method for the flag color radio buttons listeners
     */
    private void radioButtonListeners() {
        redButton.addActionListener(actionEvent -> {
            if (redButton.isSelected()) {
                redButton.setBorderPainted(true);
                setRadioButtonsBorder(redButton);
            }
        });

        greenButton.addActionListener(actionEvent -> {
            if (greenButton.isSelected()) {
                greenButton.setBorderPainted(true);
                setRadioButtonsBorder(greenButton);
            }
        });
    }

    /**
     * A method for the confirmButton's listener
     */
    private void confirmButtonListener() {
        confirmButton.addActionListener(actionEvent -> {
            String name = waypointNameInput.getText();
            String description = waypointDescriptionInput.getText();
            String flagColor = getSelectedRadioButton(); // Variable for flag color selection

            OPERATION_MANAGER.saveWaypoint(name, description, flagColor);

            String dialogText = Messages.SAVE_FILE.get();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), dialogText);
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
     * Updating info about the file currently being processed.
     *
     * @param waypointDate  the date of the waypoint
     * @param waypointName  the new filename.
     * @param queueSize     Total files in queue
     * @param queuePosition current postion in queue
     */
    public void updateCurrentFile(String waypointDate, String waypointName, String waypointComment, int queueSize, int queuePosition) {
        String currentImportedFile = Messages.IMPORTED_FROM_WAYPOINT_GPS.get() + waypointDate;
        String remainingFiles = Messages.PROCESSING_FILES.get(queuePosition + "", queueSize + "");
        waypointNameInput.setText(waypointName);
        waypointDescriptionInput.setText(waypointComment);
        currentWaypointLabel.setText(currentImportedFile);
        queueLabel.setText(remainingFiles);
    }

    /**
     * Updating the info about the position and amount of files in the queue.
     *
     * @param queueSize     the size of the queue.
     * @param queuePosition the current position in the queue.
     */
    public void updateQueueInfo(int queueSize, int queuePosition) {
        queueLabel.setText(Messages.PROCESSING_FILES.get(queuePosition + "", queueSize + ""));
    }

    /**
     * Fetches the current selected flag color radio button and returns the color
     *
     * @return the selected radio button's color
     */
    private String getSelectedRadioButton() {
        String color = "";
        for (JRadioButton rb : coloredButtons) {
            if (rb.isSelected()) {
                color = rb.getName();
            }
        }
        return color;
    }

    /**
     * Makes a color square icon with given color and size
     * @param color color of the icon
     * @param size size of the icon
     * @return the icon as an image object
     */
    private Image getColoredImage(Color color, int size) {
        BufferedImage bi = new BufferedImage(
                size,
                size,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);

        g.dispose();
        return bi;
    }

    /**
     * Sets the border true on the selected radio button
     * @param selected the selected radio button
     */
    private void setRadioButtonsBorder(JRadioButton selected) {
        for (JRadioButton rb : coloredButtons) {
            if (!rb.equals(selected)) {
                rb.setBorderPainted(false);
            }
        }
    }
}

