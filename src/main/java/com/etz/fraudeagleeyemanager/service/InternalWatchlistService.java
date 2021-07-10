package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.InternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateInternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.InternalWatchlistRedisRepository;
import com.etz.fraudeagleeyemanager.repository.InternalWatchlistRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalWatchlistService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final InternalWatchlistRepository internalWatchlistRepository;
	private final InternalWatchlistRedisRepository internalWatchlistRedisRepository;

	@Transactional(rollbackFor = Throwable.class)
	public InternalWatchlist createInternalWatchlist(InternalWatchlistRequest request){
		InternalWatchlist internalWatchlist = new InternalWatchlist();
		try {
			internalWatchlist.setBvn(request.getBvn());
			internalWatchlist.setComments(request.getComments());
			internalWatchlist.setStatus(false);
			internalWatchlist.setAuthorised(false);
			internalWatchlist.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			internalWatchlist.setEntityId(null);
			internalWatchlist.setRecordBefore(null);
			internalWatchlist.setRequestDump(request);
		} catch(Exception ex){
			log.error("Error occurred while creating Internal Watchlist entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveInternalWatchlistEntityToDatabase(internalWatchlist);
	}

	@Transactional(rollbackFor = Throwable.class)
	public InternalWatchlist updateInternalWatchlist(UpdateInternalWatchlistRequest request){
		InternalWatchlist internalWatchlist = findById(request.getWatchId()).get();
		try {
			// for auditing purpose for UPDATE
			internalWatchlist.setEntityId(String.valueOf(request.getWatchId()));
			internalWatchlist.setRecordBefore(JsonConverter.objectToJson(internalWatchlist));
			internalWatchlist.setRequestDump(request);

			internalWatchlist.setBvn(request.getBvn());
			internalWatchlist.setComments(request.getComments());
			internalWatchlist.setStatus(request.getStatus());
			internalWatchlist.setUpdatedBy(request.getUpdatedBy());
		} catch (Exception ex) {
			log.error("Error occurred while creating Internal Watchlist entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveInternalWatchlistEntityToDatabase(internalWatchlist);
	}

	private Optional<InternalWatchlist> findById(Long watchId) {
		Optional<InternalWatchlist> internalWatchlistOptional = internalWatchlistRepository.findById(watchId);
		if(!internalWatchlistOptional.isPresent()) {
			throw new ResourceNotFoundException("BVN not found on internal watchlist for ID " + watchId);
		}
		return internalWatchlistOptional;
	}

	@Transactional(readOnly = true)
	public Page<InternalWatchlist> getInternalWatchlist(Long watchId){
		if (Objects.isNull(watchId)) {
			return internalWatchlistRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Optional<InternalWatchlist> internalWatchlistOptional = findById(watchId);
		return internalWatchlistRepository.findAll(Example.of(internalWatchlistOptional.get()), PageRequestUtil.getPageRequest());
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteInternalWatchlist(Long watchId) {
		InternalWatchlist internalWatchlist = findById(watchId).get();
		// for auditing purpose for DELETE
		internalWatchlist.setEntityId(String.valueOf(watchId));
		internalWatchlist.setRecordBefore(JsonConverter.objectToJson(internalWatchlist));
		internalWatchlist.setRecordAfter(null);
		internalWatchlist.setRequestDump(watchId);
		try {
			internalWatchlistRepository.delete(internalWatchlist);
		} catch(Exception ex){
			log.error("Error occurred while deleting Internal Watchlist entity from the database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			internalWatchlistRedisRepository.setHashOperations(redisTemplate);
			internalWatchlistRedisRepository.delete(watchId);
		} catch (Exception ex) {
			log.error("Error occurred while deleting Internal Watchlist entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		return Boolean.TRUE;
	}
	
	private InternalWatchlist saveInternalWatchlistEntityToDatabase(InternalWatchlist internalWatchlistEntity) {
		InternalWatchlist persistedInternalWatchlistEntity;
		try {
			persistedInternalWatchlistEntity = internalWatchlistRepository.save(internalWatchlistEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving Internal Watchlist entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveInternalWatchlistEntityToRedis(persistedInternalWatchlistEntity);
		return persistedInternalWatchlistEntity;
	}
	
	private void saveInternalWatchlistEntityToRedis(InternalWatchlist alreadyPersistedInternalWatchlistEntity) {
		try {
			internalWatchlistRedisRepository.setHashOperations(redisTemplate);
			internalWatchlistRedisRepository.update(alreadyPersistedInternalWatchlistEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving Internal Watchlist entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
}
