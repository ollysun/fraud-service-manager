package com.etz.fraudeagleeyemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.etz.fraudeagleeyemanager.entity.ProductRuleId;
import com.etz.fraudeagleeyemanager.entity.ServiceRule;

@Repository
public interface ServiceRuleRepository extends JpaRepository<ServiceRule, ProductRuleId> {
    Optional<ServiceRule> findByServiceId(Long serviceId);
    List<ServiceRule> findByRuleId(Long ruleId);

    // NOTE: you have return void
    @Modifying
    @Transactional
    @Query(value="DELETE FROM ServiceRule WHERE serviceId = (:serviceId) and ruleId IN (:ruleIds)")
    void deleteByRuleIdSAndServiceId(@Param("serviceId") String serviceId, @Param("ruleIds") List<Long> ruleId);

    boolean existsByRuleIdAndServiceId(Long id, String serviceId);

}
