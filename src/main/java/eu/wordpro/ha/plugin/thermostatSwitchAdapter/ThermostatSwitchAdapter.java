package eu.wordpro.ha.plugin.thermostatSwitchAdapter;

import com.google.gson.Gson;
import eu.wordpro.ha.api.*;
import eu.wordpro.ha.api.model.OperationExecutionResult;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.plugin.model.SwitchData;
import eu.wordpro.ha.plugin.thermostat.model.Output;
import eu.wordpro.ha.plugin.thermostatSwitchAdapter.model.Configuration;
import eu.wordpro.ha.plugin.thermostatSwitchAdapter.model.State;

import java.util.ArrayList;
import java.util.List;

@Documentation(pathToFile = "ThermostatSwitchAdapterDocu.MD")
@DisplayName(value = "Thermostat to switch adapter")
public class ThermostatSwitchAdapter implements SignalProcessor {

    private Gson gson = new Gson();
    private State state = new State();
    private Configuration configuration = new Configuration();
    private StateListener stateListener;

    @Override
    public void setConfiguration(String configuration) throws InvalidConfigurationException {
        try {
            this.configuration = gson.fromJson(configuration, Configuration.class);
        } catch (Exception e) {
            throw new InvalidConfigurationException("JSON parse error");
        }
    }

    @Override
    public void setListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    @Override
    public void setState(String stateStr) throws InvalidStateException {
        try {
            state = gson.fromJson(stateStr, State.class);
        } catch (Exception e) {
            throw new InvalidStateException("JSON parse error");
        }
    }

    @Override
    public String getState() {
        return gson.toJson(state);
    }

    @Override
    public SignalProcessorData processInput(ProcessingData inputs) throws InvalidSignalException, SignalProcessingException {
        Output thermostatOutput = inputs.getLast(Output.class);
        SwitchData switchData = new SwitchData();
        switchData.switchOn = shouldSwitchOn(thermostatOutput);
        state.lastSwitchOn = switchData.switchOn;
        return new SignalProcessorData("thermostatSwitchAdapter", gson.toJson(switchData));
    }


    @Override
    public List<ProcessorOperationDesc> listPossibleOperations() {
        return new ArrayList<>();
    }

    @Override
    public OperationExecutionResult executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        return null;
    }



    private boolean shouldSwitchOn(Output thermostatOutput){
        float delta = thermostatOutput.delta;
        float halfOfWidth = configuration.hysteresisLoopWidth / 2;
        if (delta < (-1 * halfOfWidth)){
            return true;
        }
        if (delta > halfOfWidth){
            return false;
        }
        return state.lastSwitchOn;
    }





}
