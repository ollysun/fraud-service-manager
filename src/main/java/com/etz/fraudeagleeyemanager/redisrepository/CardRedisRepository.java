package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CardRedisRepository implements RedisRepository<Card, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(Card model) {
		hashOperations.put(FraudRedisKey.CARD.name(), model.getId(), toJsonString(model));
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
