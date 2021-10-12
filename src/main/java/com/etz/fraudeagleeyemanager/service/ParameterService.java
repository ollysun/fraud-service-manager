package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CreateParameterRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateParameterRequest;
import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.ParameterRedisRepository;
import com.etz.fraudeagleeyemanager.repository.ParameterRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ParameterService {

	private final AppUtil appUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ParameterRedisRepository parameterRedisRepository;
	private final ParameterRepository parameterRepository;

	@Transactional(rollbackFor = Throwable.class)
	public Parameter addParameter(CreateParameterRequest request) {
		if (Boolean.TRUE.equals(parameterRepository.existsByNameAndOperator(request.getName(), request.getOperator()))){
			throw new FraudEngineException("The name and operator already exists in Parameter table ");
		}

		Parameter parameterEntity = new Parameter();
		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(AppUtil.checkParameterOperator(request.getOperator()));
		parameterEntity.setAuthorised(false);
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setCreatedBy(request.getCreatedBy());

		// for auditing purpose for CREATE
		parameterEntity.setEntityId(null);
		parameterEntity.setRecordBefore(null);
		parameterEntity.setRequestDump(request);

		return addInternalWatchlistEntityToDatabase(parameterEntity, parameterEntity.getUpdatedBy());
	}

	@Transactional(rollbackFor = Throwable.class)
	public Parameter updateParameter(UpdateParameterRequest request) {
		Parameter parameterEntity = findById(request.getParamId());

		// for auditing purpose for UPDATE
		parameterEntity.setEntityId(request.getParamId().toString());
		parameterEntity.setRecordBefore(JsonConverter.objectToJson(parameterEntity));
		parameterEntity.setRequestDump(request);

		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(AppUtil.checkParameterOperator(request.getOperator()));
		parameterEntity.setRequireValue(request.getRequireValue());
		parameterEntity.setAuthorised(false);
		parameterEntity.setUpdatedBy(request.getUpdatedBy());

		return addInternalWatchlistEntityToDatabase(parameterEntity, parameterEntity.getUpdatedBy());
	}

	// todo disable parameter
	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteParameter(Long parameterId) {
		log.info("Delete parameter ID: INFO >>>>>>>>>>>>>> {}", parameterId);
		if(Objects.isNull(parameterId)){
			throw new FraudEngineException("parameterId cannot be null");
		}

		Parameter parameter = findById(parameterId);
		// for auditing purpose for DELETE
		parameter.setEntityId(String.valueOf(parameterId));
		parameter.setRecordBefore(JsonConverter.objectToJson(parameter));
		parameter.setRecordAfter(null);
		parameter.setRequestDump(parameterId);

		try {
			parameterRepository.delete(parameter);
		} catch (Exception ex) {
			log.error("Error occurred while deleting Parameter entity from the database", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			parameterRedisRepository.setHashOperations(redisTemplate);
			parameterRedisRepository.delete(parameterId);
		} catch (Exception ex) {
			log.error("Error occurred while deleting Parameter entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		log.info("Parameter ID deleted: INFO >>>>>>>>>>> {}", parameterId);
		return Boolean.TRUE;
	}

	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	public Page<Parameter> getParameter(Long paramId) {
		if (Objects.isNull(paramId)) {
			return parameterRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Parameter parameterEntityOptional  = findById(paramId);
		return parameterRepository.findAll(Example.of(parameterEntityOptional), PageRequestUtil.getPageRequest());
	}

	private Parameter findById(Long paramId) {
		Optional<Parameter> parameterEntityOptional = parameterRepository.findById(paramId);
		if(!parameterEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Parameter Not found for ID " + paramId);
		}
		return parameterEntityOptional.get();
	}
	
	private Parameter addInternalWatchlistEntityToDatabase(Parameter parameterEntity, String createdBy) {
		Parameter persistedParameterEntity;
		try {
			persistedParameterEntity = parameterRepository.save(parameterEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Parameter entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addParameterEntityToRedis(persistedParameterEntity);
		// create & user notification
		appUtil.createUserNotification(AppConstant.PARAMETER, persistedParameterEntity.getId().toString(), createdBy);
		return persistedParameterEntity;
	}
	
	private void addParameterEntityToRedis(Parameter alreadyPersistedParameterEntity) {
		try {
			parameterRedisRepository.setHashOperations(redisTemplate);
			parameterRedisRepository.update(alreadyPersistedParameterEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Parameter entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

}
