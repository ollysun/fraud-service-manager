package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.enums.FraudRedisKey;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class ProductRedisRepository implements RedisRepository<ProductEntity, String> {
	
    private HashOperations<String, String, ProductEntity> hashOperations;
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ProductEntity model) {
		hashOperations.put(FraudRedisKey.PRODUCT.name(), model.getCode(), model);
	}

	@Override
	public Map<String, ProductEntity> findAll() {
        return hashOperations.entries(FraudRedisKey.PRODUCT.name());
	}

	@Override
	public ProductEntity findById(String id) {
		return hashOperations.get(FraudRedisKey.PRODUCT.name(), id);
	}

	@Override
	public void update(ProductEntity model) {
		create(model);
	}

	@Override
	public void delete(String id) {
        hashOperations.delete(FraudRedisKey.PRODUCT.name(), id);
	}
}
