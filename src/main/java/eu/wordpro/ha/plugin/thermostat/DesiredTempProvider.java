package eu.wordpro.ha.plugin.thermostat;

import eu.wordpro.ha.plugin.thermostat.model.HeatingPlan;

class DesiredTempProvider {

    private HeatingPlan heatingPlan;

    public DesiredTempProvider(HeatingPlan heatingPlan){

        this.heatingPlan = heatingPlan;
    }

    public float getDesiredTemp(long currentTime){
        return heatingPlan.defaultTemp;
    }

}
