package eu.wordpro.ha.plugin.thermostat;

import com.google.gson.Gson;
import eu.wordpro.ha.api.*;
import eu.wordpro.ha.api.model.OperationExecutionResult;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.plugin.thermostat.model.HeatingPlan;
import eu.wordpro.ha.plugin.thermostat.model.Input;
import eu.wordpro.ha.plugin.thermostat.model.Output;
import eu.wordpro.ha.plugin.thermostat.model.State;

import java.util.List;

@DisplayName(value = "Thermostat")
@Documentation(pathToFile = "ThermostatDocu.MD")
public class Thermostat implements SignalProcessor {

    private Gson gson = new Gson();

    private State state = new State();
    private DesiredTempProvider desiredTempProvider;
    private StateListener stateListener;
    private OperationsProcessor operationsProcessor = new OperationsProcessor();

    @Override
    public void setConfiguration(String configuration) throws InvalidConfigurationException {
        try {
            HeatingPlan heatingPlan = gson.fromJson(configuration, HeatingPlan.class);
            desiredTempProvider = new DesiredTempProvider(heatingPlan);
        } catch (Exception e) {
            throw new InvalidConfigurationException("Wrong JSON syntax");
        }
    }

    @Override
    public void setListener(StateListener stateListener) {
        this.stateListener = stateListener;
        operationsProcessor.stateListener = stateListener;
    }

    @Override
    public void setState(String stateStr) throws InvalidStateException {
        try {
            state = gson.fromJson(stateStr, State.class);
            operationsProcessor.state = state;
        } catch (Exception e) {
            throw new InvalidStateException("Wrong JSON syntax");
        }
    }

    @Override
    public String getState() {
        return gson.toJson(state);
    }

    @Override
    public SignalProcessorData processInput(ProcessingData inputs) throws InvalidSignalException, SignalProcessingException {
        Input input = inputs.getLast(Input.class);
        setCurrentTemp(input.temperature);
        Output output = calculateDelta(System.currentTimeMillis(), input.temperature);
        return prepareOutput(output);
    }

    @Override
    public List<ProcessorOperationDesc> listPossibleOperations() {
        return operationsProcessor.listPossibleOperations();
    }

    @Override
    public OperationExecutionResult executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        OperationExecutionResult result = new OperationExecutionResult();
        String dataForClient = operationsProcessor.executeOperation(arguments, name);
        result.setDataForClient(dataForClient);
        Output output = calculateDelta(System.currentTimeMillis(), state.currentTemp);
        result.setSignalProcessorData(prepareOutput(output));
        return result;
    }

    private SignalProcessorData prepareOutput(Output output) {
        String outputAsJson = gson.toJson(output);
        SignalProcessorData result = new SignalProcessorData("thermostat", outputAsJson);
        return result;
    }

    private Output calculateDelta(long currentTime, float currentTemp) {
        float temperatureToKeep = state.tempToKeep != null ? state.tempToKeep : desiredTempProvider.getDesiredTemp(currentTime);
        Output output = new Output();
        output.delta = currentTemp - temperatureToKeep;
        return output;
    }

    private void setCurrentTemp(float currentTemp) {
        state.currentTemp = currentTemp;
        stateListener.onStateChanged(getState());
    }


}
