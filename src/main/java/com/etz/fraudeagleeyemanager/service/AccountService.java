package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.AccountProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.AccountRedisRepository;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
	
	private static final String SERVICE_NAME = "AccountService >>>>>>> {}";
	
	@Autowired
	private final RedisTemplate<String, Object> redisTemplate;

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

	public AccountService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

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
			log.info(SERVICE_NAME, ex.getLocalizedMessage());
			throw new FraudEngineException(AppConstant.ERROR_SET_PROPERTY);
		}
		
		try {
			Account account = accountRepository.save(accountEntity);
			accountRedisRepository.setHashOperations(redisTemplate);
			accountRedisRepository.create(account);
			return account;
		} catch(Exception ex){
			log.info(SERVICE_NAME, ex.getLocalizedMessage());
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
	}

	// update account to increment suspicious count
	public Account updateAccount(UpdateAccountRequestDto updateAccountRequestDto){
		Account account = accountRepository.findByAccountNo(updateAccountRequestDto.getAccountNumber())
				.orElseThrow(() ->  new ResourceNotFoundException("Account details not found for account number " + updateAccountRequestDto.getAccountNumber()));
		account.setSuspicionCount(updateAccountRequestDto.getCount());
		account.setBlockReason(updateAccountRequestDto.getBlockReason());
		account.setStatus(updateAccountRequestDto.getStatus());
		
		try {
			Account updatedAccount =accountRepository.save(account);
			accountRedisRepository.setHashOperations(redisTemplate);
			accountRedisRepository.update(updatedAccount);
			return updatedAccount;
		} catch(Exception ex){
			log.info(SERVICE_NAME, ex.getLocalizedMessage());
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
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

			accountRepository.findById(request.getAccountId())
					.orElseThrow(() -> new ResourceNotFoundException("Account Not found " + request.getAccountId()));

			accountProductEntity.setAccountId(request.getAccountId());
			accountProductEntity.setProductCode(request.getProductCode());
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
		List<AccountProduct> accountProdLst = accountProductRepository.findByAccountId(request.getAccountId());
		if (accountProdLst.isEmpty()){
			throw new ResourceNotFoundException("Account Product not found for this ID " +  request.getAccountId());
		}
		
		List<AccountProductResponse> accountProductResponseList = new ArrayList<>();
		AccountProductResponse accountProductResponse = new AccountProductResponse();
		accountProductRedisRepository.setHashOperations(redisTemplate);
		String entityId = "AcctId:" + request.getAccountId();
		
		// if productCode is not null, disable/enable
		if(request.getProductCode() != null){
			accountProdLst = accountProdLst.stream().filter(entity -> entity.getProductCode().equals(request.getProductCode()))
					.collect(Collectors.toList());
			if(accountProdLst.isEmpty()) {
				throw new ResourceNotFoundException("Account Product not found for this id " + request.getAccountId() + " and code " +  request.getProductCode());
			}
			entityId += " ProdCd:" + request.getProductCode();
		}

		for(AccountProduct accountProduct : accountProdLst) {
			//for auditing purpose for UPDATE
			accountProduct.setEntityId(entityId);
			accountProduct.setRecordBefore(JsonConverter.objectToJson(accountProduct));
			accountProduct.setRequestDump(request);
			
			accountProduct.setStatus(request.getStatus());
			accountProduct.setUpdatedBy(request.getUpdatedBy());
			AccountProduct accountProductSaved = accountProductRepository.save(accountProduct);
			accountProductRedisRepository.update(accountProductSaved);
			
			BeanUtils.copyProperties(accountProductSaved, accountProductResponse);
			accountProductResponseList.add(accountProductResponse);
		}
		return accountProductResponseList;
	}
	
}
