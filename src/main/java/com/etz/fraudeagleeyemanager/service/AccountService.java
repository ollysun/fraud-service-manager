package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.AccountProductResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.AccountProductId;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.AccountProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.AccountRedisRepository;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ProductEntityRepository productRepository;
		
	@Autowired
	AccountProductRepository accountProductRepository;

	@Autowired
	AccountRedisRepository accountRedisRepository;

	@Autowired
	AccountProductRedisRepository accountProductRedisRepository;


	
		
	public Account createAccount(AddAccountRequest request){
		Account accountEntity = new Account();

		if (!isNumeric(request.getAccountNo())){
			throw new FraudEngineException("Incorrect Account Number");
		}
		try {
			// check for account number digits
			accountEntity.setAccountName(request.getAccountName());
			accountEntity.setBankCode(request.getBankCode());
			accountEntity.setBankName(request.getBankName());
			accountEntity.setStatus(Boolean.TRUE);
			accountEntity.setCreatedBy(request.getCreatedBy());
			accountEntity.setSuspicionCount(request.getSuspicion());
			accountEntity.setAccountNo(request.getAccountNo());
			accountEntity.setBlockReason(request.getBlockReason());
			
			// for auditing purpose for CREATE
			accountEntity.setEntityId(null);
			accountEntity.setRecordBefore(null);
			accountEntity.setRequestDump(request);
			
			// saving to database
			Account account = accountRepository.save(accountEntity);
			// redis saving
			accountRedisRepository.setHashOperations(redisTemplate);
			accountRedisRepository.create(account);
			return account;
		} catch(Exception ex){
			throw new FraudEngineException(ex.getLocalizedMessage());
		}
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Long d = Long.parseLong(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
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

		try {

			ProductEntity productEntity = productRepository.findByCode(request.getProductCode());
			if(productEntity == null){
				throw new ResourceNotFoundException("Product not found for this code " + request.getProductCode());
			}

			accountRepository.findById(request.getAccountId()).orElseThrow(
					() -> new ResourceNotFoundException("Account Not found " + request.getAccountId()));

			AccountProductId accountProductId = new AccountProductId();
			accountProductId.setProductCode(request.getProductCode());
			accountProductId.setAccountId(request.getAccountId());
			accountProductEntity.setAccountProductId(accountProductId);
			accountProductEntity.setStatus(Boolean.TRUE);
			accountProductEntity.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			accountProductEntity.setEntityId(null);
			accountProductEntity.setRecordBefore(null);
			accountProductEntity.setRequestDump(request);
			
			log.info("entity to save " + accountProductEntity.toString());
			AccountProduct accountProduct = accountProductRepository.save(accountProductEntity);

			accountProductRedisRepository.setHashOperations(redisTemplate);
			accountProductRedisRepository.create(accountProduct);
			return accountProduct;
		}catch(Exception ex){
			throw new FraudEngineException(ex.getLocalizedMessage());
		}
	}
	
	public List<AccountProductResponse> updateAccountProduct(UpdateAccountProductRequest request) {
		List<AccountProduct> accountEntity = accountProductRepository.findByAccountProductIdAccountId(request.getAccountId());
		if (accountEntity.size() < 1){
			throw new ResourceNotFoundException("Account Product not found for this ID " +  request.getAccountId());
		}
		ProductEntity productEntity = productRepository.findByCode(request.getProductCode());
		if(productEntity == null){
			throw new ResourceNotFoundException("Product not found for this code " + request.getProductCode());
		}
		List<AccountProductResponse> accountProductResponseList = new ArrayList<>();
		accountEntity.forEach(acctprod -> {
			acctprod.setStatus(request.getStatus());
			acctprod.setUpdatedBy(request.getUpdatedBy());
			acctprod.getAccountProductId().setProductCode(request.getProductCode());
			
			//for auditing purpose for UPDATE
			acctprod.setEntityId(null);
			acctprod.setRecordBefore(null);
			acctprod.setRequestDump(request);
			
			AccountProduct accountProduct = accountProductRepository.save(acctprod);
			AccountProductResponse accountProductResponse = new AccountProductResponse();
			BeanUtils.copyProperties(accountProduct, accountProductResponse);
			accountProductResponseList.add(accountProductResponse);
		});

		accountProductRedisRepository.setHashOperations(redisTemplate);
		AccountProduct account = accountProductRedisRepository.findById(request.getAccountId());
		account.setStatus(request.getStatus());
		account.setUpdatedBy(request.getUpdatedBy());
		account.getAccountProductId().setProductCode(request.getProductCode());
		accountProductRedisRepository.update(account);
		return accountProductResponseList;
	}
	
}
