package com.etz.fraudeagleeyemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.Rule;


@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    boolean existsByName(String name);
}
