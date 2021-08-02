package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ReportScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportSchedulerRepository extends JpaRepository<ReportScheduler, Long>{

    List<ReportScheduler> findByReportId(Long reportId);

}
