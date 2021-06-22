package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class OfacWatchlistRedisRepository implements RedisRepository<OfacWatchlist, Long> {
	
    private HashOperations<String, Long, OfacWatchlist> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(OfacWatchlist model) {
		hashOperations.put(FraudRedisKey.OFACWATCHLIST.name(), model.getId(), model);
	}

	@Override
	public Map<Long, OfacWatchlist> findAll() {
        return hashOperations.entries(FraudRedisKey.OFACWATCHLIST.name());
	}

	@Override
	public OfacWatchlist findById(Long id) {
		return hashOperations.get(FraudRedisKey.OFACWATCHLIST.name(), id);
	}

	@Override
	public void update(OfacWatchlist model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.OFACWATCHLIST.name(), id);
	}
}
