package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class InternalWatchlistRedisRepository implements RedisRepository<InternalWatchlist, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(InternalWatchlist model) {

		try{
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if(TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						hashOperations.put(FraudRedisKey.INTERNALWATCHLIST.name(), model.getId(), toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}

	}

	@Override
	public Map<Long, InternalWatchlist> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.INTERNALWATCHLIST.name()),InternalWatchlist.class);
	}

	@Override
	public InternalWatchlist findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.INTERNALWATCHLIST.name(), id),InternalWatchlist.class);
	}

	@Override
	public void update(InternalWatchlist model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.INTERNALWATCHLIST.name(), id);
	}
}
