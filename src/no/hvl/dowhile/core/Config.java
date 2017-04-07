package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.StringTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Config {
    private final String VERSION = "1";
    private String pattern;

    public String[] getConfigInstructions() {
        return new String[]{
                "# TrackGrabber Config - Egendefinert navngivning av filer.",
                "# Ikke endre versjonsnummeret under!",
                "version=" + VERSION,
                "# Du kan inkludere følgende variabler i navnet. De vil erstattes med faktisk data under kjøring.",
                "# %LAGTYPE% - Type lag som lagde sporet (helikopter, bil, atv, hund osv).",
                "# %LAGNUMMER% - Nummeret på laget som hadde GPS.",
                "# %TEIGNUMMER% - Teigen det ble søkt i.",
                "# %SPORNUMMER% - Nummeret på sporet, dersom et lag har flere GPS-er.",
                "# %DATO% - Dato og tid for import av sporet.",
                "#",
                "# Endre filnavnet under slik du vil ha det. Deler du ikke vil ha med kan enkelt fjernes/utelates.",
                "# Mellomrom er ikke tillatt. Bruk understrek.",
                "",
                "filename=%LAGTYPE%%LAGNUMMER%_TEIG%TEIGNUMMER%_SPOR%SPORNUMMER%_%DATO%.gpx"
        };
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String generateFilename(TrackInfo trackInfo) {
        String crewType = trackInfo.getCrewType();
        int crewNumber = trackInfo.getCrewNumber();
        int crewCount = trackInfo.getCrewCount();
        String areaSearched = trackInfo.getAreaSearched();

        String filename = getPattern();
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
                    filename = filename.replace(variable, "Not_supported_XD");
                    break;
                case "%DATO%":
                    filename = filename.replace(variable, StringTools.formatDateForFile(Calendar.getInstance().getTime()));
                    break;
            }
        }
        return filename;
    }
}
