package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;


@Repository
public class RuleRedisRepository implements RedisRepository<Rule, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(Rule model) {

		// start the transaction
		redisTemplateField.multi();

		// register synchronisation
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					TransactionSynchronization.super.afterCommit();
					hashOperations.put(FraudRedisKey.RULE.name(), model.getId(), toJsonString(model));
				}
			});
		}

	}

	@Override
	public Map<Long, Rule> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.RULE.name()), Rule.class);
	}

	@Override
	public Rule findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.RULE.name(), id), Rule.class);
	}

	@Override
	public void update(Rule model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.RULE.name(), id);
	}
}
