import no.hvl.dowhile.core.*;
import no.hvl.dowhile.core.gui.Window;
import no.hvl.dowhile.utility.TrackTools;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;

public class OperationManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private OperationManager operationManager;
    private Window window;
    private FileManager fileManager;
    private Operation operation1;
    private Operation operation2;
    private GpxFile gpxFile1;
    private GpxFile gpxFile2;
    private File testFile1;
    private File testFile2;
    private TrackInfo trackInfo1;
    private TrackInfo trackInfo2;

    @Before
    public void before() throws IOException {
        operationManager = new OperationManager();

        fileManager = new FileManager(operationManager);
        operationManager.setFileManager(fileManager);
        fileManager.setAppFolder(tempFolder.newFolder("TrackGrabberTest"));

        fileManager.setupConfig(fileManager.getAppFolder());

        operation1 = new Operation("Test1", 29, 10, 1994, 11, 45);
        operation2 = new Operation("Test2", 16, 10, 1996, 12, 54);

        testFile1 = new File("src/test/resources/testFile.gpx");
        testFile2 = new File("src/test/resources/testFile2.gpx");

        gpxFile1 = new GpxFile(testFile1, "TestFile1", "", TrackTools.getGpxFromFile(testFile1));
        gpxFile2 = new GpxFile(testFile2, "TestFile2", "", TrackTools.getGpxFromFile(testFile2));

        trackInfo1 = new TrackInfo();
        trackInfo2 = new TrackInfo();

        window = new Window(operationManager);
        operationManager.setWindow(window);
    }

    @Test
    public void managerHasOperation() {
        operationManager.setupOperation(operation1);
        assertNotNull(operationManager.getOperation());

    }

    @Test
    public void managerGetsExistingOperationIfTheyExist() {
        operationManager.setupOperation(operation1);
        List<Operation> existingOperations = operationManager.getExistingOperations();
        assertFalse(existingOperations.isEmpty());
    }

    @Test
    public void managerDoesNotGetExistingOperationsIfTheyDoNotExist() {
        List<Operation> existingOperations = operationManager.getExistingOperations();
        assertTrue(existingOperations.isEmpty());
    }

    @Test
    public void existingOperationsAreUpdated() {
        operationManager.setupOperation(operation1);
        List<Operation> existingOperations = operationManager.getExistingOperations();
        assertTrue(existingOperations.size() == 1);

        operationManager.setupOperation(operation2);
        operationManager.reloadExistingOperations();
        existingOperations = operationManager.getExistingOperations();
        assertTrue(existingOperations.size() == 2);
    }

    @Test
    public void usefulFileIsAddedToProcessingQueue() {
        operationManager.setupOperation(operation1);
        operationManager.checkFile(testFile1);
        operationManager.checkFile(testFile2);
        assertTrue(operationManager.getQueue().size() == 2);
    }

    @Test
    public void notUsefulFileIsNotAddedToProcessingQueue() {
        operationManager.setupOperation(operation1);
        operationManager.checkFile(testFile1);
        operationManager.checkFile(testFile1);
        assertTrue(operationManager.getQueue().size() == 1);
    }

    @Test
    public void nextFileIsPrepared() {
        trackInfo1.setCrewType("Mann");
        trackInfo2.setCrewType("Hund");

        operationManager.setupOperation(operation1);

        operationManager.getQueue().add(gpxFile1);
        operationManager.getQueue().add(gpxFile2);

        operationManager.prepareNextFile();
        assertTrue(TrackTools.firstTrackpointsMatch(TrackTools.getGpxFromFile(testFile1), TrackTools.getTrackFromGPXFile(operationManager.getCurrentTrackCutter().getGpxFile().getGpx())));
        operationManager.prepareNextFile();
        assertTrue(TrackTools.firstTrackpointsMatch(TrackTools.getGpxFromFile(testFile2), TrackTools.getTrackFromGPXFile(operationManager.getCurrentTrackCutter().getGpxFile().getGpx())));
    }

    @Test
    public void alreadyExistingOperationNameAlreadyExists() {
        operationManager.setupOperation(operation1);
        operationManager.getExistingOperations();
        assertTrue(operationManager.operationNameAlreadyExists(operation1.getName()));
    }

    @Test
    public void notAlreadyExistingOperationNameDoesNotAlreadyExist() {
        operationManager.setupOperation(operation1);
        operationManager.getExistingOperations();
        assertFalse(operationManager.operationNameAlreadyExists(operation2.getName()));
    }
}
