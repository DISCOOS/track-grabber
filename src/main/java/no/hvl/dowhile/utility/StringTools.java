package no.hvl.dowhile.utility;

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
}
