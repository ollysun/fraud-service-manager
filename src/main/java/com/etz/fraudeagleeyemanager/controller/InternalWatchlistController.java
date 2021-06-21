package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.InternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateInternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.service.InternalWatchlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/watchlist/internal")
public class InternalWatchlistController {

	private final InternalWatchlistService internalWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<InternalWatchlist>> createOfac(@Valid @RequestBody  InternalWatchlistRequest request){
		ModelResponse<InternalWatchlist> response = new ModelResponse<>(internalWatchlistService.createInternalWatchlist(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping("/{watchID}")
	public ModelResponse<InternalWatchlist> updateOfac(Long watchId,
			@RequestBody @Valid UpdateInternalWatchlistRequest request){
		return new ModelResponse<>(internalWatchlistService.updateInternalWatchlist(request, watchId));
	}
	
	@GetMapping
	public PageResponse<InternalWatchlist> queryOfac(Long watchId){
		return new PageResponse<>(internalWatchlistService.getInternalWatchlist(watchId));
	}
	
	@DeleteMapping("/{watchID}")
	public BooleanResponse deleteOfac(Long watchId){
		return new BooleanResponse(internalWatchlistService.deleteInternalWatchlist(watchId));
	}
}
