package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

@Repository
public class AccountRedisRepository implements RedisRepository<Account, String> {
	
    private HashOperations<String, String, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;

	}
		
	@Override
	public void create(Account model) {

		// start the transaction
		redisTemplateField.multi();

		// register synchronisation
		if(TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					TransactionSynchronization.super.afterCommit();
					hashOperations.put(FraudRedisKey.ACCOUNT.name(), model.getAccountNo(), toJsonString(model));
				}
			});
		}
	}

	@Override
	public Map<String, Account> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.ACCOUNT.name()), Account.class);
	}

	@Override
	public Account findById(String accountNumber) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.ACCOUNT.name(), accountNumber),Account.class);
	}

	@Override
	public void update(Account model) {
		create(model);
	}

	@Override
	public void delete(String accountNumber) {
        hashOperations.delete(FraudRedisKey.ACCOUNT.name(), accountNumber);
	}
		
}
