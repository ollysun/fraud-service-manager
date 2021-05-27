package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/parameter")
public class ParameterController {
	
	@Autowired
	private ParameterService parameterService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Parameter>> createParameter(
							@RequestBody @Valid CreateParameterRequest request){
		ModelResponse<Parameter> response = new ModelResponse<Parameter>(parameterService.createParameter(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ModelResponse<Parameter> updateParameter(@RequestBody @Valid UpdateParameterRequest request){
		return new ModelResponse<>(parameterService.updateParameter(request));
	}
	
	@DeleteMapping(path = "/{id}")
	public BooleanResponse deleteParameter(@PathVariable(name = "id") Long id){
		return new BooleanResponse(parameterService.deleteParameter(id));
	}
	
	@GetMapping
	public PageResponse<Parameter> queryParameter(
			@RequestParam(name = "paramId", required = false) Long paramId){
		return new PageResponse<>(parameterService.getParameter(paramId));
	}
	
}
