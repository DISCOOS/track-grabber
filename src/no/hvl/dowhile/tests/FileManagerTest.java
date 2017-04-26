package no.hvl.dowhile.tests;

import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private OperationManager opManager;
    private FileManager fileManager;
    private File appFolder;
    private Operation operation;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        appFolder = tempFolder.newFile("TrackGrabberTest");
        operation = new Operation("TestOp", 30, 11, 2016, 11, 56);
    }

    @Test
    public void foldersAreSetUp() {
        fileManager.setupOperationFolder(operation);

    }

    @Test
    public void folderWithoutConfigGetsConfig() {

    }

    @Test
    public void folderWithConfigDoesNotGetConfig() {

    }

    @Test
    public void existingOperationsAreLoaded() {

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
