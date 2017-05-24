package no.hvl.dowhile.core;

import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.Waypoint;
import no.hvl.dowhile.core.drive.DriveDetector;
import no.hvl.dowhile.core.drive.GPSDrive;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;
import no.hvl.dowhile.utility.TrackTools;

import java.io.File;
import java.util.*;

/**
 * Handling communication between the components in the application.
 */
public class OperationManager {
    private boolean active;
    private Config config;
    private Window window;
    private DriveDetector driveDetector;
    private FileManager fileManager;
    private List<Operation> existingOperations;
    private TrackCutter currentTrackCutter;
    private List<GpxFile> queue;
    private int queueSize;
    private int queuePosition;
    private Operation operation;


    /**
     * Constructor creating the components needed from the beginning.
     */
    public OperationManager() {
        this.active = true;
        this.config = new Config();
        this.driveDetector = new DriveDetector(this);
        this.fileManager = new FileManager(this);
        this.existingOperations = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.queueSize = 0;
        this.queuePosition = 0;
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
     * Generate and show the window.
     */
    public void showWindow() {
        this.window = new Window(this);
    }

    /**
     * Perform actions required to be done before the program is shutdown.
     */
    public void stop() {
        removeUnprocessedFiles();
        window.close();
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
        fileManager.setupMainOperationFolder(operation);
        for (String path : operation.getExtraPaths()) {
            fileManager.setupExtraOperationFolder(operation, path);
        }
    }

    /**
     * Create folders to store files for the current operation.
     */
    public void updateOperationFolders() {
        fileManager.setupMainOperationFolder(operation);
        for (String path : operation.getExtraPaths()) {
            fileManager.setupExtraOperationFolder(operation, path);
        }
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
        operation.setStartTime(year, month, day, hour, minute);
        window.updateOperationInfo(operation);
        fileManager.updateOperationFile(operation);
    }

    /**
     * Update the operation file.
     */
    public void updateOperationFile() {
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
        for (File file : gpxFiles) {
            checkFile(file);
        }
        if (!queue.isEmpty() && window.isReadyToProcessFile()) {
            prepareNextFile();
        } else if (queue.isEmpty() && window.isReadyToProcessFile()) {
            window.showDialog(Messages.NO_RELEVANT_FILES_TITLE.get(), Messages.NO_RELEVANT_FILES_GPS_DESCRIPTION.get());
        }
    }

    /**
     * Getting the file, checking for duplicate, processing it and saving.
     *
     * @param file the file selected.
     */
    public void handleImportedFile(File file) {
        checkFile(file);
        if (!queue.isEmpty() && window.isReadyToProcessFile()) {
            prepareNextFile();
        } else if (queue.isEmpty() && window.isReadyToProcessFile()) {
            window.showDialog(Messages.NO_RELEVANT_FILES_TITLE.get(), Messages.NO_RELEVANT_FILES_IMPORT_DESCRIPTION.get());
        }
    }

    /**
     * Identifies a GPX file and does the appropriate processing based on its attributes.
     *
     * @param file the file to check.
     */
    public void checkFile(File file) {
        GPX gpx = TrackTools.getGpxFromFile(file);
        if (gpx == null) {
            System.err.println("Couldn't parse file. File " + file.getName() + " will not be processed.");
            return;
        }
        if (TrackTools.hasWaypoints(gpx)) {
            checkWaypoints(file);
            return;
        }
        Track track = TrackTools.getTrackFromGPXFile(gpx);
        if (track == null) {
            System.err.println("Couldn't find track. File " + file.getName() + " will not be processed.");
            return;
        }
        int allPointsCount = TrackTools.getAllTrackPoints(track).size();
        List<Waypoint> duplicatePoints = fileManager.alreadyImportedGpx(gpx);
        while (!duplicatePoints.isEmpty()) {
            if (!(duplicatePoints.size() == allPointsCount)) {
                TrackTools.removePoints(gpx, duplicatePoints);
                allPointsCount = TrackTools.getAllTrackPoints(track).size();
                duplicatePoints = fileManager.alreadyImportedGpx(gpx);
            } else {
                return;
            }
        }
        String rawfileHash = fileManager.saveRawGpxFileInFolders(gpx, file.getName());
        if (TrackTools.trackIsAnArea(gpx)) {
            fileManager.saveAreaGpxFileInFolders(gpx, file.getName());
        } else {
            if (!TrackTools.trackCreatedBeforeStartTime(gpx, operation.getStartTime())) {
                queue.add(new GpxFile(file, file.getName(), rawfileHash, gpx));
                queueSize++;
                window.updateQueueInfo(queueSize, queuePosition);
            } else {
                System.err.println("Track in file \"" + file.getName() + "\" was stopped before operation start time. Ignoring.");
            }
        }
    }

    /**
     * Takes a file containing waypoints and adds all of them to the queue.
     *
     * @param file The file containing the waypoints.
     */
    private void checkWaypoints(File file) {
        List<GPX> waypointsInGpx = TrackTools.splitWaypointGpx(file);
        for (int i = 0; i < waypointsInGpx.size(); i++) {
            GPX waypointGpx = waypointsInGpx.get(i);
            if (!TrackTools.waypointCreatedBeforeStartTime(waypointGpx, operation.getStartTime()) && !fileManager.alreadyImportedWaypoint(waypointGpx)) {
                String newRawFileName = StringTools.renameRawWaypointName(file.getName(), i);
                String hash = fileManager.saveRawGpxFileInFolders(waypointGpx, newRawFileName);
                queue.add(new GpxFile(file, newRawFileName, hash, waypointGpx));
                queueSize++;
                window.updateQueueInfo(queueSize, queuePosition);
            }
        }
    }

    /**
     * Assigns a new file to the TrackCutter and updates the GUI panel.
     * This method is used when a gps is connected and one or more gpx-files are located.
     */
    public void prepareNextFile() {
        queuePosition++;
        GpxFile gpxFile = queue.remove(0);
        currentTrackCutter = new TrackCutter(this);
        currentTrackCutter.setGpxFile(gpxFile);
        if (TrackTools.hasWaypoints(gpxFile.getGpx())) {
            Waypoint wp = gpxFile.getGpx().getWaypoints().iterator().next();
            Date wpDate = wp.getTime();
            String wpDateFormatted = StringTools.formatDateForFileProcessing(wpDate);
            String wpName = wp.getName();
            window.updateCurrentWaypointFile(wpDateFormatted, wpName, queueSize, queuePosition);
            window.openWaypointPanel();
        } else {
            double trackDistance = TrackTools.getDistanceFromTrack(gpxFile.getGpx());
            window.updateCurrentTrackFile(StringTools.startTimeAndEndTimeToString(gpxFile.getGpx()), queueSize, queuePosition, trackDistance);
            window.openTrackPanel();
        }
    }

    /**
     * Taking info about the currently imported track and starting the process.
     *
     * @param trackInfo info about the currently imported track.
     */
    public void processFile(TrackInfo trackInfo) {
        if (currentTrackCutter == null || currentTrackCutter.getGpxFile() == null) {
            Messages.ERROR_NO_TRACK_FOR_INFO.print();
            return;
        }
        currentTrackCutter.setTrackInfo(trackInfo);
        currentTrackCutter.process();
        GpxFile gpxFile = currentTrackCutter.getGpxFile();
        String newName = config.generateFilename(trackInfo);
        Track track = TrackTools.getTrackFromGPXFile(gpxFile.getGpx());
        track.setName(newName);
        if (!trackInfo.getComment().isEmpty()) {
            track.setDescription(trackInfo.getComment());
        }
        fileManager.saveProcessedGpxFileInFolders(gpxFile.getGpx(), trackInfo, newName);
        fileManager.saveTrackFileInfo(trackInfo, System.currentTimeMillis() + "", gpxFile.getFile().getName(), newName, gpxFile.getRawfileHash());
        checkForMoreFiles();
    }

    /**
     * This method is used for testing import of loads of files.
     * No user interaction is required to import the files.
     * The files you want to import need to be in a folder named "MassTesting" in the C://TrackGrabber folder.
     */
    public void importMassTestingFiles() {
        long startTime = System.currentTimeMillis();
        int filesImported = 0;
        File appFolder = fileManager.getAppFolder();
        if (appFolder == null || appFolder.listFiles() == null) {
            return;
        }
        File massTestingFolder = new File(appFolder, "MassTesting");
        File[] trackFiles = massTestingFolder.listFiles();
        if (trackFiles == null) {
            return;
        }
        Random random = new Random();
        for (File trackFile : trackFiles) {
            if (trackFile.getName().endsWith(".gpx")) {
                System.err.println("Importing " + trackFile.getName());
                GPX gpx = TrackTools.getGpxFromFile(trackFile);
                if (gpx == null) {
                    System.err.println("Failed to parse gpx.");

                } else if (TrackTools.hasWaypoints(gpx)) {
                    System.err.println("File is a waypoint file. Ignoring.");
                } else if (!TrackTools.fileHasTrack(gpx)) {
                    System.err.println("File doesn't have track.");
                } else if (fileManager.alreadyImportedGpx(gpx) != null) {
                    System.err.println("Already imported.");
                } else {
                    fileManager.saveRawGpxFileInFolders(gpx, trackFile.getName());
                    System.err.println("Saved raw file!");
                    if (TrackTools.trackIsAnArea(gpx)) {
                        fileManager.saveAreaGpxFileInFolders(gpx, trackFile.getName());
                        System.err.println("Saved area file!");
                    } else {
                        if (!TrackTools.trackCreatedBeforeStartTime(gpx, operation.getStartTime())) {
                            String crewType = config.getTeamTypes().get(random.nextInt(config.getTeamNames().size())).getName();
                            TrackInfo trackInfo = new TrackInfo(
                                    crewType, random.nextInt(40),
                                    random.nextInt(40),
                                    "[" + random.nextInt(40) + "]",
                                    TrackTools.getDistanceFromTrack(gpx), random.nextInt(40),
                                    "MassTesting"
                            );
                            String filename = config.generateFilename(trackInfo);
                            Track track = TrackTools.getTrackFromGPXFile(gpx);
                            track.setName(filename);
                            if (!trackInfo.getComment().isEmpty()) {
                                track.setDescription(trackInfo.getComment());
                            }
                            fileManager.saveProcessedGpxFileInFolders(gpx, trackInfo, filename);
                            filesImported++;
                            System.err.println("Saved processed!");
                        } else {
                            System.err.println("Track in file \"" + trackFile.getName() + "\" was stopped before operation start time. Ignoring.");
                        }
                    }
                }
            }
        }
        long stopTime = System.currentTimeMillis();
        System.err.println("Successfully imported " + filesImported + " files in " + (stopTime - startTime) / 1000 + " seconds.");
    }

    /**
     * Assigns a name to the waypoint.
     *
     * @param name        the name for the file.
     * @param description the description provided by the user.
     */
    public void saveWaypoint(String name, String description) {
        if (currentTrackCutter == null || currentTrackCutter.getGpxFile() == null) {
            Messages.ERROR_NO_TRACK_FOR_INFO.print();
            return;
        }
        GpxFile gpxFile = currentTrackCutter.getGpxFile();
        GPX gpx = gpxFile.getGpx();
        if (!name.isEmpty()) {
            gpx.getWaypoints().iterator().next().setName(name);
        }
        if (!description.isEmpty()) {
            gpx.getWaypoints().iterator().next().setDescription(description);
        }
        fileManager.saveWaypointFileInFolders(gpx, name.trim().replace(" ", "_") + ".gpx");
        fileManager.saveWaypointFileInfo(description, gpxFile.getRawFileName() + ".gpx", name.trim().replace(" ", "_") + ".gpx", gpxFile.getRawfileHash());
        checkForMoreFiles();
    }

    /**
     * Opens the operation panel if the queue is empty, and if not, prepares the next file.
     */
    public void checkForMoreFiles() {
        if (queue.isEmpty()) {
            currentTrackCutter = null;
            window.openStandByPanel();
            queueSize = 0;
            queuePosition = 0;
        } else {
            prepareNextFile();
        }
    }

    /**
     * This method will remove the files from the raw folder as they are not yet processed when in the queue.
     */
    public void removeUnprocessedFiles() {
        for (GpxFile gpxFile : queue) {
            fileManager.deleteRawFileInFolders(gpxFile.getFile().getName());
        }
        if (currentTrackCutter != null && currentTrackCutter.getGpxFile() != null) {
            fileManager.deleteRawFileInFolders(currentTrackCutter.getGpxFile().getFile().getName());
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
     * Sets the window.
     * @param window a window.
     */
    public void setWindow(Window window) {
        this.window = window;
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
}
