package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.service.RuleService;

@Validated
@RestController
@RequestMapping("/rule")
public class RuleController {

	@Autowired
	RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> createRule(@RequestBody @Valid CreateRuleRequest request){
		ModelResponse<Rule> response = new ModelResponse<Rule>(ruleService.createRule(request));
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
	public PageResponse<Rule> queryRule(@RequestParam(name = "ruleId", required = false) String ruleId){
		return new PageResponse<>(ruleService.getRule(Long.parseLong(ruleId.trim())));
	}
		
	
	@PostMapping(path = "/product")
	public ResponseEntity<ProductRule> mapRuleToProduct(@RequestBody @Valid MapRuleToProductRequest request){
		return new ResponseEntity<>(ruleService.mapRuleToProduct(request), HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/product")
	public ModelResponse<ProductRule> updateProductRule(@RequestBody @Valid UpdateMapRuleToProductRequest request){
		return new ModelResponse<>(ruleService.updateProductRule(request));
	}
	
	@DeleteMapping(path = "/product/{productRuleID}")
	public BooleanResponse deleteProductRule(@PathVariable(name = "productRuleId") Long productRuleId){
		return new BooleanResponse(ruleService.deleteProductRule(productRuleId));
	}

}
