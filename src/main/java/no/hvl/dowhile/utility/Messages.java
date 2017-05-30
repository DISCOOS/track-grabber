package no.hvl.dowhile.utility;

/**
 * Messages used in the application.
 * This will make maintaining easier.
 */
public enum Messages {
    // System
    DRIVE_DETECTOR_START("Systemet lytter nå etter enheter som kobles til."),
    DRIVE_DETECTOR_STOP("Systemet lytter ikke lenger etter enheter som kobles til."),
    ERROR_THREAD("En tråd ga en error mens den ventet på neste kjøring."),
    ERROR_NO_TRACK_FOR_INFO("Systemet prøvde å starte prosessering av fil som ikke finnes."),
    NO_RELEVANT_FILES_TITLE("Ingen relevante filer på GPS"),
    NO_RELEVANT_FILES_GPS_DESCRIPTION("Oppdaget GPS, men fant ingen relevante filer! \n Mulige årsaker: \n - GPSen inneholder ingen filer \n - Filene er for gamle (de ble laget før aksjonsstart) \n - Filene er allerede overført"),
    NO_RELEVANT_FILES_IMPORT_DESCRIPTION("Fil funnet, men den ble ikke overført! \n Mulige årsaker: \n - Filen er for gammel (den ble laget før aksjonsstart) \n - Filen er allerede overført"),
    SKIP_FILE("Hoppet over fil. \nFilen vil ikke bli lagt til i prosesserte filer, men i råfilene."),

    // Font
    FONT_NAME("Calibri"),

    // Header panel
    CONFIRM_EXIT("Vil du virkelig avslutte Track Grabber? Alle endringer vil bli lagret."),
    NO_OPERATION("Ingen aksjon valgt."),
    OPERATION_INFO("Aksjonsinfo "),
    OPERATION_INFO_NAME("Navn: %1"),
    OPERATION_INFO_START("Start: %1"),
    PROJECT_NAME("Track Grabber"),
    SPACER("                                                  "),

    // Start panel
    OPERATION_NAME("Navn på aksjon: "),
    OPERATION_START_DATE("Startdato: "),
    DEFINE_OPERATION_PATH("Legg til ekstra lagringssted"),
    EXISTING_OPERATION("Velg en allerede eksisterende aksjon."),
    NEW_OPERATION_BUTTON("Ny aksjon"),
    EXISTING_OPERATION_BUTTON("Eksisterende aksjon"),
    REGISTER_NEW_BUTTON("Opprett aksjon"),
    REGISTER_EXISTING_BUTTON("Last inn"),
    INVALID_OPERATION_NAME("Ugyldig aksjonsnavn"),
    OPERATION_NAME_ALREADY_EXISTS("Det finnes allerede en aksjon med dette navnet."),
    OPERATION_NAME_IS_TOO_LONG_OR_SHORT("Navnet må være mellom 2 og 50 tegn."),

    // Stand by panel
    IMPORT_LOCAL_FILE("Importer GPS-fil fra maskin"),
    AWAITING_GPS("Koble til GPS-enhet og vent på prosessering..."),

    // Operation panel
    CHOOSE_OTHER_OPERATION("Velg en annen aksjon"),
    EDIT_OPERATION_TIME("Endre starttid: "),
    EDIT_OPERATION_BUTTON("Lagre aksjonsinfo"),
    EDIT_INFO_SHOW_BUTTON("Rediger aksjon"),
    EDIT_INFO_HIDE_BUTTON("Skjul redigeringsinfo"),
    ALL_SAVED_PATHS("Mapper filene lagres til:"),
    GO_BACK("Tilbake"),

    // Track panel
    TRACK_HEADER("Prosesserer sporfil"),
    BACK("Tilbake"),
    NEXT("Neste"),
    TRACK_START_INFO("Du importerer nå en sporfil. Klikk på neste og følg instruksene."),
    TRACK_START("Startet: "),
    TRACK_END("Sluttet: "),
    CREW_TYPE_MESSAGE("Velg type lag"),
    CREW_NUMBER("Lagnummer (lag 21 = 2.1 osv.): "),
    CREW_COUNT("Antall mannskap: "),
    AREA_SEARCHED("Teiger: "),
    AREA_SEARCHED_INFO("Velg eller skriv inn teignummer og trykk legg til. Du kan legge til flere."),
    REGISTER_BUTTON("Registrer"),
    SKIP_BUTTON("Hopp over"),
    SAVE_FILE("Fil prosessert og lagret!"),
    TRACK_NUMBER("Spornummer (forklaring...): "),
    TRACK_COMMENT("Kommentar til sporfilen: "),
    TRACK_COMMENT_PLACEHOLDER("Valgfri"),
    CHOOSE_AREA("Legg til"),
    SUMMARY_CREW_TYPE("Lag: "),
    SUMMARY_CREW_NUMBER("Gruppenummer: "),
    SUMMARY_TRACK_NUMBER("Spornummer: "),
    SUMMARY_CREW_COUNT("Antall enheter: "),
    SUMMARY_AREA_SEARCHED("Teiger: "),
    SUMMARY_TRACK_COMMENT("Sporkommentar: "),
    TRACK_LENGTH("Lengde på sporet: "),

    // Waypoint panel
    IMPORTED_FROM_WAYPOINT_GPS("Waypoint opprettet:  "),
    WAYPOINT_HEADER("Prosesserer veipunkt"),
    NEW_NAME("Nytt navn for waypoint"),
    NEW_DESCRIPTION("Beskrivelse av waypoint"),
    FLAG_COLOR("Velg farge på Waypoint-flagg: "),

    // Track panel and Waypoint panel
    PROCESSING_FILES("Prosesserer fil %1 av %2");

    private String message;

    /**
     * Constructor for getting a message.
     *
     * @param message The message you need.
     */
    Messages(String message) {
        this.message = message;
    }

    /**
     * Gives you the current message. Short version of method mentioned below.
     *
     * @return the current message.
     * @see #getMessage()
     */
    public String get() {
        return message;
    }

    /**
     * Gives you the current message and inserts replacement into the string.
     *
     * @param replacement1 text to insert into message.
     * @return the current message.
     * @see #getMessage()
     */
    public String get(String replacement1) {
        return message.replace("%1", replacement1);
    }

    /**
     * Gives you the current message and inserts replacements into the string.
     *
     * @param replacement1 text 1 to insert into message.
     * @param replacement2 text 2 to insert into message.
     * @return the current message.
     * @see #getMessage()
     */
    public String get(String replacement1, String replacement2) {
        return message.replace("%1", replacement1).replace("%2", replacement2);
    }

    /**
     * Gives you the current message.
     *
     * @return the current message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Prints out the current message.
     */
    public void print() {
        System.err.println(message);
    }
}
