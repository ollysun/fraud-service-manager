package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RuleService {

	@Autowired @Qualifier("redisTemplate")
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;
	
	@Autowired
	RuleRedisRepository ruleRedisRepository;

	@Autowired
	ProductRuleRedisRepository productRuleRedisRepository;
		
	@Autowired
	RuleRepository ruleRepository;

	@Autowired
	ProductRuleRepository productRuleRepository;

	@Autowired
	private ProductEntityRepository productEntityRepository;

	@Autowired
	EmailGroupRepository emailGroupRepository;

	
	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
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
		
		Rule createdEntity = ruleRepository.save(ruleEntity);
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.create(createdEntity);
		
		// save rule
		// notify Super admin for approval
			// on rejection, notify admin
			// on approval, proceed
		// If rule is statistical based, refresh corresponding redis server; pocket-moni
		// refresh fraud engine redis server;
		
		return createdEntity;
	}




	public Rule updateRule(UpdateRuleRequest request) {
		Rule ruleEntity = ruleRepository.findById(request.getRuleId())
									.orElseThrow(() -> new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId() ));
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
		
		// for auditing purpose for UPDATE
		ruleEntity.setEntityId(request.getRuleId().toString());
		ruleEntity.setRecordBefore(JsonConverter.objectToJson(ruleEntity));
		ruleEntity.setRequestDump(request);

		Rule updatedEntity = ruleRepository.save(ruleEntity);

		//update redis
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.update(updatedEntity);
		
		return updatedEntity;
	}

	public boolean deleteRule(Long ruleId) {
		Rule ruleEntity = ruleRepository.findById(ruleId)
				.orElseThrow(() -> new ResourceNotFoundException("Rule Entity not found for Id " + ruleId));
		
		// for auditing purpose for DELETE
		ruleEntity.setEntityId(ruleId.toString());
		ruleEntity.setRecordBefore(JsonConverter.objectToJson(ruleId));
		ruleEntity.setRecordAfter(null);
		ruleEntity.setRequestDump(ruleId);
		
		//ruleRepository.deleteById(ruleId);
		ruleRepository.delete(ruleEntity);
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.delete(ruleId);
		
		// child records have to be deleted in Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		Set<String> keys = productRuleRedisRepository.scanKeys("*:" + ruleId);
		keys.forEach(key -> productRuleRedisRepository.delete(key));
		
		return true;
	}

	public Page<Rule> getRule(Long ruleId) {
		if (ruleId == null) {
			return ruleRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Rule ruleEntity = ruleRepository.findById(ruleId).orElseThrow(() -> new ResourceNotFoundException("Rule Not found " + ruleId));
		ruleEntity.setId(ruleId);

		return ruleRepository.findAll(Example.of(ruleEntity), PageRequestUtil.getPageRequest());
	}

	public ProductRule mapRuleToProduct(MapRuleToProductRequest request) {
		ProductRule prodRuleEntity = new ProductRule();

		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + request.getProductCode());
		}
		ruleRepository.findById(request.getRuleId()).orElseThrow(() ->
				new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId() ));

		if (request.getEmailGroupId() != null){
			emailGroupRepository.findById(request.getEmailGroupId()).orElseThrow(() ->
					new ResourceNotFoundException("Email Group Id Not found for  Id " + request.getEmailGroupId() ));
		}
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
		ProductRule createdProductRuleEntity = productRuleRepository.save(prodRuleEntity);

		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.create(createdProductRuleEntity);
		
		return createdProductRuleEntity;
	}

	public ProductRule updateProductRule(UpdateMapRuleToProductRequest request) {
		ProductRule prodRuleEntity = productRuleRepository.findById(request.getProductRuleId())
				.orElseThrow(() -> new ResourceNotFoundException("ProductRule Not found for Id " + request.getProductRuleId() ));

		if (request.getEmailGroupId() != null){
			emailGroupRepository.findById(request.getEmailGroupId()).orElseThrow(() ->
					new ResourceNotFoundException("Email Group Id Not found for  Id " + request.getEmailGroupId() ));
		}
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
		prodRuleEntity.setEmailGroupId(request.getEmailGroupId());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
		prodRuleEntity.setStatus(request.getStatus());
		prodRuleEntity.setAuthorised(request.getAuthorised());
		prodRuleEntity.setUpdatedBy(request.getUpdatedBy());
		
		// for auditing purpose for UPDATE
		prodRuleEntity.setEntityId(request.getProductRuleId().toString());
		prodRuleEntity.setRecordBefore(JsonConverter.objectToJson(prodRuleEntity));
		prodRuleEntity.setRequestDump(request);

		ProductRule updatedEntity = productRuleRepository.save(prodRuleEntity);

		//update Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.update(prodRuleEntity);
		return updatedEntity;
	}

	public boolean deleteProductRule(Long productRuleId) {
		ProductRule prodRuleEntity = productRuleRepository.findById(productRuleId)
				.orElseThrow(() -> new ResourceNotFoundException("ProductRule Not found for Id " + productRuleId ));

		// for auditing purpose for DELETE
		prodRuleEntity.setEntityId(productRuleId.toString());
		prodRuleEntity.setRecordBefore(JsonConverter.objectToJson(productRuleId));
		prodRuleEntity.setRecordAfter(null);
		prodRuleEntity.setRequestDump(productRuleId);
		
		String redisId = prodRuleEntity.getProductCode()+ ":"+prodRuleEntity.getRuleId();
		
		//productRuleRepository.deleteById(productRuleId);
		productRuleRepository.delete(prodRuleEntity);
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.delete(redisId);
		return true;
	}

	public List<RuleResponse> getRuleProduct(String code) {
		List<Rule> ruleList = new ArrayList<>();
		if (code != null) {
			ruleList =  ruleRepository.getRuleWithCode(code);
		}
		return outputRuleResponse(ruleList);
	}

	private List<RuleResponse> outputRuleResponse(List<Rule> ruleList){
		List<RuleResponse> ruleResponseList = new ArrayList<>();
		ruleList.forEach(ruleVal -> {
			RuleResponse ruleResponse = new RuleResponse();
			BeanUtils.copyProperties(ruleVal,ruleResponse,"productRule");
			ruleResponseList.add(ruleResponse);
		});
		return ruleResponseList;
	}


}
