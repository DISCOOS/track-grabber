package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Processing a GPX file. Removing unnecessary data.
 */
public class TrackCutter {
    private GPX trackFile;
    private TrackInfo trackInfo;

    /**
     * Default constructor taking the track to be processed and info about it.
     */
    public TrackCutter() {
    }

    /**
     * Processing the file to remove unnecessary data.
     */
    public void process() {

    }

    /**
     * Determines a maximum latitude and longitude, and uses to create a radius
     */
    public void setRadius() {

    }

    /**
     * Sets a time span for the track
     */
    private void setTimeSpan() {

    }

    /**
     * Removes all track points that are outside of the given radius
     */
    private void filterOnRadius() {

    }

    /**
     * Removes all track points that were created before a given time
     */
    public GPX filterOnTimeStarted(Calendar startTime) {
        Track track = TrackTools.getTrackFromGPXFile(trackFile);
        ArrayList<Waypoint> trackPts = track.getTrackPoints();
        long startTimeMillis = startTime.getTimeInMillis();

        System.out.println("Point count before cutting: " + trackPts.size());

        for(int i = 0; i < trackPts.size(); i++) {
            long pointTimeMillis = trackPts.get(i).getTime().getTime();
            if(pointTimeMillis < startTimeMillis) {
                trackPts.remove(i);
            }
        }

        track.setTrackPoints(trackPts); // Is this necessary?
        System.out.println("Point count after cutting: " + trackPts.size());

        return trackFile;
    }

    public GPX getTrackFile() {
        return trackFile;
    }

    public void setTrackFile(GPX track) {
        this.trackFile = trackFile;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }
}
