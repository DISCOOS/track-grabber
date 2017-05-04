package no.hvl.dowhile.core;

import org.alternativevision.gpx.beans.GPX;

/**
 * Wrapper class for storing filename along with a GPX object.
 */
public class GpxFile {
    private String filename;
    private GPX gpx;

    /**
     * Constructor taking the data required to create the object.
     *
     * @param filename the name of the file.
     * @param gpx      the parsed GPX object.
     */
    public GpxFile(String filename, GPX gpx) {
        this.filename = filename;
        this.gpx = gpx;
    }

    /**
     * Get the filename associated with this GPX object.
     * @return the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the parsed GPX object.
     * @return the parsed GPX object.
     */
    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }
}
