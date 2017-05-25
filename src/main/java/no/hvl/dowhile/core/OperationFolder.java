package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;

import java.io.File;
import java.io.IOException;

/**
 * Stores folders for a given file location to store files for the current operation.
 */
public class OperationFolder {
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
    public OperationFolder(Operation operation, File root, boolean mainFolder) {
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
        System.err.println("Done creating folders for operation " + operation.getName());
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
     * Sets the operation folder.
     *
     * @param operationFolder the operation folder.
     */
    public void setOperationFolder(File operationFolder) {
        this.operationFolder = operationFolder;
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
     * Sets the folder to store processed tracks.
     *
     * @param processedFolder the folder to store processed tracks.
     */
    public void setProcessedFolder(File processedFolder) {
        this.processedFolder = processedFolder;
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
     * Sets the folder to store all rawfiles.
     *
     * @param rawFolder the folder to store rawfiles..
     */
    public void setRawFolder(File rawFolder) {
        this.rawFolder = rawFolder;
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
     * Sets the folder to store areas.
     *
     * @param areaFolder the folder to store areas.
     */
    public void setAreaFolder(File areaFolder) {
        this.areaFolder = areaFolder;
    }

    /**
     * Gets the folder to store waypoints.
     *
     * @return the folder to store waypoints.
     */
    public File getWaypointFolder() {
        return waypointFolder;
    }

    /**
     * Sets the folder to store waypoints.
     *
     * @param waypointFolder the folder to store waypoints.
     */
    public void setWaypointFolder(File waypointFolder) {
        this.waypointFolder = waypointFolder;
    }

    /**
     * Gets the folder to store organizations of files.
     *
     * @return the folder to hold organized files.
     */
    public File getOrganizingFolder() {
        return organizingFolder;
    }

    /**
     * Sets the folder to store organizations of files.
     *
     * @param organizingFolder the folder to store organizations of files.
     */
    public void setOrganizingFolder(File organizingFolder) {
        this.organizingFolder = organizingFolder;
    }

    /**
     * Gets the folder to store organizations of crew types.
     *
     * @return the folder to store organizations of crew types.
     */
    public File getCrewOrgFolder() {
        return crewOrgFolder;
    }

    /**
     * Sets the folder to store organizations of crew types.
     *
     * @param crewOrgFolder the folder to store organizations of crew types.
     */
    public void setCrewOrgFolder(File crewOrgFolder) {
        this.crewOrgFolder = crewOrgFolder;
    }

    /**
     * Gets the folder to store organizations of areas.
     *
     * @return the folder to store organizations of areas.
     */
    public File getAreaOrgFolder() {
        return areaOrgFolder;
    }

    /**
     * Sets the folder to store organizations of areas.
     *
     * @param areaOrgFolder the folder to store organizations of areas.
     */
    public void setAreaOrgFolder(File areaOrgFolder) {
        this.areaOrgFolder = areaOrgFolder;
    }

    /**
     * Gets the folder to store organizations of days.
     *
     * @return the folder to store organizations of days.
     */
    public File getDayOrgFolder() {
        return dayOrgFolder;
    }

    /**
     * Sets the folder to store organizations of days.
     *
     * @param dayOrgFolder the folder to store organizations of days.
     */
    public void setDayOrgFolder(File dayOrgFolder) {
        this.dayOrgFolder = dayOrgFolder;
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
                System.err.println("Failed to create operation file.");
            }
            FileTools.writeToFile(operation.getFileContent(), operationFile);
        }
        if (!trackFileInfo.exists()) {
            try {
                trackFileInfo.createNewFile();
                FileTools.writeToCsvFile(trackFileInfo, "Lagtype", "Lagnummer", "Antall mann", "Teiger", "Lengde", "Spornummer", "Kommentar", "Tid", "Original fil", "Prosessert fil", "Original hash");
            } catch (IOException ex) {
                System.err.println("Failed to create operation file.");
            }
        }
        if (!waypointFileInfo.exists()) {
            try {
                waypointFileInfo.createNewFile();
                FileTools.writeToCsvFile(waypointFileInfo, "Kommentar", "Original fil", "Prosessert fil", "Original hash");
            } catch (IOException ex) {
                System.err.println("Failed to create operation file.");
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
    public void saveTrackFileInfo(TrackInfo info, String time, String originalFile, String processedFile, String originalFileHash) {
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
    public void saveWaypointFileInfo(String comment, String originalFile, String processedFile, String originalHash) {
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
            System.err.println("Failed to update operation file.");
        }
    }
}
