package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.etz.fraudeagleeyemanager.entity.*;
import com.etz.fraudeagleeyemanager.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.UpdatedRuleResponse;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRuleRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.RuleRedisRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ServiceRuleRepository serviceRuleRepository;
	private final EmailGroupRepository emailGroupRepository;
	private final RuleRepository ruleRepository;
	private final RuleRedisRepository ruleRedisRepository;
	private final ProductRuleRedisRepository productRuleRedisRepository;
	private final ProductServiceRepository productServiceRepository;
	private final ParameterRepository parameterRepository;

	@PersistenceContext
	private final EntityManager em;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
		if (Boolean.FALSE.equals(parameterRepository.existsByNameAndOperator(request.getFirstSourceVal(), request.getFirstOperator()))){
			throw new FraudEngineException("The source value one and operator one doesn't exists in Parameter table ");
		}

		if (!(Objects.isNull(request.getSecondSourceVal()) && Objects.isNull(request.getSecondOperator())) && Boolean.FALSE.equals(parameterRepository.existsByNameAndOperator(request.getSecondSourceVal(), request.getSecondOperator()))) {
			throw new FraudEngineException("The source value two and operator two doesn't exists in Parameter table ");
		}
		//try {
			ruleEntity.setName(request.getRuleName());
			ruleEntity.setValueOneDataType(AppUtil.checkDataType(request.getFirstDataType()));
			if(AppUtil.checkDataType(request.getFirstDataType()).equalsIgnoreCase("TIME")) {
				ruleEntity.setSourceValueOne(AppUtil.checkTimeSourceValue(request.getFirstDataType(),request.getFirstSourceVal()));
			}
			ruleEntity.setSourceValueOne(request.getFirstSourceVal());

			// check the operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstDataType(),request.getFirstOperator()));

			if(AppUtil.isCompareValueValid(request.getFirstDataType(),request.getFirstCompareVal())){
				ruleEntity.setCompareValueOne(request.getFirstCompareVal());
			}else {
				throw new FraudEngineException("Invalid  first compare value ");
			}
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSourceVal()));
			// check logical operator
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));

			if(AppUtil.checkDataType(request.getSecondDataType()).equalsIgnoreCase("TIME")) {
				ruleEntity.setSourceValueTwo(AppUtil.checkTimeSourceValue(request.getSecondDataType(),request.getSecondSourceVal()));
			}
			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());

			ruleEntity.setValueTwoDataType(AppUtil.checkDataType(request.getSecondDataType()));
			// check operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondDataType(),request.getSecondOperator()));
			if(AppUtil.isCompareValueValid(request.getSecondDataType(),request.getSecondCompareVal())){
				ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
			}else{
				throw new FraudEngineException("Please check the compare value and datatype");
			}
			ruleEntity.setDataSourceValTwo(AppUtil.checkDataSource(request.getSecondDataSourceVal()));
			ruleEntity.setSuspicionLevel(request.getSuspicion());
			ruleEntity.setAction(request.getAction());
			ruleEntity.setAuthorised(request.getAuthorised());
			ruleEntity.setStatus(Boolean.TRUE);
			ruleEntity.setCreatedBy(request.getCreatedBy());
			// for auditing purpose for CREATE
			ruleEntity.setEntityId(null);
			ruleEntity.setRecordBefore(null);
			ruleEntity.setRequestDump(request);
	//	} catch (Exception ex) {
	//		log.error("Error occurred while creating Rule entity object", ex);
	//		throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
	//	}
		return saveRuleEntityToDatabase(ruleEntity);
		
		// save rule
		// notify Super admin for approval
			// on rejection, notify admin
			// on approval, proceed
		// If rule is statistical based, refresh corresponding redis server; pocket-moni
		// refresh fraud engine redis server;
	}

	public Rule updateRule(UpdateRuleRequest request) {

		Optional<Rule> ruleEntityOptional = ruleRepository.findById(request.getRuleId());
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId());
		}
		Rule ruleEntity = ruleEntityOptional.get();
		try {
			// for auditing purpose for UPDATE
			ruleEntity.setEntityId(String.valueOf(request.getRuleId()));
			ruleEntity.setRecordBefore(JsonConverter.objectToJson(ruleEntity));
			ruleEntity.setRequestDump(request);

			ruleEntity.setSourceValueOne(request.getFirstSourceVal());
			ruleEntity.setValueOneDataType(AppUtil.checkDataType(request.getFirstDataType()));
			// check for the list of Operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstDataType(), request.getFirstOperator()));
			if (AppUtil.isCompareValueValid(request.getFirstDataType(), request.getFirstCompareVal())) {
				ruleEntity.setCompareValueOne(request.getFirstCompareVal());
			}
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSource()));
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));
			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
			ruleEntity.setValueTwoDataType(AppUtil.checkDataType(request.getSecondDataType()));
			// check for the list of operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondDataType(),request.getSecondOperator()));
			if (AppUtil.isCompareValueValid(request.getFirstDataType(), request.getFirstCompareVal())) {
				ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
			}
			ruleEntity.setDataSourceValTwo(AppUtil.checkDataSource(request.getSecondDataSource()));
			ruleEntity.setSuspicionLevel(request.getSuspicion());
			ruleEntity.setAction(request.getAction());
			ruleEntity.setAuthorised(request.getAuthorised());
			ruleEntity.setStatus(request.getStatus());
			ruleEntity.setUpdatedBy(request.getUpdatedBy());
		} catch (Exception ex) {
			log.error("Error occurred while creating Rule entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputUpdatedRuleResponse(saveRuleEntityToDatabase(ruleEntity));
	}

	private Rule saveRuleEntityToDatabase(Rule ruleEntity) {
		Rule persistedRuleEntity;
		try {
			persistedRuleEntity = ruleRepository.save(ruleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Rule entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveRuleEntityToRedis(persistedRuleEntity);
		return persistedRuleEntity;
	}
	
	private void saveRuleEntityToRedis(Rule alreadyPersistedRuleEntity) {
		try {
			ruleRedisRepository.setHashOperations(redisTemplate);
			//ruleRedisRepository.update(alreadyPersistedRuleEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving Rule entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
	private UpdatedRuleResponse outputUpdatedRuleResponse(Rule rule){

		UpdatedRuleResponse updatedRuleResponse = new UpdatedRuleResponse();
		BeanUtils.copyProperties(rule,updatedRuleResponse, "address","createdAt","createdBy");
		return updatedRuleResponse;
	}

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
			log.error("Error occurred while deleting Rule entity from the database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			ruleRedisRepository.setHashOperations(redisTemplate);
			ruleRedisRepository.delete(ruleId);
		} catch (Exception ex) {
			log.error("Error occurred while deleting Rule entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		
		// child records have to be deleted in Redis
		productRuleRedisRepository.setHashOperations(redisTemplate);
		Set<String> keys = productRuleRedisRepository.scanKeys("*:" + ruleId);
		keys.forEach(productRuleRedisRepository::delete);
		
		return Boolean.TRUE;
	}

	public Page<RuleResponse> getRule(Long ruleId) {
		List<RuleResponse> ruleResponseList;
		if (Objects.isNull(ruleId)) {
			ruleResponseList = outputRuleResponseList(ruleRepository.findAll());
			return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
		}
		Optional<Rule> ruleEntityOptional = ruleRepository.findById(ruleId);
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + ruleId);
		}
		ruleResponseList = outputRuleResponseList(ruleRepository.findAll(Example.of(ruleEntityOptional.get())));
		return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
	}

	public ServiceRule mapRuleToService(MapRuleToServiceRequest request) {
		Optional<ProductServiceEntity> productServiceEntityOptional = productServiceRepository.findById(request.getServiceId());
		if (!productServiceEntityOptional.isPresent()){
			throw new ResourceNotFoundException("Product Service  not found for serviceId " + request.getServiceId());
		}
		Optional<Rule> ruleEntityOptional = ruleRepository.findById(request.getRuleId());
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId());
		}
		
		if (!Objects.isNull(request.getEmailGroupId()) && !emailGroupRepository.findById(request.getEmailGroupId()).isPresent()) {
			throw new ResourceNotFoundException("Email Group Id Not found for Id " + request.getEmailGroupId());
		}

		ServiceRule serviceRuleEntity = new ServiceRule();
		try {
			serviceRuleEntity.setServiceId(request.getServiceId());
			serviceRuleEntity.setRuleId(request.getRuleId());
			serviceRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
			serviceRuleEntity.setEmailGroupId(request.getEmailGroupId());
			serviceRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
			serviceRuleEntity.setStatus(Boolean.TRUE);
			serviceRuleEntity.setAuthorised(request.getAuthorised());
			serviceRuleEntity.setCreatedBy(request.getCreatedBy());
		
		// for auditing purpose for CREATE
			serviceRuleEntity.setEntityId(null);
			serviceRuleEntity.setRecordBefore(null);
			serviceRuleEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating Product Rule entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveRuleServiceEntityToDatabase(serviceRuleEntity);
	}

	public List<ProductRuleResponse> updateProductRule(UpdateMapRuleToServiceRequest request) {
		List<ServiceRule> prodRuleEntityList = serviceRuleRepository.findByRuleId(request.getProductRuleId());
		if(prodRuleEntityList.isEmpty()){
			throw new FraudEngineException("ServiceRule Not found for Id " + request.getProductRuleId());
		}
		
		if (!Objects.isNull(request.getEmailGroupId()) && !emailGroupRepository.findById(request.getEmailGroupId()).isPresent()) {
			throw new ResourceNotFoundException("Email Group Id Not found for Id " + request.getEmailGroupId());
		}

		List<ServiceRule> updatedServiceRuleEntity = new ArrayList<>();
		for (ServiceRule prodRuleEntity:prodRuleEntityList) {
			try {
				// for auditing purpose for UPDATE
				prodRuleEntity.setEntityId("ruleId: " + prodRuleEntity.getRuleId() + " serviceId: " + prodRuleEntity.getServiceId());
				prodRuleEntity.setRecordBefore(JsonConverter.objectToJson(prodRuleEntity));
				prodRuleEntity.setRequestDump(request);

				prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
				prodRuleEntity.setEmailGroupId(request.getEmailGroupId());
				prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
				prodRuleEntity.setStatus(request.getStatus());
				prodRuleEntity.setAuthorised(request.getAuthorised());
				prodRuleEntity.setUpdatedBy(request.getUpdatedBy());
			} catch (Exception ex) {
				log.error("Error occurred while creating Product Rule entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			updatedServiceRuleEntity.add(saveRuleServiceEntityToDatabase(prodRuleEntity));
		}
		return outputProductRuleResponseList(updatedServiceRuleEntity);
	}

	public boolean deleteProductRule(Long ruleId, Long serviceId) {
		if (Objects.isNull(ruleId) && Objects.isNull(serviceId)){
			throw new FraudEngineException("Please enter value for code and ruleId");
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
				// serviceRuleRepository.deleteByRuleId(ruleId, code);
				serviceRuleRepository.delete(serviceRule);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product rule entity from the database", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
			}
			try {
				String redisId = serviceId + ":"+ruleId;
				productRuleRedisRepository.delete(redisId);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product rule entity from Redis", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
			}
		}else {
			throw new ResourceNotFoundException("ServiceRule Not found for ruleId " + ruleId + " and serviceId " + serviceId);
		}
		return true;
	}


	public List<RuleProductResponse> getRuleService(Long serviceId) {
		List<RuleProductResponse> ruleProductResponseList = new ArrayList<>();
		TypedQuery<RuleProductResponse> ruleProductResponseTypedQuery;
		if (serviceId != null) {
			String sqlString = " SELECT NEW com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse(" +
					"rl.id,rl.name,rl.sourceValueOne,rl.valueOneDataType, rl.operatorOne, rl.compareValueOne, rl.dataSourceValOne," +
					"rl.logicOperator,rl.sourceValueTwo,rl.operatorTwo,rl.compareValueTwo, rl.dataSourceValTwo, rl.valueTwoDataType," +
					"rl.suspicionLevel, rl.action, rl.status, rl.authorised,sr.serviceId as serviceId, sr.ruleId as productRuleId)"
					+ " FROM Rule rl inner JOIN rl.serviceRule sr "
					+ " WHERE sr.serviceId = :serviceId";
			ruleProductResponseTypedQuery =  em.createQuery(sqlString.trim(), RuleProductResponse.class)
					                     .setParameter("serviceId",serviceId);
			ruleProductResponseList = ruleProductResponseTypedQuery.getResultList();
		}
		return ruleProductResponseList;
	}

	private ServiceRule saveRuleServiceEntityToDatabase(ServiceRule serviceRuleEntity) {
		ServiceRule persistedServiceRule;
		try {
			persistedServiceRule = serviceRuleRepository.save(serviceRuleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving product rule entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveAccountProductEntityToRedis(persistedServiceRule);
		return persistedServiceRule;
	}
	
	private void saveAccountProductEntityToRedis(ServiceRule alreadyPersistedServiceRule) {
		try {
			productRuleRedisRepository.setHashOperations(redisTemplate);
			productRuleRedisRepository.update(alreadyPersistedServiceRule);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
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
