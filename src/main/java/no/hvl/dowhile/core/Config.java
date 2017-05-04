package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.StringTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Reading and writing to the configuration file.
 */
public class Config {
    private String filename;
    private List<TeamType> teamTypes;

    public Config() {
        this.filename = "%LAGTYPE%%LAGNUMMER%_TEIG%TEIGNUMMER%_SPOR%SPORNUMMER%_%DATO%.gpx";
        this.teamTypes = new ArrayList<>(Arrays.asList(new TeamType("Mannskap", "Lime"), new TeamType("Hund", "Green"), new TeamType("Helikopter", "DarkRed")));
    }

    /**
     * Get the configuration template.
     *
     * @return the lines for the configuration file.
     */
    public String[] getConfigTemplate() {
        return new String[]{
                "# TrackGrabber Config - Konfigurasjon av filnavn og lagtype.",
                "# Du kan inkludere følgende variabler i navnet. De vil erstattes med faktisk data under kjøring.",
                "# %LAGTYPE% - Type lag som lagde sporet (helikopter, bil, atv, hund osv).",
                "# %LAGNUMMER% - Nummeret på laget som hadde GPS.",
                "# %TEIGNUMMER% - Teigen det ble søkt i.",
                "# %SPORNUMMER% - Nummeret på sporet, dersom et lag har flere GPS-er.",
                "# %DATO% - Dato og tid for import av sporet.",
                "#",
                "# Endre filnavnet under slik du vil ha det. Deler du ikke vil ha med kan enkelt fjernes/utelates.",
                "# Mellomrom er ikke tillatt. Bruk understrek.",
                "#",
                "filename=%LAGTYPE%%LAGNUMMER%_TEIG%TEIGNUMMER%_SPOR%SPORNUMMER%_%DATO%.gpx",
                "#",
                "# Oppgi lagtype og fargen sporet skal få:",
                "# Format: team=[Navn på lag],color=[Farge]",
                "team=Mannskap,color=Lime",
                "team=Hund,color=Green",
                "team=Helikopter,color=DarkRed",
        };
    }

    /**
     * Get the current pattern to use for the filenames.
     *
     * @return the pattern for the filenames.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the pattern to use for the filenames.
     *
     * @param filename the pattern for the filenames.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Get a list of the current team types for this operation and the corresponding color.
     *
     * @return list of the current team types.
     */
    public List<TeamType> getTeamTypes() {
        return teamTypes;
    }

    public List<String> getTeamNames() {
        List<String> teamNames = new ArrayList<>();
        for (TeamType teamType : teamTypes) {
            teamNames.add(teamType.getName());
        }
        return teamNames;
    }

    public void parseConfigFile(File file) {
        if (file == null || !file.getName().equals("config.txt")) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("#") && !line.equals("")) {
                    if (line.startsWith("filename")) {
                        if (line.contains("=")) {
                            String[] parts = line.split("=");
                            if (parts.length == 2) {
                                filename = parts[1];
                                System.err.println("[Config] Parsed filename " + filename);
                            }
                        }
                    } else if (line.startsWith("team")) {
                        if (line.contains(",")) {
                            String[] parts = line.split(",");
                            if (parts.length == 2 && parts[0].contains("=") && parts[1].contains("=")) {
                                String teamType = parts[0].split("=")[1];
                                String teamColor = parts[1].split("=")[1];
                                teamTypes.add(new TeamType(teamType, teamColor));
                                System.err.println("[Config] Parsed team type " + teamType + " with color " + teamColor);
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Insert track info data into the pattern to generate the filename.
     *
     * @param trackInfo the info to insert.
     * @return the filename.
     */
    public String generateFilename(TrackInfo trackInfo) {
        String filename = getFilename();
        List<String> variables = findVariables(filename);
        for (String variable : variables) {
            switch (variable) {
                case "%LAGTYPE%":
                    filename = filename.replace(variable, trackInfo.getCrewType());
                    break;
                case "%LAGNUMMER%":
                    filename = filename.replace(variable, "" + trackInfo.getCrewNumber());
                    break;
                case "%TEIGNUMMER%":
                    filename = filename.replace(variable, trackInfo.getAreaSearched());
                    break;
                case "%SPORNUMMER%":
                    filename = filename.replace(variable, "" + trackInfo.getTrackNumber());
                    break;
                case "%DATO%":
                    filename = filename.replace(variable, StringTools.formatDateForFile(Calendar.getInstance().getTime()));
                    break;
            }
        }
        return filename;
    }

    /**
     * Checking the filename to find the variables in the String.
     *
     * @param filename the filename to check.
     * @return list of variables in the given filename.
     */
    private List<String> findVariables(String filename) {
        char[] filenameArray = filename.toCharArray();
        List<String> variables = new ArrayList<>();
        StringBuilder currentVariable = new StringBuilder();
        boolean findingVariable = false; // To check if the loop is searching between the percent signs.
        for (int i = 0; i < filenameArray.length; i++) {
            char c = filenameArray[i];
            if (c == '%') {
                if (findingVariable) {
                    currentVariable.append(c);
                    findingVariable = false;
                    variables.add(currentVariable.toString());
                    currentVariable.setLength(0);
                } else {
                    currentVariable.append(c);
                    findingVariable = true;
                }
            } else {
                if (findingVariable) {
                    currentVariable.append(c);
                }
            }
        }
        return variables;
    }
}
