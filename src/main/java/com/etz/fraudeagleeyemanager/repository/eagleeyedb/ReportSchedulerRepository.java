package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ReportScheduler;

import java.util.List;

@Repository
public interface ReportSchedulerRepository extends JpaRepository<ReportScheduler, Long>{

    List<ReportScheduler> findByReportId(Long reportId);

}
