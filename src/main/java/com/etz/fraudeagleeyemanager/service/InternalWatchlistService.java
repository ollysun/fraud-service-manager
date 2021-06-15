package com.etz.fraudeagleeyemanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.InternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateInternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.InternalWatchlistRedisRepository;
import com.etz.fraudeagleeyemanager.repository.InternalWatchlistRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

@Service
public class InternalWatchlistService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	InternalWatchlistRepository internalWatchlistRepository;
	
	@Autowired
	InternalWatchlistRedisRepository internalWatchlistRedisRepository;
		
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
			
			InternalWatchlist savedInternalWatchlist = internalWatchlistRepository.save(internalWatchlist);
			internalWatchlistRedisRepository.setHashOperations(redisTemplate);
			internalWatchlistRedisRepository.create(savedInternalWatchlist);
			return savedInternalWatchlist;
		} catch(Exception ex){
			throw new FraudEngineException(ex.getLocalizedMessage());
		}
	}

	public InternalWatchlist updateInternalWatchlist(UpdateInternalWatchlistRequest request){
		InternalWatchlist internalWatchlist = internalWatchlistRepository.findById(request.getWatchId())
				.orElseThrow(() ->  new ResourceNotFoundException("BVN not found on internal watchlist for ID "+ request.getWatchId()));
		
		internalWatchlist.setBvn(request.getBvn());
		internalWatchlist.setComments(request.getComments());
		internalWatchlist.setStatus(request.getStatus());
		internalWatchlist.setUpdatedBy(request.getUpdatedBy());
		
		// for auditing purpose for UPDATE
		internalWatchlist.setEntityId(request.getWatchId().toString());
		internalWatchlist.setRecordBefore(JsonConverter.objectToJson(internalWatchlist));
		internalWatchlist.setRequestDump(request);
		
		InternalWatchlist savedInternalWatchlist = internalWatchlistRepository.save(internalWatchlist);
		internalWatchlistRedisRepository.setHashOperations(redisTemplate);
		internalWatchlistRedisRepository.update(savedInternalWatchlist);
		return savedInternalWatchlist;
	}

	public Page<InternalWatchlist> getInternalWatchlist(Long watchId){
		if (watchId == null) {
			return internalWatchlistRepository.findAll(PageRequestUtil.getPageRequest());
		}
		InternalWatchlist internalWatchlist = new InternalWatchlist();
		internalWatchlist = internalWatchlistRepository.findById(watchId)
				.orElseThrow(() -> new ResourceNotFoundException("BVN not found on internal watchlist for ID "+ watchId));
		return internalWatchlistRepository.findAll(Example.of(internalWatchlist), PageRequestUtil.getPageRequest());
	}
	
	public boolean deleteInternalWatchlist(Long watchId) {
		InternalWatchlist internalWatchlist = internalWatchlistRepository.findById(watchId)
				.orElseThrow(() -> new ResourceNotFoundException("BVN not found on internal watchlist for ID "+ watchId));

		// for auditing purpose for DELETE
		internalWatchlist.setEntityId(watchId.toString());
		internalWatchlist.setRecordBefore(JsonConverter.objectToJson(internalWatchlist));
		internalWatchlist.setRecordAfter(null);
		internalWatchlist.setRequestDump(watchId);

		//internalWatchlistRepository.deleteById(watchId);
		internalWatchlistRepository.delete(internalWatchlist);
		internalWatchlistRedisRepository.setHashOperations(redisTemplate);
		internalWatchlistRedisRepository.delete(watchId);
		return true;
	}
	
}
