package no.hvl.dowhile.core;

/**
 * Created by JonKjetil on 30.03.2017.
 */
public class TrackInfo {
    private String trackName;
    private int crewCount;
    private int crewNumber;
    private String areaSearched;

    public TrackInfo(String trackName, int crewCount, int crewNumber, String areaSearched) {
        this.trackName = trackName;
        this.crewCount = crewCount;
        this.crewNumber = crewNumber;
        this.areaSearched = areaSearched;
    }

    public TrackInfo() {
        trackName = "";
        crewNumber = 0;
        crewCount = 0;
        areaSearched = "";
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getCrewCount() {
        return crewCount;
    }

    public void setCrewCount(int crewCount) {
        this.crewCount = crewCount;
    }

    public int getCrewNumber() {
        return crewNumber;
    }

    public void setCrewNumber(int groupNumber) {
        this.crewNumber = groupNumber;
    }

    public String getAreaSearched() {
        return areaSearched;
    }

    public void setAreaSearched(String areaSearched) {
        this.areaSearched = areaSearched;
    }
}
