package com.etz.fraudeagleeyemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long>{
        boolean existsByNameAndOperator(String name, String operator);
}
