package com.etz.fraudeagleeyemanager.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/card")
public class CardController {

	private final CardService cardService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<CardResponse>> createCard(@RequestBody @Valid CardRequest request){
		ModelResponse<CardResponse> response = new ModelResponse<>(cardService.createCard(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@GetMapping
	public PageResponse<CardResponse> queryCard(@RequestParam Long cardId){
		return new PageResponse<>(cardService.getCards(cardId));
	}
		
	@PostMapping("/product")
	public ResponseEntity<ModelResponse<CardProduct>> mapCardToProduct(@RequestBody @Valid CardToProductRequest request){
		ModelResponse<CardProduct> response = new ModelResponse<>(cardService.cardToProduct(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}

	@PutMapping
	public ModelResponse<Card> updateCard(@RequestBody UpdateCardRequestDto request){
		return new ModelResponse<>(cardService.updateCard(request));
	}

	
	@PutMapping("/product")
	public CollectionResponse<CardProductResponse> updateCardProduct(@RequestBody UpdateCardProductRequest request){
		return new CollectionResponse<>(cardService.updateCardProduct(request));
	}
	
}
