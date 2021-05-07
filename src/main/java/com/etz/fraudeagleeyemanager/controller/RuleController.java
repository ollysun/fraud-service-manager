package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rule")
public class RuleController {

	@Autowired
	RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> createRule(@RequestBody CreateRuleRequest request){
		ModelResponse<Rule> response = new ModelResponse<Rule>(ruleService.createRule(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ModelResponse<Rule> updateRule(@RequestBody UpdateRuleRequest request){
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
	public ResponseEntity<ProductRule> mapRuleToProduct(@RequestBody MapRuleToProductRequest request){
		return new ResponseEntity<>(ruleService.mapRuleToProduct(request), HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/product")
	public ModelResponse<ProductRule> updateProductRule(@RequestBody UpdateMapRuleToProductRequest request){
		return new ModelResponse<>(ruleService.updateProductRule(request));
	}
	
	@DeleteMapping(path = "/product/{productRuleID}")
	public BooleanResponse deleteProductRule(@PathVariable(name = "productRuleId") Long productRuleId){
		return new BooleanResponse(ruleService.deleteProductRule(productRuleId));
	}

}
