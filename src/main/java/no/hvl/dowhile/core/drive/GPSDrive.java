package no.hvl.dowhile.core.drive;

import java.io.File;

/**
 * This class is used by the DriveDetector while checking if a drive belongs to a GPS.
 *
 * @see DriveDetector
 */
public class GPSDrive extends Drive {
    private File garminFolder;
    private File gpxFolder;
    private File currentFolder;
    private File archiveFolder;

    public GPSDrive(String driveLetter) {
        super(driveLetter);
    }

    /**
     * Checking if folders exists and if they have the correct name.
     *
     * @return true if the folders are matching a GPS, false if not.
     */
    public boolean isValidGPSDrive() {
        return garminFolder != null && gpxFolder != null && currentFolder != null && archiveFolder != null && garminFolder.getName().equals("Garmin") && gpxFolder.getName().equals("GPX") && currentFolder.getName().equals("Current") && archiveFolder.getName().equals("Archive");
    }

    /**
     * Checking if the Garmin folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    public boolean hasGarminFolder() {
        return garminFolder != null && garminFolder.getName().equals("Garmin");
    }

    /**
     * Checking if the GPX folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    public boolean hasGpxFolder() {
        return gpxFolder != null && gpxFolder.getName().equals("GPX");
    }

    /**
     * Checking if the Current folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    public boolean hasCurrentFolder() {
        return currentFolder != null && currentFolder.getName().equals("Current");
    }

    /**
     * Checking if the Archive folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    public boolean hasArchiveFolder() {
        return archiveFolder != null && archiveFolder.getName().equals("Archive");
    }

    /**
     * Gives you the Garmin folder.
     * You should check for null. Class has method to check.
     *
     * @return File representing the Garmin folder.
     * @see #hasGarminFolder()
     */
    public File getGarminFolder() {
        return garminFolder;
    }

    /**
     * Assign a file as the Garmin folder.
     *
     * @param garminFolder the file to set as Garmin folder.
     */
    public void setGarminFolder(File garminFolder) {
        this.garminFolder = garminFolder;
    }

    /**
     * Gives you the GPX folder.
     * You should check for null. Class has method to check.
     *
     * @return File representing the GPX folder.
     * @see #hasGpxFolder()
     */
    public File getGpxFolder() {
        return gpxFolder;
    }

    /**
     * Assign a file as the GPX folder.
     *
     * @param gpxFolder the file to set as GPX folder.
     */
    public void setGpxFolder(File gpxFolder) {
        this.gpxFolder = gpxFolder;
    }

    public File getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(File currentFolder) {
        this.currentFolder = currentFolder;
    }

    public File getArchiveFolder() {
        return archiveFolder;
    }

    public void setArchiveFolder(File archiveFolder) {
        this.archiveFolder = archiveFolder;
    }
}
