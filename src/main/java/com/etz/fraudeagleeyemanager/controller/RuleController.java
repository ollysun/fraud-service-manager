package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rule")
public class RuleController {

	private final RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> createRule(@RequestBody @Valid CreateRuleRequest request){
		ModelResponse<Rule> response = new ModelResponse<>(ruleService.createRule(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping
	public ModelResponse<Rule> updateRule(@RequestBody @Valid UpdateRuleRequest request){
		return new ModelResponse<>(ruleService.updateRule(request));
	}
	
	@DeleteMapping("/{ruleId}")
	public BooleanResponse deleteRule(@PathVariable Long ruleId){
		return new BooleanResponse(ruleService.deleteRule(ruleId));
	}
	
	@GetMapping
	public PageResponse<RuleResponse> queryRule(Long ruleId){
		return new PageResponse<>(ruleService.getRule(ruleId));
	}
	
	@PostMapping("/product")
	public ResponseEntity<ModelResponse<ProductRule>> mapRuleToProduct(@RequestBody @Valid MapRuleToProductRequest request){
		ModelResponse<ProductRule> response = new ModelResponse<>(ruleService.mapRuleToProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping("/product")
	public CollectionResponse<ProductRuleResponse> updateProductRule(@RequestBody @Valid UpdateMapRuleToProductRequest request){
		return new CollectionResponse<>(ruleService.updateProductRule(request));
	}

	@DeleteMapping("/product/{code}/ruleId/{ruleId}")
	public BooleanResponse deleteProductRule(@PathVariable @NotNull @Positive Long ruleId, @PathVariable @NotBlank String code){
		return new BooleanResponse(ruleService.deleteProductRule(ruleId, code));
	}

	@GetMapping("/product")
	public CollectionResponse<RuleProductResponse> getRuleProduct(String code){
		return new CollectionResponse<>(ruleService.getRuleProduct(code));
	}

}
