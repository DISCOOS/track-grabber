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

    // Font
    FONT_NAME("Times New Roman"),

    // Window
    GPS_OFFLINE("GPS: Koblet fra."),
    PROJECT_NAME("Track Grabber"),
    CONFIRM_EXIT("Vil du virkelig avslutte Track Grabber? Alle endringer vil bli lagret."),

    // Track panel
    CREW_NUMBER("Gruppenummer: "),
    CREW_COUNT("Antall mann: "),
    AREA_SEARCHED("Teiger: "),
    REGISTER_BUTTON("Registrer"),
    SAVE_FILE("Vil bli lagret som: "),
    OPERATION_STARTED("Operasjon startet: "),

    // Operation panel
    OPERATION_NAME("Navn på operasjon: "),
    OPERATION_START_DATE("Startdato: "),
    EXISTING_OPERATION("Velg en allerede eksisterende operasjon.")
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
