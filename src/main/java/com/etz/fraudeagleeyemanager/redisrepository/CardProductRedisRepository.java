package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class CardProductRedisRepository implements RedisRepository<CardProduct, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(CardProduct model) {
		hashOperations.put(FraudRedisKey.CARDPRODUCT.name(), model.getId().getId(), toJsonString(model));
	}

	@Override
	public Map<Long, CardProduct> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.CARDPRODUCT.name()),CardProduct.class);
	}

	@Override
	public CardProduct findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.CARDPRODUCT.name(), id),CardProduct.class);
	}

	@Override
	public void update(CardProduct model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.CARDPRODUCT.name(), id);
	}
}
