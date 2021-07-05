package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Parameter;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class ParameterRedisRepository implements RedisRepository<Parameter, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(Parameter model) {
		hashOperations.put(FraudRedisKey.PARAMETER.name(), model.getId(), toJsonString(model));
	}

	@Override
	public Map<Long, Parameter> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.PARAMETER.name()),Parameter.class);
	}

	@Override
	public Parameter findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.PARAMETER.name(), id),Parameter.class);
	}

	@Override
	public void update(Parameter model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.PARAMETER.name(), id);
	}
}
