package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ParameterRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ParameterRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
		parameterEntity.setOperator(AppUtil.checkOperator(request.getOperator()));
		parameterEntity.setAuthorised(request.getAuthorised());
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setCreatedBy(request.getCreatedBy());
		Parameter createdEntity = parameterRepository.save(parameterEntity);
		parameterRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		parameterRedisRepository.create(createdEntity);
		
		return createdEntity;
	}

	public Parameter updateParameter(UpdateParameterRequest request) {

		Parameter parameterEntity = parameterRepository.findById(request.getParamId())
														     .orElseThrow(() -> new ResourceNotFoundException("Parameter details not found for the ID " + request.getParamId()));
		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(AppUtil.checkOperator(request.getOperator()));
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setAuthorised(request.getAuthorised());
		parameterEntity.setUpdatedBy(request.getUpdatedBy());

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
			parameterRepository.deleteById(parameterId);
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
