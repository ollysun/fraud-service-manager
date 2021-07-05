package com.etz.fraudeagleeyemanager.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardRequestDto;
import com.etz.fraudeagleeyemanager.dto.response.CardProductResponse;
import com.etz.fraudeagleeyemanager.dto.response.CardResponse;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final CardRepository cardRepository;
	private final CardProductRepository cardProductRepository;
	private final ProductEntityRepository productEntityRepository;
	private final CardRedisRepository cardRedisRepository;
	private final CardProductRedisRepository cardProductRedisRepository;

	@Transactional(rollbackFor = Throwable.class)
	public CardResponse createCard(CardRequest request) {
		Card cardEntity = new Card();
		try {
			cardEntity.setCardholderName(request.getHolderName());
			cardEntity.setCardBin(request.getCardBin());
			cardEntity.setCardBrand(AppUtil.checkBrand(request.getCardBrand()));
			cardEntity.setCardType(AppUtil.checkCardType(request.getCardType()));
			if (Boolean.TRUE.equals(AppUtil.getDate(request.getExpiry()))) {
				cardEntity.setCardExpiry(request.getExpiry());
			} else {
				throw new FraudEngineException("The card has expired " + request.getExpiry());
			}
			if (AppUtil.getLength(request.getCvv()) > 3) {
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
		} catch (Exception ex) {
			log.error("Error occurred while creating card entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return outputCardResponse(saveCardEntityToDatabase(cardEntity));
	}
	
 	// update card to increment suspicious count
	@Transactional(rollbackFor = Throwable.class)
	public Card updateCard(UpdateCardRequestDto updateCardRequestDto){
		Optional<Card> cardOptional = cardRepository.findByCardBin(updateCardRequestDto.getCardBin());
		if(!cardOptional.isPresent()) {
			throw new ResourceNotFoundException("Card details not found for bin " + updateCardRequestDto.getCardBin());
		}
		Card card = cardOptional.get();
		try {
			// for auditing purpose for UPDATE
			card.setEntityId(String.valueOf(card.getId()));
			card.setRecordBefore(JsonConverter.objectToJson(card));
			card.setRequestDump(updateCardRequestDto);
			
			card.setSuspicionCount(updateCardRequestDto.getCount());
			card.setBlockReason(updateCardRequestDto.getBlockReason());
			card.setStatus(updateCardRequestDto.getStatus());
		} catch (Exception ex) {
			log.error("Error occurred while creating card entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveCardEntityToDatabase(card);
	}

	private Card saveCardEntityToDatabase(Card cardEntity) {
		Card persistedCardEntity;
		try {
			persistedCardEntity = cardRepository.save(cardEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving card entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveCardEntityToRedis(persistedCardEntity);
		return persistedCardEntity;
	}
	
	private void saveCardEntityToRedis(Card alreadyPersistedCardEntity) {
		try {
			cardRedisRepository.setHashOperations(redisTemplate);
			cardRedisRepository.update(alreadyPersistedCardEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving card entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

	@Transactional(readOnly = true)
	public Page<CardResponse> getCards(Long cardId) {
		List<CardResponse> cardResponseList;
		if (Objects.isNull(cardId)) {
			cardResponseList = outputCardResponseList(cardRepository.findAll());
			return AppUtil.listConvertToPage(cardResponseList, PageRequestUtil.getPageRequest());
		}
		Optional<Card> cardEntityOptional = cardRepository.findById(cardId);
		if(!cardEntityOptional.isPresent()) {
			throw new ResourceNotFoundException("Card Not found " + cardId);
		}
		cardResponseList = outputCardResponseList(cardRepository.findAll(Example.of(cardEntityOptional.get())));
		return AppUtil.listConvertToPage(cardResponseList, PageRequestUtil.getPageRequest());
	}

	@Transactional(rollbackFor = Throwable.class)
	public CardProduct cardToProduct(CardToProductRequest request) {
		if (!productEntityRepository.findByCodeAndDeletedFalse(request.getProductCode()).isPresent()){
			throw new ResourceNotFoundException("Product Entity not found for productCode " + request.getProductCode());
		}
		if (!cardRepository.findById(request.getCardId()).isPresent()){
			throw new ResourceNotFoundException("Card Not found " + request.getCardId());
		}
		
		CardProduct cardToProdEntity = new CardProduct();
		try {
			cardToProdEntity.setProductCode(request.getProductCode());
			cardToProdEntity.setCardId(request.getCardId());
			cardToProdEntity.setStatus(Boolean.TRUE);
			cardToProdEntity.setCreatedBy(request.getCreatedBy());

			// for auditing purpose for CREATE
			cardToProdEntity.setEntityId(null);
			cardToProdEntity.setRecordBefore(null);
			cardToProdEntity.setRequestDump(request);
		} catch (Exception ex) {
			log.error("Error occurred while creating card product entity object", ex);
			throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
		}
		return saveCardProductEntityToDatabase(cardToProdEntity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<CardProductResponse> updateCardProduct(UpdateCardProductRequest request) {
		List<CardProduct> cardProductList = cardProductRepository.findByCardId(request.getCardId());
		if(cardProductList.isEmpty()){
			throw new ResourceNotFoundException("CardProduct Not found for cardId " + request.getCardId());
		}

		String entityId = "CardId:" + request.getCardId();
		if (Objects.isNull(request.getProductCode())) {
			cardProductList = cardProductList.stream().filter(entity -> entity.getProductCode().equals(request.getProductCode()))
					.collect(Collectors.toList());
			if(cardProductList.isEmpty()) {
				throw new ResourceNotFoundException("Card Product not found for this id " + request.getCardId() + " and code " +  request.getProductCode());
			}
			entityId += " ProdCd:" + request.getProductCode();
		}

		List<CardProductResponse> updatedCardProductResponseList = new ArrayList<>();
		CardProductResponse cardProductResponse = new CardProductResponse();
		cardProductRedisRepository.setHashOperations(redisTemplate);
		
		for(CardProduct cardProductEntity : cardProductList) {
			try {
				// for auditing purpose for UPDATE
				cardProductEntity.setEntityId(entityId);
				cardProductEntity.setRecordBefore(JsonConverter.objectToJson(cardProductEntity));
				cardProductEntity.setRequestDump(request);

				cardProductEntity.setStatus(request.getStatus());
				cardProductEntity.setUpdatedBy(request.getUpdatedBy());
			} catch (Exception ex) {
				log.error("Error occurred while creating card product entity object", ex);
				throw new FraudEngineException(AppConstant.ERROR_SETTING_PROPERTY);
			}
			BeanUtils.copyProperties(saveCardProductEntityToDatabase(cardProductEntity), cardProductResponse);
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

	private CardResponse outputCardResponse(Card card){

		CardResponse cardResponse = new CardResponse();
		BeanUtils.copyProperties(card,cardResponse,"products");
		return cardResponse;
	}
	
	private CardProduct saveCardProductEntityToDatabase(CardProduct cardProductEntity) {
		CardProduct persistedCardProductEntity;
		try {
			persistedCardProductEntity = cardProductRepository.save(cardProductEntity);
		} catch(Exception ex){
			log.error("Error occurred while saving card product entity to database" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_DATABASE);
		}
		saveCardProductEntityToRedis(persistedCardProductEntity);
		return persistedCardProductEntity;
	}
	
	private void saveCardProductEntityToRedis(CardProduct alreadyPersistedCardProductEntity) {
		try {
			cardProductRedisRepository.setHashOperations(redisTemplate);
			cardProductRedisRepository.update(alreadyPersistedCardProductEntity);
		} catch(Exception ex){
			//TODO actually delete already saved entity from the database (NOT SOFT DELETE)
			log.error("Error occurred while saving card product entity to Redis" , ex);
			throw new FraudEngineException(AppConstant.ERROR_SAVING_TO_REDIS);
		}
	}

}
