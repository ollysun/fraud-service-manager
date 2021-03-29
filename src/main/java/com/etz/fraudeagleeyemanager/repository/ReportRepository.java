package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
