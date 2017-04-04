package no.hvl.dowhile.core.drive;

/**
 * Storing info about a drive connected to system.
 */
public class Drive {
    private String driveLetter;
    private String path;

    /**
     * Default constructor taking the letter referencing this drive and the absolute path.
     *
     * @param driveLetter the letter referencing this drive.
     * @param path        the absolute path to the drive.
     */
    public Drive(String driveLetter, String path) {
        this.driveLetter = driveLetter;
        this.path = path;
    }

    /**
     * Gives to the letter referencing this drive.
     *
     * @return the letter referencing this drive.
     */
    public String getDriveLetter() {
        return driveLetter;
    }

    /**
     * Gives you the absolute path to this drive.
     *
     * @return the absolute path to this drive.
     */
    public String getPath() {
        return path;
    }
}
