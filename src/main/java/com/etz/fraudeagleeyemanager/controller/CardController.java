package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/card")
public class CardController {

	@Autowired
	CardService cardService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<CardResponse>> createCard(@RequestBody @Valid CardRequest request){
		ModelResponse<CardResponse> response = new ModelResponse<>(cardService.createCard(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@GetMapping
	public PageResponse<CardResponse> queryCard(
			@RequestParam(name = "cardId", defaultValue = "") Long cardId){
		return new PageResponse<>(cardService.getCards(cardId));
	}
		
	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<CardProduct>> mapCardToProduct(@RequestBody @Valid CardToProductRequest request){
		ModelResponse<CardProduct> response = new ModelResponse<>(cardService.cardToProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
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
