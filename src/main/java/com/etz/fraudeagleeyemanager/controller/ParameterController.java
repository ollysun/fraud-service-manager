package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;

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

import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.service.ParameterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/parameter")
public class ParameterController {
	
	private final ParameterService parameterService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Parameter>> createParameter(@RequestBody @Valid CreateParameterRequest request){
		ModelResponse<Parameter> response = new ModelResponse<>(parameterService.createParameter(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PutMapping
	public ModelResponse<Parameter> updateParameter(@RequestBody @Valid UpdateParameterRequest request){
		return new ModelResponse<>(parameterService.updateParameter(request));
	}
	
	@DeleteMapping("/{id}")
	public BooleanResponse deleteParameter(@PathVariable Long id){
		return new BooleanResponse(parameterService.deleteParameter(id));
	}
	
	@GetMapping
	public PageResponse<Parameter> queryParameter(Long paramId){
		return new PageResponse<>(parameterService.getParameter(paramId));
	}
	
}
