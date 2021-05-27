package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRuleRepository extends JpaRepository<ProductRule, Long> {
    Optional<ProductRule> findByProductCode(String code);
}
