package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		try{
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
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
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
