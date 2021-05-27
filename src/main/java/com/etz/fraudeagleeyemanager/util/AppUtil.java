package com.etz.fraudeagleeyemanager.util;

import com.etz.fraudeagleeyemanager.exception.FraudEngineException;

import java.util.Arrays;
import java.util.List;

public class AppUtil {

    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static boolean isBlank(Object text) {
        String textStr = (String) text;
        return isBlank(textStr);
    }

    public static String checkLogicOperator(String text){
        List<String> dataSourceVal = Arrays.asList("AND", "OR", "NOT");
        String output = "";
        if(!(text.isEmpty())){
            output = dataSourceVal.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(text))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("cannot found this logic operator " + text + " can be any of " + dataSourceVal.toString()));
        }
        return output;
    }

    public static String checkDataSource(String text) {
        List<String> dataSourceVal = Arrays.asList("FRAUD ENGINE", "STATISTICS");
        String output = "";
        if(!(text.isEmpty())){
            output = dataSourceVal.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(text))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("cannot found this data source " + text + " can be any " + dataSourceVal.toString()));
        }
        return output;
    }

    public static String checkOperator(String operatorRequest){
        List<String> operators = Arrays.asList("<", ">","==", "<=", "!=", ">=", "change");
        String output = "";
        if (operatorRequest != null) {
            output = operators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("Not found this Operator " + operatorRequest +
                                    " can be any of " + operators.toString()));
        }
        return output;
    }
}
