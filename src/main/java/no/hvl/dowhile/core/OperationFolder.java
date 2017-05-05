package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;

import java.io.File;
import java.io.IOException;

public class OperationFolder {
    private File operationFolder;
    private File processedFolder;
    private File rawFolder;
    private File areaFolder;
    private File wayPointFolder;

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

    public File getOperationFolder() {
        return operationFolder;
    }

    public void setOperationFolder(File operationFolder) {
        this.operationFolder = operationFolder;
    }

    public File getProcessedFolder() {
        return processedFolder;
    }

    public void setProcessedFolder(File processedFolder) {
        this.processedFolder = processedFolder;
    }

    public File getRawFolder() {
        return rawFolder;
    }

    public void setRawFolder(File rawFolder) {
        this.rawFolder = rawFolder;
    }

    public File getAreaFolder() {
        return areaFolder;
    }

    public void setAreaFolder(File areaFolder) {
        this.areaFolder = areaFolder;
    }

    public File getWayPointFolder() {
        return wayPointFolder;
    }

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
