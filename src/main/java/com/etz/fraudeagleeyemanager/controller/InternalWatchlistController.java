package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class InternalWatchlistController {

	private final InternalWatchlistService internalWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<InternalWatchlist>> createOfac(@Valid @RequestBody  InternalWatchlistRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<InternalWatchlist> response = new ModelResponse<>(internalWatchlistService.createInternalWatchlist(request), HttpStatus.CREATED);
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
	public BooleanResponse deleteOfac(@PathVariable Long watchId){
		return new BooleanResponse(internalWatchlistService.deleteInternalWatchlist(watchId));
	}
}
