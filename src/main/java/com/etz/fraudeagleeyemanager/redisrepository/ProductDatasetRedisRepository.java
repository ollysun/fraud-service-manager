package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.constant.FraudRedisKey;
import com.etz.fraudeagleeyemanager.entity.ProductDataSet;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class ProductDatasetRedisRepository implements RedisRepository<ProductDataSet, Long> {
	
    private HashOperations<String, Long, ProductDataSet> hashOperations;
	
//	//@Autowired
//    public ProductDatasetRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(ProductDataSet model) {
		hashOperations.put(FraudRedisKey.PRODUCTDATASET.name(), model.getId(), model);		
	}

	@Override
	public Map<Long, ProductDataSet> findAll() {
        return hashOperations.entries(FraudRedisKey.PRODUCTDATASET.name());
	}

	@Override
	public ProductDataSet findById(Long id) {
		return hashOperations.get(FraudRedisKey.PRODUCTDATASET.name(), id);
	}

	@Override
	public void update(ProductDataSet model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.PRODUCTDATASET.name(), id);
	}
}
