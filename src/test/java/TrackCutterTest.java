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

public class TrackCutterTest {

    OperationManager opManager;
    TrackCutter cutter;
    GPX gpx;
    Track track;
    ArrayList<Waypoint> trackPoints;

    @Before
    public void before() {
        File file = new File("src/test/resources/testFile.gpx");
        gpx = TrackTools.getGpxFromFile(file);
        track = TrackTools.getTrackFromGPXFile(gpx);
        trackPoints = track.getTrackPoints();
        opManager = new OperationManager();
        GpxFile gpxFile = new GpxFile(file, "", gpx);
        cutter = new TrackCutter(opManager);
        cutter.setTrackInfo(new TrackInfo("", 0, 0, "", 0, ""));
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

        assertFalse(track.getTrackPoints().contains(firstTrackpoint));
    }
}
