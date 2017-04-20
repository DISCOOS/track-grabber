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
     * Replaces the track in a GPX file with a new one
     *
     * @param gpx
     * @param newTrack
     */
    public static GPX replaceTrack(GPX gpx, Track newTrack) {
        HashSet<Track> tracks = new HashSet<>();
        tracks.add(newTrack);
        gpx.getTracks().clear();
        gpx.setTracks(tracks);
        return gpx;
    }

    /**
     * Checks if the track is older the operation, and therefore is irrelevant.
     *
     * @param gpx                the gpx to import.
     * @param operationStartTime the start time of the current operation.
     * @return true if the track was stopped before the operation, false if not.
     */
    public static boolean trackCreatedBeforeStartTime(GPX gpx, Date operationStartTime) {
        Track track = TrackTools.getTrackFromGPXFile(gpx);
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
    public static GPX parseFileAsGPX(File file) {
        GPX gpxVersion = null;
        GPXParser gpxParser = new GPXParser();
        DisplayColorExtensionParser colorParser = new DisplayColorExtensionParser();
        gpxParser.addExtensionParser(colorParser);
        try {
            gpxVersion = gpxParser.parseGPX(new FileInputStream(file));
        } catch (Exception ex) {
            System.err.println("File not found or something.");
            ex.printStackTrace();
        }
        return gpxVersion;
    }

    /**
     * Comparing latitude, longitude and elevation.
     *
     * @param waypoint1 waypoint to check.
     * @param waypoint2 waypoint to compare with.
     * @return true if the points are matching, false if not.
     */
    public static boolean matchingTrackPoints(Waypoint waypoint1, Waypoint waypoint2) {
        if (!waypoint1.getLatitude().equals(waypoint2.getLatitude())) {
            return false;
        }
        if (!waypoint1.getLongitude().equals(waypoint2.getLongitude())) {
            return false;
        }
        if (!waypoint1.getElevation().equals(waypoint2.getElevation())) {
            return false;
        }
        return true;
    }
}
