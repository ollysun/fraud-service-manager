package com.etz.fraudeagleeyemanager.service;


import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.OfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateOfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.OfacWatchlistRedisRepository;
import com.etz.fraudeagleeyemanager.repository.OfacWatchlistRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfacWatchlistService {

	private final AppUtil appUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	private final OfacWatchlistRepository ofacWatchlistRepository;
	private final OfacWatchlistRedisRepository ofacWatchlistRedisRepository;
	

	@Transactional(rollbackFor = Throwable.class)
	public OfacWatchlist addOfacWatchlist(OfacWatchlistRequest request){
		OfacWatchlist ofacWatchlist = new OfacWatchlist();
		ofacWatchlist.setFullName(request.getFullName());
		ofacWatchlist.setCategory(AppUtil.checkCategory(request.getCategory()));
		ofacWatchlist.setComments(request.getComments());
		ofacWatchlist.setStatus(false);
		ofacWatchlist.setAuthorised(false);
		ofacWatchlist.setCreatedBy(request.getCreatedBy());

		// for auditing purpose for CREATE
		ofacWatchlist.setEntityId(null);
		ofacWatchlist.setRecordBefore(null);
		ofacWatchlist.setRequestDump(request);

		return addOfacWatchlistEntityToDatabase(ofacWatchlist, request.getCreatedBy());
	}

	@Transactional(rollbackFor = Throwable.class)
	public OfacWatchlist updateOfacWatchlist(UpdateOfacWatchlistRequest request){
		OfacWatchlist ofacWatchlist = findById(request.getOfacId());

		// for auditing purpose for UPDATE
		ofacWatchlist.setEntityId(String.valueOf(request.getOfacId()));
		ofacWatchlist.setRecordBefore(JsonConverter.objectToJson(ofacWatchlist));
		ofacWatchlist.setRequestDump(request);
		
		ofacWatchlist.setFullName(request.getFullName());
		ofacWatchlist.setCategory(AppUtil.checkCategory(request.getCategory()));
		ofacWatchlist.setComments(request.getComments());
		ofacWatchlist.setStatus(request.getStatus());
		ofacWatchlist.setAuthorised(false);
		ofacWatchlist.setUpdatedBy(request.getUpdatedBy());

		return addOfacWatchlistEntityToDatabase(ofacWatchlist, request.getUpdatedBy());
	}

	private OfacWatchlist findById(Long ofacId) {
		Optional<OfacWatchlist> ofacWatchlistOptional = ofacWatchlistRepository.findById(ofacId);
		if(!ofacWatchlistOptional.isPresent()) {
			throw new ResourceNotFoundException("Person, with ID "+ ofacId +", not found on Ofac watchlist");
		}
		return ofacWatchlistOptional.get();
	}

	@Transactional(readOnly = true,rollbackFor = Throwable.class)
	public Page<OfacWatchlist> getOfacWatchlist(Long ofacId){
		if (Objects.isNull(ofacId)) {
			return ofacWatchlistRepository.findAll(PageRequestUtil.getPageRequest());
		}
		OfacWatchlist ofacWatchlistOptional = findById(ofacId);
		return ofacWatchlistRepository.findAll(Example.of(ofacWatchlistOptional), PageRequestUtil.getPageRequest());
	}

	@Transactional(rollbackFor = Throwable.class)
	public boolean deleteOfacWatchlist(Long ofacId) {
		OfacWatchlist ofacWatchlist = findById(ofacId);

		// for auditing purpose for DELETE
		ofacWatchlist.setEntityId(String.valueOf(ofacId));
		ofacWatchlist.setRecordBefore(JsonConverter.objectToJson(ofacWatchlist));
		ofacWatchlist.setRecordAfter(null);
		ofacWatchlist.setRequestDump(ofacId);

		try {
			ofacWatchlistRepository.delete(ofacWatchlist);
		} catch (Exception ex) {
		//	log.error("Error occurred while deleting OfacWatchlist entity from the database", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_DATABASE);
		}
		try {
			ofacWatchlistRedisRepository.setHashOperations(redisTemplate);
			ofacWatchlistRedisRepository.delete(ofacId);
		} catch (Exception ex) {
			log.error("Error occurred while deleting OfacWatchlist entity from Redis", ex);
			throw new FraudEngineException(AppConstant.ERROR_DELETING_FROM_REDIS);
		}
		return Boolean.TRUE;
	}
	
	private OfacWatchlist addOfacWatchlistEntityToDatabase(OfacWatchlist ofacWatchlistEntity, String createdBy) {
		OfacWatchlist persistedOfacWatchlistEntity;
		try {
			persistedOfacWatchlistEntity = ofacWatchlistRepository.save(ofacWatchlistEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving OfacWatchlist entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		addOfacWatchlistEntityToRedis(persistedOfacWatchlistEntity);
		
		// create & user notification
		appUtil.createUserNotification(AppConstant.OFAC_WATCHLIST, persistedOfacWatchlistEntity.getId().toString(), createdBy);
		return persistedOfacWatchlistEntity;
	}
	
	private void addOfacWatchlistEntityToRedis(OfacWatchlist alreadyPersistedOfacWatchlistEntity) {
		try {
			ofacWatchlistRedisRepository.setHashOperations(redisTemplate);
			ofacWatchlistRedisRepository.update(alreadyPersistedOfacWatchlistEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving OfacWatchlist entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
	
	
}
