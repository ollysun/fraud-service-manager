package com.etz.fraudeagleeyemanager.repository.eagleeyedb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.CardProduct;
import com.etz.fraudeagleeyemanager.entity.eagleeyedb.CardProductId;

import java.util.List;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, CardProductId>{
        List<CardProduct> findByCardId(Long cardId);
}
