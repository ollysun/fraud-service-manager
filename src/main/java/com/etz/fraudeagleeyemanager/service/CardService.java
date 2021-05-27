package com.etz.fraudeagleeyemanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.redisrepository.CardProductRedisRepository;
import com.etz.fraudeagleeyemanager.redisrepository.CardRedisRepository;
import com.etz.fraudeagleeyemanager.repository.CardProductRepository;
import com.etz.fraudeagleeyemanager.repository.CardRepository;
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
	private CardProductRedisRepository cardProductRedisRepository;

	@Autowired @Qualifier("redisTemplate")
	private RedisTemplate<String, Object> fraudEngineRedisTemplate;

	public Card createCard(CardRequest request) {
		Card cardEntity = new Card();
		cardEntity.setCardholderName(request.getHolderName());
		cardEntity.setCardBin(request.getCardBin());
		cardEntity.setCardBrand(request.getCardBrand());
		cardEntity.setCardType(request.getCardType());
		cardEntity.setCardExpiry(request.getExpiry());
		cardEntity.setCardCVV(request.getCvv());
		cardEntity.setIssuerCode(request.getBankCode());
		cardEntity.setIssuerName(request.getBankName());
		cardEntity.setIsoCountry(request.getCountry());
		cardEntity.setIsoCountryCode(Integer.parseInt(request.getCountryCode()));
		cardEntity.setSuspicionCount(request.getSuspicionCount());
		cardEntity.setBlockReason(request.getBlockReason());
		cardEntity.setStatus(request.getStatus());
		
		// for auditing purpose for CREATE
		cardEntity.setEntityId(null);
		cardEntity.setRecordBefore(null);
		cardEntity.setRequestDump(request);

		Card savedCard = cardRepository.save(cardEntity);

		cardRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardRedisRepository.create(savedCard);

		return savedCard;
	}
 	// update card to increment suspicious count
	public Card updateCard(Integer cardBin, Integer count, Boolean status, String blockReason){
		Card card = cardRepository.findByCardBin(cardBin)
				    .orElseThrow(() ->  new ResourceNotFoundException("Card details not found for bin " + cardBin));
		card.setSuspicionCount(count);
		card.setBlockReason(blockReason);
		card.setStatus(status);
		return card;
	}

	public Page<Card> getCards(Long cardId) {
		if (cardId == null) {
			return cardRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Card cardEntity  = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card Not found " + cardId));
		cardEntity.setId(cardId);

		// todo fetch from Redis
		return cardRepository.findAll(Example.of(cardEntity), PageRequestUtil.getPageRequest());
	}

	public CardProduct cardToProduct(CardToProductRequest request) {

		CardProduct cardToProdEntity = new CardProduct();
		cardToProdEntity.setProductCode(request.getProductCode());
		cardToProdEntity.setCardId(request.getCardId().longValue());
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

	public CardProduct updateCardProduct(UpdateCardProductRequest request) {
		CardProduct cardToProdEntity  = cardProductRepository.findByCardId(request.getCardId().longValue());
		cardToProdEntity.setStatus(request.getStatus());
		cardToProdEntity.setUpdatedBy(request.getUpdatedBy());
		cardToProdEntity.setProductCode(request.getProductCode());

		// for auditing purpose for UPDATE
		cardToProdEntity.setEntityId(request.getCardId().toString());
		cardToProdEntity.setRecordBefore(JsonConverter.objectToJson(cardToProdEntity));
		cardToProdEntity.setRequestDump(request);
		CardProduct cardProduct = cardProductRepository.save(cardToProdEntity);

		CardProduct cardProductRedis = cardProductRedisRepository.findById(request.getCardId().longValue());
		cardProductRedis.setStatus(request.getStatus());
		cardProductRedis.setUpdatedBy(request.getUpdatedBy());
		cardProductRedis.setProductCode(request.getProductCode());
		cardProductRedisRepository.setHashOperations(fraudEngineRedisTemplate);
		cardProductRedisRepository.update(cardProductRedis);
		return cardProduct;
	}

}
