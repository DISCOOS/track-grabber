package no.hvl.dowhile.core;

import org.alternativevision.gpx.beans.GPX;

/**
 * Processing a GPX file. Removing unnecessary data.
 */
public class TrackCutter {
    private GPX track;
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
     * Removes all track points that were created outside of the given time span
     */
    private void filterOnTimeSpan() {

    }

    public GPX getTrack() {
        return track;
    }

    public void setTrack(GPX track) {
        this.track = track;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }
}
