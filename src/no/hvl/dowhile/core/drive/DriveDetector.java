package no.hvl.dowhile.core.drive;

import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.ThreadTools;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Responsible of checking new drives connected and removed.
 * The class will also check if a drive is a GPS.
 */
public class DriveDetector implements Runnable {
    private final OperationManager OPERATION_MANAGER;
    private Map<String, Drive> detectedDrives;

    /**
     * Default constructor taking an instance of the current OperationManager.
     *
     * @param OPERATION_MANAGER current instance of the OperationManager.
     * @see OperationManager
     */
    public DriveDetector(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.detectedDrives = new HashMap<>();
    }

    /**
     * Checking the amount of drives connected and updates the Map of detected drives.
     */
    @Override
    public void run() {
        Messages.DRIVE_DETECTOR_START.print();
        do {
            ThreadTools.sleep(5);
            File[] listRoots = File.listRoots(); // A listroot is a station/drive. (C:// D:// E:// Etc).
            if (detectedDrives.size() > listRoots.length) {
                // One or more drives removed.
                unregisterRemovedDrives(listRoots);
            } else if (detectedDrives.size() < listRoots.length) {
                // One or more drives connected.
                for (File listRoot : listRoots) {
                    String driveLetter = listRoot.getAbsolutePath().substring(0, 1);
                    if (!detectedDrives.containsKey(driveLetter)) {
                        if (listRoot.getAbsolutePath().startsWith("C")) {
                            OPERATION_MANAGER.setupLocalFolders(listRoot);
                        }
                        registerConnectedDrive(driveLetter, listRoot);
                    }
                }
            } else {
                // No changes in amount of drives.
            }
        } while (OPERATION_MANAGER.isActive());
        Messages.DRIVE_DETECTOR_STOP.print();
    }

    /**
     * This method will determine whether a drive is a GPS or not and pass information to the OperationManager is it's a GPS.
     * In addition, the drive will be registered so it's not checked every time the run() method is called.
     *
     * @param driveLetter the letter of the drive to check.
     * @param listRoot    the actual drive to check.
     */
    private void registerConnectedDrive(String driveLetter, File listRoot) {
        GPSDrive gpsDrive = validateDrive(driveLetter, listRoot);
        if (gpsDrive != null) {
            OPERATION_MANAGER.handleGPSDrive(gpsDrive);
            detectedDrives.put(driveLetter, gpsDrive);
            System.err.println("GPS drive detected. There are now " + detectedDrives.size() + " connected.");
        } else {
            // Drive is not a GPS.
            Drive drive = new Drive(driveLetter, listRoot.getAbsolutePath());
            detectedDrives.put(driveLetter, drive);
            System.err.println("Normal drive detected. There are now " + detectedDrives.size() + " connected.");
        }
    }

    /**
     * This method will compare the drives detected by the system with the drives which is actually connected.
     * Removes any detected drives which is removed.
     *
     * @param listRoots the drives currently connected.
     */
    private void unregisterRemovedDrives(File[] listRoots) {
        Set<String> listRootLetters = new HashSet<>();
        Set<String> drivesToRemove = new HashSet<>();
        for (File listRoot : listRoots) {
            listRootLetters.add(listRoot.getAbsolutePath().substring(0, 1));
        }
        for (Map.Entry<String, Drive> entry : detectedDrives.entrySet()) {
            if (!listRootLetters.contains(entry.getKey())) {
                drivesToRemove.add(entry.getKey());
            }
        }
        for (String driveLetter : drivesToRemove) {
            Drive drive = detectedDrives.remove(driveLetter);
            if (drive != null) {
                if (drive instanceof GPSDrive) {
                    OPERATION_MANAGER.setStatus("Koblet fra.");
                    System.err.println("GPS drive removed. There are now " + detectedDrives.size() + " connected.");
                } else {
                    System.err.println("Normal drive removed. There are now " + detectedDrives.size() + " connected.");
                }
            }
        }
    }

    /**
     * Checks the folder structure on the drive to see if it has the folders which is supposed to be presented on a GPS.
     *
     * @param driveLetter the name of the drive.
     * @param listRoot    the root of the drive to check.
     * @return the GPSDrive with the folders needed, or null if it's not a GPS.
     */
    private GPSDrive validateDrive(String driveLetter, File listRoot) {
        File[] filesInRootFolder = listRoot.listFiles();
        if (filesInRootFolder == null) {
            return null;
        }
        GPSDrive gpsDrive = new GPSDrive(driveLetter, listRoot.getAbsolutePath());
        gpsDrive.setGarminFolder(findFileInFolder(listRoot, "Garmin"));
        if (!gpsDrive.hasGarminFolder()) {
            return null;
        }
        gpsDrive.setGpxFolder(findFileInFolder(gpsDrive.getGarminFolder(), "GPX"));
        if (!gpsDrive.hasGpxFolder()) {
            return null;
        }
        gpsDrive.setCurrentFolder(findFileInFolder(gpsDrive.getGpxFolder(), "Current"));
        gpsDrive.setCurrentFolder(findFileInFolder(gpsDrive.getGpxFolder(), "Archive"));
        if (!gpsDrive.isValidGPSDrive()) {
            return null;
        }
        return gpsDrive;
    }

    /**
     * Finding the file with specified name in the parent folder.
     *
     * @param parentFolder the folder to check.
     * @param filename     the name of the file to find.
     * @return the file which was found, null if the file doesn't exist in folder.
     */
    private File findFileInFolder(File parentFolder, String filename) {
        File[] filesInParent = parentFolder.listFiles();
        if (filesInParent == null || filesInParent.length == 0) {
            return null;
        }
        for (File file : filesInParent) {
            if (file.getName().equals(filename)) {
                return file;
            }
        }
        return null;
    }
}
