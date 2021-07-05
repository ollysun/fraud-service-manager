package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Repository
public class ProductDatasetRedisRepository implements RedisRepository<ServiceDataSet, String> {
	
    private HashOperations<String, String, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(ServiceDataSet model) {
		// start the transaction
		redisTemplateField.multi();

		// register synchronisation
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					TransactionSynchronization.super.afterCommit();
					String hashKey = model.getProductCode().toUpperCase() + ":" + model.getServiceId().toUpperCase() + ":" + model.getFieldName().toUpperCase();
					hashOperations.put(FraudRedisKey.PRODUCTDATASET.name(), hashKey, toJsonString(model));
				}
			});
		}

	}

	@Override
	public Map<String, ServiceDataSet> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PRODUCTDATASET.name()),ServiceDataSet.class);
	}

	@Override
	public ServiceDataSet findById(String id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PRODUCTDATASET.name(), id),ServiceDataSet.class);
	}

	@Override
	public void update(ServiceDataSet model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTDATASET.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {


		return scanKeys(keyPatternToMatch, hashOperations, FraudRedisKey.PRODUCTDATASET.name());
	}
}
