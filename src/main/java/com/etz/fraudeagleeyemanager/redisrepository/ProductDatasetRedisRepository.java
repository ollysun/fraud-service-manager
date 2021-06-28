package com.etz.fraudeagleeyemanager.redisrepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.ServiceDataSet;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;


@Repository
public class ProductDatasetRedisRepository implements RedisRepository<ServiceDataSet, String> {
	
    private HashOperations<String, String, ServiceDataSet> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ServiceDataSet model) {
		String hashKey = model.getProductCode().toUpperCase() + ":" + model.getId();
		hashOperations.put(FraudRedisKey.PRODUCTDATASET.name(), hashKey, model);		
	}

	@Override
	public Map<String, ServiceDataSet> findAll() {
        return hashOperations.entries(FraudRedisKey.PRODUCTDATASET.name());
	}

	@Override
	public ServiceDataSet findById(String id) {
		return hashOperations.get(FraudRedisKey.PRODUCTDATASET.name(), id);
	}

	@Override
	public void update(ServiceDataSet model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTDATASET.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {
		Set<String> foundKeys = new HashSet<>();
		ScanOptions options = ScanOptions.scanOptions().match(keyPatternToMatch).count(Integer.MAX_VALUE).build();
		Cursor<Map.Entry<String, ServiceDataSet>> cursor = hashOperations.scan(FraudRedisKey.PRODUCTDATASET.name(), options);
		while (cursor.hasNext()) {
			foundKeys.add(cursor.next().getKey());
		}
		try {
			cursor.close();
		} catch (IOException e) {
		}
		return foundKeys;
	}
}
