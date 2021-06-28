package com.etz.fraudeagleeyemanager.util;

import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
public class AppUtil {

	private AppUtil() {}
	
    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static boolean isBlank(Object text) {
        String textStr = (String) text;
        return isBlank(textStr);
    }

    public static Set<String> getNullPropertyNames (Object source, String... extra) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                log.info("attribute {}",pd.getName());
                emptyNames.add(pd.getName());
            }
        }

        emptyNames.addAll(Arrays.asList(extra));
        return emptyNames;
    }

    public static String checkLogicOperator(String text){
        List<String> dataSourceVal = Arrays.asList("AND", "OR", "NOT");
        String output = "";
        if(!Objects.isNull(text)){
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
        if(!Objects.isNull(text)){
            output = dataSourceVal.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(text))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("cannot found this data source " + text + " can be any " + dataSourceVal.toString()));
        }
        return output;
    }

    private static String checkBoolean(String dataType, String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==","!=");
        String operator = "";
        if (!Objects.isNull(dataType) && dataType.equalsIgnoreCase("BOOLEAN") && !Objects.isNull(operatorRequest)){
            operator = allowedOperators.stream()
                      .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                      .findFirst()
                      .orElseThrow(() ->
                            new FraudEngineException("operator Not found for this Datatype " + dataType + operatorRequest +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }

    private static String checkNumber(String dataType, String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==","!=", "<", ">", "<=", ">=");
        String operator = "";
        if (!Objects.isNull(dataType) && dataType.equalsIgnoreCase("NUMBER") && !Objects.isNull(operatorRequest)){
            operator = allowedOperators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("operator Not found for this Datatype " + dataType + operatorRequest +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }

    private static String checkString(String dataType, String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==", "CHANGE");
        String operator = "";
        if (!Objects.isNull(dataType) && dataType.equalsIgnoreCase("STRING") && !Objects.isNull(operatorRequest)){
            operator = allowedOperators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("operator Not found for this Datatype " + dataType + operatorRequest +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }


    /**
     * is compare value valid
     *
     * @param datatypeAllowed datatypeAllowed
     * @param compareValue compareValue
     * @return {@link boolean}
     */
    public static boolean isCompareValueValid(String datatypeAllowed, String compareValue){
	    if (!Objects.isNull(datatypeAllowed) || !Objects.isNull(compareValue)) {
            if (datatypeAllowed.equalsIgnoreCase("BOOLEAN") && !("True".equalsIgnoreCase(compareValue) || "False".equalsIgnoreCase(compareValue))) {
                log.error("compare value '{}' is not a boolean value", compareValue);
                return false;
            } else if (datatypeAllowed.equalsIgnoreCase("NUMBER")) {
                try {
                    new BigDecimal(compareValue);
                    Integer.parseInt(compareValue);
                } catch (NumberFormatException e) {
                    log.error("compare value '{}' is not a Number value", compareValue);
                    return false;
                }
            } else if (datatypeAllowed.equalsIgnoreCase("Date")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    sdf.parse(compareValue);
                } catch (ParseException e) {
                    log.error("compare value '{}' is not a Date value", compareValue);
                    return false;
                }
            } else if (datatypeAllowed.equalsIgnoreCase("Time")) {
                try {
                    Integer.parseInt(compareValue);
                } catch (NumberFormatException e) {
                    log.error("Wrong compare value for Time value" + compareValue);
                    return false;
                }
            } else if (datatypeAllowed.equalsIgnoreCase("String")) {
                return true;
            }
            return true;
        }
        return false;
    }

    public static String checkTimeSourceValue(String datatype, String sourceRequest){
        List<String> allowedOperators = Arrays.asList("DAY","HOUR", "MINUTE", "SECONDS");
        String operator = "";
        if (!Objects.isNull(datatype) && datatype.equalsIgnoreCase("TIME") && !Objects.isNull(sourceRequest)){
            operator = allowedOperators.stream()
                    .filter(bl -> bl.equalsIgnoreCase(sourceRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("SourceValue Not found for this Datatype " + datatype +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }


    private static String checkDate(String dataType, String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==","!=", "<", ">", "<=", ">=");
        String operator = "";
        if (!Objects.isNull(dataType) && dataType.toUpperCase().equalsIgnoreCase("DATE")){
            operator = allowedOperators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("operator Not found for this Datatype " + dataType + operatorRequest +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }

    private static String checkTime(String dataType, String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==","!=", "<", ">", "<=", ">=");
        String operator = "";
        if (!Objects.isNull(dataType) && dataType.toUpperCase().equalsIgnoreCase("TIME")){
            operator = allowedOperators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("operator Not found for this Datatype " + dataType + operatorRequest +
                                    " can be any of " + allowedOperators.toString()));
        }
        return operator;
    }

    public static String checkParameterOperator(String operatorRequest){
        List<String> allowedOperators = Arrays.asList("==","!=", "<", ">", "<=", ">=", "change");
        String operator = "";
            operator = allowedOperators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("operator Not found "+ operatorRequest +
                                    " can be any of " + allowedOperators.toString()));

        return operator;
    }
    public static String checkOperator(String datatype, String operatorRequest){
        String output = "";
        switch (datatype.toUpperCase()){
            case "BOOLEAN":
                  output = checkBoolean(datatype, operatorRequest);
                  break;
            case "NUMBER":
                  output = checkNumber(datatype, operatorRequest);
                  break;
            case "STRING":
                  output = checkString(datatype, operatorRequest);
                  break;
            case "DATE":
                  output = checkDate(datatype, operatorRequest);
                  break;
            case "TIME":
                  output = checkTime(datatype, operatorRequest);
                  break;
            default:
                throw new IllegalStateException("Unexpected value: " + datatype.toUpperCase());
        }
        return output;
    }

    public static String checkCardType(String operatorRequest){
        List<String> operators = Arrays.asList("CREDIT", "DEBIT", "ATM", "CHARGE");
        String output = "";
        if (operatorRequest != null) {
            output = operators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("Not found this Card Type " + operatorRequest +
                                    " can be any of " + operators.toString()));
        }
        return output;
    }

    public static String checkBrand(String operatorRequest){
        List<String> operators = Arrays.asList("MasterCard", "Visa", "Verve");

        String output = "";
        if (operatorRequest != null) {
            output = operators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("Not found this Card Brand " + operatorRequest +
                                    " can be any of " + operators.toString()));
        }
        return output;
    }
    public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
    public static String checkDataType(String operatorRequest){
        List<String> operators = Arrays.asList("Boolean", "Number", "String", "Date", "Time");

        String output = "";
        if (operatorRequest != null) {
            output = operators.stream()
                    .filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
                    .findFirst()
                    .orElseThrow(() ->
                            new FraudEngineException("Not found this Data Type " + operatorRequest +
                                    " can be any of " + operators.toString()));
        }
        return output;
    }

    public static Integer getLength(Integer cvv){
        return (int)(Math.log10(cvv)+1);
    }

    public static Boolean getDate(String strDate){
        String[] arrOfStr = strDate.split("/", 2);
        LocalDate currentDate = LocalDate.now();
        //Getting the current month
        Month currentMonth = currentDate.getMonth();
        //getting the current year
        int currentYear = currentDate.getYear() % 100;
        return (Integer.parseInt(arrOfStr[0]) >= currentMonth.getValue()) &&
                Integer.parseInt(arrOfStr[1]) >= currentYear;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}
