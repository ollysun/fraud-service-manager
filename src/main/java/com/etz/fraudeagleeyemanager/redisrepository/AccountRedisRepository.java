package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudengine.constant.FraudRedisKey;
import com.etz.fraudengine.entity.AccountEntity;
import com.etz.fraudengine.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AccountRedisRepository implements RedisRepository<AccountEntity, String> {
	
    private HashOperations<String, String, AccountEntity> hashOperations;
	
//	//@Autowired	
//    public AccountRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
        
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
		
	@Override
	public void create(AccountEntity model) {
		hashOperations.put(FraudRedisKey.ACCOUNT.name(), model.getAccountNo(), model);		
	}

	@Override
	public Map<String, AccountEntity> findAll() {
        return hashOperations.entries(FraudRedisKey.ACCOUNT.name());
	}

	@Override
	public AccountEntity findById(String accountNumber) {
		return hashOperations.get(FraudRedisKey.ACCOUNT.name(), accountNumber);
	}

	@Override
	public void update(AccountEntity model) {
		create(model);
	}

	@Override
	public void delete(String accountNumber) {
        hashOperations.delete(FraudRedisKey.ACCOUNT.name(), accountNumber);
	}
		
}
