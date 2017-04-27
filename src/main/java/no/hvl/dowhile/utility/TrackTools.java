package no.hvl.dowhile.utility;

import no.hvl.dowhile.core.parser.DisplayColorExtensionParser;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashSet;

/**
 * Utility methods to work with GPX files and tracks.
 */
public class TrackTools {
    /**
     * Takes a GPX file and returns its track.
     *
     * @param gpx the gpx file to check.
     * @return track from file.
     */
    public static Track getTrackFromGPXFile(GPX gpx) {
        HashSet<Track> tracks = gpx.getTracks();
        if (tracks != null) {
            return tracks.iterator().next();
        }
        return null;
    }

    /**
     * Checks if the track is older the operation, and therefore is irrelevant.
     *
     * @param gpx                the gpx to import.
     * @param operationStartTime the start time of the current operation.
     * @return true if the track was stopped before the operation, false if not.
     */
    public static boolean trackCreatedBeforeStartTime(GPX gpx, Date operationStartTime) {
        Track track = getTrackFromGPXFile(gpx);
        if (track == null) {
            return false;
        }
        Waypoint lastPoint = track.getTrackPoints().get(track.getTrackPoints().size() - 1);
        if (lastPoint == null) {
            return false;
        }
        Date pointDate = lastPoint.getTime();
        return (pointDate.getTime() < operationStartTime.getTime());
    }

    /**
     * Parsing a file and gives it back as GPX.
     *
     * @param file the file to parse.
     * @return file as GPX.
     */
    public static GPX getGpxFromFile(File file) {
        GPX gpx = null;
        GPXParser gpxParser = new GPXParser();
        DisplayColorExtensionParser colorParser = new DisplayColorExtensionParser();
        gpxParser.addExtensionParser(colorParser);
        try {
            gpx = gpxParser.parseGPX(new FileInputStream(file));
        } catch (Exception ex) {
            System.err.println("File not found or something.");
            ex.printStackTrace();
        }
        return gpx;
    }

    /**
     * Comparing latitude, longitude and elevation.
     *
     * @param waypoint1 waypoint to check.
     * @param waypoint2 waypoint to compare with.
     * @return true if the points are matching, false if not.
     */
    public static boolean matchingTrackPoints(Waypoint waypoint1, Waypoint waypoint2) {
        return waypoint1.getLatitude().equals(waypoint2.getLatitude()) && waypoint1.getLongitude().equals(waypoint2.getLongitude()) && waypoint1.getElevation().equals(waypoint2.getElevation());
    }
}