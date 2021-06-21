package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Rule;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class RuleRedisRepository implements RedisRepository<Rule, Long> {
	
    private HashOperations<String, Long, Rule> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(Rule model) {
		hashOperations.put(FraudRedisKey.RULE.name(), model.getId(), model);
	}

	@Override
	public Map<Long, Rule> findAll() {
        return hashOperations.entries(FraudRedisKey.RULE.name());
	}

	@Override
	public Rule findById(Long id) {
		return hashOperations.get(FraudRedisKey.RULE.name(), id);
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
