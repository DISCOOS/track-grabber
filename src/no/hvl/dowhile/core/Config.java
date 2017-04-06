package no.hvl.dowhile.core;

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
}
