package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.RuleToProductRequest;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.repository.ProductRuleRepository;
import com.etz.fraudeagleeyemanager.repository.RuleRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {

	@Autowired
	RuleRepository ruleRepository;

	@Autowired
	ProductRuleRepository productRuleRepository;

	public Rule createRule(CreateRuleRequest request) {
		Rule ruleEntity = new Rule();
		ruleEntity.setSourceValueOne(request.getFirstSourceVal());
		ruleEntity.setOperatorOne(request.getFirstOperator());
		ruleEntity.setCompareValueOne(request.getFirstCompareVal().toString());
		ruleEntity.setDataSourceOne(request.getFirstDataSource());
		ruleEntity.setLogicOperator(request.getLogicOperator());
		ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
		ruleEntity.setOperatorTwo(request.getSecondOperator());
		ruleEntity.setCompareValueTwo(request.getSecondCompareVal().toString());
		ruleEntity.setDataSourceTwo(request.getSecondDataSource());
		ruleEntity.setSuspicionLevel(request.getSuspicion());
		ruleEntity.setAction(request.getAction());
		ruleEntity.setAuthorised(request.getAuthorised());
		ruleEntity.setCreatedBy(request.getCreatedBy());
		ruleEntity.setCreatedAt(LocalDateTime.now());

		return ruleRepository.save(ruleEntity);
	}

	public Rule updateRule(CreateRuleRequest request) {
		Rule ruleEntity = ruleRepository.findById(request.getRuleId().longValue()).get();
		ruleEntity.setSourceValueOne(request.getFirstSourceVal());
		ruleEntity.setOperatorOne(request.getFirstOperator());
		ruleEntity.setCompareValueOne(request.getFirstCompareVal().toString());
		ruleEntity.setDataSourceOne(request.getFirstDataSource());
		ruleEntity.setLogicOperator(request.getLogicOperator());
		ruleEntity.setSourceValueTwo(request.getSecondSourceVal());
		ruleEntity.setOperatorTwo(request.getSecondOperator());
		ruleEntity.setCompareValueTwo(request.getSecondCompareVal().toString());
		ruleEntity.setDataSourceTwo(request.getSecondDataSource());
		ruleEntity.setSuspicionLevel(request.getSuspicion());
		ruleEntity.setAction(request.getAction());
		ruleEntity.setAuthorised(request.getAuthorised() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		ruleEntity.setUpdatedBy(request.getUpdatedBy());
		ruleEntity.setUpdatedAt(LocalDateTime.now());

		return ruleRepository.save(ruleEntity);
	}

	public boolean deleteRule(Long parameterId) {
		ruleRepository.deleteById(parameterId);
		// child records have to be deleted
		return true;
	}

	public Page<Rule> getRule(Long ruleId) {
		if (ruleId < 0) {
			return ruleRepository.findAll(PageRequestUtil.getPageRequest());
		}

		Rule ruleEntity = new Rule();
		ruleEntity.setId(ruleId);
		return ruleRepository.findAll(Example.of(ruleEntity), PageRequestUtil.getPageRequest());
	}

	public ProductRule mapRuleToProduct(RuleToProductRequest request) {
		ProductRule prodRuleEntity = new ProductRule();
		prodRuleEntity.set(request.getProductCode());
		prodRuleEntity.setRuleId(request.getRuleId().longValue());
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setEmailGroup(request.getEmailGroup().longValue());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setStatus(request.getStatus() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setAuthorised(request.getAuthorised() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setCreatedBy(request.getCreatedBy());
		prodRuleEntity.setCreatedAt(LocalDateTime.now());

		return productRuleRepository.save(prodRuleEntity);
	}

	public ProductRuleEntity updateProductRule(RuleToProductRequest request) {
		ProductRuleEntity prodRuleEntity = getProductRule(request.getProductRuleId().longValue()).get(0);
		prodRuleEntity.setNotifyAdmin(request.getNotifyAdmin() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setEmailGroup(request.getEmailGroup().longValue());
		prodRuleEntity.setNotifyCustomer(request.getNotifyCustomer() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setStatus(request.getStatus() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setAuthorised(request.getAuthorised() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		prodRuleEntity.setUpdatedBy(request.getCreatedBy());
		prodRuleEntity.setUpdatedAt(LocalDateTime.now());

		return productRuleRepository.save(prodRuleEntity);
	}

	public boolean deleteProductRule(Long productRuleId) {
		productRuleRepository.deleteById(productRuleId);
		return true;
	}

	public List<ProductRuleEntity> getProductRule(Long productRuleId) {
		if (productRuleId < 0) {
			return productRuleRepository.findAll();
		}

		List<ProductRuleEntity> prodRuleEntityList = new ArrayList<>();
		prodRuleEntityList.add(productRuleRepository.findById(productRuleId).get());
		return prodRuleEntityList;
	}

}
