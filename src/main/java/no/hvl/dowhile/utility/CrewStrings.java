package no.hvl.dowhile.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that returns a list with all the crew types
 */
public class CrewStrings {
    public static List<String> getCrewStrings() {
        List<String> crewStrings = new ArrayList<String>();
        crewStrings.add(Messages.CREW_ATV.get());
        crewStrings.add(Messages.CREW_BOAT.get());
        crewStrings.add(Messages.CREW_CAR.get());
        crewStrings.add(Messages.CREW_DOG.get());
        crewStrings.add(Messages.CREW_HELICOPTER.get());
        crewStrings.add(Messages.CREW_MEN.get());
        return crewStrings;
    }
}
