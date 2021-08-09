package com.etz.fraudeagleeyemanager.controller;


import javax.validation.Valid;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountRequestDto;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.entity.AccountProduct;
import com.etz.fraudeagleeyemanager.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
@Validated
public class AccountController {

	private final AccountService accountService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<AccountResponse>> addAccount(@RequestBody @Valid AddAccountRequest request,
																	 @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username) {
		request.setCreatedBy(username);
		ModelResponse<AccountResponse> response = new ModelResponse<>(accountService.addAccount(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@GetMapping
	public PageResponse<Account> queryAccount(@RequestParam(required = false) Long accountId) {
		return new PageResponse<>(accountService.getAccount(accountId));
	}
	
	@PutMapping
	public ModelResponse<Account> updateAccount(@RequestBody UpdateAccountRequestDto request){
		return new ModelResponse<>(accountService.updateAccount(request));
	}

	@PostMapping("/product")
	public ResponseEntity<ModelResponse<AccountProductResponse>> mapAccountToProduct(@RequestBody @Valid AccountToProductRequest request,
																			 @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username) {
		request.setCreatedBy(username);
		ModelResponse<AccountProductResponse> response = new ModelResponse<>(accountService.mapAccountProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@PutMapping("/product")
	public CollectionResponse<AccountProductResponse> updateAccountProduct(@RequestBody @Valid UpdateAccountProductRequest request,
																		   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username) {
		request.setUpdatedBy(username);
		return new CollectionResponse<>(accountService.updateAccountProduct(request), "All Updated AccountProduct");
	}

}
