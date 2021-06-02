package com.etz.fraudeagleeyemanager.controller;


import com.etz.fraudeagleeyemanager.dto.request.*;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/account")
public class AccountController {

	@Autowired
	AccountService accountService;

 // todo set preauthorise permission
	@PostMapping
	public ResponseEntity<ModelResponse<Account>> createAccount(@RequestBody @Valid AddAccountRequest request) {
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
		log.info(request.toString());
		ModelResponse<AccountProduct> response = new ModelResponse<>(
				accountService.mapAccountProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(path = "/product")
	public ResponseEntity<CollectionResponse<AccountProductResponse>> updateAccountProduct(@RequestBody @Valid UpdateAccountProductRequest request) {
		List<AccountProductResponse> userResponseList = accountService.updateAccountProduct(request);
		CollectionResponse<AccountProductResponse> collectionResponse = new CollectionResponse<>(userResponseList);
		collectionResponse.setMessage("All Updated AccountProduct");
		return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
	}

	@PutMapping
	public ModelResponse<Account> updateAccount(@RequestBody UpdateAccountRequestDto request){
		return new ModelResponse<>(accountService.updateAccount(request));
	}

}
