package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.constant.FraudRedisKey;
import com.etz.fraudeagleeyemanager.entity.Card;

import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CardRedisRepository implements RedisRepository<Card, Long> {
	
    private HashOperations<String, Long, Card> hashOperations;
	
//	//@Autowired
//    public CardRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(Card model) {
		hashOperations.put(FraudRedisKey.CARD.name(), model.getId(), model);
	}

	@Override
	public Map<Long, Card> findAll() {
        return hashOperations.entries(FraudRedisKey.CARD.name());
	}

	@Override
	public Card findById(Long cardBin) {
		return hashOperations.get(FraudRedisKey.CARD.name(), cardBin);
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
