package no.hvl.dowhile.core;

/**
 * Info about a type of team and the color representing the tracks.
 * Example: Helicopter, Car, ATV.
 */
public class TeamType {
    private String name;
    private String color;

    /**
     * Constructor taking the name and color of this team type.
     *
     * @param name  the name of this team type.
     * @param color the color of this team type.
     */
    public TeamType(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Get the name of this team type.
     *
     * @return the name of this team type.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the color for this team type.
     *
     * @return the color for this team type.
     */
    public String getColor() {
        return color;
    }
}
