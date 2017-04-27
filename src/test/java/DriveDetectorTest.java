import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.drive.Drive;
import no.hvl.dowhile.core.drive.DriveDetector;
import no.hvl.dowhile.core.drive.GPSDrive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DriveDetectorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DriveDetector driveDetector;
    private File rootFolder;
    private File garminFolder;
    private File gpxFolder;
    private File currentFolder;
    private File archiveFolder;

    private OperationManager operationManager;
    private Map<String, Drive> detectedDrives;

    @Before
    public void before() throws IOException {
        operationManager = new OperationManager();
        driveDetector = new DriveDetector(operationManager);
        detectedDrives = new HashMap<>();
        rootFolder = tempFolder.newFolder("Root");
        garminFolder = new File(rootFolder, "Garmin");
        gpxFolder = new File(garminFolder, "GPX");
        currentFolder = new File(gpxFolder, "Current");
        archiveFolder = new File(gpxFolder, "Archive");
    }

    @Test
    public void driveWithoutGarminFolderIsInvalid() {
        assertNull(driveDetector.validateDrive("E", rootFolder));
    }

    @Test
    public void driveWithoutGPXFolderIsInvalid() {
        garminFolder.mkdir();
        assertNull(driveDetector.validateDrive("E", rootFolder));
    }

    @Test
    public void driveWithoutCurrentAndArchiveFolderIsInvalid() {
        garminFolder.mkdir();
        gpxFolder.mkdir();
        assertNull(driveDetector.validateDrive("E", rootFolder));
    }

    @Test
    public void driveWithoutCurrentFolderIsInvalid() {
        garminFolder.mkdir();
        gpxFolder.mkdir();
        currentFolder.mkdir();
        assertNull(driveDetector.validateDrive("E", rootFolder));
    }

    @Test
    public void driveWithoutArchiveFolderIsInvalid() {
        garminFolder.mkdir();
        gpxFolder.mkdir();
        archiveFolder.mkdir();
        assertNull(driveDetector.validateDrive("E", rootFolder));
    }

    @Test
    public void validDriveIsValid() {
        garminFolder.mkdir();
        gpxFolder.mkdir();
        currentFolder.mkdir();
        archiveFolder.mkdir();
        assertTrue(driveDetector.validateDrive("E", rootFolder) != null);
    }

    @Test
    public void driveIsRegistered() {
        driveDetector.registerConnectedDrive("E", rootFolder);
        assertFalse(driveDetector.getDetectedDrives().isEmpty());
    }

    @Test
    public void driveIsUnregistered() {
        driveDetector.registerConnectedDrive("E", rootFolder);
        driveDetector.unregisterRemovedDrives(new File[]{rootFolder});
        assertTrue(driveDetector.getDetectedDrives().isEmpty());
    }
}
