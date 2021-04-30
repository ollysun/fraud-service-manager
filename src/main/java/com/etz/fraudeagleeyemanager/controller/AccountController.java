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
		ModelResponse<Account> response = new ModelResponse<Account>(accountService.createAccount(request));
		response.setStatus(201);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public PageResponse<Account> queryAccount(
			@RequestParam(name = "accountID", defaultValue = "-1") String accountId) {
		return new PageResponse<>(accountService.getAccount(Long.parseLong(accountId.trim())));
	}

	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<AccountProduct>> mapAccountToProduct(
			@RequestBody @Valid AccountToProductRequest request) {
		ModelResponse<AccountProduct> response = new ModelResponse<AccountProduct>(
				accountService.accountProductMap(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{accountID}")
	public ModelResponse<Account> updateAccount(@PathVariable(name = "accountID", required = true) Integer cardId,
			@RequestBody UpdateAccountProductRequest request) {
		request.setAccountId(cardId);
		return new ModelResponse<Account>(accountService.updateAccount(request));
	}

}
