package com.etz.fraudeagleeyemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.CardProduct;
import com.etz.fraudeagleeyemanager.entity.CardProductId;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, CardProductId>{
        List<CardProduct> findByCardId(Long cardId);
        
        List<CardProduct> findByProductCode(String productCode);
}
