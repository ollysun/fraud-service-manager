package com.etz.fraudeagleeyemanager.controller;


import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	@PostMapping
	public ResponseEntity<ModelResponse<Account>> createAccount(@RequestBody AddAccountRequest request) {
		ModelResponse<Account> response = new ModelResponse<>(accountService.createAccount(request));
		response.setStatus(HttpStatus.CREATED.value());

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public PageResponse<Account> queryAccount(
			@RequestParam(name = "accountId", required = false) Long accountId) {
		return new PageResponse<>(accountService.getAccount(accountId));
	}

	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<AccountProduct>> mapAccountToProduct(
			@RequestBody @Valid AccountToProductRequest request) {
		ModelResponse<AccountProduct> response = new ModelResponse<>(
				accountService.mapAccountProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(path = "/product")
	public ModelResponse<AccountProduct> updateAccount(@RequestBody UpdateAccountProductRequest request) {
		return new ModelResponse<>(accountService.updateAccountProduct(request));
	}

}
