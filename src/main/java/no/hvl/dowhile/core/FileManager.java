package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Managing files and has methods for storing files in the application file system.
 */
public class FileManager {
    private final OperationManager OPERATION_MANAGER;
    private File appFolder;
    private File processedFolder;
    private File rawFolder;

    public FileManager(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
    }

    /**
     * Deletes the specified file from the rawfolder.
     *
     * @param filename the name of the file to delete.
     */
    public void deleteRawFile(String filename) {
        File file = new File(rawFolder, filename);
        if (!file.exists()) {
            return;
        }
        boolean deleted = file.delete();
        if (deleted) {
            System.err.println("File " + filename + " deleted.");
        } else {
            System.err.println("Tried to delete " + filename + " from rawfolder, however it didn't work.");
        }
    }

    /**
     * Setting up folder for storing processed and raw files.
     *
     * @param listRoot the drive to store the files.
     */
    public void setupLocalFolders(File listRoot) {
        appFolder = setupFolder(listRoot, "TrackGrabber");
        setupConfig(appFolder);
        parseFilenameFromConfig();
    }

    /**
     * Setting up the config file.
     */
    public void setupConfig(File folder) {
        File config = FileTools.getFile(folder, "config.txt");
        if (config == null) {
            System.err.println("Config doesn't exist. Creating.");
            File file = new File(folder, "config.txt");
            try {
                file.createNewFile();
                FileTools.writeToFile(OPERATION_MANAGER.getConfig().getConfigTemplate(), file);
                System.err.println("Config created.");
            } catch (IOException ex) {
                System.err.println("Failed while creating config file.");
            }
        }
    }

    /**
     * Finding the operation folders and parsing the operation files.
     *
     * @return list of the operations existing in the file system.
     */
    public List<Operation> loadExistingOperations() {
        List<Operation> operations = new ArrayList<>();
        File[] filesInAppFolder = appFolder.listFiles();
        if (filesInAppFolder == null || filesInAppFolder.length == 0) {
            return operations;
        }
        for (File file : filesInAppFolder) {
            if (file.isDirectory()) {
                File[] operationFiles = file.listFiles();
                if (operationFiles != null && operationFiles.length != 0) {
                    for (File fileInOperationFolder : operationFiles) {
                        if (fileInOperationFolder.getName().endsWith(".txt")) {
                            try {
                                Operation operation = new Operation(fileInOperationFolder);
                                operations.add(operation);
                            } catch (Exception ex) {
                                System.err.println("Failed while parsing operation file.");
                            }
                        }
                    }
                }
            }
        }
        return operations;
    }

    /**
     * Setting up the folder for the operation with a raw folder, processed folder and operation info file.
     *
     * @param operation the operation to setup.
     */
    public void setupOperationFolder(Operation operation) {
        File operationFolder = setupFolder(appFolder, operation.getName().trim().replace(" ", "_"));
        rawFolder = setupFolder(operationFolder, "Raw");
        processedFolder = setupFolder(operationFolder, "Processed");
        createOperationFile(operation, operationFolder);
        System.err.println("Done creating folders for operation " + operation.getName());
    }

    /**
     * Creates an operation file for the given Operation, into the given operation folder.
     *
     * @param operation the operation to create the file for.
     * @param folder    the folder to save the file to.
     */
    public void createOperationFile(Operation operation, File folder) {
        File operationFile = new File(folder, operation.getName().trim().replace(" ", "_") + ".txt");
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
            File operationFolder = new File(appFolder, operation.getName().trim().replace(" ", "_"));
            if (!operationFolder.exists()) {
                operationFolder.mkdir();
            }
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

    /**
     * Sets up a folder if it doesn't already exist.
     *
     * @param parentFolder the folder where you want to create the new folder.
     * @param name         the name of the new folder.
     * @return the folder which was created.
     */
    public File setupFolder(File parentFolder, String name) {
        File folder = new File(parentFolder, name);
        boolean folderCreated = folder.mkdir();
        if (folderCreated) {
            System.err.println(name + " folder didn't exist. Created!");
        }
        return folder;
    }

    /**
     * Checking if a file has been saved in the rawfolder already.
     *
     * @param newGpx The gpx file to check.
     * @return true if the file is matching a file, false if not.
     */
    public boolean fileAlreadyImported(GPX newGpx) {
        File[] rawFiles = rawFolder.listFiles();
        if (rawFiles == null || rawFiles.length == 0) {
            return false;
        }
        Track newTrack = TrackTools.getTrackFromGPXFile(newGpx);
        return newTrack != null && TrackTools.trackPointsAreEqual(rawFiles, newTrack);
    }

    /**
     * Saving the specified file in the raw folder as the specified filename.
     *
     * @param rawGpx   the gpx file to save.
     * @param filename the name for the new file.
     */
    public void saveRawGpxFile(GPX rawGpx, String filename) {
        saveGpxFile(rawGpx, filename, rawFolder);
    }

    /**
     * Saving the specified file in the processed folder as the specified filename.
     *
     * @param processedGpx the gpx file to save.
     * @param filename     the name for the new file.
     */
    public void saveProcessedGpxFile(GPX processedGpx, String filename) {
        saveGpxFile(processedGpx, filename, processedFolder);
    }

    /**
     * Saving the file in the specified folder as the specified filename.
     *
     * @param gpx      the gpx to save.
     * @param filename the name for the new file.
     * @param folder   the folder to save it in.
     */
    public void saveGpxFile(GPX gpx, String filename, File folder) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            new GPXParser().writeGPX(gpx, outputStream);
            outputStream.close();
            FileTools.insertXmlData(gpx, file);
            FileTools.insertDisplayColor(gpx, file);
        } catch (IOException ex) {
            System.err.println("Failed to save raw file.");
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            System.err.println("Parser is not configured correctly.");
            ex.printStackTrace();
        } catch (TransformerException ex) {
            System.err.println("Failed to transform raw file.");
            ex.printStackTrace();
        }
    }

    /**
     * Gets the filename pattern from the config file.
     */
    private void parseFilenameFromConfig() {
        File config = FileTools.getFile(appFolder, "config.txt");
        if (config == null) {
            System.err.println("Config didn't exist when trying to parse filename pattern.");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("#")) {
                    if (line.startsWith("filename")) {
                        String[] parts = line.split("=");
                        if (parts.length == 2) {
                            String pattern = parts[1];
                            OPERATION_MANAGER.getConfig().setPattern(pattern);
                            System.err.println("Sporene vil lagres på følgende format: " + pattern);
                        }
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException ex) {
            System.err.println("Failed while reading from config file.");
        }
    }

    /**
     * Sets the folder for the application.
     *
     * @param appFolder an app folder
     */
    public void setAppFolder(File appFolder) {
        this.appFolder = appFolder;
    }

    /**
     * Sets the raw folder for the current operation.
     *
     * @param rawFolder the folder to save raw files.
     */
    public void setRawFolder(File rawFolder) {
        this.rawFolder = rawFolder;
    }

    /**
     * Sets the processed folder for the current operation.
     *
     * @param processedFolder the folder to save processed files.
     */
    public void setProcessedFolder(File processedFolder) {
        this.processedFolder = processedFolder;
    }
}
