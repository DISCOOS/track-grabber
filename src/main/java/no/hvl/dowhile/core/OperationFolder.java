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
    private File wayPointFolder;

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
        wayPointFolder = FileTools.setupFolder(operationFolder, "WayPoints");
        if (mainFolder) {
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
    public File getWayPointFolder() {
        return wayPointFolder;
    }

    /**
     * Set the folder to store waypoints.
     * @param wayPointFolder the folder to store waypoints.
     */
    public void setWayPointFolder(File wayPointFolder) {
        this.wayPointFolder = wayPointFolder;
    }

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
