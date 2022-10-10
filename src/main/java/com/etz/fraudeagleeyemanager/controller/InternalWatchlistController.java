package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.InternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateInternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.service.InternalWatchlistService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/watchlist/internal")
@Validated
public class InternalWatchlistController {

	private final InternalWatchlistService internalWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<InternalWatchlist>> addOfac(@Valid @RequestBody  InternalWatchlistRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<InternalWatchlist> response = new ModelResponse<>(internalWatchlistService.addInternalWatchlist(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping
	public ModelResponse<InternalWatchlist> updateOfac(
			@RequestBody @Valid UpdateInternalWatchlistRequest request,
													   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(internalWatchlistService.updateInternalWatchlist(request));
	}
	
	@GetMapping
	public PageResponse<InternalWatchlist> queryOfac(@RequestParam(required = false) Long watchId){
		return new PageResponse<>(internalWatchlistService.getInternalWatchlist(watchId));
	}
	
	@DeleteMapping("/{watchId}")
	public BooleanResponse deleteOfac(@PathVariable @Positive Long watchId){
		return new BooleanResponse(internalWatchlistService.deleteInternalWatchlist(watchId));
	}
}
