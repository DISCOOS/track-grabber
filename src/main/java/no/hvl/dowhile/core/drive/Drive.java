package no.hvl.dowhile.core.drive;

/**
 * Stores info about a drive connected to the system.
 */
public class Drive {
    private String driveLetter;

    /**
     * Default constructor taking the letter referencing this drive and the absolute path.
     *
     * @param driveLetter the letter referencing this drive.
     */
    public Drive(String driveLetter) {
        this.driveLetter = driveLetter;
    }

    /**
     * Gives the letter referencing this drive.
     *
     * @return the letter referencing this drive.
     */
    public String getDriveLetter() {
        return driveLetter;
    }
}
