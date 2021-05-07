package com.etz.fraudeagleeyemanager.redisrepository;

import com.etz.fraudeagleeyemanager.constant.FraudRedisKey;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.repository.RedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AccountRedisRepository implements RedisRepository<Account, String> {
	
    private HashOperations<String, String, Account> hashOperations;
	
//	//@Autowired	
//    public AccountRedisRepository(RedisTemplate<FraudRedisKey, Object> redisTemplate) {
//        this.hashOperations = redisTemplate.opsForHash();
//    }
        
    public void setHashOperations(RedisTemplate<String, Object> redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
		
	@Override
	public void create(Account model) {
		hashOperations.put(FraudRedisKey.ACCOUNT.name(), model.getAccountNo(), model);
	}

	@Override
	public Map<String, Account> findAll() {
        return hashOperations.entries(FraudRedisKey.ACCOUNT.name());
	}

	@Override
	public Account findById(String accountNumber) {
		return hashOperations.get(FraudRedisKey.ACCOUNT.name(), accountNumber);
	}

	@Override
	public void update(Account model) {
		create(model);
	}

	@Override
	public void delete(String accountNumber) {
        hashOperations.delete(FraudRedisKey.ACCOUNT.name(), accountNumber);
	}
		
}
