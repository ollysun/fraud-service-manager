package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.Parameter;
import com.etz.fraudeagleeyemanager.service.ParameterService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/parameter")
@Validated
public class ParameterController {
	
	private final ParameterService parameterService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Parameter>> addParameter(@RequestBody @Valid CreateParameterRequest request,@ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<Parameter> response = new ModelResponse<>(parameterService.addParameter(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping
	public ModelResponse<Parameter> updateParameter(@RequestBody @Valid UpdateParameterRequest request,
													@ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(parameterService.updateParameter(request));
	}
	
	@DeleteMapping("/{id}")
	public BooleanResponse deleteParameter(@PathVariable @Positive Long id){
		return new BooleanResponse(parameterService.deleteParameter(id));
	}
	
	@GetMapping
	public PageResponse<Parameter> queryParameter(@RequestParam(required = false) Long paramId){
		return new PageResponse<>(parameterService.getParameter(paramId));
	}
	
}
