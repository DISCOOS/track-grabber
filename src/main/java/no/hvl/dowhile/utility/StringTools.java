package no.hvl.dowhile.utility;

import org.alternativevision.gpx.beans.GPX;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility methods for formatting objects into strings.
 */
public class StringTools {
    /**
     * Formatting a Date into a string.
     *
     * @param date the date to format.
     * @return the date formatted as a String.
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM/yyyy HH:mm z").format(date);
    }

    /**
     * Formatting a Date into a string without spaces.
     *
     * @param date the date to format.
     * @return the date formatted as a String without spaces.
     */
    public static String formatDateForFile(Date date) {
        return new SimpleDateFormat("dd-MM-yy-HH-mm-ss").format(date);
    }

    /**
     * Formatting a Date into a string without spaces, hours, minutes and seconds.
     *
     * @param date the date to format.
     * @return the date formatted as a String without spaces, hours, minutes and seconds.
     */
    public static String formatDateForOrganizing(Date date) {
        return new SimpleDateFormat("dd-MM-yy").format(date);
    }

    /**
     * Formatting a Date into a String without year.
     * @param date the date to format.
     * @return the date formatted as a String without year.
     */
    public static String formatDateForFileProcessing(Date date) { return new SimpleDateFormat("dd-MM HH:mm z").format(date); }

    /**
     * Checks if a given name is a valid operation name (contains only letters and/or numbers).
     *
     * @param operationName the name to check.
     * @return true if the name is valid, false if not.
     */
    public static boolean isValidOperationName(String operationName) {
        if (operationName == null || operationName.isEmpty()) {
            return false;
        }
        for (char c : operationName.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isDigit(c) && !(c == ' ')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given operation name is between 2 and 50 characters.
     *
     * @param operationName The operation name to check.
     * @return True if the name has a valid length, false if not.
     */
    public static boolean operationNameLengthIsValid(String operationName) {
        return operationName.length() >= 2 && operationName.length() <= 50;
    }

    /**
     * Checks if a given file name contains a given string.
     *
     * @param string   The string to search for.
     * @param filename The filename to search through.
     * @return True if the filename contains the given string, false if not.
     */
    public static boolean FilenameContainsString(String string, String filename) {
        return filename.contains(string);
    }

    /**
     * Gets the track's start time and end time as one single String.
     * @param gpx The file to get the times from.
     * @return The start time and end time.
     */
    public static String startTimeAndEndTimeToString(GPX gpx) {
        return Messages.TRACK_START.get() + TrackTools.getStartTimeFromTrack(gpx) + ", " + Messages.TRACK_END.get() + TrackTools.getEndTimeFromTrack(gpx);
    }

}
