package com.etz.fraudeagleeyemanager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.config.AuthServerMandatoryURL;
import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.ApprovalRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateUserNotificationRequest;
import com.etz.fraudeagleeyemanager.dto.request.UserNotificationRequest;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.RoleIdByPermissionResponse;
import com.etz.fraudeagleeyemanager.dto.response.RoleResponse;
import com.etz.fraudeagleeyemanager.dto.response.UserResponse;
import com.etz.fraudeagleeyemanager.entity.BaseEntity;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.entity.ProductDatasetId;
import com.etz.fraudeagleeyemanager.entity.ProductRuleId;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import com.etz.fraudeagleeyemanager.entity.UserNotification;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.InternalWatchlistRepository;
import com.etz.fraudeagleeyemanager.repository.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.repository.OfacWatchlistRepository;
import com.etz.fraudeagleeyemanager.repository.ParameterRepository;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.RuleRepository;
import com.etz.fraudeagleeyemanager.repository.ServiceRuleRepository;
import com.etz.fraudeagleeyemanager.repository.UserNotificationRepository;
import com.etz.fraudeagleeyemanager.util.ApiUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.RequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilityService {

	private final OfacWatchlistRepository ofacWatchlistRepository;
	private final InternalWatchlistRepository internalWatchlistRepository;
	private final NotificationGroupRepository notificationGroupRepository;
	private final RuleRepository ruleRepository;
	private final ServiceRuleRepository serviceRuleRepository;
	private final ParameterRepository parameterRepository;
	private final ProductDataSetRepository productDataSetRepository;
	private final UserNotificationRepository userNotificationRepository;

    private final ApiUtil apiUtil;
    private final AuthServerMandatoryURL authServerUrl;
	

	@PreAuthorize("hasAnyAuthority('OFAC.APPROVE', 'WATCHLIST_INTERNAL.APPROVE', 'NOTIFICATION_GROUP.APPROVE', 'RULE.APPROVE',"
			+ " 'RULE.PRODUCT.APPROVE', 'PARAMETER.APPROVE', 'SERVICE.DATASET.APPROVE', 'USER.APPROVE', 'ROLE.APPROVE')")
	public Boolean approval(ApprovalRequest request) {

		log.info("Approval Request: {}", request);
		switch(request.getEntity().toUpperCase()) {
		
		case AppConstant.OFAC_WATCHLIST : 
			Optional<OfacWatchlist> ofacWatchlistOptional = ofacWatchlistRepository.findById(Long.valueOf(request.getEntityId()));
			if(!ofacWatchlistOptional.isPresent()) {
				log.error("OfacWatchlist not found for ID " + Long.valueOf(request.getEntityId()));
				throw new ResourceNotFoundException("OfacWatlist not found for ID " + Long.valueOf(request.getEntityId()));
			}
			OfacWatchlist ofacWatchlist = ofacWatchlistOptional.get();
			// for auditing purpose for UPDATE
			ofacWatchlist.setEntityId(request.getEntityId());
			ofacWatchlist.setRecordBefore(JsonConverter.objectToJson(ofacWatchlist));
			ofacWatchlist.setRequestDump(request);
			
			ofacWatchlist.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			ofacWatchlist.setAuthoriser(request.getCreatedBy());
			ofacWatchlist.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(ofacWatchlistRepository, ofacWatchlist);
			break;
			
		case AppConstant.INTERNAL_WATCHLIST : 
			Optional<InternalWatchlist> internalWatchlistOptional = internalWatchlistRepository.findById(Long.valueOf(request.getEntityId()));
			if(!internalWatchlistOptional.isPresent()) {
				log.error("InternalWatchlist not found for ID " + Long.valueOf(request.getEntityId()));
				throw new ResourceNotFoundException("InternalWatchlist not found for ID " + Long.valueOf(request.getEntityId()));
			}
			InternalWatchlist internalWatchlist = internalWatchlistOptional.get();
			// for auditing purpose for UPDATE
			internalWatchlist.setEntityId(request.getEntityId());
			internalWatchlist.setRecordBefore(JsonConverter.objectToJson(internalWatchlist));
			internalWatchlist.setRequestDump(request);
			
			internalWatchlist.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			internalWatchlist.setAuthoriser(request.getCreatedBy());
			internalWatchlist.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(internalWatchlistRepository, internalWatchlist);
			break;
			
		case AppConstant.NOTIFICATION_GROUPS : 
			Optional<NotificationGroup> notificationGroupOptional = notificationGroupRepository.findById(Long.valueOf(request.getEntityId()));
			if(!notificationGroupOptional.isPresent()) {
				log.error("NotificationGroup not found for ID " + Long.valueOf(request.getEntityId()));
				throw new ResourceNotFoundException("NotificationGroup not found for ID " + Long.valueOf(request.getEntityId()));
			}
			NotificationGroup notificationGroup = notificationGroupOptional.get();
			// for auditing purpose for UPDATE
			notificationGroup.setEntityId(request.getEntityId());
			notificationGroup.setRecordBefore(JsonConverter.objectToJson(notificationGroup));
			notificationGroup.setRequestDump(request);
			
			notificationGroup.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			notificationGroup.setAuthoriser(request.getCreatedBy());
			notificationGroup.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(notificationGroupRepository, notificationGroup);
			break;
			
		case AppConstant.RULE : 
			Optional<Rule> ruleOptional = ruleRepository.findById(Long.valueOf(request.getEntityId()));
			if(!ruleOptional.isPresent()) {
				log.error("Rule not found for ID " + Long.valueOf(request.getEntityId()));
				throw new ResourceNotFoundException("Rule not found for ID " + Long.valueOf(request.getEntityId()));
			}
			Rule rule = ruleOptional.get();
			// for auditing purpose for UPDATE
			rule.setEntityId(request.getEntityId());
			rule.setRecordBefore(JsonConverter.objectToJson(rule));
			rule.setRequestDump(request);
			
			rule.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			rule.setAuthoriser(request.getCreatedBy());
			rule.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(ruleRepository, rule);
			break;
			
		case AppConstant.SERVICE_RULE : //entityId => service_ruleId
			String serviceId = request.getEntityId().split("_")[0];
			Long ruleId = Long.valueOf(request.getEntityId().split("_")[1]);
			Optional<ServiceRule> serviceRuleOptional = serviceRuleRepository.findById(new ProductRuleId(ruleId, serviceId));
			if(!serviceRuleOptional.isPresent()) {
				log.error("ServiceRule not found for ID " + request.getEntityId());
				throw new ResourceNotFoundException("ServiceRule not found for ID " + request.getEntityId());
			}
			ServiceRule serviceRule = serviceRuleOptional.get();
			// for auditing purpose for UPDATE
			serviceRule.setEntityId(request.getEntityId());
			serviceRule.setRecordBefore(JsonConverter.objectToJson(serviceRule));
			serviceRule.setRequestDump(request);
			
			serviceRule.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			serviceRule.setAuthoriser(request.getCreatedBy());
			serviceRule.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(serviceRuleRepository, serviceRule);
			break;
			
		case AppConstant.PARAMETER : 
			Optional<Parameter> parameterOptional = parameterRepository.findById(Long.valueOf(request.getEntityId()));
			if(!parameterOptional.isPresent()) {
				log.error("Parameter not found for ID " + Long.valueOf(request.getEntityId()));
				throw new ResourceNotFoundException("Parameter not found for ID " + Long.valueOf(request.getEntityId()));
			}
			Parameter parameter = parameterOptional.get();
			// for auditing purpose for UPDATE
			parameter.setEntityId(request.getEntityId());
			parameter.setRecordBefore(JsonConverter.objectToJson(parameter));
			parameter.setRequestDump(request);
			
			parameter.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			parameter.setAuthoriser(request.getCreatedBy());
			parameter.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(parameterRepository, parameter);
			break;
			
		case AppConstant.SERVICE_DATASET : // EntityId => productCode_ServiceId_DatasetId
			String productCode = request.getEntityId().split("_")[0];
			String serviceCode = request.getEntityId().split("_")[1];
			Long datasetId = Long.valueOf(request.getEntityId().split("_")[2]);
			
			Optional<ServiceDataSet> serviceDatasetOptional = productDataSetRepository.findById(new ProductDatasetId(datasetId, productCode, serviceCode));
			if(!serviceDatasetOptional.isPresent()) {
				log.error("ServiceDataSet not found for ID " + request.getEntityId());
				throw new ResourceNotFoundException("ServiceDataSet not found for ID " + request.getEntityId());
			}
			ServiceDataSet serviceDataset = serviceDatasetOptional.get();
			// for auditing purpose for UPDATE
			serviceDataset.setEntityId(request.getEntityId());
			serviceDataset.setRecordBefore(JsonConverter.objectToJson(serviceDataset));
			serviceDataset.setRequestDump(request);
			
			serviceDataset.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
			serviceDataset.setAuthoriser(request.getCreatedBy());
			serviceDataset.setUpdatedBy(request.getCreatedBy());
			updateAuthorised(productDataSetRepository, serviceDataset);
			break;

		case AppConstant.USER :
			try {
				log.info("Update User authoriser URL: {}", authServerUrl.getUserAuthoriserUrl());
				
				@SuppressWarnings("unchecked")
				ModelResponse<UserResponse> userResponse = apiUtil.put(authServerUrl.getUserAuthoriserUrl(), setFixedExtraHeader(), request, 
						(Class<ModelResponse<UserResponse>>) (Class<?>) ModelResponse.class);

				log.info("Update User authoriser http status code >>> {}", userResponse.getStatus());
				log.info("Update User authoriser message >>> {}", userResponse.getMessage());
				log.info("User response object >>> {}", userResponse.getData());
			
			} catch(Exception ex) {
				log.error("Call to Update User Authoriser endpoint failed >>> {}", ex);
				throw new FraudEngineException("Could not update User Authoriser");
			}
			break;
			
		case AppConstant.ROLE :
			try {
				log.info("Update User authoriser URL: {}", authServerUrl.getRoleAuthoriserUrl());
				
				@SuppressWarnings("unchecked")
				ModelResponse<RoleResponse> roleResponse = apiUtil.put(authServerUrl.getRoleAuthoriserUrl(), setFixedExtraHeader(), request, 
						(Class<ModelResponse<RoleResponse>>) (Class<?>) ModelResponse.class);
	
				log.info("Update Role authoriser http status code >>> {}", roleResponse.getStatus());
				log.info("Update Role authoriser message >>> {}", roleResponse.getMessage());
				log.info("Role response object >>> {}", roleResponse.getData());
			
			} catch(Exception ex) {
				log.error("Call to Update Role Authoriser endpoint failed >>> {}", ex);
				throw new FraudEngineException("Could not update Role Authoriser");
			}
			break;
		}
		
		return Boolean.TRUE;
	}
	
	private static HashMap<String, String> setFixedExtraHeader() {
		HashMap<String, String> extraHeaders = new HashMap<>();
		extraHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + RequestUtil.getToken());
		extraHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return extraHeaders;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateAuthorised(JpaRepository repository, BaseEntity entity) {
		try {
			repository.save(entity);
		} catch(Exception ex){
			log.error("Error occurred while saving entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public UserNotification createUserNotification(UserNotificationRequest request){
    	
		List<Long> roleIdsHavingEntityApprovalPermissionId = new ArrayList<>();
    	try {
			
    		String url = authServerUrl.getRoleIdsByPermissionNameUrl() + "?permissionName=" + request.getEntity().concat(AppConstant.DOT_APPROVE);
    		RoleIdByPermissionResponse response = apiUtil.get(url, setFixedExtraHeader(), RoleIdByPermissionResponse.class);

			log.info("RoleIdsByPermissionName URL : {}", url);
			log.info("Get roleIdsByPermissionNameUrl http status code >>> {}", response.getStatus());
			log.info("Get roleIdsByPermissionNameUrl message >>> {}", response.getMessage());
			log.info("RoleIdsByPermissionName response object >>> {}", response.getData());
		
			roleIdsHavingEntityApprovalPermissionId = response.getData(); //.stream().collect(Collectors.toList())
		} catch(Exception ex) {
			log.error("Call to Get roleIdsByPermissionNameUrl endpoint failed >>> {}", ex);
			throw new FraudEngineException("Could not Get roleIdsByPermissionName");
		}
    	
    	
    	// creates user notification for the first role having the entity approval permission
    	UserNotification userNotificationEntity = new UserNotification();
		try {
			userNotificationEntity.setEntityName(request.getEntity().toUpperCase());
			userNotificationEntity.setEntityID(request.getEntityId());
			userNotificationEntity.setNotifyType(1);
			userNotificationEntity.setRoleId(roleIdsHavingEntityApprovalPermissionId.stream().findFirst().get());
			userNotificationEntity.setUserID(request.getUserId());
			userNotificationEntity.setSystem(Boolean.TRUE);
			userNotificationEntity.setMessage(request.getMessage());
			userNotificationEntity.setStatus(Boolean.FALSE);
			userNotificationEntity.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			userNotificationEntity.setEntityId(null);
			userNotificationEntity.setRecordBefore(null);
			userNotificationEntity.setRequestDump(request);
		} catch(Exception ex){
			log.error("Error occurred while creating UserNotification entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		UserNotification userNotificationEntityCreated = saveUserNotificationToDatabase(userNotificationEntity);
    	
		// This part creates user notification for other roles having the entity approval permission
    	roleIdsHavingEntityApprovalPermissionId.stream().skip(1).forEach(roleId -> {
			UserNotification userNotification = new UserNotification();
    		try {
    			userNotification.setEntityName(request.getEntity().toUpperCase());
    			userNotification.setEntityID(request.getEntityId());
    			userNotification.setNotifyType(1);
    			userNotification.setRoleId(roleId);
    			userNotification.setUserID(request.getUserId());
    			userNotification.setSystem(Boolean.TRUE);
    			userNotification.setMessage(request.getMessage());
    			userNotification.setStatus(Boolean.FALSE);
    			userNotification.setCreatedBy(request.getCreatedBy());
    			
    			// for auditing purpose for CREATE
    			userNotification.setEntityId(null);
    			userNotification.setRecordBefore(null);
    			userNotification.setRequestDump(request);
    		} catch(Exception ex){
    			log.error("Error occurred while setting UserNotification entity object", ex);
    			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
    		}
    		saveUserNotificationToDatabase(userNotification);
    	});
		
		return userNotificationEntityCreated;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public UserNotification updateUserNotification(UpdateUserNotificationRequest updateUserNotificationRequest){
		Optional<UserNotification> userNotifcationOptional = userNotificationRepository.findById(updateUserNotificationRequest.getId());
		if(!userNotifcationOptional.isPresent()) {
			log.error("User Notification not found for ID " + updateUserNotificationRequest.getId());
			throw new ResourceNotFoundException("User Notification not found for ID " + updateUserNotificationRequest.getId());
		}
		
		UserNotification userNotificationEntity = userNotifcationOptional.get();
		try {
			// for auditing purpose for UPDATE
			userNotificationEntity.setEntityId(String.valueOf(userNotificationEntity.getId()));
			userNotificationEntity.setRecordBefore(JsonConverter.objectToJson(userNotificationEntity));
			userNotificationEntity.setRequestDump(updateUserNotificationRequest);
			
			userNotificationEntity.setStatus(Boolean.TRUE);
			userNotificationEntity.setUpdatedBy(updateUserNotificationRequest.getUpdatedBy());
		} catch(Exception ex){
			log.error("Error occurred while setting UserNotification entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		UserNotification userNotificationEntityUpdated = saveUserNotificationToDatabase(userNotificationEntity);
		
		// This part updates the read status of similar notification but for different role(s)
		Optional<List<UserNotification>> userNotificationOptinal = userNotificationRepository.findByEntityNameAndEntityID(userNotificationEntity.getEntityName(), userNotificationEntity.getEntityID());
		List<UserNotification> userNotifications = userNotificationOptinal.orElseGet(ArrayList::new);
		userNotifications.stream().filter(userNotification -> !userNotification.getId().equals(updateUserNotificationRequest.getId())).forEach(userNotification -> {
			// for auditing purpose for UPDATE
			userNotification.setEntityId(String.valueOf(userNotification.getId()));
			userNotification.setRecordBefore(JsonConverter.objectToJson(userNotification));
			userNotification.setRequestDump(updateUserNotificationRequest);
			
			userNotification.setStatus(Boolean.TRUE);
			userNotification.setUpdatedBy(userNotification.getUpdatedBy());
			saveUserNotificationToDatabase(userNotification);
		});
		
		return userNotificationEntityUpdated;
	}
	
	@Transactional(readOnly = true)
	public List<UserNotification> getUserNotification(Long userRoleId, Long userID){
		if (Objects.isNull(userRoleId) && Objects.isNull(userID)) {
			return userNotificationRepository.findAll().stream().filter(userNotification -> userNotification.getStatus().equals(Boolean.FALSE)).collect(Collectors.toList());
		}
		
		Optional<List<UserNotification>> userNotificationOptional = Optional.empty();
		if(Objects.nonNull(userRoleId) && Objects.nonNull(userID)) {
			userNotificationOptional = userNotificationRepository.findByRoleIdAndUserID(userRoleId, userID);
			
		}else if(Objects.nonNull(userRoleId)) {
			userNotificationOptional = userNotificationRepository.findByRoleId(userRoleId);
			
		}else if(Objects.nonNull(userID)) {
			userNotificationOptional = userNotificationRepository.findByUserID(userID);
		}

		List<UserNotification> userNotifications = new ArrayList<>();
		if(userNotificationOptional.isPresent()) {
			userNotifications = userNotificationOptional.get().stream().filter(userNotification -> userNotification.getStatus().equals(Boolean.FALSE)).collect(Collectors.toList());
		}
		
		return userNotifications;
	}
	
	private UserNotification saveUserNotificationToDatabase(UserNotification userNotificationEntity) {
		UserNotification persistedUserNotificationEntity;
		try {
			persistedUserNotificationEntity = userNotificationRepository.save(userNotificationEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving UserNotification entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		return persistedUserNotificationEntity;
	}
	
}
