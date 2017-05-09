package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;

import java.io.File;
import java.io.IOException;

/**
 * Storing folders for a given file location to store files for the current operation.
 */
public class OperationFolder {
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
            createOperationFile(operation);
        }
        System.err.println("Done creating folders for operation " + operation.getName());
    }

    /**
     * Get the operation folder.
     * @return the operation folder.
     */
    public File getOperationFolder() {
        return operationFolder;
    }

    /**
     * Set the operation folder.
     * @param operationFolder the operation folder.
     */
    public void setOperationFolder(File operationFolder) {
        this.operationFolder = operationFolder;
    }

    /**
     * Get the folder to store processed tracks.
     * @return
     */
    public File getProcessedFolder() {
        return processedFolder;
    }

    /**
     * Set the folder to store processed tracks.
     * @param processedFolder the folder to store processed tracks.
     */
    public void setProcessedFolder(File processedFolder) {
        this.processedFolder = processedFolder;
    }

    /**
     * Get the folder to store rawfiles.
     * @return the folder to store rawfiles.
     */
    public File getRawFolder() {
        return rawFolder;
    }

    /**
     * Set the folder to store all rawfiles.
     * @param rawFolder the folder to store rawfiles..
     */
    public void setRawFolder(File rawFolder) {
        this.rawFolder = rawFolder;
    }

    /**
     * Get the folder to store areas.
     * @return the folder to store areas.
     */
    public File getAreaFolder() {
        return areaFolder;
    }

    /**
     * Set the folder to store areas.
     * @param areaFolder the folder to store areas.
     */
    public void setAreaFolder(File areaFolder) {
        this.areaFolder = areaFolder;
    }

    /**
     * Get the folder to store waypoints.
     * @return the folder to store waypoints.
     */
    public File getWaypointFolder() {
        return waypointFolder;
    }

    /**
     * Set the folder to store waypoints.
     * @param waypointFolder the folder to store waypoints.
     */
    public void setWaypointFolder(File waypointFolder) {
        this.waypointFolder = waypointFolder;
    }

    /**
     * Gets the folder to store organizations of files.
     */
    public File getOrganizingFolder() { return organizingFolder; }

    /**
     * Set the folder to store organizations of files.
     * @param organizingFolder the folder to store organizations of files.
     */
    public void setOrganizingFolder(File organizingFolder) { this.organizingFolder = organizingFolder; }

    /**
     * Get the folder to store organizations of crew types.
     * @return the folder to store organizations of crew types.
     */
    public File getCrewOrgFolder() {
        return crewOrgFolder;
    }

    /**
     * Set the folder to store organizations of crew types.
     * @param crewOrgFolder the folder to store organizations of crew types.
     */
    public void setCrewOrgFolder(File crewOrgFolder) { this.crewOrgFolder = crewOrgFolder; }

    /**
     * Get the folder to store organizations of areas.
     * @return the folder to store organizations of areas.
     */
    public File getAreaOrgFolder() {
        return areaOrgFolder;
    }

    /**
     * Set the folder to store organizations of areas.
     * @param areaOrgFolder the folder to store organizations of areas.
     */
    public void setAreaOrgFolder(File areaOrgFolder) { this.areaOrgFolder = areaOrgFolder; }

    /**
     * Get the folder to store organizations of days.
     * @return the folder to store organizations of days.
     */
    public File getDayOrgFolder() {
        return dayOrgFolder;
    }

    /**
     * Set the folder to store organizations of days.
     * @param dayOrgFolder the folder to store organizations of days.
     */
    public void setDayOrgFolder(File dayOrgFolder) { this.dayOrgFolder = dayOrgFolder; }

    /**
     * Creates an operation file for the given Operation, into the given operation folder.
     *
     * @param operation the operation to create the file for.
     */
    private void createOperationFile(Operation operation) {
        File operationFile = new File(operationFolder, operation.getName().trim().replace(" ", "_") + ".txt");
        if (!operationFile.exists()) {
            try {
                operationFile.createNewFile();
            } catch (IOException ex) {
                System.err.println("Failed to create operation file.");
            }
        }
        FileTools.writeToFile(operation.getFileContent(), operationFile);
    }

    /**
     * Replacing the content of the operation file with the new operation info.
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
