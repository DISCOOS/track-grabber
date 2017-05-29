import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.Waypoint;
import no.hvl.dowhile.utility.TrackTools;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TrackToolsTest {

    private GPX trackFile;
    private File waypointFile;
    private GPX waypointGpx;
    private Track track;
    private ArrayList<Waypoint> trackPoints;
    private SimpleDateFormat format;
    private Date veryLateStartTime;
    private Date veryEarlyStartTime;
    private Waypoint trackPoint0;
    private Waypoint trackPoint1;
    private Waypoint trackPoint1Copy;
    private List<GPX> waypoints;

    @SuppressWarnings("deprecation")
    @Before
    public void before() {
        waypointFile = new File("src/test/resources/testWpFile.gpx");
        waypointGpx = TrackTools.getGpxFromFile(waypointFile);
        waypoints = TrackTools.splitWaypointGpx(waypointFile);

        trackFile = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        track = TrackTools.getTrackFromGPXFile(trackFile);
        trackPoints = TrackTools.getAllTrackPoints(track);

        format = new SimpleDateFormat("dd/mm/yyyy");
        veryLateStartTime = new Date("24/12/2038");
        veryEarlyStartTime = new Date("24/12/1999");

        trackPoint0 = trackPoints.get(0);
        trackPoint1 = trackPoints.get(1);

        trackPoint1Copy = new Waypoint(14, 35);
        trackPoint1Copy.setLatitude(trackPoint1.getLatitude());
        trackPoint1Copy.setLongitude(trackPoint1.getLongitude());
        trackPoint1Copy.setElevation(trackPoint1.getElevation());
        trackPoint1Copy.setTime(trackPoint1.getTime());
    }

    @Test
    public void gpxFileHasTrack() {
        assertNotNull(track);
    }

    @Test
    public void gpxTrackHasTrackPoints() {
        assertNotNull(trackPoint0);
    }

    @Test
    public void gpxTrackPointsHaveLatitude() {
        assertNotNull(trackPoint0.getLatitude());
    }

    @Test
    public void gpxTrackPointsHaveLongitude() {
        assertNotNull(trackPoint0.getLongitude());
    }

    @Test
    public void gpxTrackPointsHaveElevation() {
        assertNotNull(trackPoint0.getElevation());
    }

    @Test
    public void trackHasStartTime() {
        assertNotNull(TrackTools.getStartTimeFromTrack(trackFile));
    }

    @Test
    public void trackHasEndTime() {
        assertNotNull(TrackTools.getEndTimeFromTrack(trackFile));
    }

    @Test
    public void differentTrackPointsDoNotMatch() {
        assertFalse(TrackTools.matchingTrackPoints(trackPoint0, trackPoint1));
    }

    @Test
    public void matchingTrackPointsMatch() {
        assertTrue(TrackTools.matchingTrackPoints(trackPoint1, trackPoint1Copy));
    }

    @Test
    public void trackWasInFactCreatedBeforeStartTime() {
        assertTrue(TrackTools.trackCreatedBeforeStartTime(trackFile, veryLateStartTime));
    }

    @Test
    public void pointsAreRemovedFromTrack() {
        List<Waypoint> pointsToRemove = new ArrayList<>();
        pointsToRemove.add(trackPoint0);
        pointsToRemove.add(trackPoint1);
        TrackTools.removePoints(track, pointsToRemove);
        trackPoints = TrackTools.getAllTrackPoints(track);
        assertFalse(trackPoints.contains(trackPoint0));
        assertFalse(trackPoints.contains(trackPoint1));
    }

    @Test
    public void trackWasActuallyNotCreatedBeforeStartTime() {
        assertFalse(TrackTools.trackCreatedBeforeStartTime(trackFile, veryEarlyStartTime));
    }

    @Test
    public void matchingWaypointsMatch() {
        Waypoint wp1 = waypoints.get(0).getWaypoints().iterator().next();
        Waypoint wp1Copy = waypoints.get(0).getWaypoints().iterator().next();
        assertTrue(TrackTools.matchingWaypoints(wp1, wp1Copy));
    }

    @Test
    public void differentWaypointsDoNotMatch() {
        Waypoint wp1 = waypoints.get(0).getWaypoints().iterator().next();
        Waypoint wp2 = waypoints.get(1).getWaypoints().iterator().next();
        assertFalse(TrackTools.matchingWaypoints(wp1, wp2));
    }
}
