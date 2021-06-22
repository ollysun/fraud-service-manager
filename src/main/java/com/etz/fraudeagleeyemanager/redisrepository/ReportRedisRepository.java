package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.Report;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class ReportRedisRepository implements RedisRepository<Report, Long> {
	
    private HashOperations<String, Long, Report> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(Report model) {
		hashOperations.put(FraudRedisKey.REPORT.name(), model.getId(), model);
	}

	@Override
	public Map<Long, Report> findAll() {
        return hashOperations.entries(FraudRedisKey.REPORT.name());
	}

	@Override
	public Report findById(Long id) {
		return hashOperations.get(FraudRedisKey.REPORT.name(), id);
	}

	@Override
	public void update(Report model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.REPORT.name(), id);
	}
}
