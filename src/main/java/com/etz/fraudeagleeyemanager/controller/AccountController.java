package com.etz.fraudeagleeyemanager.controller;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.AccountProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class AccountController {

	private final AccountService accountService;
	
	//TODO set pre-authorize permission
	@PostMapping
	public ResponseEntity<ModelResponse<Account>> createAccount(@RequestBody @Valid AddAccountRequest request) {
		ModelResponse<Account> response = new ModelResponse<>(accountService.createAccount(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@GetMapping
	public PageResponse<Account> queryAccount(Long accountId) {
		return new PageResponse<>(accountService.getAccount(accountId));
	}
	
	@PutMapping
	public ModelResponse<Account> updateAccount(@RequestBody UpdateAccountRequestDto request){
		return new ModelResponse<>(accountService.updateAccount(request));
	}

	@PostMapping("/product")
	public ResponseEntity<ModelResponse<AccountProduct>> mapAccountToProduct(@RequestBody @Valid AccountToProductRequest request) {
		ModelResponse<AccountProduct> response = new ModelResponse<>(accountService.mapAccountProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@PutMapping("/product")
	public CollectionResponse<AccountProductResponse> updateAccountProduct(@RequestBody @Valid UpdateAccountProductRequest request) {
		return new CollectionResponse<>(accountService.updateAccountProduct(request), "All Updated AccountProduct");
	}

}
