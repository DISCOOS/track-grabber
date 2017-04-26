package no.hvl.dowhile.tests;

import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.FileTools;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private OperationManager opManager;
    private FileManager fileManager;
    private File appFolder;
    private File operationFolder;
    private File rawFolder;
    private File processedFolder;
    private Operation operation;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        appFolder = tempFolder.newFolder("TrackGrabberTest");
        operationFolder = fileManager.setupFolder(appFolder, "TestOp");
        rawFolder = fileManager.setupFolder(operationFolder, "Raw");
        processedFolder = fileManager.setupFolder(operationFolder, "Processed");
        operation = new Operation("TestOp", 30, 11, 2016, 11, 56);
    }

    @Test
    public void tempFolderExists() {
        assertNotNull(tempFolder);
    }

    @Test
    public void operationFolderIsSetUp() {
        assertTrue(operationFolder.exists());
    }

    @Test
    public void rawFolderIsSetUp() {
        assertTrue(rawFolder.exists());
    }

    @Test
    public void processedFolderIsSetUp() {
        assertTrue(processedFolder.exists());
    }

    @Test
    public void operationFileIsCreated() {
        fileManager.createOperationFile(operation, operationFolder);
        File opFile = FileTools.getFile(operationFolder, operation.getName() + ".txt");
        assertNotNull(opFile);
    }

    @Test
    public void folderWithoutConfigGetsConfig() {
        fileManager.setupConfig(appFolder);
        File config = FileTools.getFile(appFolder, "config.txt");
        assertNotNull(config);
    }

    @Test
    public void existingOperationsAreLoaded() {
        List<Operation> operations = fileManager.loadExistingOperations(appFolder);
        assertNotNull(operations.get(0));
    }

    @Test
    public void existingOperationsAreNotLoadedFromEmptyFolder() {

    }

    @Test
    public void operationFileIsUpdated() {

    }

    @Test
    public void alreadyImportedFileIsAlreadyImported() {

    }

    @Test
    public void trackPointsOfTwoSimilarFilesAreEqual() {

    }

    @Test
    public void rawGPXFileIsSaved() {

    }

    @Test
    public void processedGPXFileIsSaved() {

    }
}
