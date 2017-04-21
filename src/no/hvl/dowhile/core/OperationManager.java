package no.hvl.dowhile.core;

import no.hvl.dowhile.core.drive.DriveDetector;
import no.hvl.dowhile.core.drive.GPSDrive;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handling communication between the components in the application.
 */
public class OperationManager {
    private boolean active;
    private Window window;
    private DriveDetector driveDetector;
    private FileManager fileManager;
    private Config config;
    private Operation operation;
    private List<Operation> existingOperations;
    private TrackCutter currentTrackCutter;
    private List<File> queue;

    public OperationManager() {
        this.active = true;
        this.window = new Window(this);
        this.driveDetector = new DriveDetector(this);
        this.fileManager = new FileManager(this);
        this.config = new Config();
        this.existingOperations = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    /**
     * Check if the system is active.
     * Components running should cancel if this is false.
     *
     * @return true if the system is active, false if not.
     */
    public boolean isActive() {
        return active;
    }

    public Operation getOperation() {
        return operation;
    }

    /**
     * Start components which needs to perform operations from the beginning.
     */
    public void start() {
        new Thread(driveDetector).start();
    }

    /**
     * Setting the current operation and telling file manager to create folder for the operation.
     *
     * @param operation the current operation.
     * @see FileManager
     */
    public void createOperation(Operation operation) {
        this.operation = operation;
        window.updateOperationInfo(operation);
        fileManager.setupOperationFolder(operation);
    }

    /**
     * Set the current operation and update the operation info.
     *
     * @param operation the operation to set.
     */
    public void setCurrentOperation(Operation operation) {
        this.operation = operation;
        window.updateOperationInfo(operation);
    }

    /**
     * Tell the FileManager to load existing operations from the file system.
     *
     * @return the list of current operations.
     * @see FileManager
     */
    public List<Operation> getExistingOperations() {
        if (existingOperations.isEmpty()) {
            List<Operation> operations = fileManager.loadExistingOperations();
            existingOperations.addAll(operations);
        }
        return existingOperations;
    }

    /**
     * Tell the window to add the existing operations to the selector in the user interface.
     *
     * @param operations the existing operations to add.
     * @see Window
     */
    public void addExistingOperations(List<Operation> operations) {
        window.addExistingOperations(operations);
    }

    /**
     * Getting files, checking for duplicates, processing files, saving files.
     *
     * @param gpsDrive The GPSDrive to check, with the appropriate folders.
     * @see GPSDrive
     */
    public void handleGPSDrive(GPSDrive gpsDrive) {
        setStatus("Koblet til.");
        File currentFolder = gpsDrive.getCurrentFolder();
        File archiveFolder = gpsDrive.getArchiveFolder();
        Set<File> gpxFiles = FileTools.findGpxFiles(archiveFolder);
        if (gpxFiles.isEmpty()) {
            System.err.println("No gpx files.");
            return;
        }
        for (File file : gpxFiles) {
            GPX gpx = TrackTools.parseFileAsGPX(file);
            if (!TrackTools.trackCreatedBeforeStartTime(gpx, operation.getStartTime())) {
                if (!fileManager.fileAlreadyImported(gpx)) {
                    fileManager.saveRawGpxFile(gpx, file.getName());
                    queue.add(file);
                } else {
                    System.err.println("File \"" + file.getName() + "\" has already been imported. Ignoring.");
                }
            } else {
                System.err.println("Track in file \"" + file.getName() + "\" was stopped before operation start time. Ignoring.");
            }
        }
        if (!queue.isEmpty()) {
            prepareNextFile();
        }
    }

    /**
     * Assigns a new file to the TrackCutter and updates the GUI panel.
     * This method is used when a gps is connected and one or more gpx-files are located.
     */
    public void prepareNextFile() {
        currentTrackCutter = new TrackCutter(this);
        File file = queue.remove(0);
        GPX gpx = TrackTools.parseFileAsGPX(file);
        currentTrackCutter.setTrackFile(gpx);
        window.updateCurrentFile(file.getName(), queue.size());
        window.openTrackPanel();
    }

    /**
     * Taking info about the currently imported track and starting the process.
     *
     * @param trackInfo info about the currently imported track.
     */
    public void initiateTrackCutter(TrackInfo trackInfo) {
        if (currentTrackCutter == null || currentTrackCutter.getTrackFile() == null) {
            Messages.ERROR_NO_TRACK_FOR_INFO.print();
            return;
        }
        currentTrackCutter.setTrackInfo(trackInfo);
        currentTrackCutter.process();
        GPX gpxFile = currentTrackCutter.getTrackFile();
        String newName = config.generateFilename(trackInfo);
        Track track = TrackTools.getTrackFromGPXFile(gpxFile);
        track.setName(newName);
        fileManager.saveProcessedGpxFile(gpxFile, newName);
        if (queue.isEmpty()) {
            window.openOperationPanel();
        } else {
            prepareNextFile();
        }
    }

    /**
     * Tell the Window to update the status about the current GPS connected.
     *
     * @param status the status message to display.
     * @see Window
     */
    public void setStatus(String status) {
        window.setStatus(status);
    }

    /**
     * Tell the FileManager to setup the folders on the computer for saving files later.
     *
     * @param listRoot the drive to make folders.
     * @see FileManager
     */
    public void setupLocalFolders(File listRoot) {
        fileManager.setupLocalFolders(listRoot);
    }

    /**
     * Get the current instance of the Config.
     *
     * @return the current instance of the Config.
     */
    public Config getConfig() {
        return config;
    }
}
