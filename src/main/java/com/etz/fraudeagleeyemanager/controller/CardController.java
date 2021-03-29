package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudengine.dto.request.CardRequest;
import com.etz.fraudengine.dto.request.CardToProductRequest;
import com.etz.fraudengine.dto.request.UpdateCardRequest;
import com.etz.fraudengine.dto.response.ModelResponse;
import com.etz.fraudengine.dto.response.PageResponse;
import com.etz.fraudengine.entity.CardEntity;
import com.etz.fraudengine.entity.CardProductEntity;
import com.etz.fraudengine.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/card")
public class CardController {

	@Autowired
	CardService cardService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<CardEntity>> createCard(
			@RequestBody @Valid CardRequest request){
		ModelResponse<CardEntity> response = new ModelResponse<CardEntity>(cardService.createCard(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@GetMapping
	public PageResponse<CardEntity> queryCard(
			@RequestParam(name = "cardID", defaultValue = "-1") String cardId){
		return new PageResponse<>(cardService.getCards(Long.parseLong(cardId.trim())));
	}
		
	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<CardProductEntity>> mapCardToProduct(CardToProductRequest request){
		ModelResponse<CardProductEntity> response = new ModelResponse<CardProductEntity>(cardService.cardToProduct(request));
		response.setStatus(201);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{cardID}")
	public ModelResponse<CardEntity> updateCard(@PathVariable(name = "cardID") Integer cardId,
			@RequestBody UpdateCardRequest request){
				   
		request.setCardId(cardId);
		return new ModelResponse<CardEntity>(cardService.updateCard(request));
	}
	
	
//	@DeleteMapping(path = "/{cardID}")
//	public ResponseEntity<BooleanResponse> deleteProduct(
//			@PathParam(value = "cardID") Integer cardId){
//		
//		boolean isCardDeleted = cardService.deleteCard(cardId);
//		HttpStatus httpStatusCode = HttpStatus.CREATED;
//		
//		if(!isCardDeleted) {
//			httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		
//		BooleanResponse response = new BooleanResponse(isCardDeleted);
//		return new ResponseEntity<>(response, httpStatusCode);
//	}
	

	
	
}
