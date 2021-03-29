package com.etz.fraudeagleeyemanager.service;

import com.etz.fraudengine.dto.request.CardRequest;
import com.etz.fraudengine.dto.request.CardToProductRequest;
import com.etz.fraudengine.dto.request.UpdateCardRequest;
import com.etz.fraudengine.entity.CardEntity;
import com.etz.fraudengine.entity.CardProductEntity;
import com.etz.fraudengine.enums.ConfigStatus;
import com.etz.fraudengine.repository.CardProductRepository;
import com.etz.fraudengine.repository.CardRepository;
import com.etz.fraudengine.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardService {

	@Autowired
	CardRepository cardRepository;

	@Autowired
	CardProductRepository cardProductRepository;

	public CardEntity createCard(CardRequest request) {
		CardEntity cardEntity = new CardEntity();
		// cardEntity.setId(id);
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
		cardEntity.setStatus(request.getStatus() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		cardEntity.setCreatedAt(LocalDateTime.now());

		return cardRepository.save(cardEntity);
	}

	public Page<CardEntity> getCards(Long cardId) {

		if (cardId < 0) {
			return cardRepository.findAll(PageRequestUtil.getPageRequest());
		}

		CardEntity cardEntity = new CardEntity();
		cardEntity.setId(cardId);
		return cardRepository.findAll(Example.of(cardEntity), PageRequestUtil.getPageRequest());
	}

	public CardProductEntity cardToProduct(CardToProductRequest request) {

		CardProductEntity cardToProdEntity = new CardProductEntity();
		cardToProdEntity.setProductCode(request.getProductCode());
		cardToProdEntity.setCardId(request.getCardId().longValue());
		cardToProdEntity.setStatus(request.getStatus() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		cardToProdEntity.setCreatedAt(LocalDateTime.now());

		return cardProductRepository.save(cardToProdEntity);
	}

	public CardEntity updateCard(UpdateCardRequest request) {
		CardEntity cardEntity = cardRepository.findById(request.getCardId().longValue()).get();

		cardEntity.setStatus(request.getStatus() == true ? ConfigStatus.TRUE : ConfigStatus.FALSE);
		cardEntity.setUpdatedBy(request.getUpdatedBy());
		cardEntity.setUpdatedAt(LocalDateTime.now());

		return cardRepository.save(cardEntity);
	}

}
