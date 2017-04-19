package no.hvl.dowhile.core;

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
}
