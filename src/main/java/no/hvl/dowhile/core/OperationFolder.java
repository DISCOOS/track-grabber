package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Stores folders for a given file location to store files for the current operation.
 */
public class OperationFolder {

    private static final Logger logger = LoggerFactory.getLogger(OperationFolder.class);

    private File trackFileInfo;
    private File waypointFileInfo;
    private File operationFolder;
    private File processedFolder;
    private File rawFolder;
    private File areaFolder;
    private File waypointFolder;
    private File organizingFolder;
    private File crewOrgFolder;
    private File areaOrgFolder;
    private File dayOrgFolder;

    /**
     * Constructor taking data to create the folders.
     *
     * @param operation  the current operation.
     * @param root       the folder to store the new operation folder in.
     * @param mainFolder true if this is the main folder, false if not.
     */
    OperationFolder(Operation operation, File root, boolean mainFolder) {
        operationFolder = FileTools.setupFolder(root, operation.getName().trim().replace(" ", "_"));
        rawFolder = FileTools.setupFolder(operationFolder, "Raw");
        processedFolder = FileTools.setupFolder(operationFolder, "Processed");
        areaFolder = FileTools.setupFolder(operationFolder, "Areas");
        waypointFolder = FileTools.setupFolder(operationFolder, "Waypoints");
        organizingFolder = FileTools.setupFolder(operationFolder, "Organizations");
        crewOrgFolder = FileTools.setupFolder(organizingFolder, "CrewTypes");
        areaOrgFolder = FileTools.setupFolder(organizingFolder, "Areas");
        dayOrgFolder = FileTools.setupFolder(organizingFolder, "Days");
        if (mainFolder) {
            operation.setMainPath(root.getAbsolutePath());
            createOperationFiles(operation);
        }
        logger.info("Done creating folders for operation {}", operation.getName());
    }

    /**
     * Gets the operation folder.
     *
     * @return the operation folder.
     */
    public File getOperationFolder() {
        return operationFolder;
    }

    /**
     * Gets the folder to store processed tracks.
     *
     * @return the folder to store processed files.
     */
    public File getProcessedFolder() {
        return processedFolder;
    }

    /**
     * Gets the folder to store rawfiles.
     *
     * @return the folder to store rawfiles.
     */
    public File getRawFolder() {
        return rawFolder;
    }

    /**
     * Gets the folder to store areas.
     *
     * @return the folder to store areas.
     */
    public File getAreaFolder() {
        return areaFolder;
    }

    /**
     * Gets the folder to store waypoints.
     *
     * @return the folder to store waypoints.
     */
    File getWaypointFolder() {
        return waypointFolder;
    }

    /**
     * Gets the folder to store organizations of crew types.
     *
     * @return the folder to store organizations of crew types.
     */
    File getCrewOrgFolder() {
        return crewOrgFolder;
    }

    /**
     * Gets the folder to store organizations of areas.
     *
     * @return the folder to store organizations of areas.
     */
    File getAreaOrgFolder() {
        return areaOrgFolder;
    }

    /**
     * Gets the folder to store organizations of days.
     *
     * @return the folder to store organizations of days.
     */
    File getDayOrgFolder() {
        return dayOrgFolder;
    }

    /**
     * Creates an operation file for the given Operation, into the given operation folder.
     *
     * @param operation the operation to create the file for.
     */
    private void createOperationFiles(Operation operation) {
        File operationFile = new File(operationFolder, operation.getName().trim().replace(" ", "_") + ".txt");
        trackFileInfo = new File(operationFolder, "TrackFileInfo.csv");
        waypointFileInfo = new File(operationFolder, "WaypointFileInfo.csv");
        if (!operationFile.exists()) {
            try {
                operationFile.createNewFile();
            } catch (IOException ex) {
                logger.error("Failed to create operation file.", ex);
            }
            FileTools.writeToFile(operation.getFileContent(), operationFile);
        }
        if (!trackFileInfo.exists()) {
            try {
                trackFileInfo.createNewFile();
                FileTools.writeToCsvFile(trackFileInfo, "Lagtype", "Lagnummer", "Antall mann", "Teiger", "Lengde", "Spornummer", "Kommentar", "Tid", "Original fil", "Prosessert fil", "Original hash");
            } catch (IOException ex) {
                logger.error("Failed to create operation file.", ex);
            }
        }
        if (!waypointFileInfo.exists()) {
            try {
                waypointFileInfo.createNewFile();
                FileTools.writeToCsvFile(waypointFileInfo, "Kommentar", "Original fil", "Prosessert fil", "Original hash");
            } catch (IOException ex) {
                logger.error("Failed to create operation file.", ex);
            }
        }
    }

    /**
     * Saves information about a track to the CSV file for the current operation.
     *
     * @param info             the trackinfo object with info about the track.
     * @param time             time of import.
     * @param originalFile     the name of the original/raw file.
     * @param processedFile    the name of the processed file.
     * @param originalFileHash the hash of the original/raw file.
     */
    void saveTrackFileInfo(TrackInfo info, String time, String originalFile, String processedFile, String originalFileHash) {
        FileTools.writeToCsvFile(trackFileInfo,
                info.getCrewType(), info.getCrewNumber() + "", info.getCrewCount() + "", info.getAreaSearched(),
                info.getDistance() + "", info.getTrackNumber() + "", info.getComment().isEmpty() ? "Ingen kommentar" : info.getComment(),
                time, originalFile, processedFile, originalFileHash
        );
    }

    /**
     * Saves information about a waypoint to the CSV file for the current operation.
     *
     * @param comment       comment about the waypoint.
     * @param originalFile  the name of the original/raw file.
     * @param processedFile the name of the processed file.
     * @param originalHash  the hash of the original/raw file.
     */
    void saveWaypointFileInfo(String comment, String originalFile, String processedFile, String originalHash) {
        FileTools.writeToCsvFile(waypointFileInfo, comment, originalFile, processedFile, originalHash);
    }

    /**
     * Replaces the content of the operation file with the new operation info.
     *
     * @param operation the current operation.
     */
    public void updateOperationFile(Operation operation) {
        try {
            File operationFile = new File(operationFolder, operation.getName().trim().replace(" ", "_") + ".txt");
            if (!operationFile.exists()) {
                operationFile.createNewFile();
            } else {
                FileTools.clearFile(operationFile);
            }
            FileTools.writeToFile(operation.getFileContent(), operationFile);
        } catch (IOException ex) {
            logger.error("Failed to update operation file.", ex);
        }
    }
}
