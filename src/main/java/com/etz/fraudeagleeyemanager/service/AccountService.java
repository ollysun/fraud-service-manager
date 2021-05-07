package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.AccountProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.AccountRedisRepository;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepository;
		
	@Autowired
	AccountProductRepository accountProductRepository;

	@Autowired
	AccountRedisRepository accountRedisRepository;

	@Autowired
	AccountProductRedisRepository accountProductRedisRepository;

	@Autowired @Qualifier("redisTemplate")
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;
	
		
	public Account createAccount(AddAccountRequest request) {
		Account accountEntity = new Account();

		// check for account number digits
		int digits = 1 + (int)Math.floor(Math.log10(request.getAccountNo()));
		if ( digits < 10){
			throw new FraudEngineException("Wrong Account Number");
		}
		accountEntity.setAccountName(request.getAccountName());
		accountEntity.setBankCode(request.getBankCode());
		accountEntity.setBankName(request.getBankName());
		accountEntity.setStatus(Boolean.TRUE);
		accountEntity.setCreatedBy(request.getCreatedBy());
		accountEntity.setSuspicionCount(request.getSuspicion());
		accountEntity.setBlockReason(request.getBlockReason());

		Account account = accountRepository.save(accountEntity);

		accountRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		accountRedisRepository.create(account);
		return account;
	}

	// update account to increment suspicious count
	public Account updateAccount(Long accountNumber, int count, Boolean status, String blockReason){
		Account account = accountRepository.findByAccountNo(accountNumber)
				.orElseThrow(() ->  new ResourceNotFoundException("Account details not found for account number " + accountNumber));
		account.setSuspicionCount(count);
		account.setBlockReason(blockReason);
		account.setStatus(status);
		return account;
	}

	public Page<Account> getAccount(Long accountId){
				
		if (Objects.isNull(accountId)) {
			return accountRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account Not found " + accountId));
		account.setId(accountId);
		return accountRepository.findAll(Example.of(account), PageRequestUtil.getPageRequest());		
	}
	
	public AccountProduct mapAccountProduct(AccountToProductRequest request) {
		AccountProduct accountProductEntity = new AccountProduct();
		accountProductEntity.setProductCode(request.getProductCode());
		accountProductEntity.setAccountId(request.getAccountId());
		accountProductEntity.setStatus(Boolean.TRUE);
		accountProductEntity.setCreatedBy(request.getCreatedBy());

		AccountProduct accountProduct = accountProductRepository.save(accountProductEntity);

		accountProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		accountProductRedisRepository.update(accountProduct);
		return accountProduct;
	}
	
	public AccountProduct updateAccountProduct(UpdateAccountProductRequest request) {
		AccountProduct accountEntity = accountProductRepository.findByAccountId(request.getAccountId());
		accountEntity.setStatus(request.getStatus());
		accountEntity.setUpdatedBy(request.getUpdatedBy());
		accountEntity.setProductCode(request.getProductCode());
		AccountProduct accountProduct = accountProductRepository.save(accountEntity);

		accountProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		accountProductRedisRepository.update(accountProduct);
		return accountProduct;
	}
	
}
