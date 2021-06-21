package com.etz.fraudeagleeyemanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.OfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateOfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.OfacWatchlistRedisRepository;
import com.etz.fraudeagleeyemanager.repository.OfacWatchlistRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

@Service
public class OfacWatchlistService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	OfacWatchlistRepository ofacWatchlistRepository;
	
	@Autowired
	OfacWatchlistRedisRepository ofacWatchlistRedisRepository;
		
	public OfacWatchlist createOfacWatchlist(OfacWatchlistRequest request){
		OfacWatchlist ofacWatchlist = new OfacWatchlist();
		try {
			ofacWatchlist.setFullName(request.getFullName());
			ofacWatchlist.setCategory(request.getCategory());
			ofacWatchlist.setComments(request.getComments());
			ofacWatchlist.setStatus(false);
			ofacWatchlist.setAuthorised(false);
			ofacWatchlist.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			ofacWatchlist.setEntityId(null);
			ofacWatchlist.setRecordBefore(null);
			ofacWatchlist.setRequestDump(request);
			
			OfacWatchlist savedOfacWatchlist = ofacWatchlistRepository.save(ofacWatchlist);
			ofacWatchlistRedisRepository.setHashOperations(redisTemplate);
			ofacWatchlistRedisRepository.create(savedOfacWatchlist);
			return savedOfacWatchlist;
		} catch(Exception ex){
			throw new FraudEngineException(ex.getLocalizedMessage());
		}
	}

	public OfacWatchlist updateOfacWatchlist(UpdateOfacWatchlistRequest request, Long ofacId){
		OfacWatchlist ofacWatchlist = ofacWatchlistRepository.findById(ofacId)
				.orElseThrow(() ->  new ResourceNotFoundException("Person, with ID "+ request.getOfacId() +", not found on Ofac watchlist"));

		// for auditing purpose for UPDATE
		ofacWatchlist.setEntityId(request.getOfacId().toString());
		ofacWatchlist.setRecordBefore(JsonConverter.objectToJson(ofacWatchlist));
		ofacWatchlist.setRequestDump(request);
		
		ofacWatchlist.setFullName(request.getFullName());
		ofacWatchlist.setCategory(request.getCategory());
		ofacWatchlist.setComments(request.getComments());
		ofacWatchlist.setStatus(request.getStatus());
		ofacWatchlist.setUpdatedBy(request.getUpdatedBy());
		OfacWatchlist savedOfacWatchlist = ofacWatchlistRepository.save(ofacWatchlist);
		ofacWatchlistRedisRepository.setHashOperations(redisTemplate);
		ofacWatchlistRedisRepository.update(savedOfacWatchlist);
		return savedOfacWatchlist;
	}

	public Page<OfacWatchlist> getOfacWatchlist(Long ofacId){
		if (ofacId == null) {
			return ofacWatchlistRepository.findAll(PageRequestUtil.getPageRequest());
		}
		OfacWatchlist ofacWatchlist = new OfacWatchlist();
		ofacWatchlist = ofacWatchlistRepository.findById(ofacId)
				.orElseThrow(() -> new ResourceNotFoundException("Person, with ID "+ ofacId +", not found on Ofac watchlist"));
		//return new ArrayList<>(Arrays.asList(ofacWatchlist));
		return ofacWatchlistRepository.findAll(Example.of(ofacWatchlist), PageRequestUtil.getPageRequest());
	}
	
	public boolean deleteOfacWatchlist(Long ofacId) {
		OfacWatchlist ofacWatchlist = ofacWatchlistRepository.findById(ofacId)
				.orElseThrow(() -> new ResourceNotFoundException("Person, with ID "+ ofacId +", not found on Ofac watchlist"));

		// for auditing purpose for DELETE
		ofacWatchlist.setEntityId(ofacId.toString());
		ofacWatchlist.setRecordBefore(JsonConverter.objectToJson(ofacWatchlist));
		ofacWatchlist.setRecordAfter(null);
		ofacWatchlist.setRequestDump(ofacId);

		//ofacWatchlistRepository.deleteById(ofacId);
		ofacWatchlistRepository.delete(ofacWatchlist);
		ofacWatchlistRedisRepository.setHashOperations(redisTemplate);
		ofacWatchlistRedisRepository.delete(ofacId);
		return true;
	}
	
}
