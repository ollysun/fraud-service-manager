package com.etz.fraudeagleeyemanager.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.*;

@Component
public class JasperReportService {


    public void generateReport(){
//        List<Long> employees =
//        try{
//
//            File file = ResourceUtils.getFile("classpath:employees.jrxml");
//            InputStream input = new FileInputStream(file);
//
//            // Compile the Jasper report from .jrxml to .japser
//            JasperReport jasperReport = JasperCompileManager.compileReport(input);
//
//            // Get your data source
//            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(employees);
//
//            // Add parameters
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("For", "List Report");
//
//            // Fill the report
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);
//
//
//            // Export the report to a PDF file
//            JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Empployee.pdf");
//
//
//            JasperExportManager.exportReportToXmlFile(jasperPrint, reportPath + "\\Employee.xml", true);
//            System.out.println("XML File Generated !!");
//
//
//            JasperExportManager.exportReportToHtmlFile(jasperPrint, reportPath + "\\Employee.html");
//            System.out.println("HTML Generated");
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
