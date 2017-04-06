package no.hvl.dowhile.utility;

import org.alternativevision.gpx.beans.GPX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static void insertXmlData(GPX gpx, File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            String creator = gpx.getCreator();
            String version = gpx.getVersion();
            String replacedLine = line.replace("creator=\"" + creator + "\" " + "version=\"version\"", "xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:gpxtrkx=\"http://www.garmin.com/xmlschemas/TrackStatsExtension/v1\" xmlns:wptx1=\"http://www.garmin.com/xmlschemas/WaypointExtension/v1\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" creator=\"" + creator + "\" version=\"" + version + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackStatsExtension/v1 http://www8.garmin.com/xmlschemas/TrackStatsExtension.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\"");
            FileWriter writer = new FileWriter(file);
            writer.write(replacedLine);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            System.err.println("Failed while inserting xml data.");
        }
    }
}
