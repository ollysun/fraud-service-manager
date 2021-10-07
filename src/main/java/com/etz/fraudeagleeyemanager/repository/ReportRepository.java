package com.etz.fraudeagleeyemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByName(String name);
}
