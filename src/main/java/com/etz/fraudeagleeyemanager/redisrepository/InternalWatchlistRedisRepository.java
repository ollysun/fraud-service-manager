package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.constant.FraudRedisKey;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class InternalWatchlistRedisRepository implements RedisRepository<InternalWatchlist, Long> {
	
    private HashOperations<String, Long, InternalWatchlist> hashOperations;
	
//	//@Autowired
//    public InternalWatchlistRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(InternalWatchlist model) {
		hashOperations.put(FraudRedisKey.INTERNALWATCHLIST.name(), model.getId(), model);
	}

	@Override
	public Map<Long, InternalWatchlist> findAll() {
        return hashOperations.entries(FraudRedisKey.INTERNALWATCHLIST.name());
	}

	@Override
	public InternalWatchlist findById(Long id) {
		return hashOperations.get(FraudRedisKey.INTERNALWATCHLIST.name(), id);
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
