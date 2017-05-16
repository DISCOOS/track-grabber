package no.hvl.dowhile.utility;

import com.hs.gpxparser.GPXParser;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Utility methods to work with GPX files and tracks.
 */
public class TrackTools {
    private Track rawTrack;

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
     * Checks if the given file has a track or not.
     *
     * @param gpx the gpx to check.
     * @return true if the file has a track, false if not.
     */
    public static boolean fileHasTrack(GPX gpx) {
        Track track = getTrackFromGPXFile(gpx);
        return track != null && track.getTrackSegments() != null && !track.getTrackSegments().isEmpty();
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
        if (fileHasTrack(gpx)) {
            List<Waypoint> waypoints = track.getTrackSegments().get(track.getTrackSegments().size() - 1).getWaypoints();
            Waypoint lastPoint = waypoints.get(waypoints.size() - 1);
            if (lastPoint == null) {
                return false;
            }
            Date pointDate = lastPoint.getTime();
            return (pointDate.getTime() < operationStartTime.getTime());
        }
        return false;
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
        try {
            FileInputStream inputStream = new FileInputStream(file);
            gpx = gpxParser.parseGPX(inputStream);
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*DisplayColorExtensionParser colorParser = new DisplayColorExtensionParser();
        gpxParser.addExtensionParser(colorParser);
        try {
            gpx = gpxParser.parseGPX(new FileInputStream(file));
        } catch (Exception ex) {
            System.err.println("File not found or something.");
            ex.printStackTrace();
        }*/
        return gpx;
    }

    /**
     * Compares all track points in the new track with the track points of every other track files.
     * Concludes based on this if the track already exists in the folder.
     *
     * @param rawFiles the files in the raw folder.
     * @param newTrack the new track to import.
     * @return duplicate file if the new file matches an existing file, null if it doesn't match a file.
     */
    public static List<Waypoint> duplicateGpx(File[] rawFiles, Track newTrack) {
        List<Waypoint> duplicatePoints = new ArrayList<>();
        for (File rawFile : rawFiles) {
            GPX rawGpx = TrackTools.getGpxFromFile(rawFile);
            if (rawGpx != null && fileHasTrack(rawGpx)) {
                Track rawTrack = TrackTools.getTrackFromGPXFile(rawGpx);
                if (firstWaypointsMatch(rawGpx, newTrack)) {
                    /*List<Waypoint> newPoints = newTrack.getTrackPoints();
                    List<Waypoint> rawPoints = rawTrack.getTrackPoints();*/
                    List<Waypoint> newPoints = new ArrayList<>();
                    List<Waypoint> rawPoints = new ArrayList<>();
                    for (TrackSegment newPointsSegment : newTrack.getTrackSegments()) {
                        newPoints.addAll(newPointsSegment.getWaypoints());
                    }
                    for (TrackSegment rawPointsSegment : rawTrack.getTrackSegments()) {
                        rawPoints.addAll(rawPointsSegment.getWaypoints());
                    }
                    if (newPoints != null && rawPoints != null) {
                        for (int i = 0; i < newPoints.size() && i < rawPoints.size(); i++) {
                            if (matchingTrackPoints(newPoints.get(i), rawPoints.get(i))) {
                                duplicatePoints.add(newPoints.get(i));
                            } else {
                                duplicatePoints.clear();
                                return duplicatePoints;
                            }
                        }
                    }
                }
            }
        }
        return duplicatePoints;
    }

    /**
     * Comparing latitude, longitude and elevation. If elevation doesn't exist, compare only latitude and longitude.
     *
     * @param waypoint1 waypoint to check.
     * @param waypoint2 waypoint to compare with.
     * @return true if the points are matching, false if not.
     */
    public static boolean matchingTrackPoints(Waypoint waypoint1, Waypoint waypoint2) {
        if (waypoint1.getElevation() == -1 || waypoint2.getElevation() == -1) {
            return waypoint1.getLatitude() == waypoint2.getLatitude() && waypoint1.getLongitude() == (waypoint2.getLongitude());
        } else {
            return waypoint1.getLatitude() == waypoint2.getLatitude() && waypoint1.getLongitude() == waypoint2.getLongitude() && waypoint1.getElevation() == waypoint2.getElevation();
        }
    }

    /**
     * Checks if the first waypoint in two tracks are equal.
     *
     * @param oldGpx   The already stored GPX file.
     * @param newTrack The new track to be checked.
     * @return True if the waypoints are equal, false if not.
     */
    public static boolean firstWaypointsMatch(GPX oldGpx, Track newTrack) {
        Track track1 = getTrackFromGPXFile(oldGpx);
        return matchingTrackPoints(track1.getTrackSegments().get(0).getWaypoints().get(0), newTrack.getTrackSegments().get(0).getWaypoints().get(0));
    }

    /**
     * Checks if the file only contains an area and not a track (the waypoints don't have timetags).
     *
     * @return true if the file is an area, false if not
     */
    public static boolean trackIsAnArea(GPX gpx) {
        Track track = getTrackFromGPXFile(gpx);
        if (!fileHasTrack(gpx)) {
            return false;
        }
        return track.getTrackSegments().get(0).getWaypoints().get(0).getTime() == null;
    }

    /**
     * Checks if a GPX has waypoints.
     *
     * @param gpx the GPX file to check.
     * @return true if the GPX has waypoints, false if not.
     */
    public static boolean hasWaypoints(GPX gpx) {
        if (gpx.getWaypoints() == null) {
            return false;
        }
        return gpx.getWaypoints().size() > 0;
    }

    /**
     * Gets the part of the track that was produced since last transfer
     *
     * @param gpx    the gpx to remove from.
     * @param points the points to remove.
     */
    public static void removePoints(GPX gpx, List<Waypoint> points) {
        Track track = getTrackFromGPXFile(gpx);
        if (track == null) {
            return;
        }
        List<Waypoint> allPoints = new ArrayList<>();
        for (TrackSegment allPointsSegment : track.getTrackSegments()) {
            allPoints.addAll(allPointsSegment.getWaypoints());
        }
        if (allPoints == null || allPoints.isEmpty()) {
            return;
        }
        boolean shouldRemove = true;
        List<Waypoint> pointsToRemove = new ArrayList<>();
        for (int i = 0; i < points.size() && shouldRemove; i++) {
            if (matchingTrackPoints(allPoints.get(i), points.get(i))) {
                pointsToRemove.add(allPoints.get(i));
            } else {
                shouldRemove = false;
            }
        }
        allPoints.removeAll(pointsToRemove);
    }

    /**
     * Gets a string with the date of the track.
     *
     * @param gpx The GPX file to get the date from.
     * @return The date of the GPX
     */
    public static String getDayStringFromTrack(GPX gpx) {
        if (fileHasTrack(gpx)) {
            Track track = getTrackFromGPXFile(gpx);
            Date date = track.getTrackSegments().get(0).getWaypoints().get(0).getTime();
            return StringTools.formatDateForOrganizing(date);
        } else {
            return null;
        }
    }

    /**
     * Gets a string with the start time of the track.
     *
     * @param gpx The GPX file to get the start time from.
     * @return The track's start time.
     */
    public static String getStartTimeFromTrack(GPX gpx) {
        if (fileHasTrack(gpx)) {
            Track track = getTrackFromGPXFile(gpx);
            Date startDate = track.getTrackSegments().get(0).getWaypoints().get(0).getTime();
            return StringTools.formatDateForFileProcessing(startDate);
        } else {
            return null;
        }
    }

    /**
     * Gets a string with the end time of the track.
     *
     * @param gpx The GPX file to get the end time from.
     * @return The track's end time.
     */
    public static String getEndTimeFromTrack(GPX gpx) {
        if (fileHasTrack(gpx)) {
            Track track = getTrackFromGPXFile(gpx);
            List<Waypoint> trackPoints = track.getTrackSegments().get(track.getTrackSegments().size() - 1).getWaypoints();
            Date endDate = trackPoints.get(trackPoints.size() - 1).getTime();
            return StringTools.formatDateForFileProcessing(endDate);
        } else {
            return null;
        }
    }

    public static List<Waypoint> getWaypointsFromFile(GPX gpx) {
        HashSet<Waypoint> waypointsSet = gpx.getWaypoints();
        List<Waypoint> waypoints = new ArrayList<>(waypointsSet);
        return waypoints;
    }

    public static List<GPX> splitWaypointGpx(GPX gpx) {
        List<GPX> waypointGpxs = new ArrayList<>();
        List<Waypoint> waypointsInFile = getWaypointsFromFile(gpx);
        for (int i = 0; i < waypointsInFile.size(); i++) {
            GPX gpxCopy = gpx;
            HashSet<Waypoint> waypointSet = new HashSet<>();
            waypointSet.add(waypointsInFile.get(i));
            gpxCopy.setWaypoints(waypointSet);
            waypointGpxs.add(gpx);
        }
        return waypointGpxs;
    }
}
