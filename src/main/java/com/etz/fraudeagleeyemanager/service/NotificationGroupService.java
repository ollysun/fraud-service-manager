package com.etz.fraudeagleeyemanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.NotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateNotificationGroupRequest;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.NotificationGroupRedisRepository;
import com.etz.fraudeagleeyemanager.repository.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

@Service
public class NotificationGroupService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	NotificationGroupRepository notificationGroupRepository;
	
	@Autowired
	NotificationGroupRedisRepository notificationGroupRedisRepository;
		
	public NotificationGroup createNotificationGroup(NotificationGroupRequest request){
		NotificationGroup notificationGroup = new NotificationGroup();
		try {
			notificationGroup.setGroupName(request.getName());
			notificationGroup.setEmails(request.getEmails());
			notificationGroup.setPhones(request.getPhoneNos());
			notificationGroup.setEmailAlert(request.getMailAlert());
			notificationGroup.setSmsAlert(request.getSmsAlert());
			notificationGroup.setAuthorised(false);
			notificationGroup.setStatus(false);
			notificationGroup.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			notificationGroup.setEntityId(null);
			notificationGroup.setRecordBefore(null);
			notificationGroup.setRequestDump(request);
			
			NotificationGroup savedNotificationGroup = notificationGroupRepository.save(notificationGroup);
			notificationGroupRedisRepository.setHashOperations(redisTemplate);
			notificationGroupRedisRepository.update(savedNotificationGroup);
			return savedNotificationGroup;
		} catch(Exception ex){
			throw new FraudEngineException(ex.getLocalizedMessage());
		}
	}

	public NotificationGroup updateNotificationGroup(UpdateNotificationGroupRequest request, Long groupId){
		NotificationGroup notificationGroup = notificationGroupRepository.findById(groupId)
				.orElseThrow(() ->  new ResourceNotFoundException("Notification group not found for group ID " + request.getGroupId()));
		
		// for auditing purpose for UPDATE
		notificationGroup.setEntityId(request.getGroupId().toString());
		notificationGroup.setRecordBefore(JsonConverter.objectToJson(notificationGroup));
		notificationGroup.setRequestDump(request);
				
		notificationGroup.setEmails(request.getEmails());
		notificationGroup.setPhones(request.getPhoneNos());
		notificationGroup.setEmailAlert(request.getMailAlert());
		notificationGroup.setSmsAlert(request.getSmsAlert());
		notificationGroup.setStatus(request.getStatus());
		notificationGroup.setUpdatedBy(request.getUpdatedBy());
		NotificationGroup savedNotificationGroup = notificationGroupRepository.save(notificationGroup);
		notificationGroupRedisRepository.setHashOperations(redisTemplate);
		notificationGroupRedisRepository.update(savedNotificationGroup);
		return savedNotificationGroup;
	}

	public Page<NotificationGroup> getNotificationGroup(Long groupId, String groupName){
		if (groupId == null && groupName == null) {
			return notificationGroupRepository.findAll(PageRequestUtil.getPageRequest());
		}
		NotificationGroup notificationGroup = new NotificationGroup();
		if (groupId == null && !groupName.isEmpty()) {
			notificationGroup = notificationGroupRepository.findByGroupName(groupName)
					.orElseThrow(() -> new ResourceNotFoundException("Notification Group Not found for group name " + groupName));
		} else if (groupId != null && groupName == null) {
			notificationGroup = notificationGroupRepository.findById(groupId)
					.orElseThrow(() -> new ResourceNotFoundException("Notification Group Not found for group Id " + groupId));
		} else {
			notificationGroup = notificationGroupRepository.findByIdAndGroupName(groupId, groupName).orElseThrow(
					() -> new ResourceNotFoundException("Notification Group Not found for group name " + groupName + " and group Id " + groupId));
		}
		return notificationGroupRepository.findAll(Example.of(notificationGroup), PageRequestUtil.getPageRequest());		
	}
	
}
