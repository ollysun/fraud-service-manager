package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.InternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateInternalWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import com.etz.fraudeagleeyemanager.service.InternalWatchlistService;

@Validated
@RestController
@RequestMapping("/v1/watchlist/internal")
public class InternalWatchlistController {

	@Autowired
	InternalWatchlistService internalWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<InternalWatchlist>> createOfac(
			@Valid @RequestBody  InternalWatchlistRequest request){
		ModelResponse<InternalWatchlist> response = 
				new ModelResponse<>(internalWatchlistService.createInternalWatchlist(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping(path = "/{watchID}")
	public ModelResponse<InternalWatchlist> updateOfac(@PathVariable(name = "watchID", required = true) Long watchId,
			@RequestBody @Valid UpdateInternalWatchlistRequest request){
		request.setWatchId(watchId);
		return new ModelResponse<>(internalWatchlistService.updateInternalWatchlist(request));
	}
	
	@GetMapping
	public PageResponse<InternalWatchlist> queryOfac(@RequestParam(name = "watchID", required = false) Long watchId){
		return new PageResponse<>(internalWatchlistService.getInternalWatchlist(watchId));
	}
	
	@DeleteMapping(path = "/{watchID}")
	public BooleanResponse deleteOfac(@PathVariable(name = "watchID", required = true) Long watchId){
		return new BooleanResponse(internalWatchlistService.deleteInternalWatchlist(watchId));
	}
}
