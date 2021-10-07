package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.ReportScheduler;

@Repository
public interface ReportSchedulerRepository extends JpaRepository<ReportScheduler, Long>{

    List<ReportScheduler> findByReportId(Long reportId);

}
