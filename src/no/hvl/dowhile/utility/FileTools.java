package no.hvl.dowhile.utility;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods to work with files.
 */
public class FileTools {
    /**
     * Utility method to find all .gpx files in the folders.
     *
     * @param folders The folders to scan.
     * @return set with .gpx files.
     */
    public static Set<File> findGpxFiles(File... folders) {
        Set<File> gpxFiles = new HashSet<>();
        for (File folder : folders) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".gpx")) {
                        gpxFiles.add(file);
                    }
                }
            }
        }
        return gpxFiles;
    }
}
