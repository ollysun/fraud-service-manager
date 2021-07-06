package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
	public PageResponse<RuleResponse> queryRule(@RequestParam(required = false) Long ruleId){
		return new PageResponse<>(ruleService.getRule(ruleId));
	}
	
	@PostMapping("/service")
	public ResponseEntity<ModelResponse<ServiceRule>> mapRuleToService(@RequestBody @Valid MapRuleToServiceRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<ServiceRule> response = new ModelResponse<>(ruleService.mapRuleToService(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping("/service")
	public CollectionResponse<ProductRuleResponse> updateServiceRule(@RequestBody @Valid UpdateMapRuleToServiceRequest request,
																	 @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new CollectionResponse<>(ruleService.updateServiceRule(request));
	}

	@DeleteMapping("/service/{serviceId}/ruleId/{ruleId}")
	public BooleanResponse deleteServiceRule(@PathVariable @NotNull @Positive Long ruleId, @PathVariable @NotNull @Positive String serviceId){
		return new BooleanResponse(ruleService.deleteServiceRule(ruleId, serviceId));
	}

	@GetMapping("/service/{serviceId}")
	public CollectionResponse<RuleProductResponse> getRuleService(@PathVariable @NotBlank @Positive String serviceId){
		return new CollectionResponse<>(ruleService.getRuleService(serviceId));
	}

}
