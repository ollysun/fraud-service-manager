package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepository;
		
	@Autowired
	AccountProductRepository accountProductRepository;
	
		
	public Account createAccount(AddAccountRequest request) {
		Account accountEntity = new Account();
		accountEntity.setAccountNo(request.getAccountNo());
		accountEntity.setAccountName(request.getAccountName());
		accountEntity.setBankCode(request.getBankCode());
		accountEntity.setBankName(request.getBankName());
		accountEntity.setStatus(Boolean.TRUE);
		accountEntity.setCreatedBy(request.getCreatedBy());
		accountEntity.setSuspicionCount(request.getSuspicion());
		accountEntity.setBlockReason(request.getBlockReason());
		return accountRepository.save(accountEntity);
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
		return accountProductRepository.save(accountProductEntity);
	}
	
	public AccountProduct updateAccountProduct(UpdateAccountProductRequest request) {
		AccountProduct accountEntity = accountProductRepository.findByAccountId(request.getAccountId());
		accountEntity.setStatus(request.getStatus());
		accountEntity.setUpdatedBy(request.getUpdatedBy());
		accountEntity.setProductCode(request.getProductCode());
		return accountProductRepository.save(accountEntity);
	}
	
}
