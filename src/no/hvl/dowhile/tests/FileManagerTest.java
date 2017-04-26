package no.hvl.dowhile.tests;

import no.hvl.dowhile.core.FileManager;
import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.GPX;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {

    private OperationManager opManager;
    private FileManager fileManager;
    private File appFolder;
    private File operationFolder;
    private File rawFolder;
    private File processedFolder;
    private Operation operation;
    private String operationName;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        opManager = new OperationManager();
        fileManager = new FileManager(opManager);
        operationName = "TestOp";
        appFolder = tempFolder.newFolder("TrackGrabberTest");
        operationFolder = fileManager.setupFolder(appFolder, operationName);
        rawFolder = fileManager.setupFolder(operationFolder, "Raw");
        processedFolder = fileManager.setupFolder(operationFolder, "Processed");
        operation = new Operation(operationName, 30, 11, 2016, 11, 56);
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
    public void appFolderWithoutConfigGetsConfig() {
        fileManager.setupConfig(appFolder);
        File config = FileTools.getFile(appFolder, "config.txt");
        assertNotNull(config);
    }

    @Test
    public void existingOperationsAreLoaded() {
        fileManager.createOperationFile(operation, operationFolder);
        List<Operation> operations = fileManager.loadExistingOperations(appFolder);
        assertNotNull(operations.get(0));
    }

    @Test
    public void existingOperationsAreNotLoadedFromEmptyFolder() {
        operationFolder.delete();
        List<Operation> operations = fileManager.loadExistingOperations(appFolder);
        assertTrue(operations.isEmpty());
    }

    @Test
    public void operationFileIsUpdated() throws FileNotFoundException {
        fileManager.createOperationFile(operation, operationFolder);
        operation.updateStartTime(2014, 10, 21, 10, 34);
        fileManager.updateOperationFile(operation, appFolder);
        File updatedOpFile = FileTools.getFile(operationFolder, operationName + ".txt");
        String updatedDateString = "# Starttid: 21-10/2014 10:34 CET";
        assertTrue(FileTools.txtFileContainsString(updatedOpFile, updatedDateString));
    }

    @Test
    public void trackPointsOfTwoSimilarFilesAreEqual() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        fileManager.saveGpxFile(gpx1, "Filnavn", rawFolder);
        File[] rawFiles = rawFolder.listFiles();
        assertTrue(fileManager.trackPointsAreEqual(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void trackPointsOfTwoDifferentFilesAreDifferent() {
        GPX gpx1 = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        GPX gpx2 = TrackTools.getGpxFromFile(new File("src/testFile2.gpx"));
        fileManager.saveGpxFile(gpx1, "Filnavn", rawFolder);
        File[] rawFiles = rawFolder.listFiles();
        assertFalse(fileManager.trackPointsAreEqual(rawFiles, TrackTools.getTrackFromGPXFile(gpx2)));
    }

    @Test
    public void alreadyImportedFileIsAlreadyImported() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        fileManager.saveRawGpxFile(gpx, "Filnavn", rawFolder);
        assertTrue(fileManager.fileAlreadyImported(gpx, rawFolder));
    }

    @Test
    public void rawGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        fileManager.saveRawGpxFile(gpx, "Filnavn", rawFolder);
        File savedRawFile = FileTools.getFile(rawFolder, "Filnavn");
        assertNotNull(savedRawFile);
    }

    @Test
    public void processedGPXFileIsSaved() {
        GPX gpx = TrackTools.getGpxFromFile(new File("src/testFile.gpx"));
        fileManager.saveProcessedGpxFile(gpx, "Filnavn", processedFolder);
        File savedProcessedFile = FileTools.getFile(processedFolder, "Filnavn");
        assertNotNull(savedProcessedFile);
    }
}
