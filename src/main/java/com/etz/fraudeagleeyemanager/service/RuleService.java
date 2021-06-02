package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
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
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.*;

import java.util.*;

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

	@PersistenceContext
	private EntityManager em;


	@Autowired
	ProductRuleRepository productRuleRepository;

	@Autowired
	private ProductEntityRepository productEntityRepository;

	@Autowired
	EmailGroupRepository emailGroupRepository;

	
	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
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

		Rule updatedEntity = ruleRepository.save(ruleEntity);

		//update redis
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.update(updatedEntity);
		
		return outputUpdatedRuleResponse(updatedEntity);
	}



	private UpdatedRuleResponse outputUpdatedRuleResponse(Rule rule){

		UpdatedRuleResponse updatedRuleResponse = new UpdatedRuleResponse();
		BeanUtils.copyProperties(rule,updatedRuleResponse, "address","createdAt","createdBy");
		return updatedRuleResponse;
	}

	public boolean deleteRule(Long parameterId) {
		Optional<ProductRule> prodRuleEntity = productRuleRepository.findByRuleId(parameterId);
		if (prodRuleEntity.isPresent()){
			throw new FraudEngineException("Please unmap the ruleId before deleting " + parameterId);
		}
		ruleRepository.deleteById(parameterId);
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.delete(parameterId);
		
		// child records have to be deleted in Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		Set<String> keys = productRuleRedisRepository.scanKeys("*:" + parameterId);
		keys.forEach(key -> productRuleRedisRepository.delete(key));
		
		return true;
	}



	public Page<RuleResponse> getRule(Long ruleId) {
		List<RuleResponse> ruleResponseList;
		if (ruleId == null) {
			ruleResponseList = outputRuleResponseList(ruleRepository.findAll());
			return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
		}
		Rule ruleEntity = ruleRepository.findById(ruleId).orElseThrow(() -> new ResourceNotFoundException("Rule Not found " + ruleId));
		ruleEntity.setId(ruleId);
		ruleResponseList = outputRuleResponseList(ruleRepository.findAll(Example.of(ruleEntity)));

		return AppUtil.listConvertToPage(ruleResponseList, PageRequestUtil.getPageRequest());
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
		ProductRule createdProductRuleEntity = productRuleRepository.save(prodRuleEntity);

		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.create(createdProductRuleEntity);
		
		return createdProductRuleEntity;
	}

	public ProductRuleResponse updateProductRule(UpdateMapRuleToProductRequest request) {
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
		ProductRule updatedEntity = productRuleRepository.save(prodRuleEntity);

		//update Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.update(prodRuleEntity);
		return outputProductRuleResponseList(updatedEntity);
	}

	public boolean deleteProductRule(Long productRuleId) {
		ProductRule prodRuleEntity = productRuleRepository.findByRuleId(productRuleId)
				.orElseThrow(() -> new ResourceNotFoundException("ProductRule Not found for productRuleId " + productRuleId ));
		String redisId = prodRuleEntity.getProductCode()+ ":"+prodRuleEntity.getRuleId();
		productRuleRepository.deleteByRuleId(productRuleId);
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.delete(redisId);
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

	private List<RuleResponse> outputRuleResponseList(List<Rule> ruleList){
		List<RuleResponse> ruleResponseList = new ArrayList<>();
		ruleList.forEach(ruleVal -> {
			RuleResponse ruleResponse = new RuleResponse();
			BeanUtils.copyProperties(ruleVal,ruleResponse,"productRule");
			ruleResponseList.add(ruleResponse);
		});
		return ruleResponseList;
	}

	private ProductRuleResponse outputProductRuleResponseList(ProductRule productRule){

			ProductRuleResponse ruleResponse = new ProductRuleResponse();
			BeanUtils.copyProperties(productRule,ruleResponse,"productEntity", "emailGroup", "rule");
			return ruleResponse;
	}


}
