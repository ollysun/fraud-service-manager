package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Card;
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
public class CardRedisRepository implements RedisRepository<Card, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	private RedisTemplate<String, Object> redisTemplateField;

	public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
		redisTemplateField = redisTemplate;
	}
    
	@Override
	public void create(Card model) {

		try{
			// start the transaction
			redisTemplateField.multi();

			// register synchronisation
			if(TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						TransactionSynchronization.super.afterCommit();
						hashOperations.put(FraudRedisKey.CARD.name(), model.getId(), toJsonString(model));
					}
				});
			}
		}catch(Exception ex){
			log.debug("error connecting to redis");
			throw new FraudEngineException("error connecting to redis " + ex.getMessage());
		}
	}

	@Override
	public Map<Long, Card> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.CARD.name()),Card.class);
	}

	@Override
	public Card findById(Long cardBin) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.CARD.name(), cardBin),Card.class);
	}

	@Override
	public void update(Card model) {
		create(model);
	}

	@Override
	public void delete(Long cardBin) {
        hashOperations.delete(FraudRedisKey.CARD.name(), cardBin);
	}
}
