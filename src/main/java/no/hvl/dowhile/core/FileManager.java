package no.hvl.dowhile.core;

import com.hs.gpxparser.GPXWriter;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.Waypoint;
import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Managing files and has methods for storing files in the application file system.
 */
public class FileManager {
    private final OperationManager OPERATION_MANAGER;
    private File appFolder;

    private OperationFolder mainOperationFolder;
    private List<OperationFolder> extraOperationFolders;

    public FileManager(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.extraOperationFolders = new ArrayList<>();
    }

    public String getMainOperationFolderPath() {
        return mainOperationFolder.getOperationFolder().getAbsolutePath();
    }

    /**
     * Get the appfolder which is the place for the config and all the configuration file.
     *
     * @return the folder for the app.
     */
    public File getAppFolder() {
        return appFolder;
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
     * Deletes a file from the current operation.
     * Should be used if an import is quit before the file is processed.
     *
     * @param filename the name of the file to delete.
     */
    public void deleteRawFileInFolders(String filename) {
        deleteRawFile(mainOperationFolder.getRawFolder(), filename);
        for (OperationFolder operationFolder : extraOperationFolders) {
            deleteRawFile(operationFolder.getRawFolder(), filename);
        }
    }

    /**
     * Deletes the specified file from the rawfolder.
     *
     * @param filename the name of the file to delete.
     */
    public void deleteRawFile(File rawFolder, String filename) {
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
     * Sets up folder for storing processed and raw files.
     *
     * @param listRoot the drive to store the files.
     */
    public void setupLocalFolders(File listRoot) {
        appFolder = setupFolder(listRoot, "TrackGrabber");
        setupConfig(appFolder);
    }

    /**
     * Sets up the config file.
     */
    public void setupConfig(File folder) {
        File config = FileTools.getFile(folder, "config.txt");
        if (config == null) {
            System.err.println("Config doesn't exist. Creating.");
            config = new File(folder, "config.txt");
            try {
                config.createNewFile();
                FileTools.writeToFile(OPERATION_MANAGER.getConfig().getConfigTemplate(), config);
                System.err.println("Config created.");
            } catch (IOException ex) {
                System.err.println("Failed while creating config file.");
            }
        }
        OPERATION_MANAGER.getConfig().parseConfigFile(config);
    }

    /**
     * Finds the operation folders and parses the operation files.
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
     * Sets up the folder for the operation with a raw folder, processed folder and operation info file.
     *
     * @param operation the operation to setup.
     */
    public void setupMainOperationFolder(Operation operation) {
        mainOperationFolder = new OperationFolder(operation, appFolder, true);
    }

    /**
     * Sets up the extra operation folder for the given operation at the given path.
     *
     * @param operation the operation to create a folder for.
     * @param path      the path to save it.
     */
    public void setupExtraOperationFolder(Operation operation, String path) {
        File root = new File(path);
        if (!root.exists()) {
            root.mkdir();
        }
        extraOperationFolders.add(new OperationFolder(operation, root, false));
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
     * Replaces the content of the operation file with the new operation info.
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
     * Checks if a file has already been saved in the rawfolder.
     *
     * @param newGpx The gpx file to check.
     * @return true if the file is matching a file, false if not.
     */
    public List<Waypoint> alreadyImportedTrack(GPX newGpx) {
        System.err.println("alreadyImportedTrack()");
        File[] rawFiles = mainOperationFolder.getRawFolder().listFiles();
        if (rawFiles == null || rawFiles.length == 0) {
            System.err.println("rawFiles is null or empty. Returning no points.");
            return new ArrayList<>();
        }
        Track newTrack = TrackTools.getTrackFromGPXFile(newGpx);
        if (newTrack != null) {
            return TrackTools.duplicateGpx(rawFiles, newTrack);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Checks if a waypoint has been saved in the waypoint folder already.
     *
     * @param waypointGpx the gpx with the waypoint to check.
     * @return true if the waypoint has been imported, false if not.
     */
    public boolean waypointIsAlreadyImported(GPX waypointGpx) {
        File[] waypointFiles = mainOperationFolder.getWaypointFolder().listFiles();
        if (waypointFiles == null || waypointFiles.length == 0) {
            return false;
        }
        boolean matchingWaypoint = false;
        for (int i = 0; i < waypointFiles.length && !matchingWaypoint; i++) {
            GPX oldWaypointGpx = TrackTools.getGpxFromFile(waypointFiles[i]);
            if (oldWaypointGpx != null && TrackTools.hasWaypoints(oldWaypointGpx)) {
                matchingWaypoint = TrackTools.matchingWaypoints(oldWaypointGpx.getWaypoints().iterator().next(), waypointGpx.getWaypoints().iterator().next());
            }
        }
        return matchingWaypoint;
    }

    /**
     * Saves information about a track to the CSV file for the current operation.
     *
     * @param info          the trackinfo object with info about the track.
     * @param time          time of import.
     * @param originalFile  the name of the original/raw file.
     * @param processedFile the name of the processed file.
     * @param originalHash  the hash of the original/raw file.
     */
    public void saveTrackFileInfo(TrackInfo info, String time, String originalFile, String processedFile, String originalHash) {
        mainOperationFolder.saveTrackFileInfo(info, time, originalFile, processedFile, originalHash);
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
        mainOperationFolder.saveWaypointFileInfo(comment, originalFile, processedFile, originalHash);
    }

    /**
     * Saves the given gpx files in all folders related to this operation.
     *
     * @param rawGpx   the gpx to save.
     * @param filename the name to save it as.
     */
    public String saveRawGpxFileInFolders(GPX rawGpx, String filename) {
        String hash = saveRawGpxFile(mainOperationFolder.getRawFolder(), rawGpx, filename);
        for (OperationFolder operationFolder : extraOperationFolders) {
            saveRawGpxFile(operationFolder.getRawFolder(), rawGpx, filename);
        }
        return hash;
    }

    /**
     * Saves the specified file in the raw folder as the specified filename.
     *
     * @param rawGpx   the gpx file to save.
     * @param filename the name for the new file.
     * @return hash of the saved file.
     */
    private String saveRawGpxFile(File rawFolder, GPX rawGpx, String filename) {
        return saveAndHashGpxFile(rawGpx, null, filename, rawFolder);
    }

    /**
     * Saves the gpx in the processed folder and the various orginazation folders.
     *
     * @param processedGpx the gpx to save.
     * @param trackInfo    info about the track.
     * @param filename     the name to save as.
     */
    public void saveProcessedGpxFileInFolders(GPX processedGpx, TrackInfo trackInfo, String filename) {
        saveProcessedGpxFile(mainOperationFolder.getProcessedFolder(), trackInfo, processedGpx, filename);
        for (OperationFolder operationFolder : extraOperationFolders) {
            saveProcessedGpxFile(operationFolder.getProcessedFolder(), trackInfo, processedGpx, filename);
        }
        organizeFile(processedGpx, trackInfo, filename);
    }

    /**
     * Organizes the file into folders by crew type, area and date.
     *
     * @param processedGpx the gpx to save.
     * @param trackInfo    the info about the track.
     * @param filename     the name to save the file as.
     */
    public void organizeFile(GPX processedGpx, TrackInfo trackInfo, String filename) {
        organizeGpxInCrewFolders(processedGpx, trackInfo, filename);
        organizeGpxInAreaFolders(processedGpx, trackInfo, filename);
        organizeGpxInDayFolders(processedGpx, trackInfo, filename);
    }

    /**
     * Organizes the file into the appropriate crew folder.
     *
     * @param processedGpx The file to store.
     * @param trackInfo    The info that helps find the right folder.
     * @param filename     The name of the file.
     */
    public void organizeGpxInCrewFolders(GPX processedGpx, TrackInfo trackInfo, String filename) {
        File crewFolder = setupFolder(mainOperationFolder.getCrewOrgFolder(), trackInfo.getCrewType());
        saveGpxFile(processedGpx, trackInfo, filename, crewFolder);
        for (OperationFolder operationFolder : extraOperationFolders) {
            crewFolder = setupFolder(operationFolder.getCrewOrgFolder(), trackInfo.getCrewType());
            saveGpxFile(processedGpx, trackInfo, filename, crewFolder);
        }
    }

    /**
     * Organizes the file into the appropriate area folder(s).
     *
     * @param processedGpx The file to store.
     * @param trackInfo    The info that helps find the correct folder(s).
     * @param filename     The name of the file.
     */
    public void organizeGpxInAreaFolders(GPX processedGpx, TrackInfo trackInfo, String filename) {
        List<String> areaNumbers = FileTools.getAreasFromString(trackInfo.getAreaSearched());
        for (String areaNumber : areaNumbers) {
            File areaFolder = setupFolder(mainOperationFolder.getAreaOrgFolder(), areaNumber);
            saveGpxFile(processedGpx, trackInfo, filename, areaFolder);
            for (OperationFolder operationFolder : extraOperationFolders) {
                areaFolder = setupFolder(operationFolder.getAreaOrgFolder(), areaNumber);
                saveGpxFile(processedGpx, trackInfo, filename, areaFolder);
            }
        }
    }

    /**
     * Organizes the file into the correct day folder.
     *
     * @param processedGpx The file to store.
     * @param filename     The name of the file.
     */
    public void organizeGpxInDayFolders(GPX processedGpx, TrackInfo trackInfo, String filename) {
        String dateString = TrackTools.getDayStringFromTrack(processedGpx);
        File dateFolder = setupFolder(mainOperationFolder.getDayOrgFolder(), dateString);
        saveGpxFile(processedGpx, trackInfo, filename, dateFolder);
        for (OperationFolder operationFolder : extraOperationFolders) {
            dateFolder = setupFolder(operationFolder.getDayOrgFolder(), dateString);
            saveGpxFile(processedGpx, trackInfo, filename, dateFolder);
        }
    }

    /**
     * Saves the specified file in the processed folder as the specified filename.
     *
     * @param processedGpx the gpx file to save.
     * @param filename     the name for the new file.
     */
    private void saveProcessedGpxFile(File processedFolder, TrackInfo trackInfo, GPX processedGpx, String filename) {
        saveGpxFile(processedGpx, trackInfo, filename, processedFolder);
    }

    /**
     * Saves the area file in all operation folders.
     *
     * @param areaGpx  The area file to save.
     * @param filename The name of the file.
     */
    public void saveAreaGpxFileInFolders(GPX areaGpx, String filename) {
        saveAreaGpxFile(mainOperationFolder.getAreaFolder(), areaGpx, filename);
        for (OperationFolder operationFolder : extraOperationFolders) {
            saveAreaGpxFile(operationFolder.getAreaFolder(), areaGpx, filename);
        }
    }

    /**
     * Saves the area file in the area folder as the specified filename.
     *
     * @param areaGPX  the gpx file to save.
     * @param filename the name for the new file.
     */
    private void saveAreaGpxFile(File areaFolder, GPX areaGPX, String filename) {
        saveGpxFile(areaGPX, null, filename, areaFolder);
    }

    /**
     * Saves a waypoint to all necessary folders.
     * @param waypointGpx The waypoint GPX file to save.
     * @param filename The name of the waypoint GPX file.
     */
    public void saveWaypointFileInFolders(GPX waypointGpx, String filename) {
        saveGpxFile(waypointGpx, null, filename, mainOperationFolder.getWaypointFolder());
        for (OperationFolder operationFolder : extraOperationFolders) {
            saveGpxFile(waypointGpx, null, filename, operationFolder.getWaypointFolder());
        }
    }

    /**
     * Saves the waypoint file in the waypoint folder as the specified filename.
     *
     * @param waypointFile the file to save.
     * @param filename     the name to save it as.
     */
    private void saveWaypointFile(File waypointFolder, File waypointFile, String filename) {
        saveFile(waypointFile, filename, waypointFolder);
    }

    /**
     * Saving the file in the specified folder as the specified filename.
     *
     * @param gpx      the gpx to save.
     * @param filename the name for the new file.
     * @param folder   the folder to save it in.
     */
    public void saveGpxFile(GPX gpx, TrackInfo trackInfo, String filename, File folder) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            GPXWriter gpxWriter = new GPXWriter();
            FileOutputStream outputStream = new FileOutputStream(file);
            gpxWriter.writeGPX(gpx, outputStream);
            outputStream.close();
            FileTools.cleanXmlFile(gpx, file);
            if (trackInfo != null) {
                String color = OPERATION_MANAGER.getConfig().getColorForTeam(trackInfo.getCrewType());
                if (color != null) {
                    FileTools.insertDisplayColor(gpx, file, color);
                }
            }
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
     * Saves the file in the specified folder as the specified filename.
     *
     * @param gpx      the gpx to save.
     * @param filename the name for the new file.
     * @param folder   the folder to save it in.
     * @return hash value of the saved file.
     */
    public String saveAndHashGpxFile(GPX gpx, TrackInfo trackInfo, String filename, File folder) {
        String hash = "";
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            GPXWriter gpxWriter = new GPXWriter();
            FileOutputStream outputStream = new FileOutputStream(file);
            gpxWriter.writeGPX(gpx, outputStream);
            outputStream.close();
            FileTools.cleanXmlFile(gpx, file);
            if (trackInfo != null) {
                String color = OPERATION_MANAGER.getConfig().getColorForTeam(trackInfo.getCrewType());
                if (color != null) {
                    FileTools.insertDisplayColor(gpx, file, color);
                }
            }
            hash = FileTools.hashFile(file);
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
        return hash;
    }

    /**
     * Saves the file in the specified folder as the specified filename.
     *
     * @param fileToSave the file to save.
     * @param filename   the name for the new file.
     * @param folder     the folder to save it in.
     */
    public void saveFile(File fileToSave, String filename, File folder) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.copy(fileToSave.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.err.println("Failed to save file.");
            ex.printStackTrace();
        }
    }

    /**
     * Sets the raw folder for the current operation.
     *
     * @param rawFolder the folder to save raw files.
     */
    public void setRawFolder(File rawFolder) {
        mainOperationFolder.setRawFolder(rawFolder);
    }

    /**
     * Sets the processed folder for the current operation.
     *
     * @param processedFolder the folder to save processed files.
     */
    public void setProcessedFolder(File processedFolder) {
        mainOperationFolder.setProcessedFolder(processedFolder);
    }

    /**
     * Gets the main operation folder for the current operation.
     *
     * @return the main operation folder.
     */
    public OperationFolder getMainOperationFolder() {
        return mainOperationFolder;
    }
}
