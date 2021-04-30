package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudengine.constant.FraudRedisKey;
import com.etz.fraudengine.entity.OfacWatchlistEntity;
import com.etz.fraudengine.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class OfacWatchlistRedisRepository implements RedisRepository<OfacWatchlistEntity, Long> {
	
    private HashOperations<String, Long, OfacWatchlistEntity> hashOperations;
	
//	//@Autowired
//    public OfacWatchlistRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(OfacWatchlistEntity model) {
		hashOperations.put(FraudRedisKey.OFACWATCHLIST.name(), model.getId(), model);		
	}

	@Override
	public Map<Long, OfacWatchlistEntity> findAll() {
        return hashOperations.entries(FraudRedisKey.OFACWATCHLIST.name());
	}

	@Override
	public OfacWatchlistEntity findById(Long id) {
		return hashOperations.get(FraudRedisKey.OFACWATCHLIST.name(), id);
	}

	@Override
	public void update(OfacWatchlistEntity model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.OFACWATCHLIST.name(), id);
	}
}
