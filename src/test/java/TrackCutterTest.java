import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.Waypoint;
import no.hvl.dowhile.core.GpxFile;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackCutter;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.TrackTools;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class TrackCutterTest {

    OperationManager opManager;
    TrackCutter cutter;
    GPX gpx;
    Track track;
    List<Waypoint> trackPoints;

    @Before
    public void before() {
        File file = new File("src/test/resources/testFile.gpx");
        gpx = TrackTools.getGpxFromFile(file);
        track = TrackTools.getTrackFromGPXFile(gpx);
        trackPoints = TrackTools.getAllTrackPoints(track);
        opManager = new OperationManager();
        GpxFile gpxFile = new GpxFile(file, "", "", gpx);
        cutter = new TrackCutter(opManager);
        cutter.setTrackInfo(new TrackInfo("", 0, 0, "", 0.0, 0, ""));
        cutter.setGpxFile(gpxFile);
    }

    @Test
    public void trackIsFilteredOnStartTime() {

        Waypoint firstTrackpoint = trackPoints.get(0);
        Waypoint lastTrackpoint = trackPoints.get(trackPoints.size() - 1);

        long firstPointStart = firstTrackpoint.getTime().getTime();
        long lastPointStart = lastTrackpoint.getTime().getTime();
        long middleTime = (firstPointStart + lastPointStart) / 2;

        cutter.filterOnTimeStarted(new Date(middleTime));

        trackPoints = TrackTools.getAllTrackPoints(track);
        assertFalse(trackPoints.contains(firstTrackpoint));
    }
}
