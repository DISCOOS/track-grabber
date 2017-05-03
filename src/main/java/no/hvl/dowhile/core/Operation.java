package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.StringTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Represents the current operation and has information such as name and start time.
 */
public class Operation {
    private String name;
    private int numberOfAreas;
    private Date startTime;

    /**
     * Constructor taking the information needed to create the operation.
     *
     * @param name   the name of the operation.
     * @param day    the day the operation started.
     * @param month  the month the operation started.
     * @param year   the year the operation started.
     * @param hour   the hour the operation started.
     * @param minute the minute the operation started.
     */
    public Operation(String name, int numberOfAreas, int day, int month, int year, int hour, int minute) {
        this.name = name;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        this.startTime = calendar.getTime();
    }

    /**
     * Creating an operation by parsing the data from the given file.
     *
     * @param file the file to parse.
     * @throws Exception if the file doesn't have the required data.
     */
    public Operation(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            if (!line.startsWith("#")) {
                if (line.startsWith("name")) {
                    String[] nameAndValue = line.split("=");
                    if (nameAndValue.length == 2) {
                        name = nameAndValue[1].replace("_", " ");
                    } else {
                        throw new Exception("Failed to parse name from file.");
                    }
                } else if (line.startsWith("numberOfAreas")) {
                    String[] numberOfAreasAndValue = line.split("=");
                    if (numberOfAreasAndValue.length == 2) {
                        numberOfAreas = Integer.parseInt(numberOfAreasAndValue[1]);
                    } else {
                        throw new Exception("Failed to parse number of areas from file.");
                    }
                } else if (line.startsWith("starttime")) {
                    String[] startTimeAndValue = line.split("=");
                    if (startTimeAndValue.length == 2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(startTimeAndValue[1]));
                        startTime = calendar.getTime();
                    } else {
                        throw new Exception("Failed to parse time from file.");
                    }
                }
            }
            line = reader.readLine();
        }
    }

    /**
     * Get the name of the operation.
     *
     * @return the name of the operation.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of areas for this operation.
     *
     * @return number of areas for this operation.
     */
    public int getNumberOfAreas() {
        return numberOfAreas;
    }

    /**
     * Set the number of areas for this operation.
     *
     * @param numberOfAreas number of areas for this operation.
     */
    public void setNumberOfAreas(int numberOfAreas) {
        this.numberOfAreas = numberOfAreas;
    }

    /**
     * Get the start time of the operation.
     *
     * @return the start time of the operation.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Updating the start time of the operation.
     *
     * @param year   the year it started.
     * @param month  the month it started.
     * @param day    the day it started.
     * @param hour   the hour it started.
     * @param minute the minute it started.
     */
    public void setStartTime(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        this.startTime = calendar.getTime();
    }

    /**
     * Get the content to be saved in the file representing this operation.
     */
    public String[] getFileContent() {
        return new String[]{
                "# Operasjon " + name,
                "# Antall teiger: " + numberOfAreas,
                "# Starttid: " + StringTools.formatDate(startTime),
                "# Du kan ikke endre på dataen her. Det må gjøres i programmet.",
                "name=" + name.trim().replace(" ", "_"),
                "numberOfAreas=" + numberOfAreas,
                "starttime=" + startTime.getTime(),
        };
    }
}
