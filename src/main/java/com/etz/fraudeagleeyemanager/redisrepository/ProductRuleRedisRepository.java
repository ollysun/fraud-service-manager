package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ServiceRule;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Repository
public class ProductRuleRedisRepository implements RedisRepository<ServiceRule, String> {
	
    private HashOperations<String, String, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ServiceRule model) {
		String hashKey = model.getServiceId() + ":" + model.getRuleId();
		hashOperations.put(FraudRedisKey.PRODUCTRULE.name(), hashKey, toJsonString(model));
	}

	@Override
	public Map<String, ServiceRule> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PRODUCTRULE.name()),ServiceRule.class);
	}

	@Override
	public ServiceRule findById(String id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PRODUCTRULE.name(), id),ServiceRule.class);
	}

	@Override
	public void update(ServiceRule model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTRULE.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {
		return scanKeys(keyPatternToMatch, hashOperations, FraudRedisKey.PRODUCTRULE.name());
	}
	
}
