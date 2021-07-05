package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.util.Map;


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
		hashOperations.put(FraudRedisKey.PRODUCT.name(), model.getCode(), toJsonString(model));
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

		// start the transaction
		redisTemplateField.multi();

		// register synchronisation
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					TransactionSynchronization.super.afterCommit();
					create(model);
				}
			});
		}
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCT.name(), id);
	}
}
