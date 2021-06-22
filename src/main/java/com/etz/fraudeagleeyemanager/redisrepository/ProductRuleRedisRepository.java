package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ProductRule;
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
public class ProductRuleRedisRepository implements RedisRepository<ProductRule, String> {
	
    private HashOperations<String, String, ProductRule> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ProductRule model) {
		String hashKey = model.getProductCode().toUpperCase() + ":" + model.getRuleId();
		hashOperations.put(FraudRedisKey.PRODUCTRULE.name(), hashKey, model);
	}

	@Override
	public Map<String, ProductRule> findAll() {
        return hashOperations.entries(FraudRedisKey.PRODUCTRULE.name());
	}

	@Override
	public ProductRule findById(String id) {
		return hashOperations.get(FraudRedisKey.PRODUCTRULE.name(), id);
	}

	@Override
	public void update(ProductRule model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCTRULE.name(), id);
	}
	
	public Set<String> scanKeys(String keyPatternToMatch) {
		Set<String> foundKeys = new HashSet<>();
		ScanOptions options = ScanOptions.scanOptions().match(keyPatternToMatch).count(Integer.MAX_VALUE).build();
		Cursor<Map.Entry<String, ProductRule>> cursor = hashOperations.scan(FraudRedisKey.PRODUCTRULE.name(), options);
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
