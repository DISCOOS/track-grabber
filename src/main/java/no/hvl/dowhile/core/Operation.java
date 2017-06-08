package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.StringTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Represents the current operation and has information such as name and start time.
 */
public class Operation {
    private String name;
    private Date startTime;
    private String mainPath;
    private List<String> extraPaths;

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
        calendar.set(year, month - 1, day, hour, minute);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        this.startTime = calendar.getTime();
        this.mainPath = "";
        this.extraPaths = new ArrayList<>();
    }

    /**
     * Creates an operation by parsing the data from the given file.
     *
     * @param file the file to parse.
     * @throws Exception if the file doesn't have the required data.
     */
    public Operation(File file) throws Exception {
        this.extraPaths = new ArrayList<>();
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
                } else if (line.startsWith("starttime")) {
                    String[] startTimeAndValue = line.split("=");
                    if (startTimeAndValue.length == 2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(startTimeAndValue[1]));
                        startTime = calendar.getTime();
                    } else {
                        throw new Exception("Failed to parse time from file.");
                    }
                } else if (line.startsWith("main-path")) {
                    String[] pathAndValue = line.split("=");
                    if (pathAndValue.length == 2) {
                        mainPath = pathAndValue[1];
                    } else {
                        throw new Exception("Failed to parse main path from file.");
                    }
                } else if (line.startsWith("extra-path")) {
                    String[] pathAndValue = line.split("=");
                    if (pathAndValue.length == 2) {
                        extraPaths.add(pathAndValue[1]);
                    } else {
                        throw new Exception("Failed to parse extra path from file.");
                    }
                }
            }
            line = reader.readLine();
        }
    }

    /**
     * Gets the name of the operation.
     *
     * @return the name of the operation.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the start time of the operation.
     *
     * @return the start time of the operation.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Updates the start time of the operation.
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
     * Sets the main path to save files for this operation.
     *
     * @param mainPath the main path to save files.
     */
    void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }

    /**
     * Gets the paths for this operation.
     *
     * @return list of paths.
     */
    List<String> getExtraPaths() {
        return extraPaths;
    }

    /**
     * Adds a path to store files to this operation.
     *
     * @param path the path to add.
     */
    public void addPath(String path) {
        extraPaths.add(path);
    }

    /**
     * Gets the paths as a string for display in the GUI.
     *
     * @return the paths as a string.
     */
    public String pathsToString() {
        StringBuilder allPaths = new StringBuilder("<html>");
        allPaths.append(mainPath).append("<br>");
        for (String extraPath : extraPaths) {
            allPaths.append(extraPath).append("<br>");
        }
        allPaths.append("</html>");
        return allPaths.toString();
    }

    /**
     * Gets the content to be saved in the file representing this operation.
     *
     * @return array of the strings to store in the file.
     */
    String[] getFileContent() {
        List<String> lines = new ArrayList<>();
        lines.add("# Operasjon " + name);
        lines.add("# Starttid: " + StringTools.formatDate(startTime));
        lines.add("# Du kan ikke endre på dataen her. Det må gjøres i programmet.");
        lines.add("name=" + name.trim().replace(" ", "_"));
        lines.add("starttime=" + startTime.getTime());
        lines.add("main-path=" + mainPath);
        for (String path : extraPaths) {
            lines.add("extra-path=" + path);
        }
        return lines.toArray(new String[lines.size()]);
    }
}
