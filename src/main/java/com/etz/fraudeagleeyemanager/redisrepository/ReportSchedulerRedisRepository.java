package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ReportScheduler;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class ReportSchedulerRedisRepository implements RedisRepository<ReportScheduler, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ReportScheduler model) {
		hashOperations.put(FraudRedisKey.REPORTSCHEDULER.name(), model.getId(), toJsonString(model));
	}

	@Override
	public Map<Long, ReportScheduler> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.REPORTSCHEDULER.name()),ReportScheduler.class);
	}

	@Override
	public ReportScheduler findById(Long id) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.REPORTSCHEDULER.name(), id),ReportScheduler.class);
	}

	@Override
	public void update(ReportScheduler model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.REPORTSCHEDULER.name(), id);
	}
}
