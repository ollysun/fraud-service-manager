package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ProductRuleRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.RuleRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ProductRuleRepository;
import com.etz.fraudeagleeyemanager.repository.RuleRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Set;

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

	
	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
		ruleEntity.setSourceValueOne(request.getFirstSourceVal());
		checkOperator(request.getFirstOperator());
		ruleEntity.setOperatorOne(request.getFirstOperator());
		ruleEntity.setCompareValueOne(request.getFirstCompareVal());
		ruleEntity.setDataSourceOne(request.getFirstDataSource());
		ruleEntity.setLogicOperator(request.getLogicOperator());
		ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
		checkOperator(request.getSecondOperator());
		ruleEntity.setOperatorTwo(request.getSecondOperator());
		ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
		ruleEntity.setDataSourceTwo(request.getSecondDataSource());
		ruleEntity.setSuspicionLevel(request.getSuspicion());
		ruleEntity.setAction(request.getAction());
		ruleEntity.setAuthorised(request.getAuthorised());
		ruleEntity.setStatus(Boolean.TRUE);
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

	private void checkOperator(String operatorRequest){
		String[] operators = new String[] { "<", ">","==", "<=", ">=" };
		if (operatorRequest != null) {
			for (String operator : operators) {
				if (!operator.equalsIgnoreCase(operatorRequest)) {
					throw new FraudEngineException("Please check the Operator");
				}
			}
		}
	}


	public Rule updateRule(UpdateRuleRequest request) {
		Rule ruleEntity = ruleRepository.findById(request.getRuleId())
									.orElseThrow(() -> new ResourceNotFoundException("Rule Not found for Id " + request.getRuleId() ));
		ruleEntity.setSourceValueOne(request.getFirstSourceVal());
		// check for the list of Operator
		checkOperator(request.getFirstOperator());
		ruleEntity.setOperatorOne(request.getFirstOperator());
		ruleEntity.setCompareValueOne(request.getFirstCompareVal());
		ruleEntity.setDataSourceOne(request.getFirstDataSource());
		ruleEntity.setLogicOperator(request.getLogicOperator());
		ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
		// check for the list of operator
		checkOperator(request.getSecondOperator());
		ruleEntity.setOperatorTwo(request.getSecondOperator());
		ruleEntity.setCompareValueTwo(request.getSecondCompareVal());
		ruleEntity.setDataSourceTwo(request.getSecondDataSource());
		ruleEntity.setSuspicionLevel(request.getSuspicion());
		ruleEntity.setAction(request.getAction());
		ruleEntity.setAuthorised(request.getAuthorised());
		ruleEntity.setStatus(request.getStatus());
		ruleEntity.setUpdatedBy(request.getUpdatedBy());

		Rule updatedEntity = ruleRepository.save(ruleEntity);
		ruleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		ruleRedisRepository.update(updatedEntity);
		
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
		prodRuleEntity.setProductCode(request.getProductCode());
		prodRuleEntity.setRuleId(request.getRuleId().longValue());
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
		prodRuleEntity.setEmailGroupId(request.getEmailGroupId().longValue());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
		prodRuleEntity.setStatus(Boolean.TRUE);
		prodRuleEntity.setAuthorised(request.getAuthorised());
		prodRuleEntity.setCreatedBy(request.getCreatedBy());

		ProductRule createdEntity = productRuleRepository.save(prodRuleEntity);
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.create(createdEntity);
		
		return createdEntity;
	}

	public ProductRule updateProductRule(UpdateMapRuleToProductRequest request) {
		ProductRule prodRuleEntity = productRuleRepository.findById(request.getProductRuleId())
				.orElseThrow(() -> new ResourceNotFoundException("ProductRule Not found for Id " + request.getProductRuleId() ));
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin());
		prodRuleEntity.setEmailGroupId(request.getEmailGroupId().longValue());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer());
		prodRuleEntity.setStatus(request.getStatus());
		prodRuleEntity.setAuthorised(request.getAuthorised());
		prodRuleEntity.setUpdatedBy(request.getUpdatedBy());

		ProductRule updatedEntity = productRuleRepository.save(prodRuleEntity);
		productRuleRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		productRuleRedisRepository.update(updatedEntity);
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
