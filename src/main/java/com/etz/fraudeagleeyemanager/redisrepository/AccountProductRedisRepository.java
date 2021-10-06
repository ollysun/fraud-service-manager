package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class AccountProductRedisRepository implements RedisRepository<AccountProduct, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;


	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(AccountProduct model) {

		try {
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						hashOperations.put(FraudRedisKey.ACCOUNTPRODUCT.name(), model.getAccountId(), toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}

	}

	@Override
	public Map<Long, AccountProduct> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.ACCOUNTPRODUCT.name()), AccountProduct.class);
	}

	@Override
	public AccountProduct findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.ACCOUNTPRODUCT.name(), id),AccountProduct.class);
	}

	@Override
	public void update(AccountProduct model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.ACCOUNTPRODUCT.name(), id);
	}
}
