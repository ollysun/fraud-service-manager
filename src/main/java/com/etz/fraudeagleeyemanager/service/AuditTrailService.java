package com.etz.fraudeagleeyemanager.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.entity.EventLogEntity;
import com.etz.fraudeagleeyemanager.repository.EventLogRepository;

@Service
public class AuditTrailService {

	@Autowired
	EventLogRepository eventLogRepository;
	
	@Autowired
	//UserRepository userRepository;
	
	@Async
	public void saveAuditTrail(String eventType, String eventDescription, String username) {
		Long userId = 0L;//userRepository.findByUsername(username).getId();
		
		EventLogEntity eventLogEntity = new EventLogEntity();
		eventLogEntity.setEventTime(LocalDateTime.now());
		eventLogEntity.setEventType(eventType);
		eventLogEntity.setEventDesc(eventDescription);
		eventLogEntity.setUserId(userId);
		eventLogRepository.save(eventLogEntity);
	}
}
