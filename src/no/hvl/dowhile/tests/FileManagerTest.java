package no.hvl.dowhile.tests;

import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.OperationManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private OperationManager opManager;
    private FileManager fileManager;
    private TemporaryFolder tempFolder;
    private File appFolder;
    private File operationFolder;
    private File processedFolder;
    private File rawFolder;

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        tempFolder = new TemporaryFolder();
        appFolder = tempFolder.newFile("TrackGrabberTest");
        operationFolder = fileManager.setupFolder(appFolder, "A Testing Operation");
        rawFolder = fileManager.setupFolder(operationFolder, "Raw");
        processedFolder = fileManager.setupFolder(operationFolder, "Processed");
    }

    @Test
    public void foldersAreSetUp() {
        assertTrue(appFolder.mkdir());
        assertTrue(operationFolder.mkdir());
        assertTrue(rawFolder.mkdir());
        assertTrue(processedFolder.mkdir());
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
