package eu.wordpro.ha.plugin.thermostat.model;

public class State {
    public State(){
        this.currentTemp = 0f;
        this.tempToKeep = 0f;
    }
    public Float currentTemp;
    public Float tempToKeep;
}
