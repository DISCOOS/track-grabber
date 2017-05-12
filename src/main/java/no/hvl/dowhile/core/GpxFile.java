package no.hvl.dowhile.core;

import org.alternativevision.gpx.beans.GPX;

import java.io.File;

/**
 * Wrapper class for storing the file along with the GPX object.
 */
public class GpxFile {
    private File file;
    private String rawfileHash;
    private GPX gpx;

    /**
     * Constructor taking the data required to create the object.
     *
     * @param file        actual file.
     * @param rawFileHash Hash value of the saved raw file.
     * @param gpx         the parsed GPX object.
     */
    public GpxFile(File file, String rawFileHash, GPX gpx) {
        this.file = file;
        this.rawfileHash = rawFileHash;
        this.gpx = gpx;
    }

    /**
     * Get the file associated with this GPX object.
     *
     * @return the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Get the hash value of the raw file.
     *
     * @return the hash value of the raw file.
     */
    public String getRawfileHash() {
        return rawfileHash;
    }

    /**
     * Get the parsed GPX object.
     *
     * @return the parsed GPX object.
     */
    public GPX getGpx() {
        return gpx;
    }

    public void setGpx(GPX gpx) {
        this.gpx = gpx;
    }
}
