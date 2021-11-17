package com.etz.fraudeagleeyemanager.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class ReportJob  implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // create query
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());


        // generate report
    }
}
