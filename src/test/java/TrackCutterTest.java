import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackCutter;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class TrackCutterTest {

    OperationManager opManager;
    TrackCutter cutter;
    GPX gpxFile;
    Track track;
    ArrayList<Waypoint> trackPoints;

    @Before
    public void before() {
        gpxFile = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        track = TrackTools.getTrackFromGPXFile(gpxFile);
        trackPoints = track.getTrackPoints();
        opManager = new OperationManager();
        cutter = new TrackCutter(opManager);
        cutter.setTrackInfo(new TrackInfo("", 0, 0, "", 0));
        cutter.setTrackFile(gpxFile);
    }

    @Test
    public void trackIsFilteredOnStartTime() {

        Waypoint firstTrackpoint = trackPoints.get(0);
        Waypoint lastTrackpoint = trackPoints.get(trackPoints.size() - 1);

        long firstPointStart = firstTrackpoint.getTime().getTime();
        long lastPointStart = lastTrackpoint.getTime().getTime();
        long middleTime = (firstPointStart + lastPointStart) / 2;

        GPX cutFile = cutter.filterOnTimeStarted(new Date(middleTime));

        assertFalse(track.getTrackPoints().contains(firstTrackpoint));
    }
}
