package com.etz.fraudeagleeyemanager.redisrepository;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;

@Repository
public class NotificationGroupRedisRepository implements RedisRepository<NotificationGroup, Long> {
	
    private HashOperations<String, Long, Object> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
		
	@Override
	public void create(NotificationGroup model) {
		hashOperations.put(FraudRedisKey.NOTIFICATIONGROUP.name(), model.getId(), toJsonString(model));
	}

	@Override
	public Map<Long, NotificationGroup> findAll() {
        return convertResponseToEntityMap(hashOperations.entries(FraudRedisKey.NOTIFICATIONGROUP.name()),NotificationGroup.class);
	}

	@Override
	public NotificationGroup findById(Long groupId) {
		return convertResponseToEntity(hashOperations.get(FraudRedisKey.NOTIFICATIONGROUP.name(), groupId),NotificationGroup.class );
	}

	@Override
	public void update(NotificationGroup model) {
		create(model);
	}

	@Override
	public void delete(Long groupId) {
        hashOperations.delete(FraudRedisKey.NOTIFICATIONGROUP.name(), groupId);
	}
		
}
