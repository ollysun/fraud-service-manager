package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudengine.dto.request.CreateParameterRequest;
import com.etz.fraudengine.dto.response.BooleanResponse;
import com.etz.fraudengine.dto.response.ModelResponse;
import com.etz.fraudengine.dto.response.PageResponse;
import com.etz.fraudengine.entity.ParameterEntity;
import com.etz.fraudengine.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/parameter")
public class ParameterController {
	
	@Autowired
	ParameterService parameterService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<ParameterEntity>> createParameter(
							@RequestBody @Valid CreateParameterRequest request){
		ModelResponse<ParameterEntity> response = new ModelResponse<ParameterEntity>(parameterService.createParameter(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{paramID}")
	public ModelResponse<ParameterEntity> updateParameter(@PathVariable(name = "paramID") Integer parameterId,
			@RequestBody @Valid CreateParameterRequest request){
		request.setParamId(parameterId);
		return new ModelResponse<ParameterEntity>(parameterService.updateParameter(request));
	}
	
	@DeleteMapping(path = "/{paramID}")
	public BooleanResponse deleteParameter(@PathVariable(name = "paramID") Long parameterId){
		return new BooleanResponse(parameterService.deleteParameter(parameterId));
	}
	
	@GetMapping
	public PageResponse<ParameterEntity> queryParameter(
			@RequestParam(name = "paramID", defaultValue = "-1") String paramId){
		return new PageResponse<>(parameterService.getParameter(Long.parseLong(paramId.trim())));
	}
	
}
