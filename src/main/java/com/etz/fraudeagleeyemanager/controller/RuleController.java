package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/rule")
public class RuleController {

	@Autowired
	private RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> createRule(@RequestBody @Valid CreateRuleRequest request){
		ModelResponse<Rule> response = new ModelResponse<>(ruleService.createRule(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ModelResponse<Rule> updateRule(@RequestBody @Valid UpdateRuleRequest request){
		return new ModelResponse<>(ruleService.updateRule(request));
	}
	
	@DeleteMapping(path = "/{ruleId}")
	public BooleanResponse deleteRule(@PathVariable(name = "ruleId") Long ruleId){
		return new BooleanResponse(ruleService.deleteRule(ruleId));
	}
	
	@GetMapping
	public PageResponse<RuleResponse> queryRule(@RequestParam(name = "ruleId", required = false) Long ruleId){
		return new PageResponse<>(ruleService.getRule(ruleId));
	}
		
	
	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<ProductRule>> mapRuleToProduct(@RequestBody @Valid MapRuleToProductRequest request){
		ModelResponse<ProductRule> response = new ModelResponse<>(ruleService.mapRuleToProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/product")
	public ModelResponse<ProductRule> updateProductRule(@RequestBody @Valid UpdateMapRuleToProductRequest request){
		return new ModelResponse<>(ruleService.updateProductRule(request));
	}
	
	@DeleteMapping(path = "/product/{productRuleId}")
	public BooleanResponse deleteProductRule(@PathVariable(name = "productRuleId") Long productRuleId){
		return new BooleanResponse(ruleService.deleteProductRule(productRuleId));
	}

	@GetMapping(path = "/product")
	public ResponseEntity<CollectionResponse<RuleProductResponse>> getRuleProduct(
			@RequestParam(name = "code", required = true) String code){
		List<RuleProductResponse> roleResponseList = ruleService.getRuleProduct(code);
		CollectionResponse<RuleProductResponse> collectionResponse = new CollectionResponse<>(roleResponseList);
		return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
	}

}
