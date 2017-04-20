package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.StringTools;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Represents the current operation and has information such as name and start time.
 */
public class Operation {
    private String name;
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
    public Operation(String name, int day, int month, int year, int hour, int minute) {
        this.name = name;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
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
                        name = nameAndValue[1];
                    } else {
                        throw new Exception("Failed to parse name from file.");
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
     * Get the start time of the operation.
     *
     * @return the start time of the operation.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Writing information about this operation to the file.
     *
     * @param file the file to write the data to.
     */
    public void writeToFile(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("# Operasjon " + name + System.lineSeparator());
            fileWriter.write("# Starttid: " + StringTools.formatDate(startTime) + System.lineSeparator());
            fileWriter.write("# Du kan ikke endre på dataen her. Det må gjøres i programmet." + System.lineSeparator());
            fileWriter.write("name=" + name.trim().replace(" ", "_") + System.lineSeparator());
            fileWriter.write("starttime=" + startTime.getTime());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            System.err.println("Failed while writing operation to file.");
        }
    }
}
