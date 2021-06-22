package com.etz.fraudeagleeyemanager.util;

import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
