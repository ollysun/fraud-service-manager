package com.etz.fraudeagleeyemanager.controller;

import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/card")
public class CardController {

	@Autowired
	CardService cardService;
	
	@PostMapping
	public ResponseEntity<ModelResponse<Card>> createCard(
			@RequestBody CardRequest request){
		ModelResponse<Card> response = new ModelResponse<>(cardService.createCard(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@GetMapping
	public PageResponse<Card> queryCard(
			@RequestParam(name = "cardId", defaultValue = "") String cardId){
		return new PageResponse<>(cardService.getCards(Long.parseLong(cardId.trim())));
	}
		
	@PostMapping(path = "/product")
	public ResponseEntity<ModelResponse<CardProduct>> mapCardToProduct(CardToProductRequest request){
		ModelResponse<CardProduct> response = new ModelResponse<>(cardService.cardToProduct(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{cardID}")
	public ModelResponse<CardProduct> updateCard(@PathVariable(name = "cardID") Integer cardId,
												 @RequestBody UpdateCardProductRequest request){
				   
		request.setCardId(cardId);
		return new ModelResponse<>(cardService.updateCardProduct(request));
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
