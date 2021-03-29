package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudengine.dto.request.CreateRuleRequest;
import com.etz.fraudengine.dto.request.RuleToProductRequest;
import com.etz.fraudengine.dto.response.BooleanResponse;
import com.etz.fraudengine.dto.response.ModelResponse;
import com.etz.fraudengine.dto.response.PageResponse;
import com.etz.fraudengine.entity.ProductRuleEntity;
import com.etz.fraudengine.entity.RuleEntity;
import com.etz.fraudengine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/rule")
public class RuleController {

	@Autowired
	RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<RuleEntity>> createRule(@RequestBody @Valid CreateRuleRequest request){
		ModelResponse<RuleEntity> response = new ModelResponse<RuleEntity>(ruleService.createRule(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{ruleID}")
	public ModelResponse<RuleEntity> updateRule(@PathVariable(name = "ruleID") Integer ruleId,
			@RequestBody CreateRuleRequest request){				   
		request.setRuleId(ruleId);
		return new ModelResponse<RuleEntity>(ruleService.updateRule(request));
	}
	
	@DeleteMapping(path = "/{ruleID}")
	public BooleanResponse deleteRule(@PathVariable(name = "ruleID") Long ruleId){
		return new BooleanResponse(ruleService.deleteRule(ruleId));
	}
	
	@GetMapping
	public PageResponse<RuleEntity> queryRule(@RequestParam(name = "ruleID", defaultValue = "-1") String ruleId){
		return new PageResponse<>(ruleService.getRule(Long.parseLong(ruleId.trim())));
	}
		
	
	@PostMapping(path = "/product")
	public ResponseEntity<ProductRuleEntity> mapRuleToProduct(@RequestBody @Valid RuleToProductRequest request){
		return new ResponseEntity<>(ruleService.mapRuleToProduct(request), HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/product/{productRuleID}")
	public ModelResponse<ProductRuleEntity> updateProductRule(@PathVariable(name = "productRuleID") Integer productRuleId,
			@RequestBody RuleToProductRequest request){
		return new ModelResponse<ProductRuleEntity>(ruleService.updateProductRule(request));
	}
	
	@DeleteMapping(path = "/product/{productRuleID}")
	public BooleanResponse deleteProductRule(@PathVariable(name = "productRuleID") Long productRuleId){
		return new BooleanResponse(ruleService.deleteProductRule(productRuleId));
	}
	
	
//	@GetMapping(path = "/product")
//	public ResponseEntity<CollectionResponse<ProductRuleEntity>> queryProductRule(
//			@RequestParam(name = "productRuleID", defaultValue = "-1") Long productRuleId){
//
//		CollectionResponse<ProductRuleEntity> response = 
//				new CollectionResponse<>(ruleService.getProductRule(productRuleId));
//		
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	

}
