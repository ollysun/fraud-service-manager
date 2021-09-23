package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.EventLogEntity;

@Repository
public interface EventLogRepository extends JpaRepository<EventLogEntity, Long> {

}
