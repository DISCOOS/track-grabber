import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.drive.Drive;
import no.hvl.dowhile.core.drive.DriveDetector;
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
    public void validDriveIsValid() {
        garminFolder.mkdir();
        gpxFolder.mkdir();
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
