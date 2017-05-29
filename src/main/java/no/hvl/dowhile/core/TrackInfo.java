package no.hvl.dowhile.core;

/**
 * Wrapper class to store info about a track.
 */
public class TrackInfo {
    private String crewType;
    private int crewCount;
    private int crewNumber;
    private String areaSearched;
    private double distance;
    private int trackNumber;
    private String comment;

    /**
     * Default constructor instantiating variables.
     */
    public TrackInfo() {
        this.crewType = "";
        this.crewNumber = 0;
        this.crewCount = 0;
        this.areaSearched = "";
        this.distance = 0.0;
        this.trackNumber = 0;
        this.comment = "";
    }

    /**
     * Constructor taking info for all variables.
     *
     * @param crewType     type of crew creating the track.
     * @param crewCount    number of people on the crew.
     * @param crewNumber   the number of this crew.
     * @param areaSearched the area they were searching in.
     * @param trackNumber  the number of track (if multiple tracks for a team etc).
     * @param comment      Optional comment about a track.
     */
    public TrackInfo(String crewType, int crewCount, int crewNumber, String areaSearched, double distance, int trackNumber, String comment) {
        this.crewType = crewType;
        this.crewCount = crewCount;
        this.crewNumber = crewNumber;
        this.areaSearched = areaSearched;
        this.distance = distance;
        this.trackNumber = trackNumber;
        this.comment = comment;
    }

    /**
     * Gives the type of crew creating this track. (Car, Helicopter, Scooter, ATC, Dog etc.)
     *
     * @return the type of crew creating this track.
     */
    String getCrewType() {
        return crewType;
    }

    /**
     * Sets the type of crew which was creating this track.
     *
     * @param crewType the type of crew which was creating this track.
     */
    public void setCrewType(String crewType) {
        this.crewType = crewType;
    }

    /**
     * Gets the number of people on this crew.
     *
     * @return the number of people on this creative.
     */
    int getCrewCount() {
        return crewCount;
    }

    /**
     * Sets the number of people on this crew.
     *
     * @param crewCount the number of people on this crew.
     */
    public void setCrewCount(int crewCount) {
        this.crewCount = crewCount;
    }

    /**
     * Gets the number of this crew.
     *
     * @return the number of this crew.
     */
    int getCrewNumber() {
        return crewNumber;
    }

    /**
     * Sets the number of this crew.
     *
     * @param groupNumber the number of this crew.
     */
    public void setCrewNumber(int groupNumber) {
        this.crewNumber = groupNumber;
    }

    /**
     * Gets the area where this team were searching.
     *
     * @return the area where this team were searching.
     */
    String getAreaSearched() {
        return areaSearched;
    }

    /**
     * Sets the area where this team were searching.
     *
     * @param areaSearched the area where this team were searching.
     */
    public void setAreaSearched(String areaSearched) {
        this.areaSearched = areaSearched;
    }

    /**
     * Gets the distance covered in track.
     *
     * @return distance covered.
     */
    double getDistance() {
        return distance;
    }

    /**
     * Sets the distance covered in the track.
     *
     * @param distance the distance to be set.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets the number of the track (if multiple tracks on a team etc):
     *
     * @return number of the track.
     */
    int getTrackNumber() {
        return trackNumber;
    }

    /**
     * Sets the number of the track (if multiple tracks on a team etc):
     *
     * @param trackNumber number of the track.
     */
    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
     * Gets the optional comment about this track.
     *
     * @return the comment about this track.
     */
    String getComment() {
        return comment;
    }
}
