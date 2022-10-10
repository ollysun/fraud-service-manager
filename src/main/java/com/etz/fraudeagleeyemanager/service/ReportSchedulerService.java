package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.dto.request.CreateReportSchedulerRequest;
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
import com.etz.fraudeagleeyemanager.job.ReportJob;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportSchedulerService {

    private Trigger trigger;
    private final ReportRepository reportRepository;
    private final NotificationGroupRepository notificationGroupRepository;
    private final ReportSchedulerRepository reportSchedulerRepository;


    private final  Scheduler scheduler;

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
        reportScheduler.setIntervalValue(request.getIntegerValue());
        reportScheduler.setCreatedBy(request.getCreatedBy());

        // for auditing purpose for CREATE
        reportScheduler.setEntityId(null);
        reportScheduler.setRecordBefore(null);
        reportScheduler.setRequestDump(request);
        ReportScheduler reportSchedulerResult = reportSchedulerRepository.save(reportScheduler);
        JobDetail jobDetail = buildJobDetail(reportSchedulerResult);
        Trigger trigger = buildJobTrigger(jobDetail, reportSchedulerResult);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling email", e);
            throw new FraudEngineException("Error scheduling email : " + e.getMessage());
        }

        return reportSchedulerResult;
    }

    private JobDetail buildJobDetail(ReportScheduler request){

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("Report", "ReportJob");
        return JobBuilder.newJob(ReportJob.class)
                .withIdentity(UUID.randomUUID().toString(), "Report-jobs")
                .withDescription("Send Report Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ReportScheduler request) {
        if (request.getIntervalType().equals(IntervalType.DAY)) {
            trigger = newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .withSchedule(dailyAtHourAndMinute(23, 59))
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.MINUTE) && request.getReportLoop().equals(Boolean.TRUE)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(request.getIntervalValue())
                            .repeatForever())
                    .forJob(jobDetail)
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.MINUTE) && request.getReportLoop().equals(Boolean.FALSE)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .startAt(futureDate(request.getIntervalValue(), DateBuilder.IntervalUnit.MINUTE)) // use DateBuilder to create a date in the future
                    .forJob(jobDetail)
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.HOUR) && request.getReportLoop().equals(Boolean.FALSE)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .startAt(futureDate(request.getIntervalValue(), DateBuilder.IntervalUnit.HOUR)) // use DateBuilder to create a date in the future
                    .forJob(jobDetail)
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.HOUR) && request.getReportLoop().equals(Boolean.TRUE)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .withSchedule(simpleSchedule()
                            .withIntervalInHours(request.getIntervalValue())
                            .repeatForever())
                    .forJob(jobDetail)
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.WEEK)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .withSchedule(weeklyOnDayAndHourAndMinute(DateBuilder.SATURDAY, 23, 42))
                    .forJob(jobDetail)
                    .build();
        }else if(request.getIntervalType().equals(IntervalType.MONTH)){
            trigger = newTrigger()
                    .withIdentity(jobDetail.getKey().getName(), "report-triggers")
                    .withSchedule(monthlyOnDayAndHourAndMinute(29,23, 10))
                    .forJob(jobDetail)
                    .build();
        }


        return trigger;
    }

}
