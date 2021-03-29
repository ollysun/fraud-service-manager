package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

}
