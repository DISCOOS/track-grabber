import com.hs.gpxparser.modal.GPX;
import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private OperationManager opManager;
    private FileManager fileManager;
    private File appFolder;
    private File extraFolder;
    private Operation operation;
    private String operationName;
    private TrackInfo trackInfo;

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        operationName = "TestOp";
        trackInfo = new TrackInfo();
        trackInfo.setAreaSearched("[2]");
        operation = new Operation(operationName, 30, 11, 2016, 11, 56);

        appFolder = tempFolder.newFolder("TrackGrabberTest");
        fileManager.setAppFolder(appFolder);

        extraFolder = tempFolder.newFolder("Extra folders");

        fileManager.setupMainOperationFolder(operation);
    }

    @Test
    public void tempFolderExists() {
        assertNotNull(tempFolder);
    }

    @Test
    public void operationFolderIsSetUp() {
        assertTrue(fileManager.getMainOperationFolder().getOperationFolder().exists());
    }

    @Test
    public void rawFolderIsSetUp() {
        assertTrue(fileManager.getMainOperationFolder().getRawFolder().exists());
    }

    @Test
    public void processedFolderIsSetUp() {
        assertTrue(fileManager.getMainOperationFolder().getProcessedFolder().exists());
    }

    @Test
    public void operationFileIsCreated() {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        File opFile = FileTools.getFile(fileManager.getMainOperationFolder().getOperationFolder(), operation.getName() + ".txt");
        assertNotNull(opFile);
    }

    @Test
    public void appFolderWithoutConfigGetsConfig() {
        fileManager.setupConfig(appFolder);
        File config = FileTools.getFile(appFolder, "config.txt");
        assertNotNull(config);
    }

    @Test
    public void existingOperationsAreLoaded() {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        List<Operation> operations = fileManager.loadExistingOperations();
        assertNotNull(operations.get(0));
    }

    @Test
    public void existingOperationsAreNotLoadedFromEmptyFolder() {
        fileManager.getAppFolder().delete();
        List<Operation> operations = fileManager.loadExistingOperations();
        System.out.println(operations.size());
        assertTrue(operations.isEmpty());
    }

    @Test
    public void operationFileIsUpdated() throws FileNotFoundException {
        fileManager.createOperationFile(operation, fileManager.getMainOperationFolder().getOperationFolder());
        operation.setStartTime(2014, 10, 21, 10, 34);
        fileManager.updateOperationFile(operation);
        File updatedOpFile = FileTools.getFile(fileManager.getMainOperationFolder().getOperationFolder(), operationName + ".txt");
        String updatedDateString = "21/10/2014 10:34";
        assertTrue(FileTools.txtFileContainsString(updatedOpFile, updatedDateString));
    }

    @Test
    public void trackPointsOfTwoSimilarFilesAreEqual() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveGpxFile(gpx1, trackInfo, "Filnavn", fileManager.getMainOperationFolder().getRawFolder());
        File[] rawFiles = fileManager.getMainOperationFolder().getRawFolder().listFiles();
        assertNotNull(TrackTools.duplicateGpx(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void trackPointsOfTwoDifferentFilesAreDifferent() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/test/resources/testFile2.gpx"));
        fileManager.saveGpxFile(gpx1, trackInfo, "Filnavn", fileManager.getMainOperationFolder().getRawFolder());
        File[] rawFiles = fileManager.getMainOperationFolder().getRawFolder().listFiles();
        assertNotNull(TrackTools.duplicateGpx(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void alreadyImportedTrackIsAlreadyImported() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveRawGpxFileInFolders(gpx, "Filnavn");
        int gpxTrackpointSize = TrackTools.getAllTrackPoints(TrackTools.getTrackFromGPXFile(gpx)).size();
        int duplicateTrackpointsSize = fileManager.alreadyImportedTrack(gpx).size();
        assertTrue(gpxTrackpointSize == duplicateTrackpointsSize);
    }

    @Test
    public void alreadyImportedWaypointIsAlreadyImported() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testWpFile.gpx"));
        fileManager.saveWaypointFileInFolders(gpx, "Veipunktnavn");
        assertTrue(fileManager.waypointIsAlreadyImported(gpx));
    }

    @Test
    public void rawGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveRawGpxFileInFolders(gpx, "Filnavn");
        File savedRawFile = FileTools.getFile(fileManager.getMainOperationFolder().getRawFolder(), "Filnavn");
        assertNotNull(savedRawFile);
    }

    @Test
    public void processedGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testFile.gpx"));
        fileManager.saveProcessedGpxFileInFolders(gpx, trackInfo, "Filnavn");
        File savedProcessedFile = FileTools.getFile(fileManager.getMainOperationFolder().getProcessedFolder(), "Filnavn");
        assertNotNull(savedProcessedFile);
    }

    @Test
    public void areaGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/test/resources/testAreaFile.gpx"));
        fileManager.saveAreaGpxFileInFolders(gpx, "Teignavn");
        File savedAreaFile = FileTools.getFile(fileManager.getMainOperationFolder().getAreaFolder(), "Teignavn");
        assertNotNull(savedAreaFile);
    }

    @Test
    public void extraOperationFolderIsSetUp() {
        fileManager.setupExtraOperationFolder(operation, extraFolder.getPath());
        File extra = FileTools.getFile(extraFolder, "TestOp");
        assertTrue(fileManager.getExtraOperationFolders().size() == 1);
        assertNotNull(extra);
    }
}
