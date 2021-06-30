package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class InternalWatchlistRedisRepository implements RedisRepository<InternalWatchlist, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(InternalWatchlist model) {
		hashOperations.put(FraudRedisKey.INTERNALWATCHLIST.name(), model.getId(), toJsonString(model));
	}

	@Override
	public Map<Long, InternalWatchlist> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.INTERNALWATCHLIST.name()),InternalWatchlist.class);
	}

	@Override
	public InternalWatchlist findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.INTERNALWATCHLIST.name(), id),InternalWatchlist.class);
	}

	@Override
	public void update(InternalWatchlist model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.INTERNALWATCHLIST.name(), id);
	}
}
