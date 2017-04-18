package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.FileTools;
import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
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
     * Setting up folder for storing processed and raw files.
     *
     * @param listRoot the drive to store the files.
     */
    public void setupLocalFolders(File listRoot) {
        appFolder = new File(listRoot, "TrackGrabber");
        boolean appFolderCreated = appFolder.mkdir();
        if (appFolderCreated) {
            System.err.println("App folder didn't exist. Created!");
        }
        processedFolder = new File(appFolder, "Processed");
        boolean processedFolderCreated = processedFolder.mkdir();
        if (processedFolderCreated) {
            System.err.println("Folder for processed files didn't exist. Created!");
        }
        rawFolder = new File(appFolder, "Rawfiles");
        boolean rawFolderCreated = rawFolder.mkdir();
        if (rawFolderCreated) {
            System.err.println("Folder for raw files didn't exist. Created!");
        }
        boolean configCreated = false;
        File[] appFolderFiles = appFolder.listFiles();
        if (appFolderFiles != null) {
            for (File file : appFolderFiles) {
                if (file.getName().equals("config.txt")) {
                    configCreated = true;
                }
            }
        }
        if (!configCreated) {
            System.err.println("Config doesn't exist. Creating.");
            File file = new File(appFolder, "config.txt");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                for (String line : OPERATION_MANAGER.getConfig().getConfigTemplate()) {
                    writer.write(line + System.lineSeparator());
                }
                writer.flush();
                writer.close();
                System.err.println("Config created.");
            } catch (IOException ex) {
                System.err.println("Failed while creating config file.");
            }
        }
        parseFilenameFromConfig();
    }

    /**
     * Checking if a file has been saved in the rawfolder already.
     *
     * @param newGpx The gpx file to check.
     * @return true if the file is matching a file, false if not.
     */
    public boolean fileAlreadyImported(GPX newGpx, String newFilename) {
        System.err.println("[FileManager] Duplicate check!");
        File[] rawFiles = rawFolder.listFiles();
        if (rawFiles == null || rawFiles.length == 0) {
            return false;
        }
        Track newTrack = TrackTools.getTrackFromGPXFile(newGpx);
        if (newTrack == null) {
            return false;
        }
        for (File rawFile : rawFiles) {
            GPX rawGpx = TrackTools.parseFileAsGPX(rawFile);
            if (rawGpx != null) {
                Track rawTrack = TrackTools.getTrackFromGPXFile(rawGpx);
                if (rawTrack != null) {
                    List<Waypoint> newPoints = newTrack.getTrackPoints();
                    List<Waypoint> rawPoints = rawTrack.getTrackPoints();
                    if (newPoints != null && rawPoints != null) {
                        if (newPoints.size() == rawPoints.size()) {
                            boolean trackPointsMatching = true;
                            for (int i = 0; trackPointsMatching && i < newPoints.size() && i < rawPoints.size(); i++) {
                                if (!TrackTools.matchingTrackPoints(newPoints.get(i), rawPoints.get(i))) {
                                    trackPointsMatching = false;
                                }
                            }
                            if (trackPointsMatching) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
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
    private void saveGpxFile(GPX gpx, String filename, File folder) {
        try {
            File file = new File(folder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            new GPXParser().writeGPX(gpx, new FileOutputStream(file));
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
        File file = new File(appFolder, "config.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
}
