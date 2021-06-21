package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.CardProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.CardResponse;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.CardProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.CardRedisRepository;
import com.etz.fraudeagleeyemanager.repository.CardProductRepository;
import com.etz.fraudeagleeyemanager.repository.CardRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;


@Service
public class CardService {

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	CardProductRepository cardProductRepository;

	@Autowired
	private CardRedisRepository cardRedisRepository;

	@Autowired
	private ProductEntityRepository productEntityRepository;


	@Autowired
	private CardProductRedisRepository cardProductRedisRepository;

	@Autowired @Qualifier("redisTemplate")
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;

	public CardResponse createCard(CardRequest request) {
		Card cardEntity = new Card();
		cardEntity.setCardholderName(request.getHolderName());
		cardEntity.setCardBin(request.getCardBin());
		cardEntity.setCardBrand(AppUtil.checkBrand(request.getCardBrand()));
		cardEntity.setCardType(AppUtil.checkCardType(request.getCardType()));
		if (Boolean.TRUE.equals(AppUtil.getDate(request.getExpiry()))){
			cardEntity.setCardExpiry(request.getExpiry());
		}else{
			throw new FraudEngineException("The card has expired " + request.getExpiry());
		}
		if (AppUtil.getLength(request.getCvv()) > 3){
			throw new FraudEngineException("The CVV length is greater than 3 " + request.getCvv());
		}
		cardEntity.setCardCVV(request.getCvv());
		cardEntity.setIssuerCode(request.getBankCode());
		cardEntity.setIssuerName(request.getBankName());
		cardEntity.setIsoCountry(request.getIsoCountry());
		cardEntity.setIsoCountryCode(request.getIsoCountryCode());
		cardEntity.setSuspicionCount(request.getSuspicionCount());
		cardEntity.setBlockReason(request.getBlockReason());
		cardEntity.setStatus(Boolean.TRUE);
		cardEntity.setCreatedBy(request.getCreatedBy());
		
		// for auditing purpose for CREATE
		cardEntity.setEntityId(null);
		cardEntity.setRecordBefore(null);
		cardEntity.setRequestDump(request);

		Card savedCard = cardRepository.save(cardEntity);
		cardRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardRedisRepository.create(savedCard);

		return outputCardResponse(savedCard);
	}
 	// update card to increment suspicious count
	public Card updateCard(UpdateCardRequestDto updateCardRequestDto){
		Card card = cardRepository.findByCardBin(updateCardRequestDto.getCardBin())
				    .orElseThrow(() ->  new ResourceNotFoundException("Card details not found for bin " + updateCardRequestDto.getCardBin()));
		card.setSuspicionCount(updateCardRequestDto.getCount());
		card.setBlockReason(updateCardRequestDto.getBlockReason());
		card.setStatus(updateCardRequestDto.getStatus());
		
		Card savedCard = cardRepository.save(card);
		cardRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardRedisRepository.update(savedCard);
		return savedCard;
	}

	public Page<CardResponse> getCards(Long cardId) {
		List<CardResponse> cardResponseList;
		if (cardId == null) {
			cardResponseList = outputCardResponseList(cardRepository.findAll());
			return AppUtil.listConvertToPage(cardResponseList, PageRequestUtil.getPageRequest());
		}
		Card cardEntity  = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card Not found " + cardId));
		cardEntity.setId(cardId);
		cardResponseList = outputCardResponseList(cardRepository.findAll(Example.of(cardEntity)));

		return AppUtil.listConvertToPage(cardResponseList, PageRequestUtil.getPageRequest());
	}

	public CardProduct cardToProduct(CardToProductRequest request) {

		CardProduct cardToProdEntity = new CardProduct();
		cardToProdEntity.setProductCode(request.getProductCode());
		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + request.getProductCode());
		}

		cardToProdEntity.setCardId(request.getCardId());
		cardRepository.findById(request.getCardId())
				       .orElseThrow(() -> new ResourceNotFoundException("Card Not found " + request.getCardId()));

		cardToProdEntity.setStatus(Boolean.TRUE);
		cardToProdEntity.setCreatedBy(request.getCreatedBy());
		
		// for auditing purpose for CREATE
		cardToProdEntity.setEntityId(null);
		cardToProdEntity.setRecordBefore(null);
		cardToProdEntity.setRequestDump(request);

		CardProduct cardProduct = cardProductRepository.save(cardToProdEntity);
		cardProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardProductRedisRepository.create(cardProduct);

		return cardProduct;
	}

	public List<CardProductResponse> updateCardProduct(UpdateCardProductRequest request) {
		List<CardProduct> cardProductList = cardProductRepository.findByCardId(request.getCardId());
		if(cardProductList.isEmpty()){
			throw new ResourceNotFoundException("CardProduct Not found for cardId " + request.getCardId());
		}

		List<CardProductResponse> updatedCardProductResponseList = new ArrayList<>();
		CardProductResponse cardProductResponse = new CardProductResponse();
		cardProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		String entityId = "CardId:" + request.getCardId();
		
		if (request.getProductCode() != null) {
			cardProductList = cardProductList.stream().filter(entity -> entity.getProductCode().equals(request.getProductCode()))
					.collect(Collectors.toList());
			if(cardProductList.isEmpty()) {
				throw new ResourceNotFoundException("Card Product not found for this id " + request.getCardId() + " and code " +  request.getProductCode());
			}
			entityId += " ProdCd:" + request.getProductCode();
		}
		
		for(CardProduct cardProductEntity : cardProductList) {
			// for auditing purpose for UPDATE
			cardProductEntity.setEntityId(entityId);
			cardProductEntity.setRecordBefore(JsonConverter.objectToJson(cardProductEntity));
			cardProductEntity.setRequestDump(request);
			
			cardProductEntity.setStatus(request.getStatus());
			cardProductEntity.setUpdatedBy(request.getUpdatedBy());
			CardProduct cardProductSaved = cardProductRepository.save(cardProductEntity);
			cardProductRedisRepository.update(cardProductSaved);
			
			BeanUtils.copyProperties(cardProductSaved, cardProductResponse);
			updatedCardProductResponseList.add(cardProductResponse);
		}

		return updatedCardProductResponseList;
	}

	private List<CardResponse> outputCardResponseList(List<Card> cardList){
		List<CardResponse> cardResponseList = new ArrayList<>();
		cardList.forEach(cardVal -> {
			CardResponse cardResponse = new CardResponse();
			BeanUtils.copyProperties(cardVal,cardResponse,"products");
			cardResponseList.add(cardResponse);
		});
		return cardResponseList;
	}

	private List<CardProductResponse> outputCardProductResponse(List<CardProduct> cardProductList){
		List<CardProductResponse> cardProductResponseList = new ArrayList<>();
		cardProductList.forEach(cardProduct -> {
			CardProductResponse cardProductResponse = new CardProductResponse();
			BeanUtils.copyProperties(cardProduct,cardProductResponse,"productEntity", "card");
			cardProductResponseList.add(cardProductResponse);
		});
		return cardProductResponseList;
	}

	private CardResponse outputCardResponse(Card card){

		CardResponse cardResponse = new CardResponse();
		BeanUtils.copyProperties(card,cardResponse,"products");
		return cardResponse;
	}

}
