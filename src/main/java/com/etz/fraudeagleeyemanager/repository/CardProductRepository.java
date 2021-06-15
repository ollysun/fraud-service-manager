package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.entity.CardProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, CardProductId>{
        List<CardProduct> findByCardId(Long cardId);
}
