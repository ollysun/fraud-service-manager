package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.CreateReportRequest;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.entity.Report;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.ParameterRepository;
import com.etz.fraudeagleeyemanager.repository.ReportRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ReportingService {

    private final ReportRepository reportRepository;


    @Transactional(rollbackFor = Throwable.class)
    public Report createReport(CreateReportRequest request) {
        if (Boolean.TRUE.equals(reportRepository.existsByName(request.getName()))){
            throw new FraudEngineException("The name already exists in Report table ");
        }
        Report report = new Report();
        try {
            report.setName(request.getName());
            report.setDescription(request.getDescription());
            report.setCreatedBy(request.getCreatedBy());
            reportRepository.save(report);

            // for auditing purpose for CREATE
            report.setEntityId(null);
            report.setRecordBefore(null);
            report.setRequestDump(request);
        } catch (RuntimeException ex) {
            log.error("Error occurred while creating Creating entity from the database", ex);
            throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
        }

        return report;
    }
}
