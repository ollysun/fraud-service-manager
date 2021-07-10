package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.util.Map;

@Slf4j
@Repository
public class ProductRedisRepository implements RedisRepository<ProductEntity, String> {
	
    private HashOperations<String, String, Object> hashOperations;
    private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
        redisTemplateField = redisTemplate;
    }
	@Override
	public void create(ProductEntity model) {
		try {
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						hashOperations.put(FraudRedisKey.PRODUCT.name(), model.getCode(), toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}

	}

	@Override
	public Map<String, ProductEntity> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PRODUCT.name()),ProductEntity.class);
	}

	@Override
	public ProductEntity findById(String id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PRODUCT.name(), id),ProductEntity.class);
	}

	@Override
	public void update(ProductEntity model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCT.name(), id);
	}
}
