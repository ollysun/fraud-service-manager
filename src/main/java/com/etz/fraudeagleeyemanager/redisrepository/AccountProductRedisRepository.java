package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.constant.FraudRedisKey;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class AccountProductRedisRepository implements RedisRepository<AccountProduct, Long> {
	
    private HashOperations<String, Long, AccountProduct> hashOperations;
	
//	@Autowired
//    public AccountProductRedisRepository(@Qualifier("pocketMoniRedisTemplate")RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
	
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    
	@Override
	public void create(AccountProduct model) {
		hashOperations.put(FraudRedisKey.ACCOUNTPRODUCT.name(), model.getAccountId(), model);
	}

	@Override
	public Map<Long, AccountProduct> findAll() {
        return hashOperations.entries(FraudRedisKey.ACCOUNTPRODUCT.name());
	}

	@Override
	public AccountProduct findById(Long id) {
		return hashOperations.get(FraudRedisKey.ACCOUNTPRODUCT.name(), id);
	}

	@Override
	public void update(AccountProduct model) {
		create(model);
	}

	@Override
	public void delete(Long id) {
        hashOperations.delete(FraudRedisKey.ACCOUNTPRODUCT.name(), id);
	}
}
