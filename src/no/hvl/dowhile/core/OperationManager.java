package no.hvl.dowhile.core;

import no.hvl.dowhile.core.drive.DriveDetector;
import no.hvl.dowhile.core.drive.GPSDrive;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.TrackPoint;
import org.alternativevision.gpx.beans.Waypoint;

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

    public OperationManager() {
        this.active = true;

        // TODO: Remove this time. Just for testing.
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, Calendar.MARCH, 22, 15, 15, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        this.operationStartTime = calendar.getTime();

        this.window = new Window(this);
        this.driveDetector = new DriveDetector(this);
        this.fileManager = new FileManager(this);
        this.config = new Config();
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
        File file = gpxFiles.iterator().next();
        GPX gpx = TrackTools.parseFileAsGPX(file);
        fileManager.saveRawGpxFile(gpx, file.getName());
        currentTrackCutter = new TrackCutter();
        currentTrackCutter.setTrackFile(gpx);
        window.openTrackPanel();
    }

    /**
     * Taking info about the currently imported track and starting the process.
     *
     * @param trackInfo info about the currently imported track.
     */
    public void initiateTrackCutter(TrackInfo trackInfo) {
        if (currentTrackCutter == null) {
            Messages.ERROR_NO_TRACK_FOR_INFO.print();
            return;
        }
        currentTrackCutter.setTrackInfo(trackInfo);
        currentTrackCutter.process(this);
        GPX gpxFile = currentTrackCutter.getTrackFile();
        TrackInfo info = currentTrackCutter.getTrackInfo();
        String newName = config.generateFilename(trackInfo);
        Track track = TrackTools.getTrackFromGPXFile(gpxFile);
        track.setName(newName);
        fileManager.saveProcessedGpxFile(gpxFile, newName);
    }

    /**
     * Takes a file and messes it up. NB: WILL BE REMOVED
     *
     * @param file the file to operate on.
     */
    public GPX addPointsToGPXFile(File file) {

        GPX gpxFile = TrackTools.parseFileAsGPX(file);
        Track track = TrackTools.getTrackFromGPXFile(gpxFile);
        ArrayList<Waypoint> trkPts = track.getTrackPoints();

        // Do the addin'

        TrackPoint tp1 = new TrackPoint();
        TrackPoint tp2 = new TrackPoint();
        TrackPoint tp3 = new TrackPoint();

        // Set values to new track points
        tp1.setLatitude(45.34);
        tp1.setLongitude(23.98);
        tp2.setLatitude(5.74);
        tp2.setLongitude(7.28);
        tp3.setLatitude(83.15);
        tp3.setLongitude(48.8);

        // Add new tracks AKA do the messing
        trkPts.add(tp1);
        trkPts.add(tp2);
        trkPts.add(tp3);
        track.setTrackPoints(trkPts);

        return gpxFile;
    }

    /**
     * Takes a file and cuts off track points that are outside a certain area.
     * Any useful code should be moved to TrackCutter.java.
     *
     * @param file
     * @return gpxFile
     */
    public GPX cutPointsFromGPXFile(File file) {

        GPX gpxFile = TrackTools.parseFileAsGPX(file);
        Track track = TrackTools.getTrackFromGPXFile(gpxFile);
        ArrayList<Waypoint> trackPts = track.getTrackPoints();

        System.out.println("Point count before cutting: " + trackPts.size());

        // Do the cuttin'
        if (trackPts.size() > 200) {
            System.out.println("Cutting...");
            for (int i = 100; i < 176; i++) {
                trackPts.remove(i);
            }
        }
        track.setTrackPoints(trackPts); // Is this necessary?
        System.out.println("Point count after cutting: " + trackPts.size());

        return gpxFile;
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
