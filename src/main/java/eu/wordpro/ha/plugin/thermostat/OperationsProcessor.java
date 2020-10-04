package eu.wordpro.ha.plugin.thermostat;

import com.google.gson.Gson;
import eu.wordpro.ha.api.StateListener;
import eu.wordpro.ha.api.model.*;
import eu.wordpro.ha.plugin.thermostat.model.State;

import java.util.ArrayList;
import java.util.List;

class OperationsProcessor {



    public StateListener stateListener;
    public State state;
    Gson gson = new Gson();

    public List<ProcessorOperationDesc> listPossibleOperations() {
        List<ProcessorOperationDesc> result = new ArrayList<>();
        result.add(getSetTemperatureOperationDesc());
        return result;
    }

    public String executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        if ("setTemp".equals(name)){
            return setTemperature(arguments);
        }
        return null;
    }

    private ProcessorOperationDesc getSetTemperatureOperationDesc(){
        ProcessorOperationDesc result = new ProcessorOperationDesc();
        result.setName("setTemp");
        ProcessorOperationArgumentDesc requiredTempArg = new ProcessorOperationArgumentDesc();
        requiredTempArg.setName("temp");
        requiredTempArg.setType(DataType.FLOAT);
        requiredTempArg.setOptional(false);
        result.getArguments().add(requiredTempArg);
        result.setResult(DataType.VOID);
        return result;
    }

    private String setTemperature(List<ProcessorOperationArgument> arguments){
        ProcessorOperationArgument tempArgument = arguments.stream().
                filter(elem -> "temp".equals(elem.getName())).findFirst().orElse(null);
        if (tempArgument == null){
            return null;
        }
        state.tempToKeep = Float.parseFloat(tempArgument.getValue());
        String stateAsString = gson.toJson(state);
        stateListener.onStateChanged(stateAsString);
        return null;
    }



}
