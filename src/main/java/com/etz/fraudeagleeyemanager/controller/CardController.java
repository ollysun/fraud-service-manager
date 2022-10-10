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
import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.CardProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.CardResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.service.CardService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/card")
@Validated
public class CardController {

	private final CardService cardService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<CardResponse>> addCard(@RequestBody @Valid CardRequest request,
																  @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<CardResponse> response = new ModelResponse<>(cardService.addCard(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@GetMapping
	public PageResponse<CardResponse> queryCard(@RequestParam(required = false) Long cardId){
		return new PageResponse<>(cardService.getCards(cardId));
	}
		
	@PostMapping("/product")
	public ResponseEntity<ModelResponse<CardProduct>> mapCardToProduct(@RequestBody @Valid CardToProductRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<CardProduct> response = new ModelResponse<>(cardService.cardToProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@PutMapping
	public ModelResponse<Card> updateCard(@RequestBody UpdateCardRequestDto request){
		return new ModelResponse<>(cardService.updateCard(request));
	}

	
	@PutMapping("/product")
	public CollectionResponse<CardProductResponse> updateCardProduct(@RequestBody @Valid UpdateCardProductRequest request,
																	 @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new CollectionResponse<>(cardService.updateCardProduct(request));
	}
	
}
