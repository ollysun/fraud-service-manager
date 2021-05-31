package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
}
