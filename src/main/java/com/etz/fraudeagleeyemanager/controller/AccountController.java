package com.etz.fraudeagleeyemanager.controller;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.AccountToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.AddAccountRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateAccountRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.AccountProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.AccountResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Account;
import com.etz.fraudeagleeyemanager.service.AccountService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

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
		return null;
		//return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@PutMapping("/product")
	public CollectionResponse<AccountProductResponse> updateAccountProduct(@RequestBody @Valid UpdateAccountProductRequest request,
																		   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username) {
		request.setUpdatedBy(username);
		return new CollectionResponse<>(accountService.updateAccountProduct(request), "All Updated AccountProduct");
	}

}
