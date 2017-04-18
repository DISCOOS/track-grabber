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
import java.util.*;

/**
 * Handling communication between the components in the application.
 */
public class OperationManager {
    private boolean active;
    private Date operationStartTime;
    private Window window;
    private DriveDetector driveDetector;
    private FileManager fileManager;
    private Config config;
    private TrackCutter currentTrackCutter;
    private List<File> queue;

    public OperationManager() {
        this.active = true;

        // TODO: Remove this time. Just for testing.
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.MARCH, 22, 14, 20, 30);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        this.operationStartTime = calendar.getTime();

        this.window = new Window(this);
        this.driveDetector = new DriveDetector(this);
        this.fileManager = new FileManager(this);
        this.config = new Config();
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
     * Get the time for the operation start. The operation start time is used to ignore old tracks etc.
     */
    public Date getOperationStartTime() {
        return operationStartTime;
    }

    /**
     * Set the time for the operation start. The operation start time is used to ignore old tracks etc.
     */
    public void setOperationStartTime(Date operationStartTime) {
        this.operationStartTime = operationStartTime;
    }

    /**
     * Start components which needs to perform operations from the beginning.
     */
    public void start() {
        window.open();
        new Thread(driveDetector).start();
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
            if (!fileManager.fileAlreadyImported(gpx, file.getName())) {
                fileManager.saveRawGpxFile(gpx, file.getName());
                queue.add(file);
            } else {
                System.err.println("File \"" + file.getName() + "\" has already been imported. Ignoring.");
            }
        }
        if(!queue.isEmpty()) {
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
