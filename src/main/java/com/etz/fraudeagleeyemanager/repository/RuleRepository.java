package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    @Query( value = "select rl.* from rule rl inner join product_rule pr " +
            "on rl.id = pr.rule_id " +
            "where pr.product_code = :code", nativeQuery = true)
    List<Rule> getRuleWithCode(@Param("code") String code);


}
