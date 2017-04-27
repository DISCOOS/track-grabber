import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;

public class OperationManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private OperationManager operationManager;
    FileManager fileManager;
    private Operation operation1;
    private Operation operation2;
    private File testGPX;

    @Before
    public void before() throws IOException {
        operationManager = new OperationManager();
        fileManager = new FileManager(operationManager);
        operationManager.setFileManager(fileManager);
        fileManager.setAppFolder(tempFolder.newFolder("TrackGrabberTest"));
        operation1 = new Operation("Test1", 29, 10, 1994, 11, 45);
        operation2 = new Operation("Test2", 16, 10, 1996, 12, 54);
        testGPX = new File("src/test/resources/testFile.gpx");
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

    /*
    @Test
    public void existingOperationsAreUpdated() {
        operationManager.setupOperation(operation1);
        List<Operation> existingOperations = operationManager.getExistingOperations();
        assertTrue(existingOperations.size() == 1);
        operationManager.setupOperation(operation2);
        existingOperations = operationManager.getExistingOperations();
        assertTrue(existingOperations.size() == 2);
    }
    */

    @Test
    public void usefulFileIsAddedToProcessingQueue() {
        operationManager.setupOperation(operation1);
        operationManager.processFile(testGPX);
        assertTrue(operationManager.getQueue().size() == 1);
    }

    @Test
    public void notUsefulFileIsNotAddedToProcessingQueue() {
        operationManager.setupOperation(operation1);
        operationManager.processFile(testGPX);
        operationManager.processFile(testGPX);
        assertFalse(operationManager.getQueue().size() == 2);
    }

    @Test
    public void nextFileIsPrepared() {

    }

    @Test
    public void alreadyExistingOperationNameAlreadyExists() {

    }

    @Test
    public void notAlreadyExistingOperationNameDoesNotAlreadyExist() {

    }
}
