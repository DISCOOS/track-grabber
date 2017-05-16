package no.hvl.dowhile.core.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * This class represents the Window exposed to the user for configuring the application.
 * The class has different panels for displaying information depending on certain events in the application.
 */
public class Window extends JFrame {
    final int HEADER_FONT_SIZE = 32;
    final int TEXT_FONT_SIZE = 24;
    final int BUTTON_FONT_SIZE = 20;
    final Font TEXT_FONT = new Font(Messages.FONT_NAME.get(), Font.PLAIN, TEXT_FONT_SIZE);
    final Font TEXT_BOLD_FONT = new Font(Messages.FONT_NAME.get(), Font.BOLD, TEXT_FONT_SIZE);
    final Font TEXT_ITALIC_FONT = new Font(Messages.FONT_NAME.get(), Font.ITALIC, TEXT_FONT_SIZE);
    final Font HEADER_FONT = new Font(Messages.FONT_NAME.get(), Font.BOLD, HEADER_FONT_SIZE);
    final Font BUTTON_FONT = new Font(Messages.FONT_NAME.get(), Font.BOLD, BUTTON_FONT_SIZE);
    private final OperationManager OPERATION_MANAGER;
    private JPanel cardPanel;
    private HeaderPanel headerPanel;
    private StartPanel startPanel;
    private OperationPanel operationPanel;
    private TrackPanel trackPanel;
    private WaypointPanel waypointPanel;
    private StandByPanel standByPanel;

    /**
     * Constructor setting up the Window, logo, listener for closing and creating the different panels to display.
     *
     * @param OPERATION_MANAGER the current instance of the OperationManager.
     * @see OperationManager
     */
    public Window(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;

        setTitle(Messages.PROJECT_NAME.get());
        setSize(800, 400);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Image logo = getLogo();
        if (logo != null) {
            setIconImage(logo);
        }

        headerPanel = new HeaderPanel(this);
        startPanel = new StartPanel(OPERATION_MANAGER, this);
        operationPanel = new OperationPanel(OPERATION_MANAGER, this);
        trackPanel = new TrackPanel(OPERATION_MANAGER, this);
        waypointPanel = new WaypointPanel(OPERATION_MANAGER, this);
        standByPanel = new StandByPanel(OPERATION_MANAGER, this);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(startPanel, "Start");
        cardPanel.add(operationPanel, "Operation");
        cardPanel.add(trackPanel, "Track");
        cardPanel.add(waypointPanel, "Waypoint");
        cardPanel.add(standByPanel, "StandBy");
        add(cardPanel, BorderLayout.NORTH);

        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        open();
        openStartPanel();

        // Listener for when the window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), Messages.CONFIRM_EXIT.get(), Messages.PROJECT_NAME.get(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    OPERATION_MANAGER.stop();
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Read in the logo to use as the window icon.
     *
     * @return the logo as an Image.
     */
    private Image getLogo() {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File("src/main/resources/images/red_cross_icon.jpg"));
        } catch (IOException ex) {
            return null;
        }
        return bufferedImage;
    }

    /**
     * Open/show the window.
     */
    public void open() {
        setVisible(true);
    }

    /**
     * Close/hide the window.
     */
    public void close() {
        setVisible(false);
    }

    /**
     * Open the panel allowing the administrator to change operation.
     */
    public void openStartPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Start");
    }

    /**
     * Open the panel allowing the administrator to edit the current operation.
     */
    public void openOperationPanel(String paths) {
        operationPanel.setAllSavedPathsLabel(paths);
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Operation");
    }

    /**
     * Open the panel allowing to user to give info about a track.
     */
    public void openTrackPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Track");
    }

    public void openWaypointPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "Waypoint");
    }

    public void openStandByPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "StandBy");
    }

    /**
     * Shows a dialog with the given text.
     *
     * @param text the text to show in the dialog.
     */
    public void showDialog(String title, String text) {
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updating the labels with info about the operation.
     *
     * @param operation the current operation.
     */
    public void updateOperationInfo(Operation operation) {
        operationPanel.updateOperationInfo(operation);
        headerPanel.updateOperationInfo(operation);
    }

    /**
     * Telling the track panel to update the info about which file is currently processed.
     *
     * @param filename      the name of the file.
     * @param queueSize     Total files in queue
     * @param queuePosition current postion in queue
     */
    public void updateCurrentTrackFile(String filename, int queueSize, int queuePosition, double trackDistance) {
        trackPanel.updateCurrentFile(filename, queueSize, queuePosition);
        trackPanel.updateCurrentFileDistance(trackDistance);

    }

    public void updateCurrentWaypointFile(String filename, int queueSize, int queuePosition) {
        waypointPanel.updateCurrentFile(filename, queueSize, queuePosition);
    }

    /**
     * Tell the operation panel to add existing operations to the selector in the user interface.
     *
     * @param operations the operations to add.
     */
    public void showExistingOperations(List<Operation> operations) {
        startPanel.showExistingOperations(operations);
    }

    /**
     * Makes a JLabel with given text and font size
     *
     * @param text  text that will be inserted into the JLabel
     * @param style Font style (plain, bold...)
     * @return a JLabel with given text and font size
     */
    public JLabel makeLabel(String text, int style) {
        JLabel label = new JLabel(text);
        if (style == Font.BOLD) {
            label.setFont(TEXT_BOLD_FONT);
        } else if (style == Font.ITALIC) {
            label.setFont(TEXT_ITALIC_FONT);
        } else {
            label.setFont(TEXT_FONT);
        }
        return label;
    }

    public JLabel makeHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        return label;
    }

    /**
     * Makes a JButton with given text and a specific size and color
     *
     * @param text   within the button
     * @param width
     * @param height @return a JButton with given text and a set dimension and color
     */
    public JButton makeButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(242, 94, 94));
        button.setFont(BUTTON_FONT);

        return button;
    }

    public JTextField makeTextField(int width, int height) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width, height));
        textField.setFont(TEXT_FONT);

        return textField;
    }

    public JSpinner makeSpinner(SpinnerModel spinnerModel) {
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(150, 50));
        spinner.setFont(TEXT_FONT);

        return spinner;
    }

    public DatePicker makeDatePicker(int width, int height) {
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        dateSettings.setAllowEmptyDates(false);

        DatePicker datePicker = new DatePicker(dateSettings);
        datePicker.setPreferredSize(new Dimension(width, height));
        datePicker.setFont(TEXT_FONT);

        return datePicker;
    }

    public TimePicker makeTimePicker(int width, int height) {
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.initialTime = LocalTime.now();

        TimePicker timePicker = new TimePicker(timeSettings);
        timePicker.setPreferredSize(new Dimension(width, height));
        timePicker.setFont(TEXT_FONT);

        return timePicker;
    }

    /**
     * Utility method to set the data for the constraints.
     *
     * @param constraints the constraints to modify.
     * @param x           X-coordinate.
     * @param y           Y-coordinate.
     * @param anchor      Anchor to define how the object will "float" in the window.
     * @param gridWidth   The amount of grids the element should cover.
     */
    public void modifyConstraints(GridBagConstraints constraints, int x, int y, int anchor, int gridWidth) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.anchor = anchor;
        constraints.gridwidth = gridWidth;
    }

    /**
     * Setting the insets of the constraints
     *
     * @param constraints the GridBagConstraints for which we will set the insets
     * @param borders     all borders around grid cell
     */
    public void setConstraintsInsets(GridBagConstraints constraints, int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }
}
