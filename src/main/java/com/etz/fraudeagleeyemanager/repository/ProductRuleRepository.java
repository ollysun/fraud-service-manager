package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.ProductRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRuleRepository extends JpaRepository<ProductRule, Long> {
    Optional<ProductRule> findByProductCode(String code);
    Optional<ProductRule> findByRuleId(Long ruleId);

    // NOTE: you have return void
    @Modifying
    @Transactional
    @Query(value="UPDATE ProductRule SET deleted = true WHERE ruleId = ?1")
    void deleteByRuleId(Long ruleId);
}
