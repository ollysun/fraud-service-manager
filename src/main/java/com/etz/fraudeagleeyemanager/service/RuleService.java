package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.UpdatedRuleResponse;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.ProductRuleId;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRuleRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.RuleRedisRepository;
import com.etz.fraudeagleeyemanager.repository.EmailGroupRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.repository.ProductRuleRepository;
import com.etz.fraudeagleeyemanager.repository.RuleRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ProductRuleRepository productRuleRepository;
	private final ProductEntityRepository productEntityRepository;
	private final EmailGroupRepository emailGroupRepository;
	private final RuleRepository ruleRepository;
	private final RuleRedisRepository ruleRedisRepository;
	private final ProductRuleRedisRepository productRuleRedisRepository;

	@PersistenceContext
	private final EntityManager em;

	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
		try {
			ruleEntity.setName(request.getRuleName());
			ruleEntity.setSourceValueOne(request.getFirstSourceVal());
			// check the operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstOperator().toLowerCase()));
			ruleEntity.setCompareValueOne(String.valueOf(request.getFirstCompareVal()));
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSourceVal()));
			// check logical operator
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));
			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
			// check operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondOperator().toLowerCase()));
			ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
			ruleEntity.setDataSourceValTwo(request.getSecondDataSourceVal());
			ruleEntity.setSuspicionLevel(request.getSuspicion());
			ruleEntity.setAction(request.getAction());
			ruleEntity.setAuthorised(request.getAuthorised());
			ruleEntity.setStatus(Boolean.TRUE);
			ruleEntity.setCreatedBy(request.getCreatedBy());

			// for auditing purpose for CREATE
			ruleEntity.setEntityId(null);
			ruleEntity.setRecordBefore(null);
			ruleEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating Rule entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
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
			// check for the list of Operator
			ruleEntity.setOperatorOne(AppUtil.checkOperator(request.getFirstOperator()));
			ruleEntity.setCompareValueOne(request.getFirstCompareVal());
			ruleEntity.setDataSourceValOne(AppUtil.checkDataSource(request.getFirstDataSource()));
			ruleEntity.setLogicOperator(AppUtil.checkLogicOperator(request.getLogicOperator()));
			ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
			// check for the list of operator
			ruleEntity.setOperatorTwo(AppUtil.checkOperator(request.getSecondOperator()));
			ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
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
			ruleRedisRepository.update(alreadyPersistedRuleEntity);
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
		List<ProductRule> prodRuleEntity = productRuleRepository.findByRuleId(ruleId);
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

	public ProductRule mapRuleToProduct(MapRuleToProductRequest request) {
		Optional<ProductEntity> productEntityOptional = productEntityRepository.findByCode(request.getProductCode());
		if (!productEntityOptional.isPresent()){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + request.getProductCode());
		}
		Optional<Rule> ruleEntityOptional = ruleRepository.findById(request.getRuleId());
		if (!ruleEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId());
		}
		
		if (!Objects.isNull(request.getEmailGroupId()) && !emailGroupRepository.findById(request.getEmailGroupId()).isPresent()) {
			throw new ResourceNotFoundException("Email Group Id Not found for Id " + request.getEmailGroupId());
		}

		ProductRule prodRuleEntity = new ProductRule();
		try {
		prodRuleEntity.setProductCode(request.getProductCode());
		prodRuleEntity.setRuleId(request.getRuleId());
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
		prodRuleEntity.setEmailGroupId(request.getEmailGroupId());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
		prodRuleEntity.setStatus(Boolean.TRUE);
		prodRuleEntity.setAuthorised(request.getAuthorised());
		prodRuleEntity.setCreatedBy(request.getCreatedBy());
		
		// for auditing purpose for CREATE
		prodRuleEntity.setEntityId(null);
		prodRuleEntity.setRecordBefore(null);
		prodRuleEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating Product Rule entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveRuleProductEntityToDatabase(prodRuleEntity);
	}

	public List<ProductRuleResponse> updateProductRule(UpdateMapRuleToProductRequest request) {
		List<ProductRule> prodRuleEntityList = productRuleRepository.findByRuleId(request.getProductRuleId());
		if(prodRuleEntityList.isEmpty()){
			throw new FraudEngineException("ProductRule Not found for Id " + request.getProductRuleId());
		}
		
		if (!Objects.isNull(request.getEmailGroupId()) && !emailGroupRepository.findById(request.getEmailGroupId()).isPresent()) {
			throw new ResourceNotFoundException("Email Group Id Not found for Id " + request.getEmailGroupId());
		}

		List<ProductRule> updatedProductRuleEntity = new ArrayList<>();
		for (ProductRule prodRuleEntity:prodRuleEntityList) {
			try {
				// for auditing purpose for UPDATE
				prodRuleEntity.setEntityId("ruleId: " + prodRuleEntity.getRuleId() + " prodCode: " + prodRuleEntity.getProductCode());
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
			updatedProductRuleEntity.add(saveRuleProductEntityToDatabase(prodRuleEntity));
		}
		return outputProductRuleResponseList(updatedProductRuleEntity);
	}

	public boolean deleteProductRule(Long ruleId, String code) {
		if (Objects.isNull(ruleId) && Objects.isNull(code)){
			throw new FraudEngineException("Please enter value for code and ruleId");
		}
		Optional<ProductRule> productRuleOptional = productRuleRepository.findById(new ProductRuleId(ruleId, code));
		productRuleRedisRepository.setHashOperations(redisTemplate);
		if(productRuleOptional.isPresent()){
			ProductRule productRule = productRuleOptional.get();
			// for auditing purpose for DELETE
			productRule.setEntityId("ruleId: " + ruleId + " prodCode: " + code);
			productRule.setRecordBefore(JsonConverter.objectToJson(productRule));
			productRule.setRecordAfter(null);
			productRule.setRequestDump("ruleId:" + ruleId + " prodCd:" + code);

			try {
				// productRuleRepository.deleteByRuleId(ruleId, code);
				productRuleRepository.delete(productRule);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product rule entity from the database", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
			}
			try {
				String redisId = code + ":"+ruleId;
				productRuleRedisRepository.delete(redisId);
			} catch (Exception ex) {
				log.error("Error occurred while deleting product rule entity from Redis", ex);
				throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
			}
		}else {
			throw new ResourceNotFoundException("ProductRule Not found for ruleId " + ruleId + " and code " + code);
		}
		return true;
	}


	public List<RuleProductResponse> getRuleProduct(String code) {
		List<RuleProductResponse> ruleProductResponseList = new ArrayList<>();
		TypedQuery<RuleProductResponse> ruleProductResponseTypedQuery;
		if (code != null) {
			String sqlString = " SELECT NEW com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse(" +
					"rl.id,rl.name,rl.sourceValueOne, rl.operatorOne, rl.compareValueOne, rl.dataSourceValOne," +
					"rl.logicOperator,rl.sourceValueTwo,rl.operatorTwo,rl.compareValueTwo, rl.dataSourceValTwo," +
					"rl.suspicionLevel, rl.action, rl.status, rl.authorised,pr.productCode, pr.ruleId as productRuleId)"
					+ " FROM Rule rl inner JOIN rl.productRule pr "
					+ " WHERE pr.productCode = :code";
			ruleProductResponseTypedQuery =  em.createQuery(sqlString.trim(), RuleProductResponse.class)
					                     .setParameter("code",code);
			ruleProductResponseList = ruleProductResponseTypedQuery.getResultList();
		}
		return ruleProductResponseList;
	}

	private ProductRule saveRuleProductEntityToDatabase(ProductRule productRuleEntity) {
		ProductRule persistedProductRule;
		try {
			persistedProductRule = productRuleRepository.save(productRuleEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving product rule entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveAccountProductEntityToRedis(persistedProductRule);
		return persistedProductRule;
	}
	
	private void saveAccountProductEntityToRedis(ProductRule alreadyPersistedProductRule) {
		try {
			productRuleRedisRepository.setHashOperations(redisTemplate);
			productRuleRedisRepository.update(alreadyPersistedProductRule);
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

	private List<ProductRuleResponse> outputProductRuleResponseList(List<ProductRule> productRuleList){
		List<ProductRuleResponse> productRules = new ArrayList<>();
		productRuleList.forEach( productRule -> {
			ProductRuleResponse ruleResponse = new ProductRuleResponse();
			BeanUtils.copyProperties(productRule,ruleResponse,"productEntity", "emailGroup", "rule");
			productRules.add(ruleResponse);
		});
		return productRules;
	}


}
