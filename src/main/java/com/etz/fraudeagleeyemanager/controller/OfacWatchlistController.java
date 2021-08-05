package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class OfacWatchlistController {

	private final OfacWatchlistService ofacWatchlistService;

	@PostMapping
	public ResponseEntity<ModelResponse<OfacWatchlist>> addOfac(@Valid @RequestBody  OfacWatchlistRequest request,
																   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<OfacWatchlist> response = new ModelResponse<>(ofacWatchlistService.addOfacWatchlist(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping
	public ModelResponse<OfacWatchlist> updateOfac(@RequestBody @Valid UpdateOfacWatchlistRequest request,
												   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(ofacWatchlistService.updateOfacWatchlist(request));
	}
	
	@GetMapping
	public PageResponse<OfacWatchlist> queryOfac(@RequestParam(required = false) Long ofacId){
		return new PageResponse<>(ofacWatchlistService.getOfacWatchlist(ofacId));
	}
	
	@DeleteMapping("/{ofacId}")
	public BooleanResponse deleteOfac(@PathVariable @Positive Long ofacId){
		return new BooleanResponse(ofacWatchlistService.deleteOfacWatchlist(ofacId));
	}
}
