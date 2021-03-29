package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudengine.dto.request.CreateParameterRequest;
import com.etz.fraudengine.entity.ParameterEntity;
import com.etz.fraudengine.enums.ConfigStatus;
import com.etz.fraudengine.repository.ParameterRepository;
import com.etz.fraudengine.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParameterService {

	@Autowired
	ParameterRepository parameterRepository;

	public ParameterEntity createParameter(CreateParameterRequest request) {
		ParameterEntity parameterEntity = new ParameterEntity();
		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(request.getOperator());
		parameterEntity.setRequireValue(request.getRequireValue() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		parameterEntity.setAuthorised(request.getAuthorised() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		parameterEntity.setCreatedBy(request.getCreatedBy());
		parameterEntity.setCreatedAt(LocalDateTime.now());

		return parameterRepository.save(parameterEntity);
	}

	public ParameterEntity updateParameter(CreateParameterRequest request) {
		ParameterEntity parameterEntity = parameterRepository.findById(request.getParamId().longValue()).get();

		parameterEntity.setName(request.getName());
		parameterEntity.setOperator(request.getOperator());
		parameterEntity.setRequireValue(request.getRequireValue() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		parameterEntity.setAuthorised(request.getAuthorised() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		parameterEntity.setUpdatedBy(request.getUpdatedBy());
		parameterEntity.setUpdatedAt(LocalDateTime.now());

		return parameterRepository.save(parameterEntity);
	}

	public boolean deleteParameter(Long parameterId) {
		parameterRepository.deleteById(parameterId);
		// child records have to be deleted
		return true;
	}

	public Page<ParameterEntity> getParameter(Long paramId) {
		if (paramId < 0) {
			return parameterRepository.findAll(PageRequestUtil.getPageRequest());
		}

		ParameterEntity parameterEntity = new ParameterEntity();
		parameterEntity.setId(paramId);
		return parameterRepository.findAll(Example.of(parameterEntity), PageRequestUtil.getPageRequest());
	}

}
