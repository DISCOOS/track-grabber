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
    NO_RELEVANT_FILES_FOUND("Oppdaget GPS, men fant ingen relevante filer! \n Mulige årsaker: \n - GPSen inneholder ingen filer \n - Filene er for gamle (de ble laget før operasjonsstart) \n - Filene er allerede overført"),

    // Font
    FONT_NAME("Times New Roman"),

    // Window
    GPS_OFFLINE("GPS: Koblet fra."),
    PROJECT_NAME("Track Grabber"),
    CONFIRM_EXIT("Vil du virkelig avslutte Track Grabber? Alle endringer vil bli lagret."),
    SPACER("                                                  "),

    // Track panel
    CREW_NUMBER("Gruppenummer: "),
    CREW_COUNT("Antall mann: "),
    AREA_SEARCHED("Teiger: "),
    REGISTER_BUTTON("Registrer"),
    SAVE_FILE("Fil prosessert og lagret!"),
    OPERATION_INFO("Operasjonsinfo "),
    OPERATION_INFO_NAME("Navn: %1"),
    OPERATION_INFO_START("Start: %1"),
    TRACK_NUMBER("Spornummer: "),
    IMPORTED_FROM_GPS("Oppgi data for fil: "),
    IMPORTED_FILES_LEFT_TO_PROCESS("Det er nå %1 filer i kø for prosessering."),

    // Operation panel
    OPERATION_NAME("Navn på operasjon: "),
    OPERATION_START_DATE("Startdato: "),
    EXISTING_OPERATION("Velg en allerede eksisterende operasjon."),
    NEW_OPERATION_BUTTON("Ny operasjon"),
    IMPORT_LOCAL_FILE("Importer GPS-fil fra maskin"),
    CHOOSE_OTHER_OPERATION("Velg en annen operasjon"),
    EDIT_OPERATION_TIME("Endre starttid: "),
    EDIT_OPERATION_BUTTON("Lagre operasjonsinfo"),
    EDIT_INFO_SHOW_BUTTON("Rediger operasjon"),
    EDIT_INFO_HIDE_BUTTON("Skjul redigeringsinfo"),
    EXISTING_OPERATION_BUTTON("Eksisterende operasjon"),
    REGISTER_NEW_BUTTON("Opprett operasjon"),
    REGISTER_EXISTING_BUTTON("Last inn operasjon"),
    AWAITING_GPS("Venter på at en GPS-enhet kobles til..."),
    INVALID_OPERATION_NAME("Ugyldig operasjonsnavn"),
    OPERATION_NAME_ALREADY_EXISTS("Det finnes allerede en operasjon med dette navnet."),
    GO_BACK("Tilbake"),

    // Crew type
    CREW_ATV("ATV"),
    CREW_BOAT("Båt"),
    CREW_CAR("Bil"),
    CREW_DOG("Hund"),
    CREW_HELICOPTER("Helikopter"),
    CREW_MEN("Lag")
    ;

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
