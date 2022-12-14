package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.NotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateNotificationGroupRequest;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.NotificationGroupRedisRepository;
import com.etz.fraudeagleeyemanager.repository.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationGroupService {

	private final AppUtil appUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final NotificationGroupRepository notificationGroupRepository;
	private final NotificationGroupRedisRepository notificationGroupRedisRepository;

	@Transactional(rollbackFor = Throwable.class)
	public NotificationGroup addNotificationGroup(NotificationGroupRequest request){
		NotificationGroup notificationGroup = new NotificationGroup();
		notificationGroup.setGroupName(request.getName());
		notificationGroup.setEmails(AppUtil.listEmailToString(request.getEmails()));
		notificationGroup.setPhones(AppUtil.listPhoneToString(request.getPhoneNos()));
		notificationGroup.setEmailAlert(request.getMailAlert());
		notificationGroup.setSmsAlert(request.getSmsAlert());
		notificationGroup.setAuthorised(false);
		notificationGroup.setStatus(false);
		notificationGroup.setCreatedBy(request.getCreatedBy());

		// for auditing purpose for CREATE
		notificationGroup.setEntityId(null);
		notificationGroup.setRecordBefore(null);
		notificationGroup.setRequestDump(request);

		return addNotificationGroupEntityToDatabase(notificationGroup, notificationGroup.getCreatedBy());
	}

	@Transactional(rollbackFor = Throwable.class)
	public NotificationGroup updateNotificationGroup(UpdateNotificationGroupRequest request){
		Optional<NotificationGroup> notificationGroupOptional = notificationGroupRepository.findById(request.getGroupId());
		if(!notificationGroupOptional.isPresent()) {
			throw new ResourceNotFoundException("Notification group not found for group ID " + request.getGroupId());
		}
		NotificationGroup notificationGroup = notificationGroupOptional.get();
		// for auditing purpose for UPDATE
		notificationGroup.setEntityId(String.valueOf(request.getGroupId()));
		notificationGroup.setRecordBefore(JsonConverter.objectToJson(notificationGroup));
		notificationGroup.setRequestDump(request);

		notificationGroup.setEmails(AppUtil.listEmailToString(request.getEmails()));
		notificationGroup.setGroupName(request.getName());
		notificationGroup.setPhones(AppUtil.listPhoneToString(request.getPhoneNos()));
		notificationGroup.setEmailAlert(request.getMailAlert());
		notificationGroup.setSmsAlert(request.getSmsAlert());
		notificationGroup.setAuthorised(false);
		notificationGroup.setStatus(request.getStatus());
		notificationGroup.setUpdatedBy(request.getUpdatedBy());

		return addNotificationGroupEntityToDatabase(notificationGroup, notificationGroup.getUpdatedBy());
	}

	@Transactional(readOnly = true)
	public Page<NotificationGroup> getNotificationGroup(Long groupId, String groupName){
		if (Objects.isNull(groupId) && StringUtils.isBlank(groupName)) {
			return notificationGroupRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Optional<NotificationGroup> notificationGroupOptional;
		if (Objects.isNull(groupId) && StringUtils.isNotBlank(groupName) ){
			notificationGroupOptional = notificationGroupRepository.findByGroupName(groupName);
			if(!notificationGroupOptional.isPresent()) {
				throw new ResourceNotFoundException("Notification Group Not found for group name " + groupName);
			}
		} else if (!Objects.isNull(groupId) && Objects.isNull(groupName)) {
			notificationGroupOptional = notificationGroupRepository.findById(groupId);
			if(!notificationGroupOptional.isPresent()) {
				throw new ResourceNotFoundException("Notification Group Not found for group Id " + groupId);
			}
		} else {
			notificationGroupOptional = notificationGroupRepository.findByIdAndGroupName(groupId, groupName);
			if(!notificationGroupOptional.isPresent()) {
				throw new ResourceNotFoundException("Notification Group Not found for group name " + groupName + " and group Id " + groupId);
			}
		}
		return notificationGroupRepository.findAll(Example.of(notificationGroupOptional.get()), PageRequestUtil.getPageRequest());		
	}
	
	private NotificationGroup addNotificationGroupEntityToDatabase(NotificationGroup notificationGroupEntity, String createdBy) {
		NotificationGroup persistedNotificationGrouplistEntity = new NotificationGroup();
		try {
			persistedNotificationGrouplistEntity = notificationGroupRepository.save(notificationGroupEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving NotificationGroup entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addNotificationGroupEntityToRedis(persistedNotificationGrouplistEntity);
		// create & user notification
		appUtil.createUserNotification(AppConstant.NOTIFICATION_GROUPS, persistedNotificationGrouplistEntity.getId().toString(), createdBy);
		return persistedNotificationGrouplistEntity;
	}
	
	private void addNotificationGroupEntityToRedis(NotificationGroup alreadyPersistedNotificationGroupEntity) {
		try {
			notificationGroupRedisRepository.setHashOperations(redisTemplate);
			notificationGroupRedisRepository.update(alreadyPersistedNotificationGroupEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving NotificationGroup entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
}
