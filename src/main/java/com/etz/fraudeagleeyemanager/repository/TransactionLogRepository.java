package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.TransactionLogEntity;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLogEntity, Long> {

	List<TransactionLogEntity> findByProductCode(String productCode);
}
