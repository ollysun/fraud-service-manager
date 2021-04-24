package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountRequest;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.repository.AccountProductRepository;
import com.etz.fraudeagleeyemanager.repository.AccountRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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
		//accountEntity.setStatus(request.getStatus());
		accountEntity.setCreatedAt(LocalDateTime.now());		
		return accountRepository.save(accountEntity);
	}

	public Page<Account> getAccount(Long accountId){
				
		if (accountId < 0) {
			return accountRepository.findAll(PageRequestUtil.getPageRequest());
		}
				
		Account account = new Account();
		account.setId(accountId);
		
		return accountRepository.findAll(Example.of(account), PageRequestUtil.getPageRequest());		
	}
	
	public AccountProduct accountProductMap(AccountToProductRequest request) {
		
		AccountProduct accountProductEntity = new AccountProduct();
		accountProductEntity.setProductCode(request.getProductCode()); //
		accountProductEntity.setAccountId(request.getAccountId().longValue());
		//accountProductEntity.setStatus(request.getStatus());
		accountProductEntity.setCreatedAt(LocalDateTime.now());

		return accountProductRepository.save(accountProductEntity);
	}
	
	public Account updateAccount(UpdateAccountRequest request) {
		Account accountEntity = accountRepository.findById(request.getAccountId().longValue()).get();

		//accountEntity.setStatus(request.getStatus());
		accountEntity.setUpdatedBy(request.getUpdatedBy());

		return accountRepository.save(accountEntity);
	}
	
}
