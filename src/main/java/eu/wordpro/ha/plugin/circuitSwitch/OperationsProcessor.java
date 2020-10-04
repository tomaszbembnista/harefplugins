package eu.wordpro.ha.plugin.circuitSwitch;

import com.google.gson.Gson;
import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.*;
import eu.wordpro.ha.plugin.model.SwitchData;

import java.util.ArrayList;
import java.util.List;

class OperationsProcessor {

    private final static String switchOn = "switchOn";
    private final static String switchOff = "switchOff";

    Gson gson = new Gson();


    public List<ProcessorOperationDesc> listPossibleOperations() {
        List<ProcessorOperationDesc> result = new ArrayList<>();
        result.add(getOperationDesc(switchOn));
        result.add(getOperationDesc(switchOff));
        return result;
    }

    public OperationExecutionResult executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        if (switchOn.equals(name)){
            return makeSwitch(true);
        }
        if (switchOff.equals(name)){
            return makeSwitch(false);
        }
        return null;
    }

    private ProcessorOperationDesc getOperationDesc(String name){
        ProcessorOperationDesc result = new ProcessorOperationDesc();
        result.setName(name);
        ProcessorOperationArgumentDesc requiredTempArg = new ProcessorOperationArgumentDesc();
        result.getArguments().add(requiredTempArg);
        result.setResult(DataType.VOID);
        return result;
    }


    private OperationExecutionResult makeSwitch(boolean on){
        OperationExecutionResult result = new OperationExecutionResult();
        SwitchData switchData = new SwitchData();
        switchData.switchOn = on;
        result.setSignalProcessorData(prepareOutput(switchData));
        return result;
    }

    private SignalProcessorData prepareOutput(SwitchData switchData) {
        String outputAsJson = gson.toJson(switchData);
        SignalProcessorData result = new SignalProcessorData("circuitSwitch", outputAsJson);
        return result;
    }





}
