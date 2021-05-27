package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ParameterRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ParameterRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParameterService {

	@Autowired
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;
	
	@Autowired
	ParameterRedisRepository parameterRedisRepository;
	
	@Autowired
	ParameterRepository parameterRepository;
	

	public Parameter createParameter(CreateParameterRequest request) {
		Parameter parameterEntity = new Parameter();
		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(request.getOperator());
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setAuthorised(request.getAuthorised());
		parameterEntity.setCreatedBy(request.getCreatedBy());
		
		// for auditing purpose for CREATE
		parameterEntity.setEntityId(null);
		parameterEntity.setRecordBefore(null);
		parameterEntity.setRequestDump(request);

		Parameter createdEntity = parameterRepository.save(parameterEntity);
		parameterRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		parameterRedisRepository.create(createdEntity);
		
		return createdEntity;
	}

	public Parameter updateParameter(UpdateParameterRequest request) {

		Parameter parameterEntity = parameterRepository.findById(request.getParamId())
														     .orElseThrow(() -> new ResourceNotFoundException("Parameter details not found for the ID " + request.getParamId()));
		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(request.getOperator());
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setAuthorised(request.getAuthorised());
		parameterEntity.setUpdatedBy(request.getUpdatedBy());

		// for auditing purpose for UPDATE
		parameterEntity.setEntityId(request.getParamId().toString());
		parameterEntity.setRecordBefore(JsonConverter.objectToJson(parameterEntity));
		parameterEntity.setRequestDump(request);

		Parameter updatedEntity = parameterRepository.save(parameterEntity);
		parameterRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		parameterRedisRepository.update(updatedEntity);
		
		return updatedEntity;
	}

	// todo disable parameter
	public boolean deleteParameter(Long parameterId) {
		log.info("Delete parameter ID: INFO >>>>>>>>>>>>>> {}", parameterId);
		if(Objects.isNull(parameterId)){
			throw new FraudEngineException("parameterId cannot be null");
		}

		Optional<Parameter> parameterEntity  = parameterRepository.findById(parameterId);
		if (parameterEntity.isPresent()) {

			Parameter parameter = parameterEntity.get();
			// for auditing purpose for DELETE
			parameter.setEntityId(parameterId.toString());
			parameter.setRecordBefore(JsonConverter.objectToJson(parameter));
			parameter.setRecordAfter(null);
			parameter.setRequestDump(parameterId);
			
			//parameterRepository.deleteById(parameterId);
			parameterRepository.delete(parameter);
			parameterRedisRepository.setHashOperations(fraudEngineRedisTemplate);
			parameterRedisRepository.delete(parameterId);
		}else{
			throw new ResourceNotFoundException("Parameter Not found " + parameterId);
		}
		log.info("Parameter ID deleted: INFO >>>>>>>>>>> {}", parameterId);
		return true;
	}

	public Page<Parameter> getParameter(Long paramId) {
		log.info("Query parameter ID: INFO >>>>>>>>>>>>>> {}", paramId);
		if (paramId == null) {
			return parameterRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Parameter parameterEntity  = parameterRepository.findById(paramId)
				                     .orElseThrow(() -> new ResourceNotFoundException("Parameter Not found " + paramId));
		parameterEntity.setId(paramId);
		return parameterRepository.findAll(Example.of(parameterEntity), PageRequestUtil.getPageRequest());
	}

}
