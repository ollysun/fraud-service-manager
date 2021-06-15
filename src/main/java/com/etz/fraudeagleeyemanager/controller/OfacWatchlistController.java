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

import com.etz.fraudeagleeyemanager.dto.request.OfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateOfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.service.OfacWatchlistService;

@Validated
@RestController
@RequestMapping("/v1/watchlist/ofac")
public class OfacWatchlistController {

	@Autowired
	OfacWatchlistService ofacWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<OfacWatchlist>> createOfac(
			@Valid @RequestBody  OfacWatchlistRequest request){
		ModelResponse<OfacWatchlist> response = 
				new ModelResponse<>(ofacWatchlistService.createOfacWatchlist(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping(path = "/{ofacID}")
	public ModelResponse<OfacWatchlist> updateOfac(@PathVariable(name = "ofacID", required = true) Long ofacId,
			@RequestBody @Valid UpdateOfacWatchlistRequest request){
		request.setOfacId(ofacId);
		return new ModelResponse<>(ofacWatchlistService.updateOfacWatchlist(request));
	}
	
	@GetMapping
	public PageResponse<OfacWatchlist> queryOfac(@RequestParam(name = "ofacID", required = false) Long ofacId){
		return new PageResponse<>(ofacWatchlistService.getOfacWatchlist(ofacId));
	}
	
	@DeleteMapping(path = "/{ofacID}")
	public BooleanResponse deleteOfac(@PathVariable(name = "ofacID", required = true) Long ofacId){
		return new BooleanResponse(ofacWatchlistService.deleteOfacWatchlist(ofacId));
	}
}
