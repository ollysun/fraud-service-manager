package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.ProductServiceEntity;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.eagleeyedb.RedisRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductServiceRedisRepository implements RedisRepository<ProductServiceEntity, String> {
	
    private HashOperations<String, String, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(ProductServiceEntity model) {

		try {
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if(TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						String hashKey = model.getServiceId().toUpperCase() + ":" + model.getProductCode().toUpperCase();
						hashOperations.put(FraudRedisKey.PRODUCTSERVICE.name(), hashKey, toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}
	}

	@Override
	public Map<String, ProductServiceEntity> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PRODUCTSERVICE.name()),ProductServiceEntity.class);
	}

	@Override
	public ProductServiceEntity findById(String id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PRODUCTSERVICE.name(), id),ProductServiceEntity.class);
	}

	@Override
	public void update(ProductServiceEntity model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTSERVICE.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {
		return scanKeys(keyPatternToMatch, hashOperations, FraudRedisKey.PRODUCTSERVICE.name());
	}
	
}
