package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParameterService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ParameterRedisRepository parameterRedisRepository;
	private final ParameterRepository parameterRepository;

	@Transactional(rollbackFor = Throwable.class)
	public Parameter createParameter(CreateParameterRequest request) {
		if (Boolean.TRUE.equals(parameterRepository.existsByNameAndOperator(request.getName(), request.getOperator()))){
			throw new FraudEngineException("The name and operator already exists in Parameter table ");
		}


		Parameter parameterEntity = new Parameter();
		try {
			parameterEntity.setName(request.getName());
			parameterEntity.setOperator(AppUtil.checkParameterOperator(request.getOperator()));
			parameterEntity.setAuthorised(request.getAuthorised());
			parameterEntity.setRequireValue(request.getRequireValue());
			parameterEntity.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			parameterEntity.setEntityId(null);
			parameterEntity.setRecordBefore(null);
			parameterEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating Parameter entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveInternalWatchlistEntityToDatabase(parameterEntity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public Parameter updateParameter(UpdateParameterRequest request) {
		Parameter parameterEntity = findById(request.getParamId()).get();
		try {
			// for auditing purpose for UPDATE
			parameterEntity.setEntityId(request.getParamId().toString());
			parameterEntity.setRecordBefore(JsonConverter.objectToJson(parameterEntity));
			parameterEntity.setRequestDump(request);

			parameterEntity.setName(request.getName());
			parameterEntity.setOperator(AppUtil.checkParameterOperator(request.getOperator()));
			parameterEntity.setRequireValue(request.getRequireValue());
			parameterEntity.setAuthorised(request.getAuthorised());
			parameterEntity.setUpdatedBy(request.getUpdatedBy());
		} catch (Exception ex) {
			log.error("Error occurred while creating Parameter entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveInternalWatchlistEntityToDatabase(parameterEntity);
	}

	// todo disable parameter
	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteParameter(Long parameterId) {
		log.info("Delete parameter ID: INFO >>>>>>>>>>>>>> {}", parameterId);
		if(Objects.isNull(parameterId)){
			throw new FraudEngineException("parameterId cannot be null");
		}

		Parameter parameter = findById(parameterId).get();
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
		log.info("Query parameter ID: INFO >>>>>>>>>>>>>> {}", paramId);
		if (Objects.isNull(paramId)) {
			return parameterRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Optional<Parameter> parameterEntityOptional  = findById(paramId);
		return parameterRepository.findAll(Example.of(parameterEntityOptional.get()), PageRequestUtil.getPageRequest());
	}

	@Transactional(readOnly = true)
	private Optional<Parameter> findById(Long paramId) {
		Optional<Parameter> parameterEntityOptional = parameterRepository.findById(paramId);
		if(!parameterEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Parameter Not found for ID " + paramId);
		}
		return parameterEntityOptional;
	}
	
	private Parameter saveInternalWatchlistEntityToDatabase(Parameter parameterEntity) {
		Parameter persistedParameterEntity;
		try {
			persistedParameterEntity = parameterRepository.save(parameterEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Internal Watchlist entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveParameterEntityToRedis(persistedParameterEntity);
		return persistedParameterEntity;
	}
	
	private void saveParameterEntityToRedis(Parameter alreadyPersistedParameterEntity) {
		try {
			parameterRedisRepository.setHashOperations(redisTemplate);
			parameterRedisRepository.update(alreadyPersistedParameterEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving Internal Watchlist entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

}
