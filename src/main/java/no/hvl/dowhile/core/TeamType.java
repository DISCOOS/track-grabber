package no.hvl.dowhile.core;

/**
 * Info about a type of team and the color representing the tracks.
 * Example: Helicopter, Car, ATV.
 */
public class TeamType {
    private String type;
    private String color;

    public TeamType(String type, String color) {
        this.type = type;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
