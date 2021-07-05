package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.etz.fraudeagleeyemanager.dto.request.OfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateOfacWatchlistRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import com.etz.fraudeagleeyemanager.service.OfacWatchlistService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/watchlist/ofac")
public class OfacWatchlistController {

	private final OfacWatchlistService ofacWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<OfacWatchlist>> createOfac(@Valid @RequestBody  OfacWatchlistRequest request,
																   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<OfacWatchlist> response = new ModelResponse<>(ofacWatchlistService.createOfacWatchlist(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping("/{ofacId}")
	public ModelResponse<OfacWatchlist> updateOfac(@PathVariable Long ofacId, @RequestBody @Valid UpdateOfacWatchlistRequest request,
												   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
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
