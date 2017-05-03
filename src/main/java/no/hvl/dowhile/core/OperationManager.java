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
    private List<GpxFile> queue;

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

    /**
     * Gives you the current operation.
     *
     * @return the current operation.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Method to check whether an operation is set or not.
     *
     * @return true if an operation is set, false if not.
     */
    public boolean hasOperation() {
        return operation != null;
    }

    /**
     * Starts listening for new drives connected.
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
    public void setupOperation(Operation operation) {
        this.operation = operation;
        window.updateOperationInfo(operation);
        fileManager.setupOperationFolder(operation);
    }

    /**
     * Set the date and time of the current operation and updating info in window and the file.
     *
     * @param year   the year it started.
     * @param month  the month it started.
     * @param day    the day it started.
     * @param hour   the hour it started.
     * @param minute the minute it started.
     */
    public void updateCurrentOperation(int year, int month, int day, int hour, int minute) {
        operation.updateStartTime(year, month, day, hour, minute);
        window.updateOperationInfo(operation);
        fileManager.updateOperationFile(operation);
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
     * Checking the file system to load all operations.
     */
    public void reloadExistingOperations() {
        existingOperations = fileManager.loadExistingOperations();
    }

    /**
     * Tell the window to add the existing operations to the selector in the user interface.
     *
     * @param operations the existing operations to show.
     * @see Window
     */
    public void showExistingOperations(List<Operation> operations) {
        window.showExistingOperations(operations);
    }

    /**
     * Getting files, checking for duplicates, processing files, saving files.
     *
     * @param gpsDrive The GPSDrive to check, with the appropriate folders.
     * @see GPSDrive
     */
    public void handleGPSDrive(GPSDrive gpsDrive) {
        File gpxFolder = gpsDrive.getGpxFolder();
        Set<File> gpxFiles = FileTools.findGpxFiles(gpxFolder);
        if (gpxFiles.isEmpty()) {
            System.err.println("No gpx files.");
            return;
        }
        for (File file : gpxFiles) {
            processFile(file);
        }
        if (!queue.isEmpty()) {
            prepareNextFile();
        } else {
            window.showDialog(Messages.NO_RELEVANT_FILES_FOUND.get());
        }
    }

    /**
     * Getting the file, checking for duplicate, processing it and saving.
     *
     * @param file the file selected.
     */
    public void handleImportedFile(File file) {
        processFile(file);
        if (!queue.isEmpty()) {
            prepareNextFile();
        } else {
            window.showDialog(Messages.NO_RELEVANT_FILES_FOUND.get());
        }
    }

    /**
     * Processes a single GPX file.
     *
     * @param file the file to process.
     */
    public void processFile(File file) {
        GPX gpx = TrackTools.getGpxFromFile(file);
        if (gpx == null) {
            System.err.println("Couldn't parse file. File " + file.getName() + " will not be processed.");
            return;
        }
        if (!TrackTools.fileHasTrack(gpx)) {
            System.err.println("Couldn't find track. File " + file.getName() + " will not be processed.");
            return;
        }
        if (!TrackTools.trackCreatedBeforeStartTime(gpx, operation.getStartTime())) {
            if (!fileManager.fileAlreadyImported(gpx)) {
                fileManager.saveRawGpxFile(gpx, file.getName());
                queue.add(new GpxFile(file.getName(), gpx));
            } else {
                System.err.println("File \"" + file.getName() + "\" has already been imported. Ignoring.");
            }
        } else {
            System.err.println("Track in file \"" + file.getName() + "\" was stopped before operation start time. Ignoring.");
        }
    }

    /**
     * Assigns a new file to the TrackCutter and updates the GUI panel.
     * This method is used when a gps is connected and one or more gpx-files are located.
     */
    public void prepareNextFile() {
        currentTrackCutter = new TrackCutter(this);
        GpxFile gpxFile = queue.remove(0);
        currentTrackCutter.setTrackFile(gpxFile.getGpx());
        window.updateCurrentFile(gpxFile.getFilename(), queue.size());
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
     * Checks if a given operation name is already taken by another operation.
     *
     * @param name the name of the new operation.
     * @return true if the names are equal, false if not.
     */
    public boolean operationNameAlreadyExists(String name) {
        boolean alreadyExists = false;
        String nameCopy = "";
        for (Operation op : existingOperations) {
            nameCopy = op.getName().replace("_", " ");
            if (name.equals(nameCopy)) {
                alreadyExists = true;
            }
        }
        return alreadyExists;
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

    /**
     * Gets the file manager.
     *
     * @return the file manager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Sets the file manager.
     *
     * @param fileManager a file manager
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * Gets the file queue.
     *
     * @return the file queue
     */
    public List<GpxFile> getQueue() {
        return queue;
    }

    /**
     * Gets the track cutter for the current track.
     *
     * @return the track cutter for the current track.
     */
    public TrackCutter getCurrentTrackCutter() {
        return currentTrackCutter;
    }
}
