package no.hvl.dowhile.core;

/**
 * Wrapper class to store info about a track.
 */
public class TrackInfo {
    private String crewType;
    private int crewCount;
    private int crewNumber;
    private String areaSearched;
    private int trackNumber;

    /**
     * Default constructor instantiating variables.
     */
    public TrackInfo() {
        crewType = "";
        crewNumber = 0;
        crewCount = 0;
        areaSearched = "";
        trackNumber = 0;
    }

    /**
     * Constructor taking info for all variables.
     *
     * @param crewType     type of crew creating the track.
     * @param crewCount    number of people on the crew.
     * @param crewNumber   the number of this crew.
     * @param areaSearched the area they were searching in.
     * @param trackNumber the number of track (if multiple tracks for a team etc).
     */
    public TrackInfo(String crewType, int crewCount, int crewNumber, String areaSearched, int trackNumber) {
        this.crewType = crewType;
        this.crewCount = crewCount;
        this.crewNumber = crewNumber;
        this.areaSearched = areaSearched;
        this.trackNumber = trackNumber;
    }

    /**
     * Gives the type of crew creating this track. (Car, Helicopter, Scooter, ATC, Dog etc.)
     *
     * @return the type of crew creating this track.
     */
    public String getCrewType() {
        return crewType;
    }

    /**
     * Set the type of crew which was creating this track.
     *
     * @param crewType the type of crew which was creating this track.
     */
    public void setCrewType(String crewType) {
        this.crewType = crewType;
    }

    /**
     * Get the number of people on this crew.
     * @return the number of people on this creative.
     */
    public int getCrewCount() {
        return crewCount;
    }

    /**
     * Set the number of people on this crew.
     * @param crewCount the number of people on this crew.
     */
    public void setCrewCount(int crewCount) {
        this.crewCount = crewCount;
    }

    /**
     * Get the number of this crew.
     * @return the number of this crew.
     */
    public int getCrewNumber() {
        return crewNumber;
    }

    /**
     * Set the number of this crew.
     * @param groupNumber the number of this crew.
     */
    public void setCrewNumber(int groupNumber) {
        this.crewNumber = groupNumber;
    }

    /**
     * Get the area where this team were searching.
     * @return the area where this team were searching.
     */
    public String getAreaSearched() {
        return areaSearched;
    }

    /**
     * Set the area where this team were searching.
     * @param areaSearched the area where this team were searching.
     */
    public void setAreaSearched(String areaSearched) {
        this.areaSearched = areaSearched;
    }

    /**
     * Get the number of the track (if multiple tracks on a team etc):
     *
     * @return number of the track.
     */
    public int getTrackNumber() {
        return trackNumber;
    }

    /**
     * Set the number of the track (if multiple tracks on a team etc):
     *
     * @param trackNumber number of the track.
     */
    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }
}
