package no.hvl.dowhile.utility;

import no.hvl.dowhile.core.parser.DisplayColorExtensionParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods to work with files.
 */
public class FileTools {
    /**
     * Clear the specified file.
     *
     * @param file the file to clear.
     */
    public static void clearFile(File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException ex) {
            System.err.println("Failed while attempting to clear file " + file.getName());
        }
    }

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
            String dataToReplace = "creator=\"" + creator + "\" " + "version=\"" + version + "\"";
            String replacedLine = line.replace(dataToReplace, "xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:gpxtrkx=\"http://www.garmin.com/xmlschemas/TrackStatsExtension/v1\" xmlns:wptx1=\"http://www.garmin.com/xmlschemas/WaypointExtension/v1\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" creator=\"" + creator + "\" version=\"" + version + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www8.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackStatsExtension/v1 http://www8.garmin.com/xmlschemas/TrackStatsExtension.xsd http://www.garmin.com/xmlschemas/WaypointExtension/v1 http://www8.garmin.com/xmlschemas/WaypointExtensionv1.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\"");
            FileWriter writer = new FileWriter(file);
            writer.write(replacedLine);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            System.err.println("Failed while inserting xml data.");
        }
    }

    public static void insertDisplayColor(GPX gpx, File file) {
        Track track = TrackTools.getTrackFromGPXFile(gpx);
        String displayColor = (String) track.getExtensionData(new DisplayColorExtensionParser().getId());
        if (displayColor == null) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            String[] parts = line.split("</name>");
            String replacedLine = parts[0] + "</name><extensions><gpxx:TrackExtension><gpxx:DisplayColor>" + displayColor + "</gpxx:DisplayColor></gpxx:TrackExtension></extensions>" + parts[1];
            if (replacedLine.contains("<extensions/>")) {
                replacedLine = replacedLine.replace("<extensions/>", ""); // Resolving weird parsing bug.
            }
            FileWriter writer = new FileWriter(file);
            writer.write(replacedLine);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            System.err.println("Failed while inserting xml data.");
        }
    }
}
