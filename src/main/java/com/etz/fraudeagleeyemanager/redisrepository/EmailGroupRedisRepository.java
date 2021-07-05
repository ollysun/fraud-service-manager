package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.EmailGroup;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;


@Repository
public class EmailGroupRedisRepository implements RedisRepository<EmailGroup, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(EmailGroup model) {

		// start the transaction
		redisTemplateField.multi();

		// register synchronisation
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					TransactionSynchronization.super.afterCommit();
					hashOperations.put(FraudRedisKey.EMAILGROUP.name(), model.getId(), toJsonString(model));
				}
			});
		}
	}

	@Override
	public Map<Long, EmailGroup> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.EMAILGROUP.name()),EmailGroup.class);
	}

	@Override
	public EmailGroup findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.EMAILGROUP.name(), id),EmailGroup.class);
	}

	@Override
	public void update(EmailGroup model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.EMAILGROUP.name(), id);
	}
}
