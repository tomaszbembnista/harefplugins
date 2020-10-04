package eu.wordpro.ha.plugin.thermostat.model;

import java.util.List;

public class HeatingPlan {
    public float defaultTemp; //default temp for a whole week
    public List<DayOfWeek> days;
}
