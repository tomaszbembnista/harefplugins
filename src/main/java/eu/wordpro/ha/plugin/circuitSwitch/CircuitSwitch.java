package eu.wordpro.ha.plugin.circuitSwitch;

import eu.wordpro.ha.api.*;
import eu.wordpro.ha.api.model.OperationExecutionResult;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;

import java.util.LinkedList;
import java.util.List;

@Documentation(pathToFile = "CircuitSwitchDocu.MD")
@DisplayName(value = "Circuit switch")
public class CircuitSwitch implements SignalProcessor {
    private StateListener stateListener;

    OperationsProcessor operationsProcessor = new OperationsProcessor();

    @Override
    public void setConfiguration(String s) throws InvalidConfigurationException {

    }

    @Override
    public void setListener(StateListener stateListener) {
    }

    @Override
    public void setState(String s) throws InvalidStateException {
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public SignalProcessorData processInput(ProcessingData linkedList) throws InvalidSignalException, SignalProcessingException {
        return null;
    }

    @Override
    public List<ProcessorOperationDesc> listPossibleOperations() {
        return operationsProcessor.listPossibleOperations();
    }

    @Override
    public OperationExecutionResult executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        return operationsProcessor.executeOperation(arguments, name);
    }
}
