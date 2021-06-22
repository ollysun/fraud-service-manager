package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.OfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateOfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.service.OfacWatchlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/watchlist/ofac")
public class OfacWatchlistController {

	private final OfacWatchlistService ofacWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<OfacWatchlist>> createOfac(@Valid @RequestBody  OfacWatchlistRequest request){
		ModelResponse<OfacWatchlist> response = new ModelResponse<>(ofacWatchlistService.createOfacWatchlist(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping("/{ofacId}")
	public ModelResponse<OfacWatchlist> updateOfac(@PathVariable Long ofacId, @RequestBody @Valid UpdateOfacWatchlistRequest request){
		return new ModelResponse<>(ofacWatchlistService.updateOfacWatchlist(request, ofacId));
	}
	
	@GetMapping
	public PageResponse<OfacWatchlist> queryOfac(Long ofacId){
		return new PageResponse<>(ofacWatchlistService.getOfacWatchlist(ofacId));
	}
	
	@DeleteMapping("/{ofacId}")
	public BooleanResponse deleteOfac(@PathVariable Long ofacId){
		return new BooleanResponse(ofacWatchlistService.deleteOfacWatchlist(ofacId));
	}
}
