package no.hvl.dowhile.core;

import org.alternativevision.gpx.beans.GPX;

/**
 * This class encapsules every GPX file together with its filename, into a single object.
 */
public class GPXFile {

    private String filename;
    private GPX gpx;

    public GPXFile(String filename, GPX gpx) {
        this.filename = filename;
        this.gpx = gpx;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }
}
