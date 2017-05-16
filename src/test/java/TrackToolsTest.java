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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TrackToolsTest {

    Track track;
    ArrayList<Waypoint> trackPoints;
    SimpleDateFormat format;
    Date veryLateStartTime;
    Date veryEarlyStartTime;
    Waypoint trackPoint0;
    Waypoint trackPoint1;
    Waypoint trackPoint1Copy;
    private GPX gpxFile;

    @Before
    public void before() {
        gpxFile = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        track = TrackTools.getTrackFromGPXFile(gpxFile);
        trackPoints = track.getTrackPoints();

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
    public void twoDifferentTrackPointsDoNotMatch() {
        assertFalse(TrackTools.matchingTrackPoints(trackPoint0, trackPoint1));
    }

    @Test
    public void twoMatchingTrackPointsDoMatch() {
        assertTrue(TrackTools.matchingTrackPoints(trackPoint1, trackPoint1Copy));
    }

    @Test
    public void trackWasInFactCreatedBeforeStartTime() {
        assertTrue(TrackTools.trackCreatedBeforeStartTime(gpxFile, veryLateStartTime));
    }

    @Test
    public void trackWasActuallyNotCreatedBeforeStartTime() {
        assertFalse(TrackTools.trackCreatedBeforeStartTime(gpxFile, veryEarlyStartTime));
    }
}
