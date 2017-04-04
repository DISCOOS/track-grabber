package no.hvl.dowhile.core;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Managing files and has methods for storing files in the application file system.
 */
public class FileManager {
    private File appFolder;
    private File processedFolder;
    private File rawFolder;

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

        return false;
    }

    /**
     * Saving the specified file in the raw folder as the specified filename.
     *
     * @param rawGpx   the gpx file to save.
     * @param filename the name for the new file.
     */
    public void saveRawGpxFile(GPX rawGpx, String filename) {
        try {
            File file = new File(rawFolder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            new GPXParser().writeGPX(rawGpx, new FileOutputStream(file));
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
     * Saving the specified file in the processed folder as the specified filename.
     *
     * @param processedGpx the gpx file to save.
     * @param filename     the name for the new file.
     */
    public void saveProcessedGpxFile(GPX processedGpx, String filename) {
        try {
            File file = new File(processedFolder, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            new GPXParser().writeGPX(processedGpx, new FileOutputStream(file));
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
}
