package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateReportRequest;
import com.etz.fraudeagleeyemanager.dto.request.CreateReportSchedulerRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateReportSchedulerRequest;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.entity.Report;
import com.etz.fraudeagleeyemanager.entity.ReportScheduler;
import com.etz.fraudeagleeyemanager.enums.ExportType;
import com.etz.fraudeagleeyemanager.enums.IntervalType;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.repository.ReportRepository;
import com.etz.fraudeagleeyemanager.repository.ReportSchedulerRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportRepository reportRepository;
    private final NotificationGroupRepository notificationGroupRepository;
    private final ReportSchedulerRepository reportSchedulerRepository;


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

    @Transactional(readOnly = true)
    public List<Report> getReportList(){
        return reportRepository.findAll();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ReportScheduler createReportScheduler(CreateReportSchedulerRequest request) {
       Report report = reportRepository.findById(request.getReportId())
               .orElseThrow(() -> new ResourceNotFoundException("Report not found for the Id: " + request.getReportId()));

        NotificationGroup notificationGroup = notificationGroupRepository.findById(request.getReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification Group Not Found for the Id: " + request.getNotificationId()));

        ReportScheduler reportScheduler = new ReportScheduler();
        reportScheduler.setReport(report);
        reportScheduler.setNotificationGroup(notificationGroup);
        reportScheduler.setExportType(ExportType.valueOf(AppUtil.checkExportType(request.getExportType())));
        reportScheduler.setStatus(Boolean.TRUE);
        reportScheduler.setIntervalType(IntervalType.valueOf(AppUtil.checkIntervalType(request.getIntervalType())));
        reportScheduler.setCreatedBy(request.getCreatedBy());

        // for auditing purpose for CREATE
        reportScheduler.setEntityId(null);
        reportScheduler.setRecordBefore(null);
        reportScheduler.setRequestDump(request);
        return reportSchedulerRepository.save(reportScheduler);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ReportScheduler updateReportScheduler(UpdateReportSchedulerRequest request) {

        ReportScheduler reportScheduler = reportSchedulerRepository.findById((long) request.getReportSchedulerId())
                .orElseThrow(() -> new ResourceNotFoundException("No detail for this reportScheduler id : " + request.getReportSchedulerId()));

        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for the Id: " + request.getReportId()));

        NotificationGroup notificationGroup = notificationGroupRepository.findById(request.getReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification Group Not Found for the Id: " + request.getNotificationId()));

        reportScheduler.setReport(report);
        reportScheduler.setNotificationGroup(notificationGroup);
        reportScheduler.setExportType(ExportType.valueOf(AppUtil.checkExportType(request.getExportType())));
        reportScheduler.setStatus(request.getStatus());
        reportScheduler.setIntervalType(IntervalType.valueOf(AppUtil.checkIntervalType(request.getIntervalType())));
        reportScheduler.setCreatedBy(request.getCreatedBy());

        // for auditing purpose for Update
        reportScheduler.setEntityId(null);
        reportScheduler.setRecordBefore(null);
        reportScheduler.setRequestDump(request);
        return reportSchedulerRepository.save(reportScheduler);
    }

    @Transactional(readOnly = true)
    public Page<ReportScheduler> getReportScheduler(Long reportScheduleId){
        if (Objects.isNull(reportScheduleId)) {
            return reportSchedulerRepository.findAll(PageRequestUtil.getPageRequest());
        }
        ReportScheduler reportScheduler = reportSchedulerRepository.findById(reportScheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Report Scheduler Not found " + reportScheduleId));
        reportScheduler.setId(reportScheduleId);
        return reportSchedulerRepository.findAll(Example.of(reportScheduler), PageRequestUtil.getPageRequest());
    }



}
