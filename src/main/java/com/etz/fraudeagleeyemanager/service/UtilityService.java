package com.etz.fraudeagleeyemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.ApprovalRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateUserNotificationRequest;
import com.etz.fraudeagleeyemanager.dto.request.UserNotificationRequest;
import com.etz.fraudeagleeyemanager.entity.authservicedb.PermissionEntity;
import com.etz.fraudeagleeyemanager.entity.authservicedb.Role;
import com.etz.fraudeagleeyemanager.entity.authservicedb.RolePermission;
import com.etz.fraudeagleeyemanager.entity.authservicedb.UserEntity;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.BaseEntity;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.InternalWatchlist;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.NotificationGroup;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.OfacWatchlist;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.Parameter;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ProductDatasetId;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ProductRuleId;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.Rule;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ServiceDataSet;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ServiceRule;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.UserNotification;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.authservicedb.PermissionRepository;
import com.etz.fraudeagleeyemanager.repository.authservicedb.RolePermissionRepository;
import com.etz.fraudeagleeyemanager.repository.authservicedb.RoleRepository;
import com.etz.fraudeagleeyemanager.repository.authservicedb.UserRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.InternalWatchlistRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.OfacWatchlistRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.ParameterRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.RuleRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.ServiceRuleRepository;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.UserNotificationRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;

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
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final RolePermissionRepository rolePermissionRepository;
	private final PermissionRepository permissionRepository;
	
	
	public Boolean approval(ApprovalRequest request) {

		// confirm that user role has permission to approve
		if(!hasPermissionToApprove(request)){
			log.error("User Role has no permission to approve entity {}", request.getEntity());
			throw new ResourceNotFoundException("User Role does not have the permission to approve entity " + request.getEntity());
		}
		
		switch(request.getEntity().toUpperCase()) {
		
		case AppConstant.OFAC_WATCHLIST : 
			Optional<OfacWatchlist> ofacWatchlistOptional = ofacWatchlistRepository.findById(Long.valueOf(request.getEntityId()));
			if(!ofacWatchlistOptional.isPresent()) {
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
			
		case AppConstant.SERVICE_RULE : //entityId - service_ruleId
			String serviceId = request.getEntityId().split("_")[0];
			Long ruleId = Long.valueOf(request.getEntityId().split("_")[1]);
			Optional<ServiceRule> serviceRuleOptional = serviceRuleRepository.findById(new ProductRuleId(ruleId, serviceId));
			if(!serviceRuleOptional.isPresent()) {
				throw new ResourceNotFoundException("ServiceRule not found for ID " + Long.valueOf(request.getEntityId()));
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
			
		case AppConstant.SERVICE_DATASET : // EntityId - productCode_ServiceId_DatasetId
			String productCode = request.getEntityId().split("_")[0];
			String serviceCode = request.getEntityId().split("_")[1];
			Long datasetId = Long.valueOf(request.getEntityId().split("_")[2]);
			
			Optional<ServiceDataSet> serviceDatasetOptional = productDataSetRepository.findById(new ProductDatasetId(datasetId, productCode, serviceCode));
			if(!serviceDatasetOptional.isPresent()) {
				throw new ResourceNotFoundException("ServiceDataSet not found for ID " + Long.valueOf(request.getEntityId()));
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

		case AppConstant.USER : saveUser(request);	break;
			
		case AppConstant.ROLE : saveRole(request);	break;
		}
		
		return Boolean.TRUE;
	}
	
	private boolean hasPermissionToApprove(ApprovalRequest request) {
		// get all permissions
		List<PermissionEntity> allPermissions = permissionRepository.findAll();
		
		// get all permission IDs user role have
		List<Long> rolePermissions = rolePermissionRepository.findByRoleId(request.getUserRole()).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
		
		// filter out permissions that the user role has
		List<PermissionEntity> filteredPermissions = allPermissions.stream().filter(p -> rolePermissions.contains(p.getId())).collect(Collectors.toList());
		
		// get number of permissions with approval
		Long permCount = filteredPermissions.stream().map(PermissionEntity::getName).filter(s -> s.equals(request.getEntity().concat(AppConstant.DOT_APPROVE))).count();
		
		// if user has approval permission, permCount will be equal to 1 and return TRUE
		return (permCount > 0);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateAuthorised(JpaRepository repository, BaseEntity entity) {
		//<? extends BaseEntity, ? extends Serializable>
		try {
			repository.save(entity);
		} catch(Exception ex){
			log.error("Error occurred while saving entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
	}
	
	@Transactional("authServiceTransactionManager")
	public void saveRole(ApprovalRequest request) {
		Optional<Role> roleOptional = roleRepository.findById(Long.valueOf(request.getEntityId()));
		if(!roleOptional.isPresent()) {
			throw new ResourceNotFoundException("Role not found for ID " + Long.valueOf(request.getEntityId()));
		}
		Role role = roleOptional.get();
		// for auditing purpose for UPDATE
		role.setEntityId(request.getEntityId());
		role.setRecordBefore(JsonConverter.objectToJson(role));
		role.setRequestDump(request);
		
		role.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
		role.setAuthoriser(request.getCreatedBy());
		role.setUpdatedBy(request.getCreatedBy());
		updateAuthorised(roleRepository, role);
	}
	
	@Transactional("authServiceTransactionManager")
	public void saveUser(ApprovalRequest request) {
		Optional<UserEntity> userOptional = userRepository.findById(Long.valueOf(request.getEntityId()));
		if(!userOptional.isPresent()) {
			throw new ResourceNotFoundException("UserEntity not found for ID " + Long.valueOf(request.getEntityId()));
		}
		UserEntity userEntity = userOptional.get();
		// for auditing purpose for UPDATE
		userEntity.setEntityId(request.getEntityId());
		userEntity.setRecordBefore(JsonConverter.objectToJson(userEntity));
		userEntity.setRequestDump(request);
		
		userEntity.setAuthorised(request.getAction().equalsIgnoreCase(AppConstant.APPROVE_ACTION) ? Boolean.TRUE : Boolean.FALSE);
		userEntity.setAuthoriser(request.getCreatedBy());
		userEntity.setUpdatedBy(request.getCreatedBy());
		updateAuthorised(userRepository, userEntity);
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public UserNotification createUserNotification(UserNotificationRequest request){
		UserNotification userNotificationEntity = new UserNotification();
		try {
			userNotificationEntity.setEntityName(request.getEntity().toUpperCase());
			userNotificationEntity.setEntityID(request.getEntityId());
			userNotificationEntity.setNotifyType(1);
			userNotificationEntity.setRoleId(request.getRoleId());
			userNotificationEntity.setUserId(request.getUserId());
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
		return saveUserNotificationToDatabase(userNotificationEntity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public UserNotification updateUserNotification(UpdateUserNotificationRequest updateUserNotificationRequest){
		Optional<UserNotification> userNotifcationOptional = userNotificationRepository.findById(updateUserNotificationRequest.getId());
		if(!userNotifcationOptional.isPresent()) {
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
			log.error("Error occurred while creating UserNotification entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveUserNotificationToDatabase(userNotificationEntity);
	}
	
	@Transactional(readOnly = true)
	public List<UserNotification> getUserNotification(Long userRoleId, Long userId){
		if (Objects.isNull(userRoleId) && Objects.isNull(userId)) {
			return userNotificationRepository.findAll().stream().filter(userNotification -> userNotification.getStatus().equals(Boolean.FALSE)).collect(Collectors.toList());
		}
		
		Optional<List<UserNotification>> userNotificationOptional = Optional.empty();
		if(Objects.nonNull(userRoleId) && Objects.nonNull(userId)) {
			userNotificationOptional = userNotificationRepository.findByRoleIdAndUserId(userRoleId, userId);
			
		}else if(Objects.nonNull(userRoleId)) {
			userNotificationOptional = userNotificationRepository.findByRoleId(userRoleId);
			
		}else if(Objects.nonNull(userId)) {
			userNotificationOptional = userNotificationRepository.findByUserId(userId);
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
