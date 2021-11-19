package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.UpdatedRuleResponse;
import com.etz.fraudeagleeyemanager.entity.ProductRuleId;
import com.etz.fraudeagleeyemanager.entity.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRuleRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.RuleRedisRepository;
import com.etz.fraudeagleeyemanager.repository.NotificationGroupRepository;
import com.etz.fraudeagleeyemanager.repository.ProductDataSetRepository;
import com.etz.fraudeagleeyemanager.repository.ProductServiceRepository;
import com.etz.fraudeagleeyemanager.repository.RuleRepository;
import com.etz.fraudeagleeyemanager.repository.ServiceRuleRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {

	private final AppUtil appUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ServiceRuleRepository serviceRuleRepository;
	private final NotificationGroupRepository notificationGroupRepository;
	private final RuleRepository ruleRepository;
	private final RuleRedisRepository ruleRedisRepository;
	private final ProductRuleRedisRepository productRuleRedisRepository;
	private final ProductServiceRepository productServiceRepository;
	private final ProductDataSetRepository productDataSetRepository;

	//@PersistenceContext(unitName = "primary")
	private EntityManager em;

	@CacheEvict(value = "product", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
	public Rule addRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
			if (Boolean.TRUE.equals(ruleRepository.existsByName(request.getRuleName()))){
				throw new FraudEngineException("Similar record already exists");
			}
			ruleEntity.setName(request.getRuleName());
			ruleEntity.setValueOneDataType(AppUtil.checkDataType(request.getFirstDataType()));
			ruleEntity.setSourceValueOne(request.getFirstSourceVal());

			// check the operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstDataType(),request.getFirstOperator()));

			if(AppUtil.isCompareValueValid(request.getFirstDataType(),request.getFirstCompareVal())){
				ruleEntity.setCompareValueOne(request.getFirstCompareVal());
			}else {
				throw new FraudEngineException("Invalid  first datatype and compare value ");
			}
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSourceVal()));
			// check logical operator
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));

			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());

			ruleEntity.setValueTwoDataType(AppUtil.checkDataType(request.getSecondDataType()));
			// check operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondDataType(),request.getSecondOperator()));
			if(AppUtil.isCompareValueValid(request.getSecondDataType(),request.getSecondCompareVal())){
				ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
			}
			ruleEntity.setDataSourceValTwo(AppUtil.checkDataSource(request.getSecondDataSourceVal()));
			ruleEntity.setSuspicionLevel(request.getSuspicion());
		    ruleEntity.setAction(AppUtil.getLevelAction(request.getSuspicion()));
			ruleEntity.setAuthorised(false);
			ruleEntity.setStatus(Boolean.TRUE);
			ruleEntity.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			ruleEntity.setEntityId(null);
			ruleEntity.setRecordBefore(null);
			ruleEntity.setRequestDump(request);

		return addRuleEntityToDatabase(ruleEntity, ruleEntity.getCreatedBy());
	}

	@Transactional(rollbackFor = Throwable.class)
	public Rule updateRule(UpdateRuleRequest request) {

		Optional<Rule> ruleEntityOptional = ruleRepository.findById(request.getRuleId());
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId());
		}
		Rule ruleEntity = ruleEntityOptional.get();
			// for auditing purpose for UPDATE
			ruleEntity.setEntityId(String.valueOf(request.getRuleId()));
			ruleEntity.setRecordBefore(JsonConverter.objectToJson(ruleEntity));
			ruleEntity.setRequestDump(request);


			ruleEntity.setValueOneDataType(AppUtil.checkDataType(request.getFirstDataType()));
			ruleEntity.setSourceValueOne(request.getFirstSourceVal());

			// check the operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstDataType(),request.getFirstOperator()));
			if(AppUtil.isCompareValueValid(request.getFirstDataType(),request.getFirstCompareVal())){
				ruleEntity.setCompareValueOne(request.getFirstCompareVal());
			}else {
				throw new FraudEngineException("Invalid  first compare value ");
			}
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSource()));
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));
			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
			ruleEntity.setValueTwoDataType(AppUtil.checkDataType(request.getSecondDataType()));
			// check for the list of operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondDataType(),request.getSecondOperator()));
			if (AppUtil.isCompareValueValid(request.getFirstDataType(), request.getFirstCompareVal())) {
				ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
			}else {
				throw new FraudEngineException("Invalid  second compare value ");
			}
			ruleEntity.setDataSourceValTwo(AppUtil.checkDataSource(request.getSecondDataSource()));
			ruleEntity.setSuspicionLevel(request.getSuspicion());
			ruleEntity.setAction(AppUtil.getLevelAction(request.getSuspicion()));
			ruleEntity.setAuthorised(false);
			ruleEntity.setStatus(request.getStatus());
			ruleEntity.setUpdatedBy(request.getUpdatedBy());

		return outputUpdatedRuleResponse(addRuleEntityToDatabase(ruleEntity, ruleEntity.getUpdatedBy()));
	}

	private Rule addRuleEntityToDatabase(Rule ruleEntity, String createdBy) {
		Rule persistedRuleEntity;
		try {
			persistedRuleEntity = ruleRepository.save(ruleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Rule entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addRuleEntityToRedis(persistedRuleEntity);
		// create & user notification
		appUtil.createUserNotification(AppConstant.RULE, persistedRuleEntity.getId().toString(), createdBy);
		return persistedRuleEntity;
	}
	
	private void addRuleEntityToRedis(Rule alreadyPersistedRuleEntity) {
		try {
			ruleRedisRepository.setHashOperations(redisTemplate);
			ruleRedisRepository.update(alreadyPersistedRuleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Rule entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
	private UpdatedRuleResponse outputUpdatedRuleResponse(Rule rule){

		UpdatedRuleResponse updatedRuleResponse = new UpdatedRuleResponse();
		BeanUtils.copyProperties(rule,updatedRuleResponse, "address","createdAt","createdBy");
		return updatedRuleResponse;
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteRule(Long ruleId) {
		List<ServiceRule> prodRuleEntity = serviceRuleRepository.findByRuleId(ruleId);
		if (!(prodRuleEntity.isEmpty())){
			throw new FraudEngineException("Please unmap the ruleId before deleting " + ruleId);
		}
		
		Optional<Rule> ruleEntityOptional = ruleRepository.findById(ruleId);
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + ruleId);
		}
		
		Rule ruleEntity = ruleEntityOptional.get();
		// for auditing purpose for DELETE
		ruleEntity.setEntityId(String.valueOf(ruleId));
		ruleEntity.setRecordBefore(JsonConverter.objectToJson(ruleId));
		ruleEntity.setRecordAfter(null);
		ruleEntity.setRequestDump(ruleId);
		
		try {
			ruleRepository.delete(ruleEntity);
		} catch(Exception ex){
		//	log.error("Error occurred while deleting Rule entity from the database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			ruleRedisRepository.setHashOperations(redisTemplate);
			ruleRedisRepository.delete(ruleId);
		} catch (Exception ex) {
		//	log.error("Error occurred while deleting Rule entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		
		// child records have to be deleted in Redis
		productRuleRedisRepository.setHashOperations(redisTemplate);
		Set<String> keys = productRuleRedisRepository.scanKeys("*:" + ruleId);
		keys.forEach(productRuleRedisRepository::delete);
		
		return Boolean.TRUE;
	}

	@Transactional(readOnly = true)
	public Page<RuleResponse> getRule(Long ruleId, String name) {
		List<Rule> ruleList = ruleRepository.findAll();
		List<RuleResponse> ruleResponseList = new ArrayList<>();
		if (Objects.isNull(ruleId) && StringUtils.isBlank(name)) {
			ruleResponseList = outputRuleResponseList(ruleList);
			return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());

		}else if(StringUtils.isNotBlank(name) && Objects.isNull(ruleId)){
			List<Rule> listRuleName = ruleList.parallelStream()
					.filter(sd -> sd.getName().equalsIgnoreCase(name))
					.collect(Collectors.toList());
			ruleResponseList = outputRuleResponseList(listRuleName);
			return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
		}else if(ruleId != null &&  StringUtils.isBlank(name)){
			List<Rule> listRuleId = ruleList.parallelStream()
					.filter(sd -> Objects.equals(sd.getId(),ruleId))
					.collect(Collectors.toList());
			ruleResponseList = outputRuleResponseList(listRuleId);
		}
		return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ProductRuleResponse> mapRuleToService(MapRuleToServiceRequest request) {
		List<String> datasetList = new ArrayList<>();
		Optional<ProductServiceEntity> productServiceEntityOptional = productServiceRepository.findById(request.getServiceId());
		if (!productServiceEntityOptional.isPresent()){
			throw new ResourceNotFoundException("Product Service  not found for serviceId " + request.getServiceId());
		}

		// verify the dataset on rule
		List<ServiceDataSet> serviceDataSetList = productDataSetRepository.findByServiceId(request.getServiceId());
//		if (serviceDataSetList.isEmpty()){
//			throw new ResourceNotFoundException("Service Dataset  not found for serviceId " + request.getServiceId());
//		}
//		for (ServiceDataSet sd: serviceDataSetList){
//			datasetList.add(sd.getFieldName());
//		}


//		for(String name: datasetList) {
//			if (ruleEntityOptional.get().getDataSourceValOne().equalsIgnoreCase("Transactional")
//					&& !(name.equalsIgnoreCase(ruleEntityOptional.get().getSourceValueOne()))) {
//				throw new FraudEngineException("The first source value: "  + ruleEntityOptional.get().getSourceValueOne() + "  not found in the dataset fieldName created on rule with name : " + ruleEntityOptional.get().getName());
//			}
//			if ((StringUtils.isNotBlank(ruleEntityOptional.get().getDataSourceValTwo()) && ruleEntityOptional.get().getDataSourceValTwo().equalsIgnoreCase("Transactional")) &&
//					!(name.equalsIgnoreCase(ruleEntityOptional.get().getSourceValueTwo()))) {
//				throw new FraudEngineException("The Second source value: "  + ruleEntityOptional.get().getSourceValueTwo() + "   not found in the dataset fieldName created on rule with name : " + ruleEntityOptional.get().getName());
//			}
//		}

		
//		if (!Objects.isNull(request.getNotificationGroupId()) && !notificationGroupRepository.findById(request.getNotificationGroupId()).isPresent()) {
//			throw new ResourceNotFoundException("Notification Group Id Not found for Id " + request.getNotificationGroupId());
//		}

		ServiceRule serviceRuleEntity = new ServiceRule();
		List<ServiceRule> createdServiceRuleEntity = new ArrayList<>();

		for (Long id: request.getRuleId()){
			Optional<Rule> ruleEntityOptional = ruleRepository.findById(id);
			if (!ruleEntityOptional.isPresent()) {
				throw new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId());
			}
			try {
				serviceRuleEntity.setServiceId(request.getServiceId());
				serviceRuleEntity.setRuleId(id);
				serviceRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
				if (Boolean.TRUE.equals(request.getNotifyAdmin())){
					serviceRuleEntity.setNotificationGroupId(request.getNotificationGroupId());
				}
				serviceRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
				serviceRuleEntity.setStatus(Boolean.TRUE);
				serviceRuleEntity.setAuthorised(false);
				serviceRuleEntity.setCreatedBy(request.getCreatedBy());

				// for auditing purpose for CREATE
				serviceRuleEntity.setEntityId(null);
				serviceRuleEntity.setRecordBefore(null);
				serviceRuleEntity.setRequestDump(request);
			} catch (Exception ex) {
				log.error("Error occurred while creating Product Rule entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			createdServiceRuleEntity.add(saveRuleServiceEntityToDatabase(serviceRuleEntity, serviceRuleEntity.getCreatedBy()));
		}
		return outputProductRuleResponseList(createdServiceRuleEntity);

		//return saveRuleServiceEntityToDatabase(serviceRuleEntity, serviceRuleEntity.getCreatedBy());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ProductRuleResponse> updateServiceRule(UpdateMapRuleToServiceRequest request) {
		List<ServiceRule> prodRuleEntityList = serviceRuleRepository.findByRuleId(request.getServiceRuleId());
		if(prodRuleEntityList.isEmpty()){
			throw new FraudEngineException("ServiceRule Not found for Id " + request.getServiceRuleId());
		}
		
		if (!Objects.isNull(request.getNotificationGroupId()) && !notificationGroupRepository.findById(request.getNotificationGroupId()).isPresent()) {
			throw new ResourceNotFoundException("Notification Group Id Not found for Id " + request.getNotificationGroupId());
		}


		List<ServiceRule> updatedServiceRuleEntity = new ArrayList<>();
		for (ServiceRule serviceRuleEntity:prodRuleEntityList) {
			try {
				// for auditing purpose for UPDATE
				serviceRuleEntity.setEntityId("ruleId: " + serviceRuleEntity.getRuleId() + " serviceId: " + serviceRuleEntity.getServiceId());
				serviceRuleEntity.setRecordBefore(JsonConverter.objectToJson(serviceRuleEntity));
				serviceRuleEntity.setRequestDump(request);

				serviceRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
				if (Boolean.TRUE.equals(request.getNotifyAdmin())){
					serviceRuleEntity.setNotificationGroupId(request.getNotificationGroupId());
				}
				serviceRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
				serviceRuleEntity.setStatus(request.getStatus());
				serviceRuleEntity.setAuthorised(false);
				serviceRuleEntity.setUpdatedBy(request.getUpdatedBy());
			} catch (Exception ex) {
				log.error("Error occurred while creating Product Rule entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			updatedServiceRuleEntity.add(saveRuleServiceEntityToDatabase(serviceRuleEntity, serviceRuleEntity.getUpdatedBy()));
		}
		return outputProductRuleResponseList(updatedServiceRuleEntity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteServiceRule(Long ruleId, String serviceId) {
		if (Objects.isNull(ruleId) && Objects.isNull(serviceId)){
			throw new FraudEngineException("Please enter value for serviceId and ruleId");
		}
		Optional<ServiceRule> productRuleOptional = serviceRuleRepository.findById(new ProductRuleId(ruleId, serviceId));
		productRuleRedisRepository.setHashOperations(redisTemplate);
		if(productRuleOptional.isPresent()){
			ServiceRule serviceRule = productRuleOptional.get();
			// for auditing purpose for DELETE
			serviceRule.setEntityId("ruleId: " + ruleId + " serviceId: " + serviceId);
			serviceRule.setRecordBefore(JsonConverter.objectToJson(serviceRule));
			serviceRule.setRecordAfter(null);
			serviceRule.setRequestDump("ruleId:" + ruleId + " serviceId:" + serviceId);

			try {
				serviceRuleRepository.deleteByRuleIdAndServiceId(ruleId, serviceId);
			} catch (Exception ex) {
				log.error("Error occurred while deleting service rule entity from the database", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
			}
			try {
				String redisId = serviceId + ":"+ruleId;
				productRuleRedisRepository.delete(redisId);
			} catch (Exception ex) {
				log.error("Error occurred while deleting service rule entity from Redis", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
			}
		}else {
			throw new ResourceNotFoundException("ServiceRule Not found for ruleId " + ruleId + " and serviceId " + serviceId);
		}
		return true;
	}


	@Transactional(rollbackFor = Throwable.class)
	public List<RuleProductResponse> getRuleService(String serviceId) {
		List<RuleProductResponse> ruleProductResponseList = new ArrayList<>();
		TypedQuery<RuleProductResponse> ruleProductResponseTypedQuery;
		if (StringUtils.isNotBlank(serviceId)) {
			String sqlString = " SELECT NEW com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse(" +
					"rl.id,rl.name,rl.sourceValueOne,rl.valueOneDataType, rl.operatorOne, rl.compareValueOne, rl.dataSourceValOne," +
					"rl.logicOperator,rl.sourceValueTwo,rl.operatorTwo,rl.compareValueTwo, rl.dataSourceValTwo, rl.valueTwoDataType," +
					"rl.suspicionLevel, rl.action, rl.status, rl.authorised,sr.serviceId as serviceId, sr.ruleId as serviceRuleId)"
					+ " FROM Rule rl inner JOIN rl.serviceRule sr "
					+ " WHERE sr.serviceId = :serviceId";
			ruleProductResponseTypedQuery =  em.createQuery(sqlString.trim(), RuleProductResponse.class)
					                     .setParameter("serviceId",serviceId);
			ruleProductResponseList = ruleProductResponseTypedQuery.getResultList();
		}
		return ruleProductResponseList;
	}

	private ServiceRule saveRuleServiceEntityToDatabase(ServiceRule serviceRuleEntity, String createdBy) {
		ServiceRule persistedServiceRule;
		try {
			persistedServiceRule = serviceRuleRepository.save(serviceRuleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving product rule entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addAccountProductEntityToRedis(persistedServiceRule);
		// create & user notification
		appUtil.createUserNotification(AppConstant.SERVICE_RULE, persistedServiceRule.getServiceId() + "_" + persistedServiceRule.getRuleId(), createdBy);
		return persistedServiceRule;
	}
	
	private void addAccountProductEntityToRedis(ServiceRule alreadyPersistedServiceRule) {
		try {
			productRuleRedisRepository.setHashOperations(redisTemplate);
			productRuleRedisRepository.update(alreadyPersistedServiceRule);
		} catch(Exception ex){
			log.error("Error occurred while saving product rule to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
	private List<RuleResponse> outputRuleResponseList(List<Rule> ruleList){
		List<RuleResponse> ruleResponseList = new ArrayList<>();
		ruleList.forEach(ruleVal -> {
			RuleResponse ruleResponse = new RuleResponse();
			BeanUtils.copyProperties(ruleVal,ruleResponse,"productRule");
			ruleResponseList.add(ruleResponse);
		});
		return ruleResponseList;
	}

	private List<ProductRuleResponse> outputProductRuleResponseList(List<ServiceRule> serviceRuleList){
		List<ProductRuleResponse> productRules = new ArrayList<>();
		serviceRuleList.forEach(productRule -> {
			ProductRuleResponse ruleResponse = new ProductRuleResponse();
			BeanUtils.copyProperties(productRule,ruleResponse,"productEntity", "emailGroup", "rule");
			productRules.add(ruleResponse);
		});
		return productRules;
	}


}
