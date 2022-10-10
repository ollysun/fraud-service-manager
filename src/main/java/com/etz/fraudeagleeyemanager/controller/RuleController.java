package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.etz.fraudeagleeyemanager.dto.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductRuleResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.RuleResponse;
import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import com.etz.fraudeagleeyemanager.service.RuleService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rule")
@Validated
public class RuleController {

	private final RuleService ruleService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Rule>> addRule(@RequestBody @Valid CreateRuleRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<Rule> response = new ModelResponse<>(ruleService.addRule(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping
	public ModelResponse<Rule> updateRule(@RequestBody @Valid UpdateRuleRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(ruleService.updateRule(request));
	}
	
	@DeleteMapping("/{ruleId}")
	public BooleanResponse deleteRule(@PathVariable @NotNull @Positive Long ruleId){
		return new BooleanResponse(ruleService.deleteRule(ruleId));
	}
	
	@GetMapping
	public PageResponse<RuleResponse> queryRule(@RequestParam(required = false) Long ruleId,
												@RequestParam(required = false) String ruleName){
		return new PageResponse<>(ruleService .getRule(ruleId,ruleName));
	}
	
	@PostMapping("/service")
	public CollectionResponse<ProductRuleResponse> mapRuleToService(@RequestBody @Valid MapRuleToServiceRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		return new CollectionResponse<>(ruleService.mapRuleToService(request), HttpStatus.CREATED.toString());
	}
	
//	@PutMapping("/service")
//	public CollectionResponse<ProductRuleResponse> updateServiceRule(@RequestBody @Valid UpdateMapRuleToServiceRequest request,
//																	 @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
//		request.setUpdatedBy(username);
//		return new CollectionResponse<>(ruleService.updateServiceRule(request));
//	}

	@PutMapping("/unmap/service")
	public BooleanResponse unmapServiceRule(@RequestBody @Valid UnmapServiceRuleRequest unmapServiceRuleRequest){
		return new BooleanResponse(ruleService.deleteServiceRule(unmapServiceRuleRequest));
	}

	@GetMapping("/service/{serviceId}")
	public CollectionResponse<RuleProductResponse> getRuleService(@PathVariable @NotBlank String serviceId){
		return new CollectionResponse<>(ruleService.getRuleService(serviceId));
	}



}
