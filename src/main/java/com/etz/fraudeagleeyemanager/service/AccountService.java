package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.AccountProductResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.AccountProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.AccountRedisRepository;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
	
	private final RedisTemplate<String, Object> redisTemplate;
	private final AccountRepository accountRepository;
	private final ProductEntityRepository productRepository;
	private final AccountProductRepository accountProductRepository;
	private final AccountRedisRepository accountRedisRepository;
	private final AccountProductRedisRepository accountProductRedisRepository;

	@CacheEvict(value = "account", allEntries=true)
	@Transactional(rollbackFor = Throwable.class)
	public Account createAccount(AddAccountRequest request){
		Account accountEntity = new Account();
		try {
			// check for account number digits
			accountEntity.setAccountName(request.getAccountName());
			accountEntity.setBankCode(request.getBankCode());
			accountEntity.setBankName(request.getBankName());
			accountEntity.setStatus(Boolean.TRUE);
			accountEntity.setCreatedBy(request.getCreatedBy());
			accountEntity.setSuspicionCount(request.getSuspicionCount());
			accountEntity.setAccountNo(request.getAccountNo());
			accountEntity.setBlockReason(request.getBlockReason());
			
			// for auditing purpose for CREATE
			accountEntity.setEntityId(null);
			accountEntity.setRecordBefore(null);
			accountEntity.setRequestDump(request);
		} catch(Exception ex){
			log.error("Error occurred while creating account entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveAccountEntityToDatabase(accountEntity);
	}

	// update account to increment suspicious count
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public Account updateAccount(UpdateAccountRequestDto updateAccountRequestDto){
		Optional<Account> accountOptional = accountRepository.findByAccountNo(updateAccountRequestDto.getAccountNumber());
		if(!accountOptional.isPresent()) {
			throw new ResourceNotFoundException("Account details not found for account number " + updateAccountRequestDto.getAccountNumber());
		}
		
		Account account = accountOptional.get();
		try {
			// for auditing purpose for UPDATE
			account.setEntityId(String.valueOf(account.getId()));
			account.setRecordBefore(JsonConverter.objectToJson(account));
			account.setRequestDump(updateAccountRequestDto);
			
			account.setSuspicionCount(updateAccountRequestDto.getCount());
			account.setBlockReason(updateAccountRequestDto.getBlockReason());
			account.setStatus(updateAccountRequestDto.getStatus());
		} catch(Exception ex){
			log.error("Error occurred while creating account entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveAccountEntityToDatabase(account);
	}
	
	private Account saveAccountEntityToDatabase(Account accountEntity) {
		Account persistedAccountEntity;
		try {
			persistedAccountEntity = accountRepository.save(accountEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving account entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveAccountEntityToRedis(persistedAccountEntity);
		return persistedAccountEntity;
	}
	
	private void saveAccountEntityToRedis(Account alreadyPersistedAccountEntity) {
		try {
			accountRedisRepository.setHashOperations(redisTemplate);
			accountRedisRepository.update(alreadyPersistedAccountEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving account entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@Transactional(readOnly = true)
	public Page<Account> getAccount(Long accountId){
		if (Objects.isNull(accountId)) {
			return accountRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account Not found " + accountId));
		account.setId(accountId);
		return accountRepository.findAll(Example.of(account), PageRequestUtil.getPageRequest());		
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public AccountProduct mapAccountProduct(AccountToProductRequest request) {
		
		if(!productRepository.findByCodeAndDeletedFalse(request.getProductCode()).isPresent()){
			throw new ResourceNotFoundException("Product not found for this code " + request.getProductCode());
		}

		if(!accountRepository.findById(request.getAccountId()).isPresent()){
			throw new ResourceNotFoundException("Account Not found " + request.getAccountId());
		}
		
		AccountProduct accountProductEntity = new AccountProduct();
		try {
			accountProductEntity.setAccountId(request.getAccountId());
			accountProductEntity.setProductCode(request.getProductCode());
			accountProductEntity.setStatus(Boolean.TRUE);
			accountProductEntity.setCreatedBy(request.getCreatedBy());
			
			// for auditing purpose for CREATE
			accountProductEntity.setEntityId(null);
			accountProductEntity.setRecordBefore(null);
			accountProductEntity.setRequestDump(request);
		} catch(Exception ex){
			log.error("Error occurred while creating account product entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveAccountProductEntityToDatabase(accountProductEntity);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public List<AccountProductResponse> updateAccountProduct(UpdateAccountProductRequest request) {
		List<AccountProduct> accountProdLst = accountProductRepository.findByAccountId(request.getAccountId());
		if (accountProdLst.isEmpty()){
			throw new ResourceNotFoundException("Account Product not found for this ID " +  request.getAccountId());
		}
		
		String entityId = "AcctId:" + request.getAccountId();
		if(!Objects.isNull(request.getProductCode())){
			accountProdLst = accountProdLst.stream().filter(entity -> entity.getProductCode().equals(request.getProductCode()))
					.collect(Collectors.toList());
			if(accountProdLst.isEmpty()) {
				throw new ResourceNotFoundException("Account Product not found for this id " + request.getAccountId() + " and code " +  request.getProductCode());
			}
			entityId += " ProdCd:" + request.getProductCode();
		}

		List<AccountProductResponse> accountProductResponseList = new ArrayList<>();
		AccountProductResponse accountProductResponse = new AccountProductResponse();
		accountProductRedisRepository.setHashOperations(redisTemplate);
		
		for(AccountProduct accountProduct : accountProdLst) {
			try {
				// for auditing purpose for UPDATE
				accountProduct.setEntityId(entityId);
				accountProduct.setRecordBefore(JsonConverter.objectToJson(accountProduct));
				accountProduct.setRequestDump(request);

				accountProduct.setStatus(request.getStatus());
				accountProduct.setUpdatedBy(request.getUpdatedBy());
			} catch (Exception ex) {
				log.error("Error occurred while creating account product entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			BeanUtils.copyProperties(saveAccountProductEntityToDatabase(accountProduct), accountProductResponse);
			accountProductResponseList.add(accountProductResponse);
		}
		return accountProductResponseList;
	}
	
	private AccountProduct saveAccountProductEntityToDatabase(AccountProduct accountProductEntity) {
		AccountProduct persistedAccountProductEntity;
		try {
			persistedAccountProductEntity = accountProductRepository.save(accountProductEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving account product entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveAccountProductEntityToRedis(persistedAccountProductEntity);
		return persistedAccountProductEntity;
	}
	
	private void saveAccountProductEntityToRedis(AccountProduct alreadyPersistedAccountProductEntity) {
		try {
			accountProductRedisRepository.setHashOperations(redisTemplate);
			accountProductRedisRepository.update(alreadyPersistedAccountProductEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving account product entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}
	
}
