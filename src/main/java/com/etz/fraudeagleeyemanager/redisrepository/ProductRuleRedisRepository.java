package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class ProductRuleRedisRepository implements RedisRepository<ServiceRule, String> {
	
    private HashOperations<String, String, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(ServiceRule model) {

		try {
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						String hashKey = model.getServiceId().toUpperCase() + ":" + model.getRuleId();
						hashOperations.put(FraudRedisKey.PRODUCTRULE.name(), hashKey, toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}
	}

	@Override
	public Map<String, ServiceRule> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PRODUCTRULE.name()),ServiceRule.class);
	}

	@Override
	public ServiceRule findById(String id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PRODUCTRULE.name(), id),ServiceRule.class);
	}

	@Override
	public void update(ServiceRule model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTRULE.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {
		return scanKeys(keyPatternToMatch, hashOperations, FraudRedisKey.PRODUCTRULE.name());
	}
	
}
