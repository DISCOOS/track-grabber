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

    GPSDrive(String driveLetter) {
        super(driveLetter);
    }

    /**
     * Checks if folders exist and if they have the correct name.
     *
     * @return true if the folders are matching a GPS, false if not.
     */
    boolean isValidGPSDrive() {
        return garminFolder != null && gpxFolder != null && garminFolder.getName().equals("Garmin") && gpxFolder.getName().equals("GPX");
    }

    /**
     * Checks if the Garmin folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    boolean hasGarminFolder() {
        return garminFolder != null && garminFolder.getName().equals("Garmin");
    }

    /**
     * Checks if the GPX folder has been set.
     *
     * @return true if the folder has been set, false if not.
     */
    boolean hasGpxFolder() {
        return gpxFolder != null && gpxFolder.getName().equals("GPX");
    }

    /**
     * Gives you the Garmin folder.
     * You should check for null. Class has method to check.
     *
     * @return File representing the Garmin folder.
     * @see #hasGarminFolder()
     */
    File getGarminFolder() {
        return garminFolder;
    }

    /**
     * Assigns a file as the Garmin folder.
     *
     * @param garminFolder the file to set as Garmin folder.
     */
    void setGarminFolder(File garminFolder) {
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
     * Assigns a file as the GPX folder.
     *
     * @param gpxFolder the file to set as GPX folder.
     */
    void setGpxFolder(File gpxFolder) {
        this.gpxFolder = gpxFolder;
    }
}
