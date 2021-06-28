package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.etz.fraudeagleeyemanager.dto.request.CreateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.request.MapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateMapRuleToServiceRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateRuleRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.service.RuleService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rule")
public class RuleController {

	private final RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> createRule(@RequestBody @Valid CreateRuleRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<Rule> response = new ModelResponse<>(ruleService.createRule(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping
	public ModelResponse<Rule> updateRule(@RequestBody @Valid UpdateRuleRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
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
	
	@PostMapping("/service")
	public ResponseEntity<ModelResponse<ServiceRule>> mapRuleToProduct(@RequestBody @Valid MapRuleToServiceRequest request){
		ModelResponse<ServiceRule> response = new ModelResponse<>(ruleService.mapRuleToService(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping("/product")
	public CollectionResponse<ProductRuleResponse> updateProductRule(@RequestBody @Valid UpdateMapRuleToServiceRequest request){
		return new CollectionResponse<>(ruleService.updateProductRule(request));
	}

	@DeleteMapping("/product/{code}/ruleId/{ruleId}")
	public BooleanResponse deleteProductRule(@PathVariable @NotNull @Positive Long ruleId, @PathVariable @NotNull Long serviceId){
		return new BooleanResponse(ruleService.deleteProductRule(ruleId, serviceId));
	}

	@GetMapping("/product")
	public CollectionResponse<RuleProductResponse> getRuleService(Long serviceId){
		return new CollectionResponse<>(ruleService.getRuleService(serviceId));
	}

}
