package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
		ruleEntity.setCompareValueOne(request.getFirstCompareVal());
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

//	private String checkLogicOperator(String text){
//		List<String> dataSourceVal = Arrays.asList("AND", "OR", "NOT");
//		String output = "";
//		if(!(text.isEmpty())){
//			output = dataSourceVal.stream()
//					.filter(bl -> bl.toUpperCase().equalsIgnoreCase(text))
//					.findFirst()
//					.orElseThrow(() ->
//							new FraudEngineException("cannot found this logic operator " + text + " can be any of " + dataSourceVal.toString()));
//		}
//		return output;
//	}
//
//	private String checkDataSource(String text) {
//		List<String> dataSourceVal = Arrays.asList("FRAUD ENGINE", "STATISTICS");
//		String output = "";
//		if(!(text.isEmpty())){
//			output = dataSourceVal.stream()
//					.filter(bl -> bl.toUpperCase().equalsIgnoreCase(text))
//					.findFirst()
//					.orElseThrow(() ->
//							new FraudEngineException("cannot found this data source " + text + " can be any " + dataSourceVal.toString()));
//		}
//		return output;
//	}
//
//	private String checkOperator(String operatorRequest){
//		List<String> operators = Arrays.asList("<", ">","==", "<=", "!=", ">=", "change");
//		String output = "";
//		if (operatorRequest != null) {
//			output = operators.stream()
//					.filter(bl -> bl.toUpperCase().equalsIgnoreCase(operatorRequest))
//					.findFirst()
//					.orElseThrow(() ->
//							new FraudEngineException("Not found this Operator " + operatorRequest +
//									"can be any of " + operators.toString()));
//		}
//		return output;
//	}


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
		Rule ruleRedis = ruleRedisRepository.findById(request.getRuleId());
		ruleRedis.setSourceValueOne(request.getFirstSourceVal());
		// check for the list of Operator
		ruleRedis.setOperatorOne(AppUtil.checkOperator(request.getFirstOperator()));
		ruleRedis.setCompareValueOne(request.getFirstCompareVal());
		ruleRedis.setDataSourceValOne(request.getFirstDataSource());
		ruleRedis.setLogicOperator(request.getLogicOperator());
		ruleRedis.setSourceValueTwo(request.getSecondSourceVal());
		// check for the list of operator
		//AppUtil.checkOperator(request.getSecondOperator());
		ruleRedis.setOperatorTwo(AppUtil.checkOperator(request.getSecondOperator()));
		ruleRedis.setCompareValueTwo(request.getSecondCompareVal());
		ruleRedis.setDataSourceValTwo(request.getSecondDataSource());
		ruleRedis.setSuspicionLevel(request.getSuspicion());
		ruleRedis.setAction(request.getAction());
		ruleRedis.setAuthorised(request.getAuthorised());
		ruleRedis.setStatus(request.getStatus());
		ruleRedis.setUpdatedBy(request.getUpdatedBy());

		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.update(ruleRedis);
		
		return updatedEntity;
	}

	public boolean deleteRule(Long parameterId) {
		ruleRepository.deleteById(parameterId);
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.delete(parameterId);
		
		// child records have to be deleted in Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		Set<String> keys = productRuleRedisRepository.scanKeys("*:" + parameterId);
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
		ProductRule updatedEntity = productRuleRepository.save(prodRuleEntity);

		//update Redis
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.update(prodRuleEntity);
		return updatedEntity;
	}

	public boolean deleteProductRule(Long productRuleId) {
		ProductRule prodRuleEntity = productRuleRepository.findById(productRuleId)
				.orElseThrow(() -> new ResourceNotFoundException("ProductRule Not found for Id " + productRuleId ));
		String redisId = prodRuleEntity.getProductCode()+ ":"+prodRuleEntity.getRuleId();
		productRuleRepository.deleteById(productRuleId);
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.delete(redisId);
		return true;
	}
}
