package com.etz.fraudeagleeyemanager.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.entity.BaseAuditEntity;
import com.etz.fraudeagleeyemanager.entity.EventLogEntity;
import com.etz.fraudeagleeyemanager.repository.EventLogRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;

@Service
public class AuditTrailService {

	@Autowired
	EventLogRepository eventLogRepository;
	
	//@Async
	public void save(BaseAuditEntity baseAuditEntity) {
		Long userId = 0L;//userRepository.findByUsername(baseEntityModel.getUsername()).getId();
		
		EventLogEntity eventLogEntity = new EventLogEntity();
		eventLogEntity.setEntity(baseAuditEntity.getEntity());
		eventLogEntity.setEntityId(baseAuditEntity.getEntityId());
		eventLogEntity.setEventDesc(baseAuditEntity.getEventDescription());
		eventLogEntity.setRecordBefore(baseAuditEntity.getRecordBefore());
		eventLogEntity.setRecordAfter(baseAuditEntity.getRecordAfter());
		eventLogEntity.setEndpoint(baseAuditEntity.getEndpoint());
		eventLogEntity.setRequestDump(JsonConverter.objectToJson(baseAuditEntity.getRequestDump()));
		eventLogEntity.setUserId(userId);
		eventLogEntity.setEventType(baseAuditEntity.getEventType());
		eventLogEntity.setEventTime(LocalDateTime.now());
		
		eventLogRepository.save(eventLogEntity);
	}
}
