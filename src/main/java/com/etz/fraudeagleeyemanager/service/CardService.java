package com.etz.fraudeagleeyemanager.service;


import com.etz.fraudeagleeyemanager.dto.request.CardRequest;
import com.etz.fraudeagleeyemanager.dto.request.CardToProductRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateCardProductRequest;
import com.etz.fraudeagleeyemanager.entity.Card;
import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.exception.ResourceNotFoundException;
import com.etz.fraudeagleeyemanager.repository.CardProductRepository;
import com.etz.fraudeagleeyemanager.repository.CardRepository;
import com.etz.fraudeagleeyemanager.util.PageRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class CardService {

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	CardProductRepository cardProductRepository;

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

		return cardRepository.save(cardEntity);
	}

	public Page<Card> getCards(Long cardId) {
		if (cardId == null) {
			return cardRepository.findAll(PageRequestUtil.getPageRequest());
		}
		Card cardEntity  = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card Not found " + cardId));
		cardEntity.setId(cardId);
		return cardRepository.findAll(Example.of(cardEntity), PageRequestUtil.getPageRequest());
	}

	public CardProduct cardToProduct(CardToProductRequest request) {

		CardProduct cardToProdEntity = new CardProduct();
		cardToProdEntity.setProductCode(request.getProductCode());
		cardToProdEntity.setCardId(request.getCardId().longValue());
		cardToProdEntity.setStatus(Boolean.TRUE);
		cardToProdEntity.setCreatedBy(request.getCreatedBy());
		return cardProductRepository.save(cardToProdEntity);
	}

	public CardProduct updateCardProduct(UpdateCardProductRequest request) {
		CardProduct cardToProdEntity  = cardProductRepository.findByCardId(request.getCardId().longValue());
		cardToProdEntity.setStatus(request.getStatus());
		cardToProdEntity.setUpdatedBy(request.getUpdatedBy());
		cardToProdEntity.setProductCode(request.getProductCode());
		return cardProductRepository.save(cardToProdEntity);
	}

}
