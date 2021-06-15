package com.etz.fraudeagleeyemanager.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.entity.ProductRule;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.service.RuleService;

import lombok.extern.slf4j.Slf4j;

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
	public CollectionResponse<ProductRuleResponse> updateProductRule(@RequestBody @Valid UpdateMapRuleToProductRequest request){
		return new CollectionResponse<>(ruleService.updateProductRule(request));
	}

	@DeleteMapping(path = "/product/{code}/ruleId/{ruleId}")
	public BooleanResponse deleteProductRule(@NotNull @Positive @PathVariable(value = "ruleId") Long ruleId,
											 @NotBlank @PathVariable(value = "code") String code){
		return new BooleanResponse(ruleService.deleteProductRule(ruleId, code));
	}

	@GetMapping(path = "/product")
	public ResponseEntity<CollectionResponse<RuleProductResponse>> getRuleProduct(
			@RequestParam(name = "code", required = true) String code){
		List<RuleProductResponse> roleResponseList = ruleService.getRuleProduct(code);
		CollectionResponse<RuleProductResponse> collectionResponse = new CollectionResponse<>(roleResponseList);
		return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
	}

}
