package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.constant.CardBrand;
import com.etz.fraudeagleeyemanager.constant.CardType;
import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.*;
import com.etz.fraudeagleeyemanager.entity.*;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.AccountRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.CardProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.CardRedisRepository;
import com.etz.fraudeagleeyemanager.repository.CardProductRepository;
import com.etz.fraudeagleeyemanager.repository.CardRepository;
import com.etz.fraudeagleeyemanager.repository.ProductEntityRepository;
import com.etz.fraudeagleeyemanager.util.AppUtil;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import com.etz.fraudeagleeyemanager.util.RestTemplateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
		return card;
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

		CardProduct cardProduct = cardProductRepository.save(cardToProdEntity);
		cardProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardProductRedisRepository.create(cardProduct);

		return cardProduct;
	}

	public CardProduct updateCardProduct(UpdateCardProductRequest request) {
		Optional<CardProduct> cardToProdEntity = Optional.ofNullable(cardProductRepository.findById(request.getCardId())
				.orElseThrow(() -> new ResourceNotFoundException("CardProduct Not found for cardId " + request.getCardId())));
		if (cardToProdEntity.isPresent()){
			cardToProdEntity.get().setStatus(request.getStatus());
			cardToProdEntity.get().setUpdatedBy(request.getUpdatedBy());
			cardToProdEntity.get().setProductCode(request.getProductCode());
		}

		ProductEntity productEntity = productEntityRepository.findByCode(request.getProductCode());
		if (productEntity == null){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + request.getProductCode());
		}
		CardProduct cardProduct = cardProductRepository.save(cardToProdEntity.get());

		cardProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardProductRedisRepository.update(cardProduct);
		return outputCardProductResponse(cardProductRepository.save(cardToProdEntity.get()));
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

	private CardProductResponse outputCardProductResponse(CardProduct cardProduct){

		CardProductResponse cardProductResponse = new CardProductResponse();
		BeanUtils.copyProperties(cardProduct,cardProductResponse,"productEntity", "card");
		return cardProductResponse;
	}

	private CardResponse outputCardResponse(Card card){

		CardResponse cardResponse = new CardResponse();
		BeanUtils.copyProperties(card,cardResponse,"products");
		return cardResponse;
	}

}
