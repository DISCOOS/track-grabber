package no.hvl.dowhile.core.parser;

import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Route;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;
import org.alternativevision.gpx.extensions.IExtensionParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ExtensionParser implements IExtensionParser {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Object parseWaypointExtension(Node node) {
        return null;
    }

    @Override
    public Object parseTrackExtension(Node node) {
        return null;
    }

    @Override
    public Object parseGPXExtension(Node node) {
        return null;
    }

    @Override
    public Object parseRouteExtension(Node node) {
        return null;
    }

    @Override
    public void writeGPXExtensionData(Node node, GPX gpx, Document document) {

    }

    @Override
    public void writeWaypointExtensionData(Node node, Waypoint waypoint, Document document) {

    }

    @Override
    public void writeTrackExtensionData(Node node, Track track, Document document) {

    }

    @Override
    public void writeRouteExtensionData(Node node, Route route, Document document) {

    }
}
