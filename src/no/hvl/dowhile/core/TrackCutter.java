package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.Date;

/**
 * Processing a GPX file. Removing unnecessary data.
 */
public class TrackCutter {
    private final OperationManager OPERATION_MANAGER;
    private GPX trackFile;
    private TrackInfo trackInfo;

    /**
     * Default constructor taking the track to be processed and info about it.
     */
    public TrackCutter(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
    }

    /**
     * Processing the file to remove unnecessary data.
     */
    public void process() {
        trackFile = filterOnTimeStarted(OPERATION_MANAGER.getOperation().getStartTime());
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
    public GPX filterOnTimeStarted(Date startTime) {
        Track track = TrackTools.getTrackFromGPXFile(trackFile);
        ArrayList<Waypoint> trackPoints = track.getTrackPoints();
        ArrayList<Waypoint> pointsToRemove = new ArrayList<>();
        long startTimeMillis = startTime.getTime();

        for (Waypoint waypoint : trackPoints) {
            long pointTimeMillis = waypoint.getTime().getTime();
            if (pointTimeMillis < startTimeMillis) {
                pointsToRemove.add(waypoint);
            }
        }
        trackPoints.removeAll(pointsToRemove);
        track.setTrackPoints(trackPoints);
        return trackFile;
    }

    public GPX getTrackFile() {
        return trackFile;
    }

    public void setTrackFile(GPX trackFile) {
        this.trackFile = trackFile;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }
}
